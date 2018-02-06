package com.lianjiu.rest.service.search.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.search.SearchMapper;
import com.lianjiu.rest.model.ItemSearch;
import com.lianjiu.rest.service.search.SearchManagerService;

@Service
public class SearchManagerServiceImpl implements SearchManagerService {
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchMapper searchMapper;
	@Autowired
	private CategoryMapper categoryMapper;

	private List<Long> categoryList = new ArrayList<Long>();

	/**
	 * 
	 * zhaoxi 2017年11月9日 descrption:获取分类下的所有分类
	 * 
	 * @param categoryId
	 *            void
	 */
	private void getDescendants(Long categoryId) {
		List<Long> categoryIds = categoryMapper.selectCidsByPid(categoryId);
		// 边界条件
		if (null == categoryIds || categoryIds.size() == 0) {
			return;
		}
		categoryList.addAll(categoryIds);
		// 遍历
		for (Long cId : categoryIds) {
			// 递归
			getDescendants(cId);
		}
	}

	@Override
	public LianjiuResult importItemToIndex() {
		// 查询商品列表

		categoryList.clear();
		Long cid = categoryMapper.selectByCategoryName("家电信息");
		this.getDescendants(cid);// 选出家电的id
		List<ItemSearch> items = new ArrayList<>();
		// 卖手机，卖家电
		List<ItemSearch> itemsProduct = searchMapper.getProductItemAll();
		for (ItemSearch itemSearch : itemsProduct) {
			System.out.println(itemSearch.getCategory());
			if (categoryList.contains(itemSearch.getCategory())) {
				itemSearch.setCategory((long) 2);// 家电
			} else {
				itemSearch.setCategory((long) 3);// 手机
			}
		}
		if (null != itemsProduct) {
			items.addAll(itemsProduct);
		}
		// 卖废品
		List<ItemSearch> itemsWaste = searchMapper.getWasteItemAll();
		if (null != itemsProduct) {
			items.addAll(itemsWaste);
		}
		try {
			// 将商品列表导入solr
			for (ItemSearch item : items) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", item.getProductId());
				document.addField("item_id", item.getProductId());
				document.addField("item_title", item.getProductName());
				document.addField("item_price", item.getProductMaxPrice());
				document.addField("item_category_id", item.getCategory());
				// 将文档写入索引库
				solrServer.add(document);
			}
			// 提交修改
			solrServer.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return LianjiuResult.ok();
	}

}
