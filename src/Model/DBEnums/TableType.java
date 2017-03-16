package Model.DBEnums;

/**
 * Created by Philip on 27/02/2017.
 *
 * Simple and easy categorisation of the four different tables. To use: just pass in the type of table and the database
 * will handle the various types of table
 */
public enum TableType {
	CLICK("click"),
	SERVER_LOG("server_log"),
	SITE_IMPRESSION("site_impression"),
	USER("user");
	
	private final String sqlText;
	
	private TableType(final String text) {
		sqlText = text;
	}
	
	@Override
	public String toString() {
		return sqlText;
	}
}
