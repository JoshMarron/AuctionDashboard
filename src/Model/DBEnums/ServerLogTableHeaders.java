package Model.DBEnums;

/**
 * Created by Philip on 08/03/2017.
 */
public enum ServerLogTableHeaders {
	SERVER_LOG_ID("server_log_id"),
	ENTRY_DATE("entry_date"),
	EXIT_DATE("exit_date"),
	PAGES_VIEWED("pages_viewed"),
	CONVERSION("conversion");
	
	private final String sqlText;
	
	ServerLogTableHeaders(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return sqlText;
	}
}
