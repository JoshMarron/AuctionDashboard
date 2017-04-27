
package Model;

import Controllers.ProjectSettings;
import Controllers.Queries.*;
import Controllers.Results.AttributeQueryResult;
import Controllers.Results.QueryResult;
import Controllers.Results.TimeQueryResult;
import Controllers.Results.TotalQueryResult;
import DataStructures.CsvInterfaces.Gender;
import DataStructures.CsvInterfaces.Income;
import Model.DBEnums.DatabaseStatements;
import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Model.DBEnums.TableType;
import Model.DBEnums.headers.*;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Model.TableModels.ServerVisit;
import Model.TableModels.User;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.io.File;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
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
	    String sql = "SELECT count(server_log_id) FROM server_log WHERE pages_viewed <= " + ProjectSettings.getBouncePages() +
		" AND ( ((strftime('%s', exit_date) - strftime('%s','1970-01-01 00:00:00'))) " +
				"- ((strftime('%s', exit_date) - strftime('%s','1970-01-01 00:00:00'))) ) <= " + ProjectSettings.getBounceSeconds() + ";";

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

	// TODO replace the test methods using above to using below

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

    public synchronized QueryResult resolveQuery(Query q) {

		System.out.println(q);

		if (q instanceof TimeDataQuery) {
			return resolveTimeDataQuery((TimeDataQuery) q);
		} else if (q instanceof AttributeDataQuery) {
			return resolveAttributeDataQuery((AttributeDataQuery) q);
		} else if (q instanceof TotalQuery) {
			return resolveTotalQuery((TotalQuery) q);
		}

		System.out.println("failed");
		return null;
	}

	private TotalQueryResult resolveTotalQuery(TotalQuery q) {
		String sql = null;

		switch (q.getMetric()) {
			case TOTAL_IMPRESSIONS:
				sql = "SELECT count(site_impression_id) " +
						"FROM site_impression " +
						"JOIN user ON site_impression.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "impression_date") +
						this.setFilters(q) + ";";
				break;
			case TOTAL_CLICKS:
				sql = "SELECT count(click_id) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) + ";";
				break;
			case TOTAL_UNIQUES:
				sql = "SELECT count( DISTINCT click.user_id) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) + ";";
				break;
			case TOTAL_BOUNCES:
				sql = "SELECT count(server_log.server_log_id) " +
						"FROM server_log " +
						"JOIN user ON server_log.user_id = user.user_id " +
						"JOIN site_impression ON user.user_id = site_impression.user_id " +
						"JOIN ( " +
						"SELECT " +
						"server_log_id, " +
						"CASE " +
						"WHEN exit_date = \"n/a\" " +
						"THEN pages_viewed <= " + ProjectSettings.getBouncePages() + " " +
						"ELSE " +
						"pages_viewed <= " + ProjectSettings.getBouncePages() + " " +
						"OR ( (strftime('%s', exit_date)) " +
						"- (strftime('%s', entry_date)) ) <=  " + ProjectSettings.getBounceSeconds() +
						" END bounce " +
						"FROM server_log " +
						") aux ON aux.server_log_id = server_log.server_log_id AND bounce = 1 " +
						"WHERE " +
						this.setBetween(q, "entry_date") +
						this.setFilters(q) + ";";
				break;
			case TOTAL_CONVERSIONS:
				sql = "SELECT count(server_log_id) " +
						"FROM server_log " +
						"JOIN user ON server_log.user_id = user.user_id " +
						"JOIN site_impression ON server_log.user_id = site_impression.user_id " +
						"WHERE conversion = 'Yes' AND " +
						this.setBetween(q, "entry_date") +
						this.setFilters(q) + ";";
				break;
			case CPA:  // total cost / total conversion
				TotalQuery tqrCostCPA = q.deriveQuery(MetricType.TOTAL_COST);
				TotalQueryResult costCPA = resolveTotalQuery(tqrCostCPA);

				TotalQuery tqrConversionCPA = q.deriveQuery(MetricType.TOTAL_CONVERSIONS);
				TotalQueryResult conversionCPA = resolveTotalQuery(tqrConversionCPA);

				return new TotalQueryResult(q.getMetric(), costCPA.getData().doubleValue() / conversionCPA.getData().doubleValue());
			case CPC: // total cost / total clicks
				TotalQuery tqrCostCPC = q.deriveQuery(MetricType.TOTAL_COST);
				TotalQueryResult costCPC = resolveTotalQuery(tqrCostCPC);

				TotalQuery tqrClicksCPC = q.deriveQuery(MetricType.TOTAL_CLICKS);
				TotalQueryResult clicksCPC = resolveTotalQuery(tqrClicksCPC);

				return new TotalQueryResult(q.getMetric(), costCPC.getData().doubleValue() / clicksCPC.getData().doubleValue());
			case CPM: // 1000 * ( total cost / total impressions ):
				TotalQuery tqrCostCPM = q.deriveQuery(MetricType.TOTAL_COST);
				TotalQueryResult costCPM = resolveTotalQuery(tqrCostCPM);

				TotalQuery tqrImpressionsCPM = q.deriveQuery(MetricType.TOTAL_IMPRESSIONS);
				TotalQueryResult impressionsCPM = resolveTotalQuery(tqrImpressionsCPM);

				return new TotalQueryResult(q.getMetric(), 1000 * costCPM.getData().doubleValue() / impressionsCPM.getData().doubleValue());
			case BOUNCE_RATE: // total bounces / total clicks
				TotalQuery tqrBounceBR = q.deriveQuery(MetricType.TOTAL_BOUNCES);
				TotalQueryResult bounceBR = resolveTotalQuery(tqrBounceBR);

				TotalQuery tqrClicksBR = q.deriveQuery(MetricType.TOTAL_CLICKS);
				TotalQueryResult clicksBR = resolveTotalQuery(tqrClicksBR);

				return new TotalQueryResult(q.getMetric(), bounceBR.getData().doubleValue() / clicksBR.getData().doubleValue());
			case CTR: // total clicks / total impressions
				TotalQuery tqrClickCTR = q.deriveQuery(MetricType.TOTAL_CLICKS);
				TotalQueryResult clickCTR = resolveTotalQuery(tqrClickCTR);

				TotalQuery tqrImpressionsCTR = q.deriveQuery(MetricType.TOTAL_IMPRESSIONS);
				TotalQueryResult impressionsCTR = resolveTotalQuery(tqrImpressionsCTR);

				return new TotalQueryResult(q.getMetric(), clickCTR.getData().doubleValue() / impressionsCTR.getData().doubleValue());
			case TOTAL_COST:
				String impressionSql = "SELECT SUM(impression_cost) " +
						"FROM site_impression " +
						"JOIN user ON site_impression.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "impression_date") +
						this.setFilters(q) + ";";
				String clickSql = "SELECT SUM(cost) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"JOIN site_impression ON click.user_id = site_impression.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) + ";";
				Number impressionCost = getSingleMetric(impressionSql);
				Number clickCost = getSingleMetric(clickSql);

				return new TotalQueryResult(q.getMetric(), impressionCost.doubleValue() + clickCost.doubleValue());
		}
		System.out.println(sql);

		return new TotalQueryResult(q.getMetric(), getSingleMetric(sql));
	}

	private AttributeQueryResult resolveAttributeDataQuery(AttributeDataQuery q) {
		String sql = null;
		String att = q.getAttribute().getQueryBit();

		switch (q.getMetric()) {
			case TOTAL_IMPRESSIONS:
				sql = "SELECT " + att + ", count(site_impression_id) " +
						"FROM site_impression " +
						"JOIN user ON site_impression.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "impression_date") +
						this.setFilters(q) +
						" GROUP BY " + att + ";";
				break;
			case TOTAL_CLICKS:
				sql = "SELECT " + att + ", count(click_id) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"JOIN site_impression ON click.user_id = site_impression.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) +
						" GROUP BY " + att + ";";
				break;
			case TOTAL_UNIQUES:
				sql = "SELECT " + att + ", count( DISTINCT click.user_id) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"JOIN site_impression ON click.user_id = site_impression.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) +
						" GROUP BY " + att + ";";
				break;
			case TOTAL_BOUNCES:
				sql = "SELECT " + att + ", count(server_log.server_log_id) " +
						"FROM server_log " +
						"JOIN user ON server_log.user_id = user.user_id " +
						"JOIN site_impression ON user.user_id = site_impression.user_id " +
						"JOIN ( " +
						"SELECT " +
						"server_log_id, " +
						"CASE " +
						"WHEN exit_date = \"n/a\" " +
						"THEN pages_viewed <= " + ProjectSettings.getBouncePages() + " " +
						"ELSE " +
						"pages_viewed <= " + ProjectSettings.getBouncePages() + " " +
						"OR ( (strftime('%s', exit_date)) " +
						"- (strftime('%s', entry_date)) ) <=  " + ProjectSettings.getBounceSeconds() +
						" END bounce " +
						"FROM server_log " +
						") aux ON aux.server_log_id = server_log.server_log_id AND bounce = 1 " +
						"WHERE " +
						this.setBetween(q, "entry_date") +
						this.setFilters(q) +
						"GROUP BY " + att + ";";
				break;
			case TOTAL_CONVERSIONS:
				sql = "SELECT " + att + ", count(server_log_id) " +
						"FROM server_log " +
						"JOIN user ON server_log.user_id = user.user_id " +
						"JOIN site_impression ON server_log.user_id = site_impression.user_id " +
						"WHERE conversion = 'Yes' AND " +
						this.setBetween(q, "entry_date") +
						this.setFilters(q) +
						" GROUP BY " + att + ";";
				break;
			case CPA: // total cost / total conversion
				AttributeDataQuery tqrCostCPA = q.deriveQuery(MetricType.TOTAL_COST, q.getAttribute());
				AttributeQueryResult costCPA = resolveAttributeDataQuery(tqrCostCPA);

				AttributeDataQuery tqrConversionCPA = q.deriveQuery(MetricType.TOTAL_CONVERSIONS, q.getAttribute());
				AttributeQueryResult conversionCPA = resolveAttributeDataQuery(tqrConversionCPA);

				costCPA.getData().forEach((attr, value) -> {
					if (conversionCPA.getData().containsKey(attr)) {
						conversionCPA.getData().put(attr, (value.doubleValue() / conversionCPA.getData().get(attr).doubleValue()));
					} else {
						conversionCPA.getData().put(attr, 0.0);
					}
				});

				return new AttributeQueryResult(q.getMetric(), conversionCPA.getData());
			case CPC: // total cost / total clicks
				AttributeDataQuery tqrCostCPC = q.deriveQuery(MetricType.TOTAL_COST, q.getAttribute());
				AttributeQueryResult costCPC = resolveAttributeDataQuery(tqrCostCPC);

				AttributeDataQuery tqrClickCPC = q.deriveQuery(MetricType.TOTAL_CLICKS, q.getAttribute());
				AttributeQueryResult clickCPC = resolveAttributeDataQuery(tqrClickCPC);

				costCPC.getData().forEach((attr, value) -> {
					if (clickCPC.getData().containsKey(attr)) {
						clickCPC.getData().put(attr, (value.doubleValue() / clickCPC.getData().get(attr).doubleValue()));
					} else {
						clickCPC.getData().put(attr, 0.0);
					}
				});

				return new AttributeQueryResult(q.getMetric(), clickCPC.getData());
			case CPM: // 1000 * ( total cost / total impressions )
				AttributeDataQuery tqrCostCPM = q.deriveQuery(MetricType.TOTAL_COST, q.getAttribute());
				AttributeQueryResult costCPM = resolveAttributeDataQuery(tqrCostCPM);

				AttributeDataQuery tqrImpressionsCPM = q.deriveQuery(MetricType.TOTAL_IMPRESSIONS, q.getAttribute());
				AttributeQueryResult impressionsCPM = resolveAttributeDataQuery(tqrImpressionsCPM);

				costCPM.getData().forEach((attr, value) -> {
					if (impressionsCPM.getData().containsKey(attr)) {
						impressionsCPM.getData().put(attr, ((value.doubleValue() / impressionsCPM.getData().get(attr).doubleValue()) * 1000));
					} else {
						impressionsCPM.getData().put(attr, 0.0);
					}
				});

				return new AttributeQueryResult(q.getMetric(), impressionsCPM.getData());
			case BOUNCE_RATE: // total bounces / total clicks
				AttributeDataQuery tqrBounceBR = q.deriveQuery(MetricType.TOTAL_BOUNCES, q.getAttribute());
				AttributeQueryResult bounceBR = resolveAttributeDataQuery(tqrBounceBR);

				AttributeDataQuery tqrClickBR = q.deriveQuery(MetricType.TOTAL_CLICKS, q.getAttribute());
				AttributeQueryResult clickBR = resolveAttributeDataQuery(tqrClickBR);

				clickBR.getData().forEach((attr, value) -> {
					if (bounceBR.getData().containsKey(attr)) {
						bounceBR.getData().put(attr, bounceBR.getData().get(attr).doubleValue() / value.doubleValue());
					} else {
						bounceBR.getData().put(attr, 0.0);
					}
				});

				return new AttributeQueryResult(q.getMetric(), bounceBR.getData());
			case CTR: // total clicks / total impressions
				AttributeDataQuery tqrClickCTR = q.deriveQuery(MetricType.TOTAL_CLICKS, q.getAttribute());
				AttributeQueryResult clickCTR = resolveAttributeDataQuery(tqrClickCTR);

				AttributeDataQuery tqrImpressionsCTR = q.deriveQuery(MetricType.TOTAL_IMPRESSIONS, q.getAttribute());
				AttributeQueryResult impressionsCTR = resolveAttributeDataQuery(tqrImpressionsCTR);

				impressionsCTR.getData().forEach((attr, value) -> {
					if (clickCTR.getData().containsKey(attr)) {
						clickCTR.getData().put(attr, clickCTR.getData().get(attr).doubleValue() / value.doubleValue());
					} else {
						clickCTR.getData().put(attr, 0.0);
					}
				});

				return new AttributeQueryResult(q.getMetric(), clickCTR.getData());
			case TOTAL_COST:
				String impressionSql = "SELECT " + att + ", SUM(impression_cost) " +
						"FROM site_impression " +
						"JOIN user ON site_impression.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "impression_date") +
						this.setFilters(q) +
						" GROUP BY " + att + ";";
				System.out.println(impressionSql);
				String clickSql = "SELECT " + att + ", SUM(cost) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"JOIN site_impression ON click.user_id = site_impression.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) +
						" GROUP BY " + att + ";";
				System.out.println(clickSql);
				Map<String, Number> impressionCostMap = createAttributeMap(impressionSql);
				Map<String, Number> clickCostMap = createAttributeMap(clickSql);

				clickCostMap.forEach((string, val) -> {
					if (impressionCostMap.containsKey(string)) {
						impressionCostMap.put(string, val.doubleValue() + impressionCostMap.get(string).doubleValue());
					} else {
						impressionCostMap.put(string, val);
					}
				});

				return new AttributeQueryResult(q.getMetric(), impressionCostMap);
		}
		System.out.println(sql);

		return new AttributeQueryResult(q.getMetric(), createAttributeMap(sql));
	}

	private TimeQueryResult resolveTimeDataQuery(TimeDataQuery q) {
		String sql = null;

		switch (q.getMetric()) {
			case TOTAL_IMPRESSIONS:
				sql = "SELECT impression_date, count(site_impression_id) " +
						"FROM site_impression " +
						"JOIN user ON site_impression.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "impression_date") +
						this.setFilters(q) +
						this.timeGroup(q, "impression_date") +
						" ORDER BY impression_date;";
				break;
			case TOTAL_CLICKS:
				sql = "SELECT click_date, count(click_id) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"JOIN site_impression ON click.user_id = site_impression.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) +
						this.timeGroup(q, "click_date") +
						" ORDER BY click_date;";
				break;
			case TOTAL_UNIQUES:
				sql = "SELECT click_date, count( DISTINCT click.user_id) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"JOIN site_impression ON click.user_id = site_impression.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) +
						this.timeGroup(q, "click_date") +
						" ORDER BY click_date;";
				break;
			case TOTAL_BOUNCES:  //TODO set custom bounce rate definition
				sql = "SELECT entry_date, count(server_log.server_log_id) " +
						"FROM server_log " +
						"JOIN user ON server_log.user_id = user.user_id " +
						"JOIN site_impression ON user.user_id = site_impression.user_id " +
						"JOIN ( " +
						"SELECT " +
						"server_log_id, " +
						"CASE " +
						"WHEN exit_date = \"n/a\" " +
						"THEN pages_viewed <= " + ProjectSettings.getBouncePages() + " " +
						"ELSE " +
						"pages_viewed <= " + ProjectSettings.getBouncePages() + " " +
						"OR ( (strftime('%s', exit_date))) " +
						"- (strftime('%s', entry_date)) <=  " + ProjectSettings.getBounceSeconds() +
						" END bounce " +
						"FROM server_log " +
						") aux ON aux.server_log_id = server_log.server_log_id AND bounce = 1 " +
						"WHERE " +
						this.setBetween(q, "entry_date") +
						this.setFilters(q) +
						this.timeGroup(q, "entry_date") +
						" ORDER BY entry_date;";
				break;
			case TOTAL_CONVERSIONS:
				sql = "SELECT entry_date, count(server_log_id) " +
						"FROM server_log " +
						"JOIN user ON server_log.user_id = user.user_id " +
						"JOIN site_impression ON server_log.user_id = site_impression.user_id " +
						"WHERE conversion = 'Yes' AND " +
						this.setBetween(q, "entry_date") +
						this.setFilters(q) +
						this.timeGroup(q, "entry_date") +
						" ORDER BY entry_date;";
				break;
			case CPA: // total cost / total conversion
				TimeDataQuery tqrCostCPA = q.deriveQuery(MetricType.TOTAL_COST);
				TimeQueryResult costCPA = resolveTimeDataQuery(tqrCostCPA);

				TimeDataQuery tqrConversionCPA = q.deriveQuery(MetricType.TOTAL_CONVERSIONS);
				TimeQueryResult conversionCPA = resolveTimeDataQuery(tqrConversionCPA);

				Map<Instant, Number> resultDataCPA = new HashMap<>();

				costCPA.getData().forEach(resultDataCPA::put);

				conversionCPA.getData().forEach((date, val) -> resultDataCPA.put(date, resultDataCPA.get(date).doubleValue() / val.doubleValue()));

				return new TimeQueryResult(q.getMetric(), resultDataCPA);
			case CPC: // total cost / total clicks
				TimeDataQuery tqrCostCPC = q.deriveQuery(MetricType.TOTAL_COST);
				TimeQueryResult costCPC = resolveTimeDataQuery(tqrCostCPC);

				TimeDataQuery tqrClickCPC = q.deriveQuery(MetricType.TOTAL_CLICKS);
				TimeQueryResult clickCPC = resolveTimeDataQuery(tqrClickCPC);

				Map<Instant, Number> resultDataCPC = new HashMap<>();

				costCPC.getData().forEach(resultDataCPC::put);

				clickCPC.getData().forEach((date, val) -> resultDataCPC.put(date, resultDataCPC.get(date).doubleValue() / val.doubleValue()));

				return new TimeQueryResult(q.getMetric(), resultDataCPC);
			case CPM: // 1000 * ( total cost / total impressions )
				TimeDataQuery tqrCostCPM = q.deriveQuery(MetricType.TOTAL_COST);
				TimeQueryResult costCPM = resolveTimeDataQuery(tqrCostCPM);

				TimeDataQuery tqrImpressionCPM = q.deriveQuery(MetricType.TOTAL_IMPRESSIONS);
				TimeQueryResult impressionCPM = resolveTimeDataQuery(tqrImpressionCPM);

				Map<Instant, Number> resultDataCPM = new HashMap<>();

				costCPM.getData().forEach(resultDataCPM::put);

				impressionCPM.getData().forEach((date, val) -> resultDataCPM.put(date, 1000 * (resultDataCPM.get(date).doubleValue() / val.doubleValue())));

				return new TimeQueryResult(q.getMetric(), resultDataCPM);
			case BOUNCE_RATE: // total bounces / total clicks
				TimeDataQuery tqrBounceBR = q.deriveQuery(MetricType.TOTAL_BOUNCES);
				TimeQueryResult bounceBR = resolveTimeDataQuery(tqrBounceBR);

				TimeDataQuery tqrClickBR = q.deriveQuery(MetricType.TOTAL_CLICKS);
				TimeQueryResult clickBR = resolveTimeDataQuery(tqrClickBR);

				Map<Instant, Number> resultDataBR = new HashMap<>();

				bounceBR.getData().forEach(resultDataBR::put);

				clickBR.getData().forEach((date, val) -> resultDataBR.put(date, resultDataBR.get(date).doubleValue() / val.doubleValue()));

				return new TimeQueryResult(q.getMetric(), resultDataBR);
			case CTR: // total clicks / total impressions
				TimeDataQuery tqrClickCTR = q.deriveQuery(MetricType.TOTAL_CLICKS);
				TimeQueryResult clickCTR = resolveTimeDataQuery(tqrClickCTR);

				TimeDataQuery tqrImpressionCTR = q.deriveQuery(MetricType.TOTAL_IMPRESSIONS);
				TimeQueryResult impressionCTR = resolveTimeDataQuery(tqrImpressionCTR);

				Map<Instant, Number> resultDataCTR = new HashMap<>();

				clickCTR.getData().forEach(resultDataCTR::put);

				impressionCTR.getData().forEach((date, val) -> resultDataCTR.put(date, resultDataCTR.get(date).doubleValue() / val.doubleValue()));

				return new TimeQueryResult(q.getMetric(), resultDataCTR);
			case TOTAL_COST:
				String impressionSql = "SELECT impression_date, SUM(impression_cost) " +
						"FROM site_impression " +
						"JOIN user ON site_impression.user_id = user.user_id " +
						"WHERE " + this.setBetween(q, "impression_date") +
						this.setFilters(q) +
						this.timeGroup(q, "impression_date") +
						" ORDER BY impression_date;";
				String clickSql = "SELECT click_date, SUM(cost) " +
						"FROM click " +
						"JOIN user ON click.user_id = user.user_id " +
						"JOIN site_impression ON click.user_id = site_impression.user_id " +
						"WHERE " + this.setBetween(q, "click_date") +
						this.setFilters(q) +
						this.timeGroup(q, "click_date") +
						" ORDER BY click_date;";
				Map<Instant, Number> impressionCostMap = DBUtils.truncateInstantMap(createMap(impressionSql), q.getGranularity());
				Map<Instant, Number> clickCostMap = DBUtils.truncateInstantMap(createMap(clickSql), q.getGranularity());

				impressionCostMap.forEach((date, val) -> {
					if (clickCostMap.containsKey(date)) {
						clickCostMap.put(date, val.doubleValue() + clickCostMap.get(date).doubleValue());
					} else {
						clickCostMap.put(date, val);
					}
				});

				return new TimeQueryResult(q.getMetric(), clickCostMap);
		}
		System.out.println(sql);

		return new TimeQueryResult(q.getMetric(), DBUtils.truncateInstantMap(createMap(sql), q.getGranularity()));
	}

	/* Relies on granualarity */
	private String timeGroup(TimeDataQuery q, String dateString) {
		return "GROUP BY strftime(" + q.getGranularity().getSql() + ", " + dateString + ")";
	}

	private String setFilters(Query q) {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<AttributeType, List<String>> entry : q.getFilters().entrySet()) {
			builder.append(" AND (");
			for (int i = 0; i < entry.getValue().size(); i++) {
				builder.append(entry.getKey().getQueryBit() + " = '" + entry.getValue().get(i) + "'");
				if (i < entry.getValue().size() - 1) {
					builder.append(" OR ");
				}
			}
			builder.append(")");
		}
		return builder.toString();
	}

	private String setBetween(Query q, String dateString) {
		return "strftime('%d,%m,%Y'," + dateString + ") BETWEEN " +
				"strftime('%d,%m,%Y','" + q.getStartDate().toString().replace("Z", "") + "') AND " +
				"strftime('%d,%m,%Y','" + q.getEndDate().toString().replace("Z", "") + "') ";
	}
}
