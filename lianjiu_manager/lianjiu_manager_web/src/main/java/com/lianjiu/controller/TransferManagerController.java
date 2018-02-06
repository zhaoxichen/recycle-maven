package com.lianjiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.service.TransferManagerService;

/**
 * 微信银行卡提现  个人提现  后台管理系统操作
 * 创建时间：2016年12月28日 
 * 
 * @author huangfu
 * 
 */

@Controller
@RequestMapping("/tmController")
public class TransferManagerController {
	
	@Autowired
	TransferManagerService transferManagerService;
	
	/**
	 * 确认提现完成
	 * 
	 * @param money
	 * @param vip
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/transferOk", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult transferOk(String userId,String recordId){
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}
		if(StringUtil.isEmpty(recordId)){
			return LianjiuResult.build(401, "订单id不能为空");
		}
		return transferManagerService.transferOk(userId,recordId);
	}
	
	/**
	 * 查询所有待提现的订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getAllTransfer/{pageNum}/{pageSize}/{state}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getAllTransfer( @PathVariable int pageNum, @PathVariable int pageSize ,@PathVariable String state){
		System.out.println("state:"+state);
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		if(null == state || state.equals("{state}")){
			return LianjiuResult.build(401, "请传入正确的state(null)");
		}
		if(!state.equals("0")&&!state.equals("1")){
			return LianjiuResult.build(401, "请传入正确的state");
		}
		return transferManagerService.getAllTransfer(state,pageNum,pageSize);
	}
	
	/**
	 * 驳回
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/transferNo", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult transferNo(String userId,String recordId,String msg){
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}
		if(StringUtil.isEmpty(recordId)){
			return LianjiuResult.build(401, "订单id不能为空");
		}
		if(StringUtil.isEmpty(msg)){
			return LianjiuResult.build(401, "驳回原因不能为空");
		}
		return transferManagerService.transferNo(userId,recordId,msg);
	}
	
	/**
	 * 查询提现的订单 单独
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getTransferById/{userId}/{recordId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getTransferById(@PathVariable String userId,@PathVariable String recordId){
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}
		if(StringUtil.isEmpty(recordId)){
			return LianjiuResult.build(401, "订单id不能为空");
		}
		return transferManagerService.getTransferById(userId,recordId);
	}
	
	/**
	 * 
	 * huangfu 2017年1月9日 descrption:提现信息用户模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(UserWalletRecord userWalletRecord,String state, int pageNum,int pageSize,String cratedStart,String cratedOver) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("state:"+state);
		if(null == state || state.equals("{state}")){
			return LianjiuResult.build(401, "请传入正确的state(null)");
		}
		if(!state.equals("0")&&!state.equals("1")){
			return LianjiuResult.build(401, "请传入正确的state");
		}
		System.out.println("userWalletRecord getUserId："+userWalletRecord.getUserId());
		System.out.println("userWalletRecord getRecordName："+userWalletRecord.getRecordName());
		System.out.println("userWalletRecord getRecordStatus："+userWalletRecord.getRecordStatus());
		System.out.println("userWalletRecord getMsg："+userWalletRecord.getMsg());
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
		return transferManagerService.vagueQuery(userWalletRecord,state, pageNum,pageSize,cratedStart,cratedOver);
	}
}
