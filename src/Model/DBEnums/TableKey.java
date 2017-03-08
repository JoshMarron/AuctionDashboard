package Model.DBEnums;

/**
 * Created by Philip on 08/03/2017.
 */
public enum TableKey {
	USER_ID("user_id");
	
	private final String sqlText;
	
	TableKey(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return sqlText;
	}
}
