package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductExcellent;
import com.lianjiu.service.product.ProductExcellentService;

/**
 * 精品信息
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/excellent")
public class ProductExcellentController {

	@Autowired
	ProductExcellentService productExcellentService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有精品
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getExcellentAll() {
		return productExcellentService.getExcellentAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有精品
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productExcellentService.getExcellentAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有精品
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{Cid}/ByCid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable Long Cid) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid对象绑定错误");
		}
		return productExcellentService.getExcellentByCid(Cid);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有精品，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{Cid}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable Long Cid, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return productExcellentService.getExcellentByCid(Cid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{excellentId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable String excellentId) {
		if(Util.isEmpty(excellentId)){
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		return productExcellentService.getExcellentById(excellentId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加精品
	 * 
	 * @param excellent
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addExcellent", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addExcellent(ProductExcellent excellent) {
		if(null == excellent){
			return LianjiuResult.build(401, "excellent对象绑定错误");
		}
		return productExcellentService.addExcellent(excellent);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新精品
	 * 
	 * @param excellent
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyExcellent", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateExcellent(ProductExcellent excellent) {
		if(null == excellent){
			return LianjiuResult.build(401, "excellent对象绑定错误");
		}
		return productExcellentService.updateExcellent(excellent);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除精品
	 * 
	 * @param excellentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeExcellent/{excellentId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteExcellent(@PathVariable String excellentId) {
		if(Util.isEmpty(excellentId)){
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		return productExcellentService.deleteExcellent(excellentId);
	}

}
