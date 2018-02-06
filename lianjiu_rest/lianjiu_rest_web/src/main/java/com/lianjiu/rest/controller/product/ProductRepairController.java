package com.lianjiu.rest.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.service.product.ProductRepairService;

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
	 * zhaoxi 2017年9月7日 descrption:获取当前分类的所有废品，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRepair/{cId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByCid(@PathVariable Long cId) {
		if (null == cId) {
			return LianjiuResult.build(401, "cId不能为空");
		}
		return productRepairService.getRepairByCid(cId);
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
	 * huangfu 2017年9月22日 descrption:根据维修id返回json模板
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getParamTemplate/{repairId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRepairByRepairParamTemplate(@PathVariable String repairId) {
		if(Util.isEmpty(repairId)){
			return LianjiuResult.build(401, "请传入正确的repairId");
		}
		return productRepairService.getRepairByRepairParamTemplate(repairId);
	}

}
