
package Model;

import Controllers.DashboardMainFrameController;
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
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.scene.chart.NumberAxis;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
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
		file.getParentFile().mkdirs();
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

	private Number getSingleMetric(String sql) {
		ResultSet result;
		Number numResult = 0;

		if (sql != null) {
			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				result = stmt.executeQuery(sql);
				numResult = result.getDouble(1);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return numResult;
	}

	public Map<AttributeType, List<String>> getAllValuesOfAttributes() {
	    Map<AttributeType, List<String>> resultMap = new HashMap<>();

	    for (AttributeType attr: AttributeType.values()) {
	        resultMap.put(attr, this.getValuesOfAttribute(attr));
        }

        return resultMap;
    }

	private List<String> getValuesOfAttribute(AttributeType attr) {
	    List<String> values = new ArrayList<>();
		ResultSet result;

        String table = attr.equals(AttributeType.CONTEXT) ? TableType.SITE_IMPRESSION.toString() : TableType.USER.toString();

		String sql = "SELECT DISTINCT " + attr.getQueryBit() + " FROM " + table + ";";

		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
		    result = stmt.executeQuery(sql);
		    while (result.next()) {
		        values.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return values;
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

	//===============================================================================
    //=====================SINGLE METRIC METHODS=====================================
    //===============================================================================

	public Number getTotalCampaignCost() {
		String impressionSql = "SELECT SUM(impression_cost) FROM site_impression;";
		String clickSql = "SELECT SUM(cost) FROM click;";

		Number impressionCost = getSingleMetric(impressionSql);
		Number clickCost = getSingleMetric(clickSql);

		return impressionCost.doubleValue() + clickCost.doubleValue();
	}

	public Number getTotalImpressions() {
	    String sql = "SELECT count(site_impression_id) FROM site_impression;";

	    return getSingleMetric(sql);
    }

    public Number getTotalClicks() {
	    String sql = "SELECT count(click_id) FROM click;";

	    return getSingleMetric(sql);
    }

    public Number getTotalBounces() {
	    String sql = "SELECT count(server_log_id) FROM server_log WHERE pages_viewed = 1;";

	    return getSingleMetric(sql);
    }

    public Number getTotalConversions() {
	    String sql = "SELECT count(server_log_id) FROM server_log WHERE conversion = 'Yes';";

	    return getSingleMetric(sql);
    }

    public Number getTotalUniques() {
	    String sql = "SELECT count(DISTINCT user_id) FROM click;";

	    return getSingleMetric(sql);
    }

    public Number getCPA() {
	    Number totalCost = getTotalCampaignCost();
	    Number conversions = getTotalConversions();

	    return totalCost.doubleValue() / conversions.doubleValue();
    }

    public Number getCPC() {
	    Number totalClicks = getTotalClicks();
	    Number totalCost = getTotalCampaignCost();

	    return totalCost.doubleValue() / totalClicks.doubleValue();
    }

    public Number getCPM() {
	    Number totalImpressions = getTotalImpressions();
	    Number totalCost = getTotalCampaignCost();

	    return (totalCost.doubleValue() / totalImpressions.doubleValue()) * 1000;
    }

    public Number getBounceRate() {
	    Number totalBounces = getTotalBounces();
	    Number totalClicks = getTotalClicks();

	    return (totalBounces.doubleValue() / totalClicks.doubleValue());
    }

    public Number getCTR() {
	    Number totalClicks = getTotalClicks();
	    Number totalImpressions = getTotalImpressions();

	    return (totalClicks.doubleValue() / totalImpressions.doubleValue());
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
		
		return DBUtils.truncateInstantMap(createMap(sql), dateEnum);
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
		return DBUtils.truncateInstantMap(createMap(sql), dateEnum);
	}
	
	public Map<LocalDateTime, Number> getCostOfImpressionsPer(DateEnum dateEnum) {
		String sql = null;
		
		switch (dateEnum) {
			case HOURS:
				sql = "SELECT impression_date, SUM(impression_cost) " +
						"FROM site_impression " +
						"GROUP BY strftime('%H,%d', impression_date);";
				break;
            case DAYS:
                sql = "SELECT impression_date, SUM(impression_cost) " +
                        "FROM site_impression " +
                        "GROUP BY strftime('%d', impression_date);";
                break;
            case WEEKS:
                sql = "SELECT impression_date, SUM(impression_cost) " +
                        "FROM site_impression " +
                        "GROUP BY strftime('%W', impression_date);";
                break;
		}
		
		return createMapWithLocalDateTime(sql, dateEnum);
	}

	public Map<LocalDateTime, Number> getCostOfClicksPer(DateEnum dateEnum) {
	    String sql = null;

        switch (dateEnum) {
            case HOURS:
                sql = "SELECT click_date, SUM(cost) " +
                        "FROM click " +
                        "GROUP BY strftime('%H,%d', click_date);";
                break;
            case DAYS:
                sql = "SELECT click_date, SUM(cost) " +
                        "FROM click " +
                        "GROUP BY strftime('%d', click_date);";
                break;
            case WEEKS:
                sql = "SELECT click_date, SUM(cost) " +
                        "FROM click " +
                        "GROUP BY strftime('%W', click_date);";
                break;
        }

        return createMapWithLocalDateTime(sql, dateEnum);
    }

    public Map<Instant, Number> getTotalCostPer(DateEnum dateEnum) {
	    Map<LocalDateTime, Number> impressionMap = getCostOfImpressionsPer(dateEnum);
	    Map<LocalDateTime, Number> clickMap = getCostOfClicksPer(dateEnum);
        Map<Instant, Number> resultMap = new HashMap<>();

        impressionMap.forEach((date, val) -> {
            resultMap.put(date.toInstant(ZoneOffset.UTC), val);
        });

        clickMap.forEach((date, val) -> {
            if (resultMap.containsKey(date.toInstant(ZoneOffset.UTC))) {
                resultMap.put(date.toInstant(ZoneOffset.UTC), val.doubleValue() + resultMap.get(date.toInstant(ZoneOffset.UTC)).doubleValue());
            }
        });

        return resultMap;

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
		
		return DBUtils.truncateInstantMap(createMap(sql), dateEnum);
	}
	
	//Cost per acquisition - total cost/num conversions
	public Map<Instant, Number> getCPAPer(DateEnum dateEnum) {
        Map<Instant, Number> costMap = DBUtils.truncateInstantMap(getTotalCostPer(dateEnum), dateEnum);
        Map<Instant, Number> conversionMap = DBUtils.truncateInstantMap(getConversionNumberPer(dateEnum), dateEnum);
        Map<Instant, Number> resultMap = new HashMap<>();

        costMap.forEach(resultMap::put);

        conversionMap.forEach((date, val) -> {
            resultMap.put(date, costMap.get(date).doubleValue() / val.doubleValue());
        });

        return resultMap;

	}

	//Cost per click - total cost/num click
	public Map<Instant, Number> getCPCPer(DateEnum dateEnum) {
		Map<Instant, Number> costMap = DBUtils.truncateInstantMap(getTotalCostPer(dateEnum), dateEnum);
		Map<Instant, Number> clickMap = DBUtils.truncateInstantMap(getClickCountPer(dateEnum, false), dateEnum);
		Map<Instant, Number> resultMap = new HashMap<>();

		costMap.forEach(resultMap::put);

		clickMap.forEach((date, val) -> resultMap.put(date, costMap.get(date).doubleValue() / val.doubleValue()));

		return resultMap;

	}

	//CPM - (cost/impressions) * 1000
	public Map<Instant, Number> getCPMPer(DateEnum dateEnum) {
        Map<Instant, Number> costMap = DBUtils.truncateInstantMap(getTotalCostPer(dateEnum), dateEnum);
        Map<Instant, Number> impressionMap = DBUtils.truncateInstantMap(getImpressionCountPer(dateEnum), dateEnum);
        Map<Instant, Number> resultMap = new HashMap<>();

        costMap.forEach((date, val) -> resultMap.put(date, (val.doubleValue() / impressionMap.get(date).doubleValue()) * 1000));

        return resultMap;
	}

	//CTR - number of clicks/number of impressions
	public Map<Instant, Number> getCTRPer(DateEnum dateEnum) {
		Map<Instant, Number> clickMap = DBUtils.truncateInstantMap(getClickCountPer(dateEnum, false), dateEnum);
		Map<Instant, Number> impressionMap = DBUtils.truncateInstantMap(getImpressionCountPer(dateEnum), dateEnum);
        Map<Instant, Number> resultMap = new HashMap<>();

        clickMap.forEach((date, val) -> resultMap.put(date, (val.doubleValue() / impressionMap.get(date).doubleValue())));

        return resultMap;
	}

	//Can't remember, ask joe
	public Map<Instant, Number> getBounceRatePer(DateEnum dateEnum) {
		Map<Instant, Number> bounceMap = DBUtils.truncateInstantMap(getBounceNumberPer(dateEnum), dateEnum);
		Map<Instant, Number> clickMap = DBUtils.truncateInstantMap(getClickCountPer(dateEnum, false), dateEnum);
		Map<Instant, Number> resultMap = new HashMap<>();

		bounceMap.forEach((date, val) -> resultMap.put(date, val.doubleValue() / clickMap.get(date).doubleValue()));

		return resultMap;
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
		
		return DBUtils.truncateInstantMap(createMap(sql), dateEnum);
	}

	public Map<String, Number> getTotalImpressionsForAttribute(AttributeType attributeType) {
		String sql = null;

		if (attributeType.equals(AttributeType.CONTEXT)) {
			sql = "SELECT " + attributeType.getQueryBit() + ", count(site_impression_id) FROM site_impression GROUP BY " +
					" context";
		}
		else {
			sql = "SELECT " + attributeType.getQueryBit() + ", count(site_impression_id) FROM site_impression JOIN " +
					" user ON site_impression.user_id = user.user_id GROUP BY " + attributeType.getQueryBit() + ";";
		}

		return createAttributeMap(sql);


	}

	public Map<String, Number> getTotalClicksForAttribute(AttributeType attributeType) {
		String sql = null;

		if (attributeType.equals(AttributeType.CONTEXT)) {
			sql = "SELECT " + attributeType.getQueryBit() + ", count(click_id) FROM click " +
					"JOIN site_impression ON click.user_id = site_impression.user_id " +
					"GROUP BY " + attributeType.getQueryBit() + ";";
		} else {
			sql = "SELECT " + attributeType.getQueryBit() + ", count(click_id) FROM click " +
					"JOIN user ON click.user_id = user.user_id GROUP BY " + attributeType.getQueryBit() + ";";
		}

		return createAttributeMap(sql);
	}

	public Map<String, Number> getTotalBouncesForAttribute(AttributeType attributeType) {
		String sql = null;

		if (attributeType.equals(AttributeType.CONTEXT)) {
		    sql = "SELECT " + attributeType.getQueryBit() + ", count(server_log_id)  FROM " +
                    "server_log JOIN site_impression ON server_log.user_id = site_impression.user_id  WHERE pages_viewed = 1" +
                    " GROUP BY " + attributeType.getQueryBit() + ";";
        } else {
            sql = "SELECT " + attributeType.getQueryBit() + ", count(server_log_id) FROM " +
                    "server_log JOIN user ON server_log.user_id = user.user_id WHERE pages_viewed = 1 "  +
                    "GROUP BY " + attributeType.getQueryBit() + ";";
        }
		return createAttributeMap(sql);
	}

	public Map<String, Number> getTotalConversionsForAttribute(AttributeType attributeType) {

	    String sql;

		if (attributeType.equals(AttributeType.CONTEXT)) {
		    sql = "SELECT " + attributeType.getQueryBit() + ", count(server_log_id) FROM " +
                    "server_log JOIN site_impression ON server_log.user_id = site_impression.user_id WHERE conversion = 'Yes' GROUP BY " + attributeType.getQueryBit() + ";";
        } else {
		    sql = "SELECT " + attributeType.getQueryBit() + ", count(server_log_id) FROM " +
                    "server_log JOIN user ON server_log.user_id = user.user_id WHERE conversion = 'Yes' GROUP BY " + attributeType.getQueryBit() + ";";
        }

        return createAttributeMap(sql);
	}

	public Map<String, Number> getTotalUniquesForAttribute(AttributeType attributeType) {
        String sql = null;

        if (attributeType.equals(AttributeType.CONTEXT)) {
            sql = "SELECT " + attributeType.getQueryBit() + ", count(DISTINCT click.user_id) FROM click " +
                    "JOIN site_impression ON click.user_id = site_impression.user_id " +
                    "GROUP BY " + attributeType.getQueryBit() + ";";
        } else {
            sql = "SELECT " + attributeType.getQueryBit() + ", count(DISTINCT click.user_id) FROM click " +
                    "JOIN user ON click.user_id = user.user_id GROUP BY " + attributeType.getQueryBit() + ";";
        }

        return createAttributeMap(sql);
	}

	public Map<String, Number> getTotalImpressionCostForAttribute(AttributeType attributeType) {
        String sql;

        if (attributeType.equals(AttributeType.CONTEXT)) {
            sql = "SELECT " + attributeType.getQueryBit() + ", SUM(impression_cost) FROM site_impression " +
                    "GROUP BY " + attributeType.getQueryBit() + ";";
        } else {
            sql = "SELECT " + attributeType.getQueryBit() + ", SUM(impression_cost) FROM " +
                    "site_impression JOIN user ON site_impression.user_id = user.user_id " +
                    "GROUP BY " + attributeType.getQueryBit() + ";";
        }

        return createAttributeMap(sql);
	}

	public Map<String, Number> getTotalClickCostForAttribute(AttributeType attributeType) {
        String sql;

        if (attributeType.equals(AttributeType.CONTEXT)) {
            sql = "SELECT " + attributeType.getQueryBit() + ", SUM(cost) FROM click " +
                    "JOIN site_impression ON click.user_id = site_impression.user_id " +
                    "GROUP BY " + attributeType.getQueryBit() + ";";
        } else {
            sql = "SELECT " + attributeType.getQueryBit() + ", SUM(cost) FROM " +
                    "click JOIN user ON click.user_id = user.user_id " +
                    "GROUP BY " + attributeType.getQueryBit() + ";";
        }

        return createAttributeMap(sql);
	}

	public Map<String, Number> getTotalCostForAttribute(AttributeType attributeType) {

	    Map<String, Number> clickMap = getTotalClickCostForAttribute(attributeType);
	    Map<String, Number> impressionMap = getTotalImpressionCostForAttribute(attributeType);

	    clickMap.forEach((attr, value) -> {
            if (impressionMap.containsKey(attr)) {
                impressionMap.put(attr, (impressionMap.get(attr).doubleValue() + value.doubleValue()));
            } else {
                impressionMap.put(attr, value);
            }
        });

        return impressionMap;
    }

	public Map<String, Number> getCPAForAttribute(AttributeType attributeType) {
		Map<String, Number> costMap = getTotalCostForAttribute(attributeType);
		Map<String, Number> conversionMap = getTotalConversionsForAttribute(attributeType);

        costMap.forEach((attr, value) -> {
            if (conversionMap.containsKey(attr)) {
                conversionMap.put(attr, (value.doubleValue() / conversionMap.get(attr).doubleValue()));
            } else {
                conversionMap.put(attr, 0.0);
            }
        });

		return conversionMap;
	}

	public Map<String, Number> getCPCForAttribute(AttributeType attributeType) {
		Map<String, Number> costMap = getTotalCostForAttribute(attributeType);
		Map<String, Number> clickMap = getTotalClicksForAttribute(attributeType);

		costMap.forEach((attr, value) -> {
		    if (clickMap.containsKey(attr)) {
                clickMap.put(attr, (value.doubleValue() / clickMap.get(attr).doubleValue()));
            } else {
		        clickMap.put(attr, 0.0);
            }
        });

		return clickMap;
	}

	public Map<String, Number> getCPMForAttribute(AttributeType attributeType) {
		Map<String, Number> costMap = getTotalCostForAttribute(attributeType);
		Map<String, Number> impressionMap = getTotalImpressionsForAttribute(attributeType);

		costMap.forEach((attr, value) -> {
		    impressionMap.put(attr, ((value.doubleValue() / impressionMap.get(attr).doubleValue()) * 1000));
        });

		return impressionMap;
	}

	public Map<String, Number> getCTRForAttribute(AttributeType attributeType) {
		Map<String, Number> clickMap = getTotalClicksForAttribute(attributeType);
		Map<String, Number> impressionMap = getTotalImpressionsForAttribute(attributeType);

        impressionMap.forEach((attr, value) -> {
		    if (clickMap.containsKey(attr)) {
		        clickMap.put(attr, clickMap.get(attr).doubleValue() / value.doubleValue());
            } else {
		        clickMap.put(attr, 0.0);
            }
        });

        return clickMap;
	}

	public Map<String, Number> getBounceRateForAttribute(AttributeType attributeType) {
		Map<String, Number> bounceMap = getTotalBouncesForAttribute(attributeType);
		Map<String, Number> clickMap = getTotalClicksForAttribute(attributeType);

        System.out.println(bounceMap);
        System.out.println(clickMap);

        clickMap.forEach((attr, value) -> {
            if (bounceMap.containsKey(attr)) {
                bounceMap.put(attr, (bounceMap.get(attr).doubleValue() / value.doubleValue()));
            } else {
                bounceMap.put(attr, 0.0);
            }
        });

        return bounceMap;
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
	
	private Map<LocalDateTime, Number> createMapWithLocalDateTime(String sql, DateEnum dateEnum) {
		Map<LocalDateTime, Number> resultMap = new HashMap<>();
		ResultSet resultSet;
		
		if (sql != null) {
			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				resultSet = stmt.executeQuery(sql);
				
				switch(dateEnum) {
                    case DAYS:
                        while (resultSet.next()) {
                            resultMap.put(stringToDateTime(resultSet.getString(1)).truncatedTo(ChronoUnit.DAYS), resultSet.getDouble(2));
                        }
                        break;
                    case HOURS:
                        while (resultSet.next()) {
                            resultMap.put(stringToDateTime(resultSet.getString(1)).truncatedTo(ChronoUnit.HOURS), resultSet.getDouble(2));
                        }
                        break;
                    case WEEKS:
                        while (resultSet.next()) {
                            resultMap.put(stringToDateTime(resultSet.getString(1)).truncatedTo(ChronoUnit.WEEKS), resultSet.getDouble(2));
                        }
                        break;
                }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}

	private Map<String, Number> createAttributeMap(String sql) {
		Map<String, Number> resultMap = new HashMap<>();
		ResultSet resultSet;

		if (sql != null) {
			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				resultSet = stmt.executeQuery(sql);

				while (resultSet.next()) {
					resultMap.put(resultSet.getString(1), resultSet.getDouble(2));
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

	public List<LogType> getAvailableLogsFromTables() {
	    List<LogType> logs = new ArrayList<>();
	    String sql = "SELECT COUNT(*) FROM site_impression;";
	    Number test = getSingleMetric(sql);

	    if (test.intValue() != 0) {
            logs.add(LogType.IMPRESSION);
        }

        sql = "SELECT COUNT(*) FROM click;";
	    test = getSingleMetric(sql);

	    if (test.intValue() != 0) {
	        logs.add(LogType.CLICK);
        }

        sql = "SELECT COUNT(*) FROM server_log;";
	    test = getSingleMetric(sql);

	    if (test.intValue() != 0) {
	        logs.add(LogType.SERVER_LOG);
        }

        return logs;
    }
}
