package Model.DBEnums;

/**
 * Created by Philip on 08/03/2017.
 */
public enum UserTableHeaders {
	AGE("age"),
	GENDER("gender"),
	INCOME("income");
	
	private final String sqlText;
	
	UserTableHeaders(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return sqlText;
	}
}
