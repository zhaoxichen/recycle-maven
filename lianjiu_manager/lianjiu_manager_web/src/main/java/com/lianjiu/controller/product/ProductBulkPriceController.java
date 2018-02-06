package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductBulkPrice;
import com.lianjiu.service.product.ProductBulkPriceService;

/**
 * 大宗产品价格关联
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/bulkPrice")
public class ProductBulkPriceController {

	@Autowired
	ProductBulkPriceService productBulkPriceService;

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:分页获取价格
	 * 
	 * @param pid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBulkPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getBulkPriceByPid(String pid, int pageNum, int pageSize) {
		if (Util.isEmpty(pid)) {
			return LianjiuResult.build(401, "pid绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return productBulkPriceService.getBulkPriceByPid(pid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:添加价格
	 * 
	 * @param bulkPrice
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addBulkPrice(ProductBulkPrice bulkPrice) {
		if (null == bulkPrice) {
			return LianjiuResult.build(401, "furniture对象绑定错误");
		}
		if(!Util.isNumeric(bulkPrice.getPriceSingle())){
			return LianjiuResult.build(402, "请输入正确的价格！");
		}
		if(!Util.isNumeric(bulkPrice.getPriceSingleAlliance())){
			return LianjiuResult.build(403, "请输入正确的价格！");
		}
		return productBulkPriceService.addBulkPrice(bulkPrice);
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:移除价格
	 * 
	 * @param priceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteBulkPrice(String priceId) {
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(401, "请传入正确的priceId");
		}
		return productBulkPriceService.deleteBulkPrice(priceId);
	}
}
