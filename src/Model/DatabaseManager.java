package Model;

import DataStructures.CsvInterfaces.Gender;
import DataStructures.CsvInterfaces.Income;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Model.TableModels.ServerVisit;
import Model.TableModels.User;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 26/02/2017.
 * <p>
 * Do not run this file unless JDBC-sqlite driver is configured
 */
public class DatabaseManager {
	
	private String filename;
	private String url;
	
	public DatabaseManager() {
		
		filename = "db/model3.db";
		url = "jdbc:sqlite:" + filename;
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
	 * Sets up the database for the dashboard. First checks for existence of the database and attempts to connect to it
	 * else it creates the database in the select directory.
	 */
	public void init() {
		File dir = new File("db");
		File file = new File(filename);
		
		if (!dir.exists()) {
			System.out.println("Directory doesn't exist");
			dir.mkdir();
			System.out.println("Creating DIR");
		} else {
			System.out.println("Directory exists");
		}
		
		if (!file.exists()) {
			System.out.println("Database doesn't exist");
			System.out.println("Creating database...");
			
			try {
				Connection conn = connect();
				
				if (conn != null) {
					Class.forName("org.sqlite.JDBC");
					DatabaseMetaData meta = conn.getMetaData();
					System.out.println("Driver name is " + meta.getDriverName());
					System.out.println("Database successfully created!");
				}
				conn.close();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Database exists");
		}
		
		System.out.println("Database created and ready to use");
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
	 * Insert data into the database
	 *
	 * @param logType type of log file being inserted
	 * @param list list of arrays of strings of data to be inserted
	 */
	public void insertData(LogType logType, List<String[]> list) {

		try {
			Connection conn = connect();
			conn.setAutoCommit(false);
			
			String sql;
			
			switch (logType) {
				case CLICK:
				    wipeTable(logType);
					sql = "INSERT INTO click(user_id, click_date, cost) VALUES (?, ?, ?)";
					
					for (String[] row : list) {
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setLong(1, Long.parseLong(row[1]));
						pstmt.setString(2, row[0]);
						pstmt.setDouble(3, Double.parseDouble(row[2]));
						pstmt.executeUpdate();
						pstmt.close();
					}
					break;
				case IMPRESSION:
				    wipeTable(logType);
					String sqlSiteImpression = "INSERT INTO site_impression(user_id, context, impression_cost, impression_date) VALUES (?, ?, ?, ?)";
					String sqlUser = "INSERT INTO user(user_id, age, gender, income) VALUES (?,?,?,?)";
					
					// Date 0,ID 1,Gender 2, Age 3 ,Income 4,Context 5,Impression Cost 6
					
					for (String[] row : list) {
						PreparedStatement pSiteImpression = conn.prepareStatement(sqlSiteImpression);
						pSiteImpression.setLong(1, Long.parseLong(row[1]));
						pSiteImpression.setString(2, row[5]);
						pSiteImpression.setDouble(3, Double.parseDouble(row[6]));
						pSiteImpression.setString(4, row[0]);
						pSiteImpression.executeUpdate();
						pSiteImpression.close();
						
						PreparedStatement pUser = conn.prepareStatement(sqlUser);
						pUser.setLong(1, Long.parseLong(row[1]));
						pUser.setString(2, row[3]);
						pUser.setString(3, row[2]);
						pUser.setString(4, row[4]);
						pUser.executeUpdate();
						pUser.close();
					}
					break;
				case SERVER_LOG:
				    wipeTable(logType);
					sql = "INSERT INTO server_log(user_id, entry_date, exit_date, pages_viewed, conversion) VALUES (?,?,?,?,?)";
					
					// Entry Date 0,ID 1,Exit Date 2,Pages Viewed 3,Conversion 4
					
					for (String[] row : list) {
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setLong(1, Long.parseLong(row[1]));
						pstmt.setString(2, row[0]);
						pstmt.setString(3, row[2]);
						pstmt.setInt(4, Integer.parseInt(row[3]));
						pstmt.setString(5, row[4]);
						pstmt.executeUpdate();
						pstmt.close();
					}
					break;
				default:
					System.out.println("Not a valid input file");
			}
			
			conn.commit();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Database insertion complete");
	}
	
	/**
	 * Selects all data from one table and returns it
	 *
	 * @param table TableType from which the data is being accessed
	 * @return ResultSet of data, null if exception thrown
	 */
	public ResultSet selectAllTableData(TableType table) {
		String sql = "SELECT * FROM " + table.toString();
		
		ResultSet resultSet = null;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	/**
	 * Get all the data from the impressions table
	 * @return List of Impression data
	 */
	public List<Impression> getAllImpressions() {
		List<Impression> impressions = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.SITE_IMPRESSION.toString();

		ResultSet resultSet = null;

		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);

			while (resultSet.next()) {
				long impressionID = resultSet.getLong(1);
				long userID = resultSet.getLong(2);
				String context = resultSet.getString(3);
				double impressionCost = resultSet.getDouble(4);
				Instant impressionDate = this.stringToInstant(resultSet.getString(5));

				Impression i = new Impression(impressionID, userID, context, impressionCost, impressionDate);
				impressions.add(i);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return impressions;
	}
	
	/**
	 * Selects all data from click table and returns it
	 * @return List of all Click data
	 */
	public List<Click> getAllClicks() {
		List<Click> clicks = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.CLICK.toString();
		
		ResultSet resultSet;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			
			while (resultSet.next()) {
				long clickID = resultSet.getInt(1);
				long userID = resultSet.getInt(2);
				
				Instant clickDate = stringToInstant(resultSet.getString(3));
				
				double cost = resultSet.getDouble(4);
				
				Click c = new Click(clickID, userID, clickDate, cost);
				clicks.add(c);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return clicks;
	}
	
	/**
	 * Selects all data from user database and returns it
	 * @return List of all User information
	 */
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.USER.toString();
		
		ResultSet resultSet;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			
			while (resultSet.next()) {
				long userID = resultSet.getInt(1);
				String ageRange = resultSet.getString(2);
				
				String genderString = resultSet.getString(3);
				Gender gender = Gender.valueOf(genderString);
				
				String incomeString = resultSet.getString(4);
				Income income = Income.valueOf(incomeString);
				
				User u = new User(userID, ageRange, gender, income);
				users.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	/**
	 * Gets all data from server_log database and returns it
	 * @return List of all ServerVisit information
	 */
	public List<ServerVisit> getAllServerVisits() {
		List<ServerVisit> serverVisits = new ArrayList<>();
		String sql = "SELECT * FROM " + TableType.SERVER_LOG.toString();
		
		ResultSet resultSet;
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			resultSet = stmt.executeQuery(sql);
			
			while (resultSet.next()) {
				long ServerID = resultSet.getLong(1);
				long userID = resultSet.getLong(2);
				Instant entryDate = stringToInstant(resultSet.getString(3));
				Instant exitDate = stringToInstant(resultSet.getString(4));
				int pagesViewed = resultSet.getInt(5);
				boolean conversion = false;
				try {
					conversion = conversionToBoolean(resultSet.getString(6));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ServerVisit sv = new ServerVisit(ServerID, userID, entryDate, exitDate, pagesViewed, conversion);
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

	private void wipeTable(LogType logType) {

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
