package com.lianjiu.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBuappRemarks;
import com.lianjiu.service.user.AlRemarksService;

/**
 * 备注控制层
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/remarks")
public class AlRemarksController {
	@Autowired
	private AlRemarksService alRemarksService;

	
	
	
	/**
	 * whd 2017年09月06日 descrption:添加申请表备注
	 * 
	 * @param allianceBusinessApplication
	 * @return
	 */
	@RequestMapping(value = "/addAllBusinessRemarks", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addAllBusinessRemarks(AllianceBuappRemarks allianceBuappRemarks) {
		if(null == allianceBuappRemarks){
			return LianjiuResult.build(401, "comment对象绑定错误");
		}
		Util.printModelDetails(allianceBuappRemarks);
		return alRemarksService.addAllBusinessRemarks(allianceBuappRemarks);
	}

	/**
	 * whd 2017年09月06日 descrption:获取当前的加盟商申请备注信息分页
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/selectAllBusinessRemarks/{albnessApplicationId}/{pageNum}/{pageSize}/1/5", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAllBusinessRemarks(@PathVariable String albnessApplicationId,@PathVariable int pageNum, @PathVariable int pageSize) {
		if(Util.isEmpty(albnessApplicationId)){
			return LianjiuResult.build(401, "请传入正确的albnessApplicationId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return alRemarksService.selectAllBusinessRemarks(albnessApplicationId,pageNum, pageSize);
	}

	/**
	 * whd 2017年09月06日 descrption:获取当前加盟商的所有申请备注信息
	 * @return
	 */
	@RequestMapping(value = "/selectAllBusinessRemarksByCid/{albnessApplicationId}/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAllBusinessRemarksByCid(@PathVariable String albnessApplicationId)
	{
		if(Util.isEmpty(albnessApplicationId)){
			return LianjiuResult.build(401, "请传入正确的albnessApplicationId");
		}
		return alRemarksService.selectAllBusinessRemarksByCid(albnessApplicationId);
	}
}
