package Model;

import DataStructures.ClickLog;
import DataStructures.CsvInterfaces.Gender;
import DataStructures.CsvInterfaces.Income;
import Model.DBEnums.DatabaseStatements;
import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Model.DBEnums.TableType;
import Model.DBEnums.headers.ClickTableHeaders;
import Model.DBEnums.headers.Header;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Model.TableModels.ServerVisit;
import Model.TableModels.User;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.util.*;

/**
 * Created by Philip on 26/02/2017.
 * <p>
 * Do not run this file unless JDBC-sqlite driver is configured
 */
public class DatabaseManager {
	
	private String filename;
	private String url;
	
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
	
	public void saveDB() {}
	
	/**
	 * Loads a database. Pass in a file name and it will attempt to create a connection to it, will throw Exception if
	 * it fails
	 * @param location file path of the database
	 * @return boolean whether it finds the file or not
	 * @throws SQLException if it fails to create a connection to the database
	 */
	public boolean loadDB(String location) throws SQLException {
		filename  = location;
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
	
	private void testForTables(Connection conn) {
		String sql;
		List<Header> headerList = Arrays.asList(
				ClickTableHeaders.CLICK_ID,
				ClickTableHeaders.USER_ID,
				ClickTableHeaders.CLICK_DATE,
				ClickTableHeaders.COST);
		ResultSet resultSet;
		ResultSetMetaData rsmd;
		
		// Check for click table
		sql = "SELECT * FROM click LIMIT 1";
		headerList = Arrays.asList();
		try (Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			rsmd = resultSet.getMetaData();
			
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
//		System.out.println(sql);
		
		return createMap(sql);
	}
	
	/**
	 * Takes a time period and returns map mapping each period of time period and a count for site impressions in said period.
	 * The instant returned for time will be the earliest time the database finds and will be the full date and time
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
	
	/**
	 * Executes a given sql String Statement and produces a map of the results which it then returns
	 * @param sql statement to execute
	 * @return HashMap of the time to a given count of that time period
	 */
	private Map<Instant, Number> createMap(String sql) {
		Map<Instant, Number> resultMap = new HashMap<>();
		ResultSet resultSet;
		
		if (sql != null) {
			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				resultSet = stmt.executeQuery(sql);
//				printToConsole(resultSet);
				
				while (resultSet.next()) {
					resultMap.put(stringToInstant(resultSet.getString(1)), resultSet.getInt(2));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
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
