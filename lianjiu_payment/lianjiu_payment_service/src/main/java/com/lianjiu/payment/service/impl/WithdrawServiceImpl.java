package com.lianjiu.payment.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.StringUtils;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.dao.config.CollectionUtil;
import com.lianjiu.payment.dao.config.HttpUtils;
import com.lianjiu.payment.dao.config.JsonResult;
import com.lianjiu.payment.dao.config.PayUtil;
import com.lianjiu.payment.dao.config.ResponseData;
import com.lianjiu.payment.dao.config.SerializerFeatureUtil;
import com.lianjiu.payment.dao.config.WebUtil;
import com.lianjiu.payment.dao.config.XmlUtil;
import com.lianjiu.payment.dao.wxh5.CommonUtil;
import com.lianjiu.payment.dao.wxh5.Sha1Util;
import com.lianjiu.payment.service.WithdrawService;
import com.lianjiu.payment.service.util.HttpClientCustomSSL;
import com.lianjiu.payment.service.util.SignTools;
import com.lianjiu.payment.service.util.WXPayUtil;
import com.lianjiu.payment.service.util.XMLUtil;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.WxOrderMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;

import net.sf.json.JSONObject;

@Service
public class WithdrawServiceImpl implements WithdrawService {


	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private WxOrderMapper wxOrderMapper;
	@Autowired
	private JedisClient jedisClient;

	private static Logger LOG = Logger.getLogger("visit");
	
	@Override
	public LianjiuResult wxpayCheck(String userId) {
		String openid = userDetailsMapper.selectOpenidByUser(userId);
		if(openid==null||openid.trim().isEmpty()){
			return LianjiuResult.build(501, "验证不通过,请先用获取openid");
		}
		return LianjiuResult.ok("验证通过");
	}

