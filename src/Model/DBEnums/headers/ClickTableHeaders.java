package Model.DBEnums.headers;

import Model.DBEnums.TableType;

/**
 * Created by Philip on 08/03/2017.
 *
 * Simple header defining class for the click table
 */
public enum ClickTableHeaders implements Header {
	USER_ID("user_id"),
	CLICK_ID("click_id"),
	CLICK_DATE("click_date"),
	COST("cost");
	
	private final String sqlText;
	
	private ClickTableHeaders(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return sqlText;
	}
	
	@Override
	public TableType getTable() {
		return TableType.CLICK;
	}
}