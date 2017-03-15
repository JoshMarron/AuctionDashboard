
package Model;

import DataStructures.ClickLog;
import DataStructures.CsvInterfaces.Gender;
import DataStructures.CsvInterfaces.Income;
import DataStructures.ServerLog;
import Model.DBEnums.DatabaseStatements;
import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Model.DBEnums.TableType;
import Model.DBEnums.attributes.Attribute;
import Model.DBEnums.headers.*;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Model.TableModels.ServerVisit;
import Model.TableModels.User;
import Views.ViewPresets.AttributeType;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Philip on 26/02/2017.
 * <p>
 * Do not run this file unless JDBC-sqlite driver is configured
 */
public class DatabaseManager {
	
	private String filename;
	private String url;
	private Map<Instant, Number> totalCostDaysMap;
	
	public DatabaseManager() {

//		filename = "db/model3.db";
//		url = "jdbc:sqlite:" + filename;
	}
	
	/**
	 * Attempts connection to the database file
	 *
	 * @return connection to the database file
	 * @throws SQLException Upon failure of connection
	 */
	private Connection connect() throws SQLException {
		return DriverManager.getConnection(url);
	}
	
	/**
	 * Initialises the tables in the database (see schema for details). Is called by the init() function
	 */
	public void initTables() {
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			stmt.execute(DatabaseStatements.DROP_USER.getStatement());
			stmt.execute(DatabaseStatements.CREATE_USER.getStatement());
			stmt.execute(DatabaseStatements.DROP_CLICK.getStatement());
			stmt.execute(DatabaseStatements.CREATE_CLICK.getStatement());
			stmt.execute(DatabaseStatements.DROP_SITE_IMPRESSION.getStatement());
			stmt.execute(DatabaseStatements.CREATE_SITE_IMPRESSION.getStatement());
			stmt.execute(DatabaseStatements.DROP_SERVER_LOG.getStatement());
			stmt.execute(DatabaseStatements.CREATE_SERVER_LOG.getStatement());
			
			// Indexes
			stmt.execute(DatabaseStatements.INDEX_CLICK_DATE.getStatement());
			stmt.execute(DatabaseStatements.INDEX_IMPRESSION_DATE.getStatement());
			stmt.execute(DatabaseStatements.INDEX_SERVER_LOG_DATE.getStatement());
			
			Statement syncOff = conn.createStatement();
			String sqlSyncOff = "PRAGMA synchronous=OFF";
			syncOff.execute(sqlSyncOff);
			syncOff.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a database if it doesn't already exist and sets the filename to it
	 *
	 * @param location location of the file that is to be created
	 */
	public void createDB(String location) {
		this.filename = location;
		File file = new File(filename);
		url = "jdbc:sqlite:" + filename;
		
		if (!file.exists()) {
			System.out.println("Database doesn't exist at: " + url);
			System.out.println("Attempting to create database at: " + url);
			
			try {
				Connection conn = connect();
				
				if (conn != null) {
					Class.forName("org.sqlite.JDBC");
					DatabaseMetaData meta = conn.getMetaData();
					System.out.println("Driver name is " + meta.getDriverName());
					System.out.println("Database successfully created at: " + url);
				}
				conn.close();
				initTables();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Database exists at: " + url);
		}
	}
	
	/**
	 * Return the File form of the url
	 * @return File of the url
	 */
	public File saveDB() {
		return new File(filename);
	}
	
	/**
	 * Loads a database. Pass in a file name and it will attempt to create a connection to it, will throw Exception if
	 * it fails
	 *
	 * @param location file path of the database
	 * @return boolean whether it finds the file or not
	 * @throws SQLException if it fails to create a connection to the database
	 */
	public boolean loadDB(String location) throws SQLException, CorruptTableException {
		filename = location;
		File file = new File(filename);
		url = "jdbc:sqlite:" + filename;
		
		if (file.exists()) {
			Connection conn = connect();
			System.out.println("Database loaded successfully from: " + url);
			
			System.out.println("Testing for data...");
			testForTables(conn);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Simple function to test for table existence, executes a simple SQLite command for each table, gets the meta data
	 * for each table and loops through the headers and checks for their existence
	 * @param conn Connection to pass the SQLite statement through
	 * @throws SQLException Thrown when the SQLite statement fails
	 * @throws CorruptTableException Thrown when one of the table headers doesn't equal the enum headers
	 */
	private void testForTables(Connection conn) throws SQLException, CorruptTableException {
		String sql;
		List<String> headerList;
		ResultSet resultSet;
		ResultSetMetaData rsmd;
		Statement stmt = conn.createStatement();;
		
		// Check for click table
		sql = "SELECT * FROM click LIMIT 1";
		headerList = Arrays.asList(
				ClickTableHeaders.CLICK_ID.toString(),
				ClickTableHeaders.USER_ID.toString(),
				ClickTableHeaders.CLICK_DATE.toString(),
				ClickTableHeaders.COST.toString());
		resultSet = stmt.executeQuery(sql);
		rsmd = resultSet.getMetaData();
		
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			if (!rsmd.getColumnName(i).equals(headerList.get(i - 1)))
				throw new CorruptTableException("Click table is corrupted, the header " + headerList.get(i - 1) + " doesn't exist");
		}
		
		// Check for server log table
		sql = "SELECT * FROM server_log LIMIT 1";
		headerList = Arrays.asList(
				ServerLogTableHeaders.SERVER_LOG_ID.toString(),
				ServerLogTableHeaders.USER_ID.toString(),
				ServerLogTableHeaders.ENTRY_DATE.toString(),
				ServerLogTableHeaders.EXIT_DATE.toString(),
				ServerLogTableHeaders.PAGES_VIEWED.toString(),
				ServerLogTableHeaders.CONVERSION.toString());
		resultSet = stmt.executeQuery(sql);
		rsmd = resultSet.getMetaData();
		
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			if (!rsmd.getColumnName(i).equals(headerList.get(i - 1)))
				throw new CorruptTableException("Server log table is corrupted, header " + headerList.get(i - 1) + " doesn't exist");
		}
		
		// Check for site impression table
		sql = "SELECT * FROM site_impression";
		headerList = Arrays.asList(
				SiteImpressionHeaders.SITE_IMPRESSION_ID.toString(),
				SiteImpressionHeaders.USER_ID.toString(),
				SiteImpressionHeaders.CONTEXT.toString(),
				SiteImpressionHeaders.IMPRESSION_COST.toString(),
				SiteImpressionHeaders.IMPRESSION_DATE.toString());
		resultSet = stmt.executeQuery(sql);
		rsmd = resultSet.getMetaData();
		
		for (int i =1; i <= rsmd.getColumnCount(); i++) {
			if (!rsmd.getColumnName(i).equals(headerList.get(i-1)))
				throw new CorruptTableException("Site impression table is corrupted, header " + headerList.get(i - 1) + " doesn't exist");
		}
		
		// Check for user table
		sql = "SELECT * FROM user ";
		headerList = Arrays.asList(
				UserTableHeaders.USER_ID.toString(),
				UserTableHeaders.AGE.toString(),
				UserTableHeaders.GENDER.toString(),
				UserTableHeaders.INCOME.toString());
		resultSet = stmt.executeQuery(sql);
		rsmd = resultSet.getMetaData();
		
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			if (!rsmd.getColumnName(i).equals(headerList.get(i-1)))
				throw new CorruptTableException("User table is corrupted, header " + headerList.get(i - 1) + " doesn't exist");
		}
		
		System.out.println("Tables exist");
	}
	
	/**
	 * Insert data into the database
	 *
	 * @param logType type of log file being inserted
	 * @param list    list of arrays of strings of data to be inserted
	 */
	public void insertData(LogType logType, List<String[]> list) {
//		long startTime = System.nanoTime();
		
		try {
			System.out.println(System.nanoTime());
			Connection conn = connect();
			conn.setAutoCommit(false);
			
			String sql;
			
			switch (logType) {
				case CLICK:
					sql = "INSERT INTO click(user_id, click_date, cost) VALUES (?, ?, ?)";
					
					PreparedStatement pstmt = conn.prepareStatement(sql);
					
					for (String[] row : list) {
//						pstmt.setLong(1, Long.parseLong(row[1]));
						pstmt.setString(1, row[1]);
						pstmt.setString(2, row[0]);
//						pstmt.setDouble(3, Double.parseDouble(row[2]));
						pstmt.setString(3, row[2]);
						pstmt.executeUpdate();
					}
					pstmt.close();
					break;
				case IMPRESSION:
					String sqlSiteImpression = "INSERT INTO site_impression(user_id, context, impression_cost, impression_date) VALUES (?, ?, ?, ?)";
					String sqlUser = "INSERT INTO user(user_id, age, gender, income) VALUES (?,?,?,?)";
					
					// Date 0,ID 1,Gender 2, Age 3 ,Income 4,Context 5,Impression Cost 6
					
					PreparedStatement pSiteImpression = conn.prepareStatement(sqlSiteImpression);
					PreparedStatement pUser = conn.prepareStatement(sqlUser);
					
					for (String[] row : list) {
//						pSiteImpression.setLong(1, Long.parseLong(row[1]));
						pSiteImpression.setString(1, row[1]);
						pSiteImpression.setString(2, row[5]);
//						pSiteImpression.setDouble(3, Double.parseDouble(row[6]));
						pSiteImpression.setString(3, row[6]);
						pSiteImpression.setString(4, row[0]);
						pSiteImpression.executeUpdate();

//						pUser.setLong(1, Long.parseLong(row[1]));
						pUser.setString(1, row[1]);
						pUser.setString(2, row[3]);
						pUser.setString(3, row[2]);
						pUser.setString(4, row[4]);
						pUser.executeUpdate();
					}
					pSiteImpression.close();
					pUser.close();
					break;
				case SERVER_LOG:
					sql = "INSERT INTO server_log(user_id, entry_date, exit_date, pages_viewed, conversion) VALUES (?,?,?,?,?)";
					
					// Entry Date 0,ID 1,Exit Date 2,Pages Viewed 3,Conversion 4
					
					PreparedStatement pServerLog = conn.prepareStatement(sql);
					
					for (String[] row : list) {
//						pstmt.setLong(1, Long.parseLong(row[1]));
						pServerLog.setString(1, row[1]);
						pServerLog.setString(2, row[0]);
						pServerLog.setString(3, row[2]);
//						pstmt.setInt(4, Integer.parseInt(row[3]));
						pServerLog.setString(4, row[3]);
						pServerLog.setString(5, row[4]);
						pServerLog.executeUpdate();
					}
					pServerLog.close();
					break;
				default:
					System.out.println("Not a valid input file");
			}
			
			conn.commit();
			conn.close();
			System.out.println(System.nanoTime());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Database insertion complete");
//		long finalTime = System.nanoTime() - startTime;
//		System.out.println("Time taken for " + logType + ": " + (finalTime / 1000000) + "ms = " + (finalTime / 1000000000) + "s");
	}
	
	/**
	 * Get all the data from the impressions table
	 *
	 * @return List of Impression data
	 */
	public List<Impression> selectAllImpressions() {
		List<Impression> impressions = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.SITE_IMPRESSION.toString();
		
		ResultSet resultSet = null;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			long impressionID;
			long userID;
			String context;
			double impressionCost;
			Instant impressionDate;
			Impression i;
			
			while (resultSet.next()) {
				impressionID = resultSet.getLong(1);
				userID = resultSet.getLong(2);
				context = resultSet.getString(3);
				impressionCost = resultSet.getDouble(4);
				impressionDate = this.stringToInstant(resultSet.getString(5));
				
				i = new Impression(impressionID, userID, context, impressionCost, impressionDate);
				impressions.add(i);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return impressions;
	}
	
	/**
	 * Selects all data from click table and returns it
	 *
	 * @return List of all Click data
	 */
	public List<Click> selectAllClicks() {
		List<Click> clicks = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.CLICK.toString();
		
		ResultSet resultSet;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			long clickID;
			long userID;
			Instant clickDate;
			double cost;
			Click c;
			
			while (resultSet.next()) {
				clickID = resultSet.getInt(1);
				userID = resultSet.getInt(2);
				
				clickDate = stringToInstant(resultSet.getString(3));
				
				cost = resultSet.getDouble(4);
				
				c = new Click(clickID, userID, clickDate, cost);
				clicks.add(c);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return clicks;
	}
	
	/**
	 * Selects all data from user database and returns it
	 *
	 * @return List of all User information
	 */
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.USER.toString();
		
		ResultSet resultSet;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			long userID;
			String ageRange;
			Gender gender;
			Income income;
			User u;
			
			while (resultSet.next()) {
				userID = resultSet.getInt(1);
				ageRange = resultSet.getString(2);
				
				gender = Gender.valueOf(resultSet.getString(3));
				
				income = Income.valueOf(resultSet.getString(4));
				
				u = new User(userID, ageRange, gender, income);
				users.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	/**
	 * Gets all data from server_log database and returns it
	 *
	 * @return List of all ServerVisit information
	 */
	public List<ServerVisit> getAllServerVisits() {
		List<ServerVisit> serverVisits = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.SERVER_LOG.toString();
		
		ResultSet resultSet;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			long ServerID;
			long userID;
			Instant entryDate;
			String exitDateString;
			Instant exitDateInstant;
			int pagesViewed;
			boolean conversion;
			ServerVisit sv;
			
			while (resultSet.next()) {
				ServerID = resultSet.getLong(1);
				userID = resultSet.getLong(2);
				entryDate = stringToInstant(resultSet.getString(3));
				
				// Catch the n/a case
				exitDateString = resultSet.getString(4);
				if (exitDateString.equals("n/a")) {
					exitDateInstant = null;
				} else {
					exitDateInstant = stringToInstant(exitDateString);
				}
				
				pagesViewed = resultSet.getInt(5);
				conversion = false;
				try {
					conversion = conversionToBoolean(resultSet.getString(6));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				sv = new ServerVisit(ServerID, userID, entryDate, exitDateInstant, pagesViewed, conversion);
				serverVisits.add(sv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return serverVisits;
	}
	
	/**
	 * Simple class I used to test database queries in the console
	 *
	 * @param resultSet the ResultSet which is gathered from the SQL query
	 */
	public void printToConsole(ResultSet resultSet) {
		try {
			ResultSetMetaData resultData = resultSet.getMetaData();
			System.out.println("Printing Result Set Data...");
			int cols = resultData.getColumnCount();
			
			while (resultSet.next()) {
				for (int i = 1; i <= cols; i++) {
					if (i > 1) System.out.print("   ");
					System.out.print(resultSet.getString(i) + " " + resultData.getColumnName(i));
				}
				System.out.println("");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Simple function to get a unique number of Header from a TableType
	 *
	 * @param header distinct header
	 * @return distinct header count for table
	 */
	public int getNoOfUnique(Header header) {
		String sql = "" +
				"SELECT count(" + header.toString() + ") FROM (" +
				"   SELECT DISTINCT " + header.toString() + " FROM " + header.getTable().toString() +
				")";
		
		ResultSet resultSet;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Takes a time period and returns map mapping each period of time period and a count for clicks in said period.
	 * The instant returned for time will be the earliest time the database finds and will be the full date and time
	 *
	 * @param dateEnum time period to get click count by
	 * @return map mapping time each period of time to count of clicks in said period
	 */
	public Map<Instant, Number> getClickCountPer(DateEnum dateEnum, boolean unique) {
		String sql = null;
		String count = "count(user_id)";
		if (unique) count = "count(DISTINCT user_id)";
		
		switch (dateEnum) {
			case HOURS:
				sql = "SELECT click_date, " + count + " FROM click GROUP BY strftime('%H,%d',click_date) ORDER BY click_date";
				break;
			case DAYS:
				sql = "SELECT click_date, " + count + " FROM click GROUP BY date(click_date) ORDER BY click_date";
				break;
			case WEEKS:
				sql = "SELECT click_date, " + count + " FROM click GROUP BY strftime('%W', click_date) ORDER BY click_date";
				break;
		}
		
		return createMap(sql);
	}
	
	/**
	 * Get bounce number per certain time period
	 * @param dateEnum time period to get number of bounces per
	 * @return map mapping time Instant to number of bounces
	 */
	public Map<Instant, Number> getBounceNumberPer(DateEnum dateEnum) {
		String sql = null;
		
		switch (dateEnum) {
			case HOURS:
				sql = "SELECT entry_date, count(pages_viewed) FROM server_log WHERE pages_viewed = 1 GROUP BY strftime('%H,%d',click_date)";
				break;
			case DAYS:
				sql = "SELECT entry_date, count(pages_viewed) FROM server_log WHERE pages_viewed = 1 GROUP BY date(entry_date)";
				break;
			case WEEKS:
				sql = "SELECT entry_date, count(pages_viewed) FROM server_log WHERE pages_viewed = 1 GROUP BY strftime('%W', entry_date)";
				break;
		}
		return createMap(sql);
	}
	
	public Map<Instant, Number> getCostOfCampaignPer(DateEnum dateEnum) {
		String sql = null;
		
		switch (dateEnum) {
			case HOURS:
				sql = "SELECT ifnull(impression_date, click_date), (SUM(cost) + ifnull(SUM(impression_cost),0))\n" +
						"FROM click\n" +
						"LEFT JOIN site_impression ON strftime('%H,%d',click.click_date) = strftime('%H,%d', site_impression.impression_date)\n" +
						"GROUP BY strftime('%H,%d', click_date)\n" +
						"  UNION\n" +
						"    SELECT ifnull(click_date, impression_date),(SUM(impression_cost) + ifnull(SUM(cost),0))\n" +
						"    FROM site_impression\n" +
						"    LEFT JOIN click ON strftime('%H,%d',site_impression.impression_date) = strftime('%H,%d', click.click_date)\n" +
						"    GROUP BY strftime('%H,%d', impression_date);";
		}
		
		return null;
	}

	//TODO ALL OF THESE NEED TO ALSO HAVE "GET ALL" METHODS WHICH RETURN JUST A NUMBER
	//All these methods need to get the metric per date enum
	
	/**
	 * Get number of conversions for a given time period
	 * @param dateEnum Time period to get conversion number per
	 * @return map mapping Instant date to number of conversions
	 */
	public Map<Instant, Number> getConversionNumberPer(DateEnum dateEnum) {
		String sql = null;
		
		switch (dateEnum) {
			case HOURS:
				sql = "SELECT entry_date, count(conversion) FROM server_log WHERE conversion = 'Yes' GROUP BY strftime('%H,%d', entry_date)";
				break;
			case DAYS:
				sql = "SELECT entry_date, count(conversion) FROM server_log WHERE conversion = 'Yes' GROUP BY date(entry_date)";
				break;
			case WEEKS:
				sql = "SELECT entry_date, count(conversion) FROM server_log WHERE conversion = 'Yes' GROUP BY strftime('%W', entry_date)";
				break;
		}
		
		return createMap(sql);
	}
	
	//Cost per acquisition - total cost/num conversions
	public Map<Instant, Number> getCostPerAcquisitionPer(DateEnum dateEnum) {
		return null;
	}

	//Cost per click - total cost/num click
	// TODO this
	public Map<Instant, Number> getCostPerClickPer(DateEnum dateEnum) {
		
		
		return null;
	}

	//CPM - (cost/impressions) * 1000
	public Map<Instant, Number> getCostPerThousandImpressionsPer(DateEnum dateEnum) {
		return null;
	}

	//CTR - number of clicks/number of impressions
	public Map<Instant, Number> getClickThroughRatePer(DateEnum dateEnum) {
		return null;
	}

	//Can't remember, ask joe
	public Map<Instant, Number> getBounceRatePer(DateEnum dateEnum) {
		return null;
	}
	
	/**
	 * Takes a time period and returns map mapping each period of time period and a count for site impressions in said period.
	 * The instant returned for time will be the earliest time the database finds and will be the full date and time
	 *
	 * @param dateEnum time period to get impression count by
	 * @return map mapping time each period of time to count of impressions in said period
	 */
	public Map<Instant, Number> getImpressionCountPer(DateEnum dateEnum) {
		String sql = null;
		
		switch (dateEnum) {
			case HOURS:
				sql = "SELECT impression_date, count(site_impression_id) FROM site_impression GROUP BY strftime('%H,%d',impression_date) ORDER BY impression_date";
				break;
			case DAYS:
				sql = "SELECT impression_date, count(site_impression_id) FROM site_impression GROUP BY date(impression_date) ORDER BY impression_date";
				break;
			case WEEKS:
				sql = "SELECT impression_date, count(site_impression_id) FROM site_impression GROUP BY strftime('%W', impression_date) ORDER BY impression_date";
				break;
			
		}
		
		return createMap(sql);
	}

	private Map<Attribute, Number> getTotalImpressionsForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getTotalClicksForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getTotalBouncesForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getTotalConversionsForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getTotalUniquesForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getTotalImpressionCostForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getTotalClickCostForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getCPAForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getCPCForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getCPMForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getCTRForAttribute(AttributeType attributeType) {
		return null;
	}

	private Map<Attribute, Number> getBounceRateForAttribute(AttributeType attributeType) {
		return null;
	}
	
	/**
	 * Executes a given sql String Statement and produces a map of the results which it then returns
	 *
	 * @param sql statement to execute
	 * @return HashMap of the time to a given count of that time period
	 */
	private Map<Instant, Number> createMap(String sql) {
		Map<Instant, Number> resultMap = new HashMap<>();
		ResultSet resultSet;
		
		if (sql != null) {
			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				resultSet = stmt.executeQuery(sql);
				
				while (resultSet.next()) {
					resultMap.put(stringToInstant(resultSet.getString(1)), resultSet.getDouble(2));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	private Map<LocalDateTime, Number> createMapWithLocalDateTime(String sql) {
		Map<LocalDateTime, Number> resultMap = new HashMap<>();
		ResultSet resultSet;
		
		if (sql != null) {
			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				resultSet = stmt.executeQuery(sql);
				
				while (resultSet.next()) {
					resultMap.put(stringToDateTime(resultSet.getString(1)), resultSet.getDouble(2));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * Literal dirt: Returns map of total cost for given time period
	 * @param dateEnum Time period to get costs for
	 * @return Map of Instant date to cost for that period
	 */
	public Map<Instant, Number> getTotalCostPer(DateEnum dateEnum) {
		String sqlClick = null;
		String sqlImpression = null;
		Map<Instant, Number> resultMap = null;
		
		switch (dateEnum) {
			case HOURS:
				sqlClick = "SELECT click_date, SUM(cost) FROM click GROUP BY strftime('%H,%d', click_date);";
				sqlImpression = "SELECT impression_date, SUM(impression_cost) FROM site_impression GROUP BY strftime('%H,%d', impression_date);";
				break;
			case DAYS:
				sqlClick = "SELECT click_date, SUM(cost) FROM click GROUP BY date(click_date);";
				sqlImpression = "SELECT impression_date, SUM(impression_cost) FROM site_impression GROUP BY strftime('%d', impression_date);";
				break;
			case WEEKS:
				sqlClick = "SELECT click_date, SUM(cost) FROM click GROUP BY strftime('%W', click_date);";
				sqlImpression = "SELECT impression_date, SUM(impression_cost) FROM site_impression GROUP BY strftime('%W', impression_date);";
				break;
		}
		
//		if (sqlClick != null && sqlImpression != null) {
//			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
//				ResultSet rsClick = stmt.executeQuery(sqlClick);
//				printToConsole(rsClick);
//				ResultSet rsImpression = stmt.executeQuery(sqlImpression);
//
//				resultMap = new HashMap<>();
//				while (rsClick.next()) {
//					resultMap.put(stringToInstant(rsClick.getString(2)), (rsClick.getDouble(3) + rsImpression.getDouble(3)));
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		
		Map<LocalDateTime, Number> clickMap = createMapWithLocalDateTime(sqlClick);
		Map<LocalDateTime, Number> impressionMap = createMapWithLocalDateTime(sqlImpression);
		
		clickMap.forEach((date, num) -> {
			date.
			
		});
		
		return resultMap;
	}
	
	/**
	 * Simple conversion that takes a String date from the database and converts it to a Java Instant readable format
	 * and returns it.
	 *
	 * @param dateToParse date from database in String format to parse
	 * @return Java 8 Instant form of said date
	 */
	private Instant stringToInstant(String dateToParse) {
		return Instant.parse(dateToParse.replace(" ", "T") + "Z");
	}
	
	private LocalDateTime stringToDateTime(String dateToParse) {
		Instant ins = stringToInstant(dateToParse);
		return LocalDateTime.ofInstant(ins, ZoneId.systemDefault());
	}
	
	/**
	 * Simple conversion taking a conversion String from the database and converts it to a boolean format
	 *
	 * @param conversion Conversion String
	 * @return
	 * @throws Exception
	 */
	private boolean conversionToBoolean(String conversion) throws Exception {
		switch (conversion) {
			case "Yes":
			case "yes":
				return true;
			case "No":
			case "no":
				return false;
			default:
				throw new Exception("Conversion is not of type \"yes/no\"");
		}
	}
	
	/**
	 * Simple method to wipe all the tables if need be
	 *
	 * @param logType the log to which the tables that need deleting are attached
	 */
	//TODO This is gross but I'm making it public until we can talk about a better management system
	//TODO Josh
	public void wipeTable(LogType logType) {
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			
			switch (logType) {
				case CLICK:
					stmt.execute(DatabaseStatements.DROP_CLICK.getStatement());
					stmt.execute(DatabaseStatements.CREATE_CLICK.getStatement());
					break;
				case IMPRESSION:
					stmt.execute(DatabaseStatements.DROP_USER.getStatement());
					stmt.execute(DatabaseStatements.CREATE_USER.getStatement());
					stmt.execute(DatabaseStatements.DROP_SITE_IMPRESSION.getStatement());
					stmt.execute(DatabaseStatements.CREATE_SITE_IMPRESSION.getStatement());
					break;
				case SERVER_LOG:
					stmt.execute(DatabaseStatements.DROP_SERVER_LOG.getStatement());
					stmt.execute(DatabaseStatements.CREATE_SERVER_LOG.getStatement());
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
