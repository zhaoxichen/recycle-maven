package com.lianjiu.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.service.user.AllianceBusinessDetailsService;

/**
 * 加盟商详情对应操作AllianceBusinessDetails
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/allianceDetails")
public class AllianceBusinessDetailsController {

	@Autowired
	private AllianceBusinessDetailsService allianceBusinessDetailsService;

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption: 通过分类id获取加盟商信息
	 * 
	 * @param cId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBusinessDetailsByCid/{categoryId}/{pageNum}/{pageSize}")
	@ResponseBody
	public LianjiuResult getBusinessDetailsByCid(@PathVariable long categoryId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return allianceBusinessDetailsService.getBusinessDetailsByCid(categoryId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:查询所有加盟商信息
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBusinessDetails/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getBusinessDetails() {
		return allianceBusinessDetailsService.getBusinessDetails();
	}

	/**
	 * whd 2017年09月06日 descrption:获取所有的加盟商信息分页
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/allianceAll/{pageNum}/{pageSize}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAllBusiness(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return allianceBusinessDetailsService.selectAllBusinessDetails(pageNum, pageSize);
	}

	/**
	 * 
	 * whd 2017年9月8日 descrption:获取当前加盟商的所有信息
	 * 
	 * @param abunesId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBusinessDetailsById/{abunesId}/ByAbunesId/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getBusinessDetailsById(@PathVariable String abunesId) {
		if (Util.isEmpty(abunesId)) {
			return LianjiuResult.build(400, "请传入加盟商编号");
		}
		return allianceBusinessDetailsService.getBusinessDetailsById(abunesId);
	}

	/**
	 * whd 2017年09月06日 descrption:添加加盟商所有信息
	 * 
	 * @param allianceBusinessDetails
	 * @return
	 */
	@RequestMapping(value = "/addAllianceBusinessDetails", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails) {
		if(null == allianceBusinessDetails){
			return LianjiuResult.build(401, "allianceBusinessDetails对象绑定错误");
		}
		Util.printModelDetails(allianceBusinessDetails);
		return allianceBusinessDetailsService.addAllianceBusinessDetails(allianceBusinessDetails);
	}

	/**
	 * whd 2017年09月06日 descrption:根据abunesId删除加盟商
	 * 
	 * @param abunesId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deleteAllianceBusinessDetailsById/{abunesId}/id=23", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteAllianceBusinessDetailsById(@PathVariable String abunesId) {
		if(Util.isEmpty(abunesId)){
			return LianjiuResult.build(401, "请传入正确的abunesId");
		}
		System.out.println("abunesId:" + abunesId);
		return allianceBusinessDetailsService.deleteAllianceBusinessDetailsById(abunesId);
	}

	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:更新加盟商信息
	 * 
	 * @param allianceBusinessDetails
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateAllianceBusinessDetails", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails) {
		if(null == allianceBusinessDetails){
			return LianjiuResult.build(401, "allianceBusinessDetails对象绑定错误");
		}
		Util.printModelDetails(allianceBusinessDetails);
		return allianceBusinessDetailsService.updateAllianceBusinessDetails(allianceBusinessDetails);
	}

	/**
	 * 
	 * whd 2017年8月29日 descrption:操作（开通取消接单资格）
	 * 
	 * @param allianceBusinessDetails
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyAccept", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyAccept(String abunesId, int type) {
		if (Util.isEmpty(abunesId)) {
			return LianjiuResult.build(400, "请传入加盟商编号");
		}
		if (type != 0) {
			if (type != 1) {
				System.err.println("type值：" + type);
				return LianjiuResult.build(401, "请传入正确的type值");
			}
		}
		System.err.println("type值：" + type);
		return allianceBusinessDetailsService.modifyAccept(abunesId, type);
	}
	/**
	 * 
	 * whd 2017年8月29日 descrption:钱包明细
	 * 
	 * @param allianceBusinessDetails
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectAssetDetails", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAssetDetails(String abunesId) {
		if (Util.isEmpty(abunesId)) {
			return LianjiuResult.build(400, "请传入加盟商编号");
		}
		
		return allianceBusinessDetailsService.selectAssetDetails(abunesId);
	}
	
	/**
	 * 
	 * huangfu 2017年1月9日 descrption:加盟商用户模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(AllianceBusinessDetails allianceBusinessDetails, int pageNum,int pageSize,String cratedStart,String cratedOver) {
		System.out.println("allianceBusinessDetails getAbunesId："+allianceBusinessDetails.getAbunesId());
		System.out.println("allianceBusinessDetails getAbunesPhone："+allianceBusinessDetails.getAbunesPhone());
		System.out.println("allianceBusinessDetails getAbunesAcceptOrder："+allianceBusinessDetails.getAbunesAcceptOrder());
		System.out.println("allianceBusinessDetails getAbunesOperation："+allianceBusinessDetails.getAbunesOperation());
		System.out.println("allianceBusinessDetails getAbunesCreated："+allianceBusinessDetails.getAbunesCreated());
		System.out.println("allianceBusinessDetails getAbunesUnfinishedOrder："+allianceBusinessDetails.getAbunesUnfinishedOrder());
		System.out.println("allianceBusinessDetails getAbunesCancelOrders："+allianceBusinessDetails.getAbunesCancelOrders());
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
		
		return allianceBusinessDetailsService.vagueQuery(allianceBusinessDetails, pageNum,pageSize,cratedStart,cratedOver);
	}
}
