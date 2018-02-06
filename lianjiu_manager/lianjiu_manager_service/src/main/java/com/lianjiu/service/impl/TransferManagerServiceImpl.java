package com.lianjiu.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.service.TransferManagerService;
@Service
public class TransferManagerServiceImpl implements TransferManagerService {

	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;

	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	
	@Autowired
	UserDetailsMapper userDetailsMapper;
	
	@Autowired
	private UserMapper userMapper;
	//确认提现
	@Override
	public LianjiuResult transferOk(String userId,String recordId) {
		UserWalletRecord userWalletRecord = userWalletRecordMapper.selectByPrimaryKey(recordId);
		if(null == userWalletRecord){
			return LianjiuResult.build(501, "订单不存在！");
		}
		byte income = 0;
		if(userWalletRecord.getIsIncome().equals(1)||userWalletRecord.getIsIncome()==1){
			income = 0;
		}else if(userWalletRecord.getIsIncome().equals(9)||userWalletRecord.getIsIncome()==9){
			income = 2;
		}else{
			return LianjiuResult.build(503, "订单状态错误！");
		}
		//待提现的金额
		String money = userWalletRecord.getRecordPrice();
		System.out.println("待提现的金额："+money);
		//提现中
		String drawingMoney = walletLianjiuMapper.selectUserDrawingMoney(userId);
		System.out.println("提现中的金额："+drawingMoney);
		//已提现
		String drawedMoney = walletLianjiuMapper.selectUserDrawedMoney(userId);
		System.out.println("已提现的金额："+drawedMoney);
		if(null == money || money.length()==0){
			return LianjiuResult.build(504, "待提现的金额错误！");
		}else if(null == drawingMoney || drawingMoney.length()==0){
			return LianjiuResult.build(504, "提现中的金额错误！");
		}else if(null == drawedMoney || drawedMoney.length()==0){
			return LianjiuResult.build(504, "已提现的金额错误！");
		}
		BigDecimal bmoney = new BigDecimal(money);
		BigDecimal bdrawingMoney = new BigDecimal(drawingMoney);
		BigDecimal bdrawedMoney = new BigDecimal(drawedMoney);
		//提现中的金额等与正在提现中的订单的金额
		if(money.equals(drawingMoney)){
			//修改已提现的金额
			int counts = walletLianjiuMapper.updateUserWalletDrawedMoney(bdrawedMoney.add(bmoney).toString(), userId);
			System.out.println("updateUserWalletDrawedMoney 修改了："+counts+"条数据");
			//修改提现中的金额
			int countss = walletLianjiuMapper.updateUserWalletDrawingMoney("0", userId);
			System.out.println("updateUserWalletDrawingMoney 修改了："+countss+"条数据");
		}else{
			//提现中金额
			String overMoney = bdrawingMoney.subtract(bmoney).toString();
			if(overMoney.contains("-")){
				return LianjiuResult.build(502, "金额处理时发生错误！");
			}
			drawedMoney = bdrawedMoney.add(bmoney).toString();
			//修改已提现的金额
			int counts = walletLianjiuMapper.updateUserWalletDrawedMoney(drawedMoney, userId);
			System.out.println("updateUserWalletDrawedMoney 修改了："+counts+"条数据");
			//修改提现中的金额
			int countss = walletLianjiuMapper.updateUserWalletDrawingMoney(overMoney, userId);
			System.out.println("updateUserWalletDrawingMoney 修改了："+countss+"条数据");
			
		}
		//修改支付状态
		UserWalletRecord record = new UserWalletRecord();
		record.setRecordId(recordId);
		record.setRecordStatus("1");
		record.setIsIncome(income);
		record.setRecordUpdated(new Date());
		
		int count = userWalletRecordMapper.updateByPrimaryKeySelective(record);
		System.out.println("userWalletRecordMapper 修改了："+count+"条数据");
		return LianjiuResult.ok("确认提现成功~");
	}
	//查询所有
	@Override
	public LianjiuResult getAllTransfer(String state,int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<UserWalletRecord> lists = null;
		if(state.equals("0")){
			lists = userWalletRecordMapper.selectAll();
		}else if(state.equals("1")){
			lists = userWalletRecordMapper.selectAlls();
		}
		Page<UserWalletRecord> list = (Page<UserWalletRecord>) lists;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(lists);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}
	//驳回
	@Override
	public LianjiuResult transferNo(String userId, String recordId, String msg) {
		String phone = userMapper.getPhoneById(userId);
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(501, "该用户手机错误");
		}
		UserWalletRecord userWalletRecord = userWalletRecordMapper.selectByPrimaryKey(recordId);
		if(null == userWalletRecord){
			return LianjiuResult.build(501, "订单不存在！");
		}
		byte income = 0;
		System.out.println("userWalletRecord.getIsIncome()："+userWalletRecord.getIsIncome());
		if(userWalletRecord.getIsIncome().equals(1)||userWalletRecord.getIsIncome()==1){
			income = 0;
		}else if(userWalletRecord.getIsIncome().equals(9)||userWalletRecord.getIsIncome()==9){
			income = 2;
		}else{
			return LianjiuResult.build(503, "订单状态错误！");
		}
		//待提现的金额
		String money = userWalletRecord.getRecordPrice();
		//提现中
		String drawingMoney = walletLianjiuMapper.selectUserDrawingMoney(userId);
		//用户余额
		String userMoney = walletLianjiuMapper.selectUserMoney(userId);
		
