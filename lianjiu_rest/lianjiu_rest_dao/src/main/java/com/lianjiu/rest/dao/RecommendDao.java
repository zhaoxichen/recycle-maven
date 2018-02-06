package com.lianjiu.rest.dao;

public interface RecommendDao {
	public boolean payForRecomend(String udetailsReferrer);

	public boolean payForOrdersExpressFirst(String udetailsReferrer);
}
