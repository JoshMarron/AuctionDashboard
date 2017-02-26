package Model;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Philip on 26/02/2017.
 */
public class DatabaseModel {
	
	private String filename;
	private String url;
	
	public DatabaseModel() {
		
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
		
		if (dir.exists()) {
			System.out.println("Directory exists");
			
			if (file.exists()) {
				System.out.println("Databse exists");
			} else {
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
			}
		} else {
			System.out.println("Directory doesn't exist");
			dir.mkdir();
			System.out.println("Creating DIR");
			init(); //Attempts to iniliase itself again
		}
	}
}
