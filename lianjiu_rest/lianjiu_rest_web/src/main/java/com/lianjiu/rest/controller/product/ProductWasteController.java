package com.lianjiu.rest.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.rest.service.product.ProductWasteService;

/**
 * 废品信息
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/waste")
public class ProductWasteController {

	@Autowired
	ProductWasteService productWasteService;

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有废品，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWaste/{cId}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getWasteByCid(@PathVariable Long cId, @PathVariable int pageNum, @PathVariable int pageSize) {
		if (null == cId) {
			return LianjiuResult.build(401, "请传入正确的cId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return productWasteService.getWasteByCid(cId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月22日 descrption:通过主键查询
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWasteDetails/{wPriceId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getWasteDetails(@PathVariable String wPriceId) {
		if (Util.isEmpty(wPriceId)) {
			return LianjiuResult.build(401, "请传入正确的wPriceId");
		}
		return productWasteService.getWasteDetails(wPriceId);
	}

	/**
	 * 
	 * huangfu 2017年10月10日 descrption:废品，加入回收车
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/introductionWaste", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult introductionWaste(String userId, OrdersItem wasteItem) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (null == wasteItem) {
			return LianjiuResult.build(402, "请传入正确的wasteItem");
		}
		if (Util.isEmpty(wasteItem.getOrItemsName())) {
			return LianjiuResult.build(403, "请传入正确的废品名称");
		}
		if (Util.isEmpty(wasteItem.getOrItemsPicture())) {
			return LianjiuResult.build(404, "请传入正确的图片地址");
		}
		if (Util.isEmpty(wasteItem.getOrItemsProductId())) {
			return LianjiuResult.build(405, "请传入正确的价格id");
		}
		if (null == wasteItem.getOrItemsStemFrom()) {
			wasteItem.setOrItemsStemFrom(3);// 卖废品
		}
		if (Util.isEmpty(wasteItem.getOrItemsNum()) || wasteItem.getOrItemsNum().matches("[\u4e00-\u9fa5]")) {
			return LianjiuResult.build(406, "请传入数量");
		}
		if (null == wasteItem.getOrItemsPriceUnit()) {
			return LianjiuResult.build(407, "请传入正确的单位");
		}
		if (Util.isEmpty(wasteItem.getOrItemsPrice())) {
			return LianjiuResult.build(408, "请传入正确的单价");
		}
		wasteItem.setOrItemsChooseFlag(0);
		wasteItem.setOrItemsVolume((long) 1);// 体积预存
		System.out.println("wPriceId:" + wasteItem.getOrItemsProductId());
		return productWasteService.introductionWaste(userId, wasteItem);
	}

}
