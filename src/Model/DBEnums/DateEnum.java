package Model.DBEnums;

/**
 * Created by Philip on 10/03/2017.
 */
public enum DateEnum {
	HOURS("'%H,%d,%m,%Y'"),
	DAYS("'%d,%m,%Y'"),
	WEEKS("'%W,%m,%Y'");

	private String sql;

	DateEnum(String s) {
		sql = s;
	}

	public String getSql() {
		return sql;
	}
}
