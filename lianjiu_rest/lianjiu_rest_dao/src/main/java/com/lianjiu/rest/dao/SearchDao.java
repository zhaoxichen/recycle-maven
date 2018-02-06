package com.lianjiu.rest.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.lianjiu.common.pojo.LianjiuResult;

public interface SearchDao {

	LianjiuResult search(SolrQuery query) throws Exception;
}
