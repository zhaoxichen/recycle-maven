package com.lianjiu.rest.service.search.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.dao.SearchDao;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.search.SearchMapper;
import com.lianjiu.rest.model.CategoryInfo;
import com.lianjiu.rest.model.ItemSearch;
import com.lianjiu.rest.service.search.SearchService;

/**
 * 搜索Service
 * <p>
 * Title: SearchServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	@Autowired
	private SearchMapper searchMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 查询索引库
	 */
	@Override
	public LianjiuResult search(String queryString, int page, int rows) throws Exception {
		// 创建查询对象
		SolrQuery query = new SolrQuery();
		// 设置查询条件
		query.setQuery(queryString);
		// 设置分页
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		// 设置默认搜索域
		query.set("df", "item_keywords");
		// 设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		// 执行查询
		return searchDao.search(query);
	}

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
	public LianjiuResult searchBySql(String queryString, int page, int rows) {
		categoryList.clear();
		Long cid = categoryMapper.selectByCategoryName("家电信息");
		this.getDescendants(cid);// 选出家电的id
		// 设置分页
		PageHelper.startPage(page, rows);
		List<ItemSearch> items = new ArrayList<>();
		// 卖手机，卖家电
		List<ItemSearch> itemsProduct = searchMapper.getProductItemList(queryString);
		for (ItemSearch itemSearch : itemsProduct) {
			System.out.println(itemSearch.getCategory());
			if (categoryList.contains(itemSearch.getCategory())) {
				itemSearch.setCategory((long) 2);// 家电
			} else {
				itemSearch.setCategory((long) 3);// 手机
			}
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ItemSearch> listItem = (Page<ItemSearch>) itemsProduct;
		Long totalRows = listItem.getTotal();
		if (null != itemsProduct) {
			items.addAll(itemsProduct);
		}
		// 卖废品
		PageHelper.startPage(page, rows);
		List<ItemSearch> itemsWaste = searchMapper.getWasteItemList(queryString);
		if (null != itemsProduct) {
			items.addAll(itemsWaste);
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		listItem = (Page<ItemSearch>) itemsWaste;
		totalRows += listItem.getTotal();
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(items);
		result.setTotal(totalRows);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 获取当前分类下的子分类
	 */
	@Override
	public LianjiuResult getCategoryByName(String categoryName) {
		Long categoryParentId = categoryMapper.selectByCategoryName(categoryName);
		if (null == categoryParentId) {
			return LianjiuResult.build(501, "精品信息类目不存在");
		}
		List<CategoryInfo> categories = categoryMapper.selectCategoryInfoByPId(categoryParentId);
		if (null == categories || 0 == categories.size()) {
			return LianjiuResult.build(502, "精品信息类目下没有产品");
		}
		return LianjiuResult.ok(categories);
	}

	/**
	 * 取出搜索历史记录
	 */
	@Override
	public LianjiuResult getSearchHistory(String userId) {
		String searchHistoryJson = jedisClient.get(userId);
		if (Util.isEmpty(searchHistoryJson)) {
			return LianjiuResult.ok(new ArrayList<String>(0));
		}
		List<String> historyList = JsonUtils.jsonToList(searchHistoryJson, String.class);
		if (null == historyList) {
			return LianjiuResult.ok(new ArrayList<String>(0));
		}
		return LianjiuResult.ok(historyList);
	}

	@Override
	public LianjiuResult searchExcellentBySql(String queryString, Integer pageNum, Integer pageSize) {
		List<ItemSearch> itemsProduct = searchMapper.getProductExcellentItemList(queryString);
		return LianjiuResult.ok(itemsProduct);
	}

}
