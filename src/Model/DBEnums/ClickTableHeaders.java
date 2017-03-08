package Model.DBEnums;

/**
 * Created by Philip on 08/03/2017.
 */
public enum ClickTableHeaders {
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
}