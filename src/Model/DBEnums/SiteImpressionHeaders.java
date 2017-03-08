package Model.DBEnums;

/**
 * Created by Philip on 08/03/2017.
 */
public enum SiteImpressionHeaders {
	SITE_IMPRESSION_ID("site_impression_id"),
	CONTEXT("context"),
	IMPRESSION_COST("impression_cost"),
	IMPRESSION_DATE("impression_date");
	
	private final String sqlText;
	
	SiteImpressionHeaders(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return this.sqlText;
	}
}
