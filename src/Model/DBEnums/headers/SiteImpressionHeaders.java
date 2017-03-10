package Model.DBEnums.headers;

import Model.DBEnums.TableType;

/**
 * Created by Philip on 08/03/2017.
 *
 * Simple header defining class for the site impressions table
 */
public enum SiteImpressionHeaders implements Header {
	USER_ID("user_id"),
	SITE_IMPRESSION_ID("site_impression_id"),
	CONTEXT("context"),
	IMPRESSION_COST("impression_cost"),
	IMPRESSION_DATE("impression_date"),;
	
	private final String sqlText;
	
	private SiteImpressionHeaders(String sqlText) {
		this.sqlText = sqlText;
	}
	
	@Override
	public String toString() {
		return this.sqlText;
	}
	
	@Override
	public TableType getTable() {
		return TableType.SITE_IMPRESSION;
	}
}
