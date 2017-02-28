package Model;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.util.List;

/**
 * Created by Philip on 26/02/2017.
 *
 * Do not run this file unless JDBC-sqlite driver is configured
 */
public class DatabaseManager {
	
	private String filename;
	private String url;
	
	public DatabaseManager() {
		
		filename = "db/model.db";
		url = "jdbc:sqlite:" + filename;
	}
	
	/**
	 * Attempts connection to the database file
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
		File file = new File("db/model.db");
		
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
		
		String sqlUser = "" +
				"CREATE TABLE IF NOT EXISTS user (\n" +
				" user_id INTEGER PRIMARY KEY, \n" +
				" age TEXT NOT NULL, \n" +
				" gender TEXT NOT NULL, \n" +
				" income TEXT NOT NULL \n" +
				");";
		
		String sqlClick = "" +
				"CREATE TABLE IF NOT EXISTS click (\n" +
				" user_id INTEGER, \n" +
				" date STRING TEXT NOT NULL, \n" +
				" cost REAL NOT NULL \n" +
				");";
		
		String sqlSiteImpression = "" +
				"CREATE TABLE IF NOT EXISTS site_impression (\n" +
				" user_id INTEGER, \n" +
				" context STRING NOT NULL, \n" +
				" impression_cost INTEGER NOT NULL \n" +
				");";
		
		String sqlServerLog = "" +
				"CREATE TABLE IF NOT EXISTS server_log (\n" +
				" user_id INTEGER, \n" +
				" entry_date TEXT NOT NULL, \n" +
				" exit_date TEXT NOT NULL, \n" +
				" pages_viewed INTEGER NOT NULL, \n" +
				" conversion INTEGER NOT NULL \n" + // use 0 and 1 for true and false
				");";
		
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			stmt.execute(sqlUser);
			stmt.execute(sqlClick);
			stmt.execute(sqlSiteImpression);
			stmt.execute(sqlServerLog);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertData(LogType logType, List<String[]> list) {
		System.out.println("I've been called with a list of size " + list.size() + " and of type " + logType.toString());
		
		int acc = 0;
		
		for (String[] row : list) {
			System.out.println("acc: " + acc++);
			System.out.println(row[0] + ", " + row[1] + ", " + row[2]);
			String sql = "INSERT INTO click(user_id, date, cost) VALUES (?, ?, ?)";
			
			try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setLong(1, Long.parseLong(row[1]));
				pstmt.setString(2, row[0]);
				pstmt.setDouble(3, Double.parseDouble(row[2]));
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
	 * Simple class I used to test database queries in the console
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
}
