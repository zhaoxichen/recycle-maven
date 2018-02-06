package com.lianjiu.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 
 * 加盟商申请表对应AllianceBusinessApplication
 * @author whd
 *
 */
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBusinessApplication;
import com.lianjiu.service.user.AllianceBusinessApplicationService;

@Controller
@RequestMapping("/alliaceApplication")
public class AllianceBusinessApplicationController {
	@Autowired
	private AllianceBusinessApplicationService appService;

	/**
	 * whd 2017年09月06日 descrption:获取所有的加盟商申请信息分页
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/getAllianceAppByCid/{categoryId}/{pageNum}/{pageSize}")
	@ResponseBody
	public LianjiuResult getAllianceAppByCid(@PathVariable long categoryId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return appService.getAllianceAppByCid(categoryId, pageNum, pageSize);
	}
	/**
	 * whd 2017年09月06日 descrption:获取所有的加盟商申请信息分页
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/selectalliaceApplication/{pageNum}/{pageSize}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAlliaceApplication(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return appService.selectAlliaceApplication(pageNum, pageSize);
	}
	/**
	 * whd 2017年09月06日 descrption:获取所有的加盟商申请信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectalliaceApplication/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAlliaceApplication() {
		return appService.selectAlliaceApplication();
	}
	/**
	 * 
	 * whd 2017年9月8日 descrption:获取当前加盟商的所有申请信息
	 * 
	 * @param abunesId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBusinessDetailsByCid/{albnessApplicationId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getBusinessDetailsByCid(@PathVariable String albnessApplicationId) {
		if(Util.isEmpty(albnessApplicationId)){
			return LianjiuResult.build(401, "请传入正确的albnessApplicationId");
		}
		return appService.getBusinessDetailsByCid(albnessApplicationId);
	}

	/**
	 * whd 2017年09月06日 descrption:添加加盟商所有申请信息
	 * 
	 * @param allianceBusinessApplication
	 * @return
	 */
	@RequestMapping(value = "/addalliaceApplication", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addalliaceApplication(AllianceBusinessApplication allianceBusinessApplication) {
		if(null == allianceBusinessApplication){
			return LianjiuResult.build(401, "allianceBusinessApplication对象绑定错误");
		}
		Util.printModelDetails(allianceBusinessApplication);
		return appService.addAlliaceApplication(allianceBusinessApplication);
	}
	/**
	 * whd 2017年09月06日 descrption:更新修改加盟商所有申请信息
	 * 
	 * @param allianceBusinessApplication
	 * @return
	 */
	@RequestMapping(value = "/updatealliaceApplication", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updatealliaceApplication(AllianceBusinessApplication allianceBusinessApplication) {
		if(null == allianceBusinessApplication){
			return LianjiuResult.build(401, "allianceBusinessApplication对象绑定错误");
		}
		Util.printModelDetails(allianceBusinessApplication);
		return appService.updateAlliaceApplication(allianceBusinessApplication);
	}
	/**
	 * whd 2017年09月06日 descrption:根据abunesId删除加盟商
	 * 
	 * @param abunesId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deleteAllianceBusinessApplicationById/{albnessApplicationId}/id=23", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteAllianceBusinessApplicationById(@PathVariable String albnessApplicationId) {
		if(Util.isEmpty(albnessApplicationId)){
			return LianjiuResult.build(401, "请传入正确的albnessApplicationId");
		}
		System.out.println("allianceBusinessApplication:" + albnessApplicationId);
		return appService.deleteAllianceBusinessApplicationById(albnessApplicationId);
	}
	
	/**
	 * 
	 * huangfu 2017年1月9日 descrption:维修订单模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(AllianceBusinessApplication allianceBusinessApplication, int pageNum,int pageSize,String cratedStart,String cratedOver) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("allianceBusinessApplication getAlbnessApplicationName："+allianceBusinessApplication.getAlbnessApplicationName());
		System.out.println("getAlbnessApplicationName getAlbnessApplicationPhone："+allianceBusinessApplication.getAlbnessApplicationPhone());
		System.out.println("cratedStart："+cratedStart);
		System.out.println("cratedOver："+cratedOver);
		System.out.println("pageNum："+pageNum);
		System.out.println("pageSize："+pageSize);
		
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return appService.vagueQuery(allianceBusinessApplication, pageNum,pageSize,cratedStart,cratedOver);
	}
}
