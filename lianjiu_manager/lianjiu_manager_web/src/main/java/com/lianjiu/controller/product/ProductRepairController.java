package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductRepair;
import com.lianjiu.service.product.ProductRepairService;

/**
 * 维修方案信息
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/repair")
public class ProductRepairController {

	@Autowired
	ProductRepairService productRepairService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有废品
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRepairAll() {
		return productRepairService.getRepairAll();
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有废品
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productRepairService.getRepairAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有废品
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{Cid}/ByCid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable Long Cid) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid绑定错误");
		}
		return productRepairService.getRepairByCid(Cid);
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
	@RequestMapping(value = "/getRepair/{Cid}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable Long Cid, @PathVariable int pageNum, @PathVariable int pageSize) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productRepairService.getRepairByCid(Cid, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{repairId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable String repairId) {
		if(Util.isEmpty(repairId)){
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		return productRepairService.getRepairById(repairId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:添加废品
	 * 
	 * @param repair
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addRepair", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addRepair(ProductRepair repair) {
		if(null == repair){
			return LianjiuResult.build(401, "repair对象绑定错误");
		}
		return productRepairService.addRepair(repair);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:更新废品
	 * 
	 * @param repair
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyRepair", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRepair(ProductRepair repair) {
		if(null == repair){
			return LianjiuResult.build(401, "repair对象绑定错误");
		}
		return productRepairService.updateRepair(repair);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:删除废品
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeRepair/{repairId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteRepair(@PathVariable String repairId) {
		if(Util.isEmpty(repairId)){
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		return productRepairService.deleteRepair(repairId);
	}
}
