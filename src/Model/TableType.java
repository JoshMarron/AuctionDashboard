package Model;

/**
 * Created by Philip on 27/02/2017.
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