	@Override
	public LianjiuResult wxpayCode(String userId, String money, String name) {
		System.out.println("wxpayCode传递参数:userID"+userId+"money:"+money+":name:"+name);
		WalletLianjiu wallet = walletLianjiuMapper.selectWalletByUserId(userId);
		//金额判断
		/*if(Double.parseDouble(money)<10){
			return LianjiuResult.build(501,"提现金额最低10元"); 
		}*/
		if(wallet==null||wallet.getWalletMoney().equals("0")){
			return LianjiuResult.build(501,"钱包余额为零");
		}
		Double walletMoney = Double.valueOf(wallet.getWalletMoney());
		if(Double.valueOf(money)>walletMoney){
			return LianjiuResult.build(501,"提现金额超出钱包余额");
		}
		
		//共账号及商户相关参数
		String appid = WXPayUtil.APPID;
		money=String.valueOf((int)(Double.valueOf(money)*100));
		String backUri = "https://payment.lianjiuhuishou.com/payment/withdrawController/GZHpayReturn";
		//授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
		//最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名,
		//缓存中文名字
		jedisClient.set(userId+"user_name_pass", name);
		//比如 Sign = %3D%2F%CS% 
		backUri = backUri+"?userId="+userId+"&money="+money+"&describe=test";
		//URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		try {
			backUri = URLEncoder.encode(backUri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
				"appid=" + appid+
				"&redirect_uri=" +
				 backUri+
				"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		System.out.println("最终:url:"+url);
		return LianjiuResult.ok(url);
	}

	@Override
	public void transferPay(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("[/WXtransfer/GZHpay]");
		//业务判断 openid是否有收款资格
		
		//获取openid
		//网页授权后获取传递的参数
		String userId = request.getParameter("userId"); 	
		String partner_trade_no = Common.getczid();
		String money = request.getParameter("money");
		String name = jedisClient.get(userId+"user_name_pass");
		jedisClient.del(userId+"user_name_pass");
		String code = request.getParameter("code");
		System.out.println("回调参数:userID"+userId+"money:"+money+"code"+code+":name:"+name);
		String openid ="";
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WXPayUtil.APPID+"&secret="+WXPayUtil.API_SECRET+"&code="+code+"&grant_type=authorization_code";
		
		JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
		if (null != jsonObject) {
			openid = jsonObject.getString("openid");
			//保存用户唯一openId'
			System.out.println("openid:"+openid);
			userDetailsMapper.saveOpenId(openid,userId);
		}
		
		// 单位为分
		try {
			TreeMap<String, String> parm = new TreeMap<String, String>();  
			parm.put("mch_appid", WXPayUtil.APPID); //公众账号appid
			parm.put("mchid", WXPayUtil.MCH_ID); //商户号
			parm.put("nonce_str", PayUtil.getNonceStr()); //随机字符串
			parm.put("partner_trade_no", partner_trade_no); //商户订单号
			parm.put("openid", openid); //用户openid	
			parm.put("check_name", "FORCE_CHECK"); //校验用户姓名选项 OPTION_CHECK
			parm.put("re_user_name", name);//check_name设置为FORCE_CHECK或OPTION_CHECK，则必填
			parm.put("amount", money); //转账金额
			parm.put("desc", "转账到个人"); //企业付款描述信息
			parm.put("spbill_create_ip", PayUtil.getLocalIp(request)); //Ip地址
			parm.put("sign", SignTools.buildRequestMysign(parm));
			//减去钱包里边对应的金额 失败再转回去
			WalletLianjiu wall = new WalletLianjiu();
			wall.setUserId(userId);
			money = Double.toString(Double.valueOf(money) / 100);
			Double cash = Double.valueOf(money);
			money = String.format("%.2f",cash);
			wall.setWalletMoney(money);
			int i = walletLianjiuMapper.reduceWalletMoney(wall);
			while(i<1){
				int f = walletLianjiuMapper.reduceWalletMoney(wall);
				if(f==1){
					break;
				}
			}
			
			String resultXML = HttpClientCustomSSL.httpClientResult(parm);//转账
			System.out.println(resultXML);
			//交易结果处理
			Map<String, Object> resultMap =  XMLUtil.doXMLParse(resultXML);
            String return_code = (String) resultMap.get("return_code");
            String result_code = (String) resultMap.get("result_code");
            
			System.out.println("请求结果:"+resultMap);
			
			response.setContentType("application/xml; charset=utf-8");
			if (return_code.equalsIgnoreCase("SUCCESS") && result_code.equalsIgnoreCase("SUCCESS")){
            	//交易成功
				LOG.info("转账成功：" +return_code+ ":" + result_code);
				
				//微信提现账单
				WxOrder order = new WxOrder();
				//order.setOutTradeNo(out_trade_no);
				order.setType("3");// 提现
				order.setUserId(userId);
				order.setNonce_str(Common.getUuid());
				order.setUser_ip(request.getRemoteAddr());
				order.setReMessage(resultMap.toString());
				order.setTotalFee(money);
				order.setDate(DateUtils.getTime());
				order.setMessage(name+"GZH");
				order.setSign(openid);
				order.setState("4");
				//提现的都是订单号
				order.setOutTradeNo((String) resultMap.get("partner_trade_no"));
				wxOrderMapper.insertWXOrder(order);
				//用户的流水账单
				UserWalletRecord user = new UserWalletRecord();
				user.setRecordId(IDUtils.genGUIDs());
				user.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));
				user.setRecordCreated(new Date());
				user.setRecordUpdated(new Date());
				user.setIsIncome((byte) 0);
				user.setUserId(userId);
				userWalletRecordMapper.insertSelective(user);
				//添加已提现
				walletLianjiuMapper.addWalletDrawedMoney(wall);
				
				
				
				Map<String, String> transferMap = new HashMap<>();
				transferMap.put("partner_trade_no", (String) resultMap.get("partner_trade_no"));//商户转账订单号
				transferMap.put("payment_no",  (String) resultMap.get("payment_no")); //微信订单号
				transferMap.put("payment_time",  (String) resultMap.get("payment_time")); //微信支付成功时间
				
				// 定向返回给前端
				System.out.println("请求成功https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true");
				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true&msg=提现成功");
				System.out.println("请求成功");
            	return;
            }else{ 
            	//转账失败再把钱转回去
            	int m = walletLianjiuMapper.addWalletMoney(wall);
    			while(m<1){
    				int n = walletLianjiuMapper.addWalletMoney(wall);
    				if(n==1){
    					break;
    				}
    			}
            	
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("NOTENOUGH")){
            		LOG.info("转账失败：" + (String) resultMap.get("return_msg") + ":" + (String) resultMap.get("err_code_des"));
            		UserWalletRecord wallet = new UserWalletRecord();
            		wallet.setRecordId(IDUtils.genWalletLianjiuId());
            		wallet.setRecordCreated(new Date());
            		wallet.setRecordUpdated(new Date());
            		wallet.setRecordName(name);
            		wallet.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));
            		wallet.setRelativeId(openid);
            		wallet.setIsIncome((byte)1);
            		wallet.setRecordStatus("0");
            		wallet.setUserId(userId);
            		if(userWalletRecordMapper.insertSelective(wallet)!=1){
            			while(true){
            				int s = userWalletRecordMapper.insertSelective(wallet);
            				if(s==1){
            					break;
            				}
            			}
            		}
            		
