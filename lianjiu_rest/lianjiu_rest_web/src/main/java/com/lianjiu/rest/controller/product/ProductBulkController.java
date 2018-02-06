package com.lianjiu.rest.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersBulkItem;
import com.lianjiu.rest.service.product.ProductBulkService;

/**
 * 家具信息
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/bulk")
public class ProductBulkController {

	@Autowired
	ProductBulkService productBulkService;

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:查询相关大宗回收，临时分配一个清单TAKEN
	 * 
	 * @param keyword
	 * @param categoryId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult search(@RequestParam(defaultValue = "1111-2222-3333") String keyword, Long categoryId,
			String TAKEN, @RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "15") int pageSize) {
		if (Util.isEmpty(keyword)) {
			return LianjiuResult.build(401, "请传入搜索词语");
		}
		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(402, "请传入正确的TAKEN");
		}
		if (null == categoryId) {
			return LianjiuResult.build(403, "请传入categoryId");
		}

		return productBulkService.getBulkByKeyword(keyword, categoryId, TAKEN, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:制定清单
	 * 
	 * @param BulkPriceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/setBulkItemCart", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult setBulkItemCart(String TAKEN, OrdersBulkItem item) {
		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(401, "请传入正确的TAKEN");
		}
		if (null == item) {
			return LianjiuResult.build(402, "对象绑定错误");
		}
		String bulkNum = item.getBulkItemNum();
		if (Util.isEmpty(bulkNum) || bulkNum.matches("[\u4e00-\u9fa5]") || bulkNum.length() > 10) {
			return LianjiuResult.build(403, "请传入数量,或者不要输入中文");
		}
		if (Util.isEmpty(item.getBulkItemProductId())) {
			return LianjiuResult.build(404, "请传入产品id");
		}
		if (Util.isEmpty(item.getBulkItemName())) {
			return LianjiuResult.build(405, "请传入产品名称");
		}
		if (Util.isEmpty(item.getBulkItemPrice())) {
			return LianjiuResult.build(406, "请传入产品价格");
		}
		if (null == item.getBulkItemUnit()) {
			return LianjiuResult.build(407, "请传入产品单位");
		}
		if (null == item.getBulkItemProductCid()) {
			return LianjiuResult.build(408, "请传入产品分类id");
		}
		return productBulkService.setBulkItemCart(TAKEN, item);
	}

	/**
	 * zhaoxi 2018年1月12日 descrption:获取清单
	 * 
	 * @param TAKEN
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBulkItemCart/{TAKEN}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getBulkItemCart(@PathVariable String TAKEN) {
		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(401, "请传入正确的TAKEN");
		}
		return productBulkService.getBulkItemCart(TAKEN);
	}

}