		if(null == money || money.length()==0){
			return LianjiuResult.build(504, "待提现的金额错误！");
		}else if(null == drawingMoney || drawingMoney.length()==0){
			return LianjiuResult.build(504, "提现中的金额错误！");
		}else if(null == userMoney || userMoney.length()==0){
			return LianjiuResult.build(504, "用户余额错误！");
		}
		
		BigDecimal bmoney = new BigDecimal(money);
		System.out.println("待提现金额："+money);
		
		BigDecimal bdrawingMoney = new BigDecimal(drawingMoney);
		System.out.println("提现中金额："+drawingMoney);
		
		BigDecimal buserMoney = new BigDecimal(userMoney);
		System.out.println("用户余额："+userMoney);
		
		//提现中的金额等与正在提现中的订单的金额
		if(money.equals(drawingMoney)){
			//修改用户的金额
			int counts = walletLianjiuMapper.updateUserWalletMoney(buserMoney.add(bmoney).toString(), userId);
			System.out.println("updateUserWalletDrawedMoney 修改了："+counts+"条数据");
			//修改提现中的金额
			int countss = walletLianjiuMapper.updateUserWalletDrawingMoney("0", userId);
			System.out.println("updateUserWalletDrawingMoney 修改了："+countss+"条数据");
		}else{
			//提现中金额-去该订单的金额
			String overMoney = bdrawingMoney.subtract(bmoney).toString();
			System.out.println("提现中金额："+bdrawingMoney);
			System.out.println("该订单金额："+bmoney);
			if(overMoney.contains("-")){
				return LianjiuResult.build(502, "金额处理时发生错误！");
			}
			userMoney = buserMoney.add(bmoney).toString();
			System.out.println("已提现用户的余额+该笔订单的金额："+userMoney);
			//修改已提现的金额
			int counts = walletLianjiuMapper.updateUserWalletMoney(userMoney, userId);
			System.out.println("updateUserWalletDrawedMoney 修改了："+counts+"条数据");
			//修改提现中的金额
			System.out.println("提现中金额："+overMoney);
			int countss = walletLianjiuMapper.updateUserWalletDrawingMoney(overMoney, userId);
			System.out.println("updateUserWalletDrawingMoney 修改了："+countss+"条数据");
			
		}
		//修改支付状态
		UserWalletRecord record = new UserWalletRecord();
		record.setRecordId(recordId);
		record.setRecordStatus("2");
		record.setIsIncome(income);
		record.setMsg(msg);
		record.setRecordUpdated(new Date());
		int count = userWalletRecordMapper.updateByPrimaryKeySelective(record);
		System.out.println("userWalletRecordMapper 修改了："+count+"条数据");
		