            		//先扣钱
                	int h = walletLianjiuMapper.reduceWalletMoney(wall);
        			while(h<1){
        				int f = walletLianjiuMapper.reduceWalletMoney(wall);
        				if(f==1){
        					break;
        				}
        			}
        			//加到提现中
        			walletLianjiuMapper.walletDrawingMoney(wall);
            		// 定向返回给前端
        			String keyword1 = "提现已处理，金额将在24小时内转入微信零钱";
    				System.out.println("请求成功https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true&msg="+URLEncoder.encode(keyword1));
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true&msg="+URLEncoder.encode(keyword1));
    				System.out.println("请求成功");
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("NAME_MISMATCH")){
    				// 定向返回给前端
    				String keyword = "当前手机的微信真实姓名与所填姓名不一致";
    				System.out.println("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword));
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword));
    				System.out.println("请求失败");
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("V2_ACCOUNT_SIMPLE_BAN")){
    				// 定向返回给前端
    				System.out.println("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false");
    				String keyword2 = "您申请提现微信账户尚未实名认证，请更换其他微信账户";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword2));
    				System.out.println("请求失败");
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("SYSTEMERROR")){
    				// 定向返回给前端
    				System.out.println("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false");
    				String keyword3 = "系统繁忙，请稍后再试";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword3));
    				System.out.println("请求失败");
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("AMOUNT_LIMIT")){
    				// 定向返回给前端
    				System.out.println("提现金额超出限制。低于最小金额1.00元或累计超过20000.00元");
    				System.out.println("请求失败");
    				String  keyword4 = "提现金额超出限制。低于最小金额1.00元或累计超过20000.00元";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword4));
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("PARAM_ERROR")){
    				// 定向返回给前端
    				System.out.println("金额格式有误");
    				System.out.println("请求失败");
    				String  keyword5 = "金额格式有误";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword5));
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("ORDERPAID")){
    				// 定向返回给前端
    				System.out.println("该订单已支付");
    				System.out.println("请求失败");
    				String  keyword6 = "该订单已支付";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword6));
    				return;
    			}
    			
    			LOG.info("转账失败：" + (String) resultMap.get("err_code") + ":" + (String) resultMap.get("err_code_des"));
    			// 定向返回给前端
    			String keyword7 = (String) resultMap.get("err_code_des");
				System.out.println("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword7));
				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword7));
				return;
            }
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public LianjiuResult transferPayOther(HttpServletRequest request, HttpServletResponse response, String userId,
			String money, String openid, String name) {
		LOG.info("[/transfer/pay]");
		//业务判断 openid是否有收款资格
		System.out.println("支现获取的当下openid:"+openid+"userId:"+userId+"money"+money+"name:"+name);
		WalletLianjiu wallet = walletLianjiuMapper.selectWalletByUserId(userId);
		//金额判断
		/*if(Double.parseDouble(money)<10){
			return LianjiuResult.build(501,"提现金额最低10元"); 
		}*/
		if(wallet==null||wallet.getWalletMoney().equals("0")){
			return LianjiuResult.build(501,"钱包余额为零");
		}
		Double walletMoney = Double.valueOf(wallet.getWalletMoney());
		if(Double.valueOf(money)>walletMoney){
			return LianjiuResult.build(501,"提现金额超出钱包余额");
		}
		//获取openid
		//网页授权后获取传递的参数
		String partner_trade_no = Common.getczid(); 
		//更新最新的openid
		String openid1 = userDetailsMapper.selectOpenidByUser(userId);
		if(openid.equals("1")||openid==null||openid.trim().isEmpty()){
			if(openid1!=null&&!openid1.trim().isEmpty()){
				openid = openid1;
			}else{
				return LianjiuResult.build(501, "请先用获取openid");
			}
		}
		if(!StringUtils.areNotEmpty(userId,money)){
			return LianjiuResult.build(501, "参数不能为空");
		}
		
		if(!openid.equals(openid1)){
			userDetailsMapper.saveOpenId(openid, userId);
		}
		String ormoney = money;
		// 单位为分
		money = String.valueOf((int) (Double.valueOf(money) * 100));
		try {
			TreeMap<String, String> parm = new TreeMap<String, String>();  
			parm.put("mch_appid", WXPayUtil.APPID); //公众账号appid
			parm.put("mchid", WXPayUtil.MCH_ID); //商户号
			parm.put("nonce_str", PayUtil.getNonceStr()); //随机字符串
			parm.put("partner_trade_no", partner_trade_no); //商户订单号
			parm.put("openid", openid); //用户openid	
			//parm.put("check_name", "NO_CHECK"); //校验用户姓名选项 OPTION_CHECK
			parm.put("check_name", "FORCE_CHECK"); 
			parm.put("re_user_name", name);//check_name设置为FORCE_CHECK或OPTION_CHECK，则必填
			parm.put("amount", money); //转账金额
			parm.put("desc", "转账到个人"); //企业付款描述信息
			parm.put("spbill_create_ip", PayUtil.getLocalIp(request)); //Ip地址
			parm.put("sign", SignTools.buildRequestMysign(parm));
			//先减去钱包里边对应的金额 失败再加回去
			WalletLianjiu wall1 = new WalletLianjiu();
			wall1.setUserId(userId);
			/*money = Double.toString(Double.valueOf(money) / 100);
			Double cash = Double.valueOf(money);
			money = String.format("%.2f",cash);*/
			wall1.setWalletMoney(ormoney);
			int i = walletLianjiuMapper.reduceWalletMoney(wall1);
			while(i<1){
				int f = walletLianjiuMapper.reduceWalletMoney(wall1);
				if(f==1){
					break;
				}
			}
			
			
			String resultXML = HttpClientCustomSSL.httpClientResult(parm);//转账
			//交易结果处理
			Map<String, Object> resultMap =  XMLUtil.doXMLParse(resultXML);
            String return_code = (String) resultMap.get("return_code");
            String result_code = (String) resultMap.get("result_code");
			System.out.println("请求结果:"+resultMap);
			if (return_code.equalsIgnoreCase("SUCCESS") && result_code.equalsIgnoreCase("SUCCESS")){
            	//交易成功
				LOG.info("转账成功：" +return_code+ ":" + result_code);
				
				//微信提现账单
				WxOrder order = new WxOrder();
				order.setOutTradeNo((String) resultMap.get("partner_trade_no"));
				order.setType("3");// 提现
				order.setUserId(userId);
				order.setNonce_str(Common.getUuid());
				order.setUser_ip(request.getRemoteAddr());
				order.setReMessage(resultXML);
				order.setTotalFee(ormoney);
				order.setDate(DateUtils.getTime());
				order.setState("4");
				order.setSign(openid);
				order.setMessage(name);
				//提现的都是订单号
				order.setOutTradeNo((String) resultMap.get("partner_trade_no"));
				wxOrderMapper.insertWXOrder(order);
				//用户的流水账单
				UserWalletRecord user = new UserWalletRecord();
				user.setRecordId(IDUtils.genGUIDs());
				user.setRecordPrice(String.format("%.2f", Float.parseFloat(ormoney)));
				user.setRecordCreated(new Date());
				user.setRecordUpdated(new Date());
				user.setIsIncome((byte) 0);
				user.setUserId(userId);
				userWalletRecordMapper.insertSelective(user);
				
				//添加已提现
				walletLianjiuMapper.addWalletDrawedMoney(wall1);
				
				/*	Map<String, String> transferMap = new HashMap<>();
				transferMap.put("partner_trade_no", (String) resultMap.get("partner_trade_no"));//商户转账订单号
				transferMap.put("payment_no",  (String) resultMap.get("payment_no")); //微信订单号
				transferMap.put("payment_time",  (String) resultMap.get("payment_time")); //微信支付成功时间
				LianjiuResult li = new LianjiuResult(transferMap);*/
				return LianjiuResult.ok("提现成功,请耐心等待!");
            	
            }else{ 
            	//转账失败再把钱加回去
            	int m = walletLianjiuMapper.addWalletMoney(wall1);
    			while(m<1){
    				int n = walletLianjiuMapper.addWalletMoney(wall1);
    				if(n==1){
    					break;
    				}
    			}
            	
            	
            	if(((String) resultMap.get("err_code_des")).equalsIgnoreCase("余额不足")||((String) resultMap.get("err_code")).equalsIgnoreCase("NOTENOUGH")){
            		LOG.info("转账失败：" + (String) resultMap.get("return_msg") + ":" + (String) resultMap.get("err_code_des"));
            		
            		
            		UserWalletRecord wall = new UserWalletRecord();
            		wall.setRecordId(IDUtils.genWalletLianjiuId());
            		wall.setRecordCreated(new Date());
            		wall.setRecordUpdated(new Date());
            		wall.setRecordPrice(String.format("%.2f", Float.parseFloat(ormoney)));		
            		wall.setIsIncome((byte)1);
            		wall.setRecordStatus("0");
            		wall.setUserId(userId);
            		if(userWalletRecordMapper.insertSelective(wall)!=1){
            			return LianjiuResult.build(501, "微信支现添加失败");
            		}
            		
            		//待提现,提前减钱
                	int f = walletLianjiuMapper.reduceWalletMoney(wall1);
        			while(f<1){
        				int n = walletLianjiuMapper.reduceWalletMoney(wall1);
        				if(n==1){
        					break;
        				}
        			}
        			//加到提现中
        			walletLianjiuMapper.walletDrawingMoney(wall1);
            		
        			return LianjiuResult.ok("提现已处理，金额将在24小时内转入微信零钱");
            	}
            	
            	if(((String) resultMap.get("err_code")).equalsIgnoreCase("NAME_MISMATCH")){
    				// 定向返回给前端
    				System.out.println("当前手机的微信真实姓名与所填姓名不一致");
    				return LianjiuResult.build(501,"当前手机的微信真实姓名与所填姓名不一致");
    			}
            	
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("V2_ACCOUNT_SIMPLE_BAN")){
    				// 定向返回给前端
    				System.out.println("您申请提现微信账户尚未实名认证，请更换其他微信账户");
    				return LianjiuResult.build(501,"您申请提现微信账户尚未实名认证，请更换其他微信账户");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("SYSTEMERROR")){
    				// 定向返回给前端
    				System.out.println("系统繁忙，请稍后再试");
    				System.out.println("请求失败");
    				return LianjiuResult.build(501,"系统繁忙，请稍后再试");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("AMOUNT_LIMIT")){
    				// 定向返回给前端
    				System.out.println("提现金额超出限制。低于最小金额1.00元或累计超过20000.00元");
    				System.out.println("请求失败");
    				return LianjiuResult.build(501,"提现金额超出限制。低于最小金额1.00元或累计超过20000.00元");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("PARAM_ERROR")){
    				// 定向返回给前端
    				System.out.println("金额格式有误");
    				System.out.println("请求失败");
    				return LianjiuResult.build(501,"金额格式有误");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("ORDERPAID")){
    				// 定向返回给前端
    				System.out.println("该订单已支付");
    				System.out.println("请求失败");
    				return LianjiuResult.build(501,"该订单已支付");
    			}
    			
    			LOG.info("转账失败：" + (String) resultMap.get("return_msg") + ":" + (String) resultMap.get("err_code_des"));
    			return LianjiuResult.build(501, ""+(String) resultMap.get("err_code_des"));
            }
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return LianjiuResult.build(501, "传递参数有误");

		/*if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
			LOG.info("转账成功：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			Map<String, String> transferMap = new HashMap<>();
			transferMap.put("partner_trade_no", restmap.get("partner_trade_no"));//商户转账订单号
			transferMap.put("payment_no", restmap.get("payment_no")); //微信订单号
			transferMap.put("payment_time", restmap.get("payment_time")); //微信支付成功时间
			return LianjiuResult.ok(transferMap);
		}else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("转账失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
			return LianjiuResult.build(401, "转账失败:"+new ResponseData());
		} */
	}

	
	@Override
	public void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno,
			String callback) {
		LOG.info("[/transfer/pay/query]");
		if (StringUtil.isEmpty(tradeno)) {
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "转账订单号不能为空", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}

		Map<String, String> restmap = null;
		try {
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", WXPayUtil.APPID);
			parm.put("mch_id", WXPayUtil.MCH_ID);
			parm.put("partner_trade_no", tradeno);
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("sign", PayUtil.getSign(parm, WXPayUtil.API_SECRET));

			String restxml = HttpUtils.posts(WXPayUtil.TRANSFERS_PAY_QUERY, XmlUtil.xmlFormat(parm, true));
			restmap = XmlUtil.xmlParse(restxml);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
			// 订单查询成功 处理业务逻辑
			LOG.info("订单查询：订单" + restmap.get("partner_trade_no") + "支付成功");
			Map<String, String> transferMap = new HashMap<>();
			transferMap.put("partner_trade_no", restmap.get("partner_trade_no"));//商户转账订单号
			transferMap.put("openid", restmap.get("openid")); //收款微信号
			transferMap.put("payment_amount", restmap.get("payment_amount")); //转账金额
			transferMap.put("transfer_time", restmap.get("transfer_time")); //转账时间
			transferMap.put("desc", restmap.get("desc")); //转账描述
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(1, "订单转账成功", new ResponseData(null, transferMap)), SerializerFeatureUtil.FEATURES)));
		}else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("订单转账失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "订单转账失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
	}

	@Override
	public LianjiuResult unionpay(String userId, String money, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.info("银行卡提现:unionpay");
		if(StringUtil.hasEmpty(userId,money)){
			return LianjiuResult.build(501, "参数不能为空");
		}
		//金额判断
		/*if(Double.parseDouble(money)<10){
			return LianjiuResult.build(501,"提现金额最低10元"); 
		}*/

		Double walletMoney = Double.valueOf(walletLianjiuMapper.selectWalletByUserId(userId).getWalletMoney());
		if(Double.valueOf(money)>walletMoney){
			return LianjiuResult.build(401, "支现金额超出钱包余额");
		}
		
		WalletLianjiu wall1 = new WalletLianjiu();
		wall1.setUserId(userId);
		wall1.setWalletMoney(money);
		//待提现,提前减钱
    	int f = walletLianjiuMapper.reduceWalletMoney(wall1);
		while(f<1){
			int n = walletLianjiuMapper.reduceWalletMoney(wall1);
			if(n==1){
				break;
			}
		}
		//加到提现中
		walletLianjiuMapper.walletDrawingMoney(wall1);
		
		UserWalletRecord wallet = new UserWalletRecord();
		wallet.setRecordId(IDUtils.genWalletLianjiuId());
		wallet.setRecordCreated(new Date());
		wallet.setRecordUpdated(new Date());
		wallet.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));		
		wallet.setRecordStatus("0");
		wallet.setIsIncome((byte)2);
		wallet.setUserId(userId);
		if(userWalletRecordMapper.insertSelective(wallet)!=1){
			return LianjiuResult.build(501, "银行卡支现添加失败");
		}
		return LianjiuResult.ok("添加成功");
	}

	@Override
	public void wxpayGZHreturn(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("微信公众号支付:wxpayGZHreturn");
		try {
			// 网页授权后获取传递的参数
			System.out.println("微信GZH return");
			String code = request.getParameter("code");
			String userId = request.getParameter("userId");
			System.out.println("微信code:" + code);
			if (StringUtil.isEmpty(code)) {
				throw new RuntimeException("获取code失败");
			}
			// 通过code获取公众号对应的用户唯一openid code 5分钟不用就会过期
			String openId = "";
			String access_token = "";
			String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WXPayUtil.APPID + "&secret=" + WXPayUtil.API_SECRET
					+ "&code=" + code + "&grant_type=authorization_code";
			System.out.println("微信openId获取前");
			// https请求
			JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
			System.out.println(jsonObject);
			if (null != jsonObject) {
				openId = jsonObject.getString("openid");
				access_token = jsonObject.getString("access_token");
				System.out.println("微信openId:" + openId);
				// 保存用户唯一openId
				UserDetails ud = userDetailsMapper.selectByUserId(userId);
				String checkCode = IDUtils.genPWDId();
				if(ud.getCheckTime()==null||ud.getCheckTime().equals("")){
					int i = userDetailsMapper.saveOpenIdH5(openId, userId,new Date(),checkCode);
					// 定向返回给前端
					System.out.println("首次:https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode);
					response.sendRedirect("https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode);
					System.out.println("请求成功");
				}else{
					//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			       // Date date1 = sdf.parse(ud.getCheckTime());  
			       // Date date2 = sdf.parse("2015-07-23 12:59:28");  
					Date date = new Date();
			        long l = date.getTime() - ud.getCheckTime().getTime();  
			        long day = l / (24 * 60 * 60 * 1000);  
			        long hour = (l / (60 * 60 * 1000) - day * 24);  
			        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);  
			       // long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  
			        //超过三分钟就重新生成二维码
			        if(min>3){
			        	String checkCode1 = IDUtils.genPWDId();
						int i = userDetailsMapper.saveOpenIdH5(openId, userId,new Date(),checkCode1);
						// 定向返回给前端
						System.out.println("超过三分钟:https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode1);
						response.sendRedirect("https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode1);
						System.out.println("请求成功");
			        }else{
			        	int i = userDetailsMapper.saveOpenIdH5(openId, userId,ud.getCheckTime(),ud.getUserCheck());
						// 定向返回给前端
						System.out.println("未超过三分钟:https://m.lianjiuhuishou.com/weiX?checkCode="+ud.getUserCheck());
						response.sendRedirect("https://m.lianjiuhuishou.com/weiX?checkCode="+ud.getUserCheck());
						System.out.println("请求成功");
			        }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public LianjiuResult wxpayCheckCode(String userId, String checkCode, HttpServletRequest request,
			HttpServletResponse response) {
		UserDetails ud = userDetailsMapper.selectByUserId(userId);
		System.out.println(ud);
		String openid = ud.getUserOpenid();
		Date checkTime = ud.getCheckTime();
		String userCode = ud.getUserCheck();
		if(openid==null||openid.trim().isEmpty()){
			return LianjiuResult.build(501, "验证不通过,请先用获取openid");
		}
		Date date = new Date();
        long l = date.getTime() - checkTime.getTime();  
        long day = l / (24 * 60 * 60 * 1000);  
        long hour = (l / (60 * 60 * 1000) - day * 24);  
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);  
        //long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  
        if(min>3){
        	System.out.println("验证码超过三分钟,请重新扫描二维码");
        	return LianjiuResult.build(501, "验证码超过三分钟,请重新扫描二维码");
        }else{
        	if(checkCode.equals(userCode)){
        		System.out.println("验证码通过");
        		return LianjiuResult.ok("验证码通过");
        	}else{
        		System.out.println("验证码错误");
        		return LianjiuResult.build(501, "验证码错误");
        	}
        }
	}

	@Override
	public void invisit(HttpServletRequest request, HttpServletResponse response) {
		String TOKEN = "123456789";
	     // 微信加密签名  
	     String signature = request.getParameter("signature"); 
	     System.out.println("signature:"+signature);
	     // 随机字符串  
	     String echostr = request.getParameter("echostr");  
	     // 时间戳  
	     String timestamp = request.getParameter("timestamp");  
	     // 随机数  
	     String nonce = request.getParameter("nonce");  
	  
	     String[] str = { TOKEN, timestamp, nonce };  
	     Arrays.sort(str); // 字典序排序  
	     String bigStr = str[0] + str[1] + str[2];  
	     // SHA1加密  
	     String digest = Sha1Util.getSha1(bigStr)  
	             .toLowerCase();  
	     System.out.println("digest:"+digest);
	     // 确认请求来至微信  
	     if (digest.equals(signature)) {  
	         try {
				response.getWriter().print(echostr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error:"+e.getMessage());
			}  
	     }  
	}

}
