package com.lianjiu.rest.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.rest.dao.SearchDao;
import com.lianjiu.rest.model.ItemSearch;

/**
 * 在索引库中查询商品信息
 * 
 * @author zhaoxi
 *
 */
public class SearchDaoImpl implements SearchDao {

	@Autowired
	private SolrServer solrServer;

	@Override
	public LianjiuResult search(SolrQuery query) throws Exception {
		// 返回值对象
		LianjiuResult result = new LianjiuResult();
		// 根据查询条件查询solr索引库
		QueryResponse queryResponse = solrServer.query(query);
		// 取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 取查询结果总数量
		result.setTotal(solrDocumentList.getNumFound());
		// 商品列表
		List<ItemSearch> itemList = new ArrayList<>();
		// 取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		// 取商品列表
		for (SolrDocument solrDocument : solrDocumentList) {
			// 创建一商品对象
			ItemSearch item = new ItemSearch();
			item.setProductId((String) solrDocument.get("id"));
			// 取高亮显示的结果
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (list != null && list.size() > 0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			item.setProductName(title);
			item.setProductMaxPrice((String) solrDocument.get("item_price"));
			item.setCategory((Long) solrDocument.get("item_category_id"));
			// 添加的商品列表
			itemList.add(item);
		}
		result.setStatus(200);
		result.setMsg("ok");
		result.setLianjiuData(itemList);
		return result;
	}

}