		System.out.println("用户" + userId + "的手机号：" + phone);
		System.out.println("开始发送短信：");
		sendSMS.sendMessage("15", "+86", phone, msg);
		return LianjiuResult.ok("发送成功");
	}
	//查看详情
	@Override
	public LianjiuResult getTransferById(String userId, String recordId) {
		UserWalletRecord userWalletRecord = userWalletRecordMapper.selectByPrimaryKey(recordId);
		if(null == userWalletRecord){
			return LianjiuResult.build(501, "订单不存在！");
		}
		Map<String, Object> map = new HashMap<String,Object>();
		String detailsId = userMapper.getDetailsId(userId);
		UserDetails ud = userDetailsMapper.selectByPrimaryKey(detailsId);
		if(null == ud){
			UserDetails uds = userDetailsMapper.selectByPrimaryKey(userId);
			UserDetails udss = userDetailsMapper.selectByPrimaryKey(userId+"1");
			System.out.println("uds:"+uds);
			System.out.println("udss:"+udss);
			if(null == uds &&null == udss){
				return LianjiuResult.build(502, "获取用户信息错误！");
			}
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String str=sdf.format(userWalletRecord.getRecordCreated());  
		String updateDate = sdf.format(userWalletRecord.getRecordUpdated()); 
		
		if(userWalletRecord.getIsIncome()==1||userWalletRecord.getIsIncome().equals("1")
				||userWalletRecord.getIsIncome()==0||userWalletRecord.getIsIncome().equals("0")){
			
			if(null == ud.getUserOpenid()||ud.getUserOpenid().length()==0){
				if(null == userWalletRecord.getRelativeId()||userWalletRecord.getRelativeId().length()==0){
					return LianjiuResult.build(503, "获取openId错误！"); 
				}
				ud.setUserOpenid(userWalletRecord.getRelativeId());
			}
			map.put("id", recordId);
			map.put("opendId", ud.getUserOpenid());
			map.put("cardId", "");
			map.put("cardName", "");
			map.put("recordStatus", userWalletRecord.getRecordStatus());
			if(userWalletRecord.getRecordStatus().equals("2")){
				map.put("msg", userWalletRecord.getMsg());
			}
			map.put("updateDate", updateDate);
			map.put("createDate", str);
		}else if(userWalletRecord.getIsIncome()==9||userWalletRecord.getIsIncome().equals("9")||
				userWalletRecord.getIsIncome()==2||userWalletRecord.getIsIncome().equals("2")){
			if(null == ud.getUdetailsCardNo() || ud.getUdetailsCardNo().length()==0
					||null == ud.getUdetailsCardBank() || ud.getUdetailsCardBank().length()==0
					){
				return LianjiuResult.build(504, "获取用户信息银行卡错误！");
			}
			map.put("id", recordId);
			map.put("opendId", "");
			map.put("cardId", ud.getUdetailsCardNo());
			map.put("cardName", ud.getUdetailsCardBank());
			map.put("recordStatus", userWalletRecord.getRecordStatus());
			if(userWalletRecord.getRecordStatus().equals("2")){
				map.put("msg", userWalletRecord.getMsg());
			}
			map.put("updateDate", updateDate);
			map.put("createDate", str);
		}else{
			return LianjiuResult.build(503, "订单状态错误！");
		}
		return LianjiuResult.ok(map);
	}

	@Override
	public LianjiuResult vagueQuery(UserWalletRecord userWalletRecord,String state, int pageNum, int pageSize, String cratedStart,
			String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<UserWalletRecord> ordersList = null;
		if(state.equals("0")){
			ordersList = userWalletRecordMapper.vagueQuery(userWalletRecord,cratedStart,cratedOver);
		}else if(state.equals("1")){
			ordersList = userWalletRecordMapper.vagueQuerys(userWalletRecord,cratedStart,cratedOver);
		}
		
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<UserWalletRecord> list = (Page<UserWalletRecord>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}
}
