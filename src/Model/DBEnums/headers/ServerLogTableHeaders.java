package Model.DBEnums.headers;

import Model.DBEnums.TableType;

/**
 * Created by Philip on 08/03/2017.
 *
 * Simple header defining class for the server log table
 */
public enum ServerLogTableHeaders implements Header {
	USER_ID("user_id"),
	SERVER_LOG_ID("server_log_id"),
	ENTRY_DATE("entry_date"),
	EXIT_DATE("exit_date"),
	PAGES_VIEWED("pages_viewed"),
	CONVERSION("conversion"),;
	
	private final String sqlText;
	
	private ServerLogTableHeaders(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return sqlText;
	}
	
	@Override
	public TableType getTable() {
		return TableType.SERVER_LOG;
	}
}
