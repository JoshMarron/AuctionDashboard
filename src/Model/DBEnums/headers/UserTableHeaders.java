package Model.DBEnums.headers;

import Model.DBEnums.TableType;

/**
 * Created by Philip on 08/03/2017.
 *
 * Simple header defining class for the user table
 */
public enum UserTableHeaders implements Header {
	USER_ID("user_id"),
	AGE("age"),
	GENDER("gender"),
	INCOME("income");
	
	private final String sqlText;
	
	private UserTableHeaders(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return sqlText;
	}
	
	@Override
	public TableType getTable() {
		return TableType.USER;
	}
}
