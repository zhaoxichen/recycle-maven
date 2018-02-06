package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.WastePrice;
import com.lianjiu.service.product.ProductWastePriceService;

/**
 * 废品信息
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/wastePrice")
public class ProductWastePriceController {

	@Autowired
	ProductWastePriceService productWastePriceService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有废品
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPrice/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getPriceAll() {
		return productWastePriceService.getPriceAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有价格
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPrice/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getPriceAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productWastePriceService.getPriceAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前废品的价格表
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPrice/{wasteId}/ByWasteId/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getPriceByWasteId(@PathVariable String wasteId) {
		if(Util.isEmpty(wasteId)){
			return LianjiuResult.build(401, "请传入正确的wasteId");
		}
		return productWastePriceService.getPriceByWasteId(wasteId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前废品的价格表，分页
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPrice/{wasteId}/{pageNum}/{pageSize}/ByWasteId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getPriceByWasteId(@PathVariable String wasteId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if(Util.isEmpty(wasteId)){
			return LianjiuResult.build(401, "请传入正确的wasteId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return productWastePriceService.getPriceByWasteId(wasteId,pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加废品
	 * 
	 * @param waste
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addPrice(WastePrice wastePrice) {
		if(null == wastePrice){
			return LianjiuResult.build(401, "wastePrice对象绑定错误");
		}
		return productWastePriceService.addPrice(wastePrice);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新废品
	 * 
	 * @param waste
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updatePrice(WastePrice wastePrice) {
		if(null == wastePrice){
			return LianjiuResult.build(401, "wastePrice对象绑定错误");
		}
		return productWastePriceService.updatePrice(wastePrice);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除废品
	 * 
	 * @param wasteId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removePrice/{wPriceId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deletePrice(@PathVariable String wPriceId) {
		if(Util.isEmpty(wPriceId)){
			return LianjiuResult.build(401, "请传入正确的wPriceId");
		}
		return productWastePriceService.deletePrice(wPriceId);
	}
}
