package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductFurniturePrice;
import com.lianjiu.service.product.ProductFurniturePriceService;

/**
 * 家具产品价格关联
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/furniturePrice")
public class ProductFurniturePriceController {

	@Autowired
	ProductFurniturePriceService productFurniturePriceService;

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:分页获取价格
	 * 
	 * @param pid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFurniturePrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getFurniturePriceByPid(String pid, int pageNum, int pageSize) {
		if (Util.isEmpty(pid)) {
			return LianjiuResult.build(401, "pid绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return productFurniturePriceService.getFurniturePriceByPid(pid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:添加价格
	 * 
	 * @param furniturePrice
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addFurniturePrice(ProductFurniturePrice furniturePrice) {
		if (null == furniturePrice) {
			return LianjiuResult.build(401, "furniture对象绑定错误");
		}
		return productFurniturePriceService.addFurniturePrice(furniturePrice);
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:移除价格
	 * 
	 * @param priceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/remove/{priceId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteFurniturePrice(@PathVariable String priceId) {
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(401, "请传入正确的priceId");
		}
		return productFurniturePriceService.deleteFurniturePrice(priceId);
	}

}
