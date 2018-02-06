package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Waste;
import com.lianjiu.service.product.ProductWasteService;

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
	 * zhaoxi 2017年9月6日 descrption:查询所有废品
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWaste/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getWasteAll() {
		return productWasteService.getWasteAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有废品
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWaste/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getWasteAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productWasteService.getWasteAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有废品
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWaste/{Cid}/ByCid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getWasteByCid(@PathVariable Long Cid) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid绑定错误");
		}
		return productWasteService.getWasteByCid(Cid);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有废品，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWaste/{Cid}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getWasteByCid(@PathVariable Long Cid, @PathVariable int pageNum, @PathVariable int pageSize) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productWasteService.getWasteByCid(Cid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加废品
	 * 
	 * @param waste
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addWaste", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addWaste(Waste waste) {
		if(null == waste){
			return LianjiuResult.build(401, "waste对象绑定错误");
		}
		return productWasteService.addWaste(waste);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新废品
	 * 
	 * @param waste
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyWaste", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateWaste(Waste waste) {
		if(null == waste){
			return LianjiuResult.build(401, "waste对象绑定错误");
		}
		return productWasteService.updateWaste(waste);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除废品
	 * 
	 * @param wasteId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeWaste/{wasteId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteWaste(@PathVariable String wasteId) {
		if(Util.isEmpty(wasteId)){
			return LianjiuResult.build(401, "请传入正确的commentId");
		}
		return productWasteService.deleteWaste(wasteId);
	}
}
