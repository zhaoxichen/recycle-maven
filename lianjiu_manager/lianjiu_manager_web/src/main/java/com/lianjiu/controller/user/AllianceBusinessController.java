package com.lianjiu.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBusiness;
import com.lianjiu.service.user.AllianceBusinessService;

/**
 * 加盟商登陆信息对应AllianceBusiness
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/allianceBusiness")
public class AllianceBusinessController {
	
	@Autowired
	private AllianceBusinessService allianceBusinessService;

	/**
	 * whd 2017年09月06日 descrption:获取所有的加盟商申请信息分页
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/selectAllBusiness/{pageNum}/{pageSize}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAllBusiness(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return allianceBusinessService.selectAllBusiness(pageNum, pageSize);
	}
	/**
	 * whd 2017年09月06日 descrption:获取所有的加盟商申请信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectAllBusiness/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAllBusiness() {
		return allianceBusinessService.selectAllBusiness();
	}
	/**
	 * 
	 * whd 2017年9月8日 descrption:获取当前加盟商的所有申请信息
	 * 
	 * @param abunesId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getAllBusinessById/{allianceBusinessId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getAllBusinessById(@PathVariable String allianceBusinessId) {
		if(Util.isEmpty(allianceBusinessId)){
			return LianjiuResult.build(401, "请传入正确的allianceBusinessId");
		}
		return allianceBusinessService.getAllBusinessById(allianceBusinessId);
	}
	/**
	 * whd 2017年09月06日 descrption:更新修改加盟商所有申请信息
	 * 
	 * @param allianceBusinessApplication
	 * @return
	 */
	@RequestMapping(value = "/updateAllBusiness", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateAllBusiness(AllianceBusiness allianceBusiness) {
		if(null == allianceBusiness){
			return LianjiuResult.build(401, "allianceBusiness对象绑定错误");
		}
		Util.printModelDetails(allianceBusiness);
		return allianceBusinessService.updateAllBusiness(allianceBusiness);
	}

	/**
	 * whd 2017年09月06日 descrption:添加加盟商所有信息
	 * 
	 * @param allianceBusiness
	 * @return
	 */
	@RequestMapping(value = "/addAllianceBusiness", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addAllianceBusiness(AllianceBusiness allianceBusiness) {
		if(null == allianceBusiness){
			return LianjiuResult.build(401, "allianceBusiness对象绑定错误");
		}
		Util.printModelDetails(allianceBusiness);
		return allianceBusinessService.addAllianceBusiness(allianceBusiness);
	}

	/**
	 * whd 2017年09月06日 descrption:根据abunesId删除加盟商
	 * 
	 * @param abunesId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deleteAllianceBusinessById/{allianceBusinessId}/id=23", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteAllianceBusinessById(@PathVariable String allianceBusinessId) {
		if(Util.isEmpty(allianceBusinessId)){
			return LianjiuResult.build(401, "请传入正确的allianceBusinessId");
		}
		System.out.println("allianceBusinessId:" + allianceBusinessId);
		return allianceBusinessService.deleteAllianceBusinessById(allianceBusinessId);
	}
}
