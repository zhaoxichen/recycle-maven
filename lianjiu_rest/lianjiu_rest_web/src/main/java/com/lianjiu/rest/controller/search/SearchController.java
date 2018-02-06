package com.lianjiu.rest.controller.search;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.rest.service.product.ProductService;
import com.lianjiu.rest.service.product.ProductWasteService;
import com.lianjiu.rest.service.search.SearchService;

/**
 * 商品查询Controller
 * <p>
 * Title: SearchController
 * </p>
 * <p>
 * Description:
 * </p>
 */
@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	@Autowired
	private ProductService productService;// 卖家电，卖手机
	@Autowired
	private ProductWasteService productWasteService;// 卖废品

	/**
	 * 
	 * zhaoxi 2017年10月12日 descrption:搜索
	 * 
	 * @param keyword
	 * @param orderBy
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult search(@RequestParam String keyword, @RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		// 查询条件不能为空
		if (StringUtils.isBlank(keyword)) {
			return LianjiuResult.build(401, "查询条件不能为空");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		System.out.println(keyword);
		try {
			return searchService.searchBySql(keyword, pageNum, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2017年12月26日 descrption:solr搜索引擎
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/querySolr", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult searchSolr(@RequestParam String keyword, @RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		// 查询条件不能为空
		if (StringUtils.isBlank(keyword)) {
			return LianjiuResult.build(401, "查询条件不能为空");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		System.out.println(keyword);
		try {
			return searchService.search(keyword, pageNum, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2017年11月29日 descrption:
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/queryExcellent", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult searchExcellent(@RequestParam String keyword,
			@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
		// 查询条件不能为空
		if (StringUtils.isBlank(keyword)) {
			return LianjiuResult.build(401, "查询条件不能为空");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		System.out.println(keyword);
		try {
			return searchService.searchExcellentBySql(keyword, pageNum, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:获取精品分类名称
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDetails/{category}/{productId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getSearchDetails(@PathVariable Integer category, @PathVariable String productId) {
		// 判断是什么类型的产品
		if (null == category) {
			return LianjiuResult.build(401, "请传入category");
		}
		if (StringUtils.isBlank(productId)) {
			return LianjiuResult.build(402, "请传入正确的productId");
		}
		if (0 == category) {
			// 查买家电，卖手机
			return productService.selectProductByid(productId);
		} else if (1 == category) {
			// 查卖废品
			return productWasteService.getWasteDetails(productId);
		}
		return LianjiuResult.build(509, "category=" + category);
	}

	/**
	 * 
	 * zhaoxi 2017年11月13日 descrption:获取搜索历史记录
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getSearchHistory/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getSearchHistory(@PathVariable String userId) {
		// 判断是什么类型的产品
		if (StringUtils.isBlank(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		return searchService.getSearchHistory(userId);
	}

}
