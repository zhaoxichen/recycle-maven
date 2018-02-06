package com.lianjiu.rest.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.service.product.ProductFurnitureService;

/**
 * 家具信息
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/furniture")
public class ProductFurnitureController {

	@Autowired
	ProductFurnitureService productFurnitureService;

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:分页获取家具
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFurniture", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getFurnitureByCid(Long cid, int pageNum, int pageSize) {
		if (null == cid) {
			return LianjiuResult.build(401, "cid绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productFurnitureService.getFurnitureByCid(cid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:获取家具详情
	 * 
	 * @param furniturePriceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDetails/{priceId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFurnitureDetails(@PathVariable String priceId) {
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(401, "请传入正确的priceId");
		}
		return productFurnitureService.getFurnitureDetails(priceId);
	}

}
