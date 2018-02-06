package com.lianjiu.payment.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import com.lianjiu.payment.service.TransferService;
import com.lianjiu.payment.service.util.HttpClientCustomSSL;
import com.lianjiu.payment.service.util.QRCodeUtil;
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
public class TransferServiceImpl implements TransferService{

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
	
	private static Logger loggerVisit = Logger.getLogger("visit");
	
	//微信企业支付资格验证
	@Override
	public LianjiuResult wxpayCheck(String userId, HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("开始进入方法：wxpayCheck 微信企业支付资格验证");
		String openid = userDetailsMapper.selectOpenidByUser(userId);
		if(openid==null||openid.trim().isEmpty()){
			return LianjiuResult.build(501, "验证不通过,请先用获取openid");
		}
		return LianjiuResult.ok("验证通过");
	}
	
	//微信企业支付 GZH内 获取code接口
	@Override
	public LianjiuResult wxpayCode(String userId, String money, String name, HttpServletRequest request,
			HttpServletResponse response) {
		loggerVisit.info("开始进入方法：wxpayCode 微信企业支付 GZH内 获取code接口");
		loggerVisit.info("wxpayCode传递参数:userID"+userId+"money:"+money+":name:"+name);
		WalletLianjiu wallet = walletLianjiuMapper.selectWalletByUserId(userId);
		//金额判断
		if(Double.parseDouble(money)<10){
			return LianjiuResult.build(501,"提现金额最低10元"); 
		}
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
		String backUri = "https://payment.lianjiuhuishou.com/payment/transferController/GZHpayReturn";
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
			//e.printStackTrace();
			loggerVisit.info("error:"+e.getMessage());
			return LianjiuResult.build(501, "编码转换出错");
		}
		//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
				"appid=" + appid+
				"&redirect_uri=" +
				 backUri+
				"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		loggerVisit.info("最终:url:"+url);
		loggerVisit.info("方法结束：wxpayCode");
		return LianjiuResult.ok(url);
	}

	//企业向个人支付转账  微信内
	@Override
	public void transferPay(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		//loggerVisit.info("[/WXtransfer/GZHpay]");
		loggerVisit.info("开始进入方法：transferPay 企业向个人支付转账 GZH内");
		//业务判断 openid是否有收款资格
		
		//获取openid
		//网页授权后获取传递的参数
		String userId = request.getParameter("userId"); 	
		String partner_trade_no = Common.getczid();
		String money = request.getParameter("money");
		String name = jedisClient.get(userId+"user_name_pass");
		jedisClient.del(userId+"user_name_pass");
		String code = request.getParameter("code");
		loggerVisit.info("回调参数:userID"+userId+"money:"+money+"code"+code+":name:"+name);
		String openid ="";
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WXPayUtil.APPID+"&secret="+WXPayUtil.API_SECRET+"&code="+code+"&grant_type=authorization_code";
		
		JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
		if (null != jsonObject) {
			openid = jsonObject.getString("openid");
			//保存用户唯一openId'
			loggerVisit.info("openid:"+openid);
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
			loggerVisit.info("金额1："+money);
			money = Double.toString(Double.valueOf(money) / 100);
			Double cash = Double.valueOf(money);
			money = String.format("%.2f",cash);
			loggerVisit.info("金额2：" + money);
			walletLianjiuMapper.selectPaymentByUserId(userId);
			wall.setWalletMoney(money);
			//获取用户金额
    		String moneys = walletLianjiuMapper.selectUserMoney(userId);
//			int i = walletLianjiuMapper.reduceWalletMoney(wall);
//			while(i<1){
//				int f = walletLianjiuMapper.reduceWalletMoney(wall);
//				if(f==1){
//					break;
//				}
//			}
    		// userMoney = 用户余额    monetary = 用户提现金额
			BigDecimal userMoney = new BigDecimal(moneys);
			loggerVisit.info("userMoney:"+userMoney);
			BigDecimal monetary = new BigDecimal(money);
			loggerVisit.info("monetary:"+monetary);
			moneys = userMoney.subtract(monetary).toString();
			loggerVisit.info("moneys:"+moneys);
			loggerVisit.info("开始进行修改~~~~~~~~~~~~~~~~~~~~~~~~updateUserWalletMoney");
			int count = walletLianjiuMapper.updateUserWalletMoney(moneys, userId);
			loggerVisit.info("修改了updateUserWalletMoney：" + count);
			
			String resultXML = HttpClientCustomSSL.httpClientResult(parm);//转账
			loggerVisit.info(resultXML);
			//交易结果处理
			Map<String, Object> resultMap =  XMLUtil.doXMLParse(resultXML);
            String return_code = (String) resultMap.get("return_code");
            String result_code = (String) resultMap.get("result_code");
            
			loggerVisit.info("请求结果:"+resultMap);
			if (return_code.equalsIgnoreCase("SUCCESS") && result_code.equalsIgnoreCase("SUCCESS")){
            	//交易成功
				loggerVisit.info("转账成功：" +return_code+ ":" + result_code);
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
				//user.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));
				user.setRecordPrice(money);
				user.setRecordName(name);
				user.setRecordCreated(new Date());
				user.setRecordUpdated(new Date());
				user.setIsIncome((byte) 0);
				user.setUserId(userId);
				userWalletRecordMapper.insertSelective(user);
				//添加已提现
				//walletLianjiuMapper.addWalletDrawedMoney(wall);
				

				String userDrawedMoney = walletLianjiuMapper.selectUserDrawedMoney(userId);
				BigDecimal bds = new BigDecimal(userDrawedMoney);
				BigDecimal bd2 = new BigDecimal(money);
				bds = bds.add(bd2);
				int count3 = walletLianjiuMapper.updateUserWalletDrawedMoney(bds.toString(), userId);
				loggerVisit.info("调用 updateUserWalletDrawedMoney 修改已提现的金额："+userDrawedMoney);
				loggerVisit.info("调用 updateUserWalletDrawedMoney 修改已提现的金额（添加过后）："+bds);
				loggerVisit.info("调用 updateUserWalletDrawedMoney 修改已提现的金额数据："+count3+"条");
				
				
				Map<String, String> transferMap = new HashMap<>();
				transferMap.put("partner_trade_no", (String) resultMap.get("partner_trade_no"));//商户转账订单号
				transferMap.put("payment_no",  (String) resultMap.get("payment_no")); //微信订单号
				transferMap.put("payment_time",  (String) resultMap.get("payment_time")); //微信支付成功时间
				
				// 定向返回给前端
				loggerVisit.info("开始请求：https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true");
				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true&msg=提现成功");
				loggerVisit.info("请求成功");
            	
            }else{ 
            	//转账失败再把钱转回去
    			int count1 = walletLianjiuMapper.updateUserWalletMoney(userMoney.toString(), userId);
    			loggerVisit.info("//转账失败再把钱转回去  :"+userMoney);
    			loggerVisit.info("修改了updateUserWalletMoney：" + count1);
            	
//            	int m = walletLianjiuMapper.addWalletMoney(wall);
//    			while(m<1){
//    				int n = walletLianjiuMapper.addWalletMoney(wall);
//    				if(n==1){
//    					break;
//    				}
//    			}
            	
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("NOTENOUGH")){
            		loggerVisit.info("转账失败：" + (String) resultMap.get("return_msg") + ":" + (String) resultMap.get("err_code_des"));
            		UserWalletRecord wallet = new UserWalletRecord();
            		wallet.setRecordId(IDUtils.genWalletLianjiuId());
            		wallet.setRecordCreated(new Date());
            		wallet.setRecordUpdated(new Date());
            		wallet.setRecordName(name);
            		wallet.setRecordPrice(money);
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
//                	int h = walletLianjiuMapper.reduceWalletMoney(wall);
//        			while(h<1){
//        				int f = walletLianjiuMapper.reduceWalletMoney(wall);
//        				if(f==1){
//        					break;
//        				}
//        			}
            		int count3 = walletLianjiuMapper.updateUserWalletMoney(moneys, userId);
            		loggerVisit.info("修改了   walletLianjiuMapper.updateUserWalletMoney"+count3+"条数据");
        			//加到提现中
        			//walletLianjiuMapper.walletDrawingMoney(wall);
            		String userDrawingMoney = walletLianjiuMapper.selectUserDrawingMoney(userId);
            		BigDecimal userDrawingMoneys = new BigDecimal(userDrawingMoney);
            		userDrawingMoney = userDrawingMoneys.add(monetary).toString();
            		int count2 = walletLianjiuMapper.updateUserWalletDrawingMoney(userDrawingMoney.toString(), userId);
            		loggerVisit.info(count2+"加到提现中的金额："+userDrawingMoney);
            		// 定向返回给前端
        			String keyword1 = "提现已处理，金额将在24小时内转入微信零钱";
    				loggerVisit.info("请求成功https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true&msg="+URLEncoder.encode(keyword1,"UTF-8"));
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=true&msg="+URLEncoder.encode(keyword1,"UTF-8"));
    				loggerVisit.info("请求成功");
    				return;
            	}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("NAME_MISMATCH")){
    				// 定向返回给前端
    				String keyword = "当前手机的微信真实姓名与所填姓名不一致";
    				loggerVisit.info("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword,"UTF-8"));
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword,"UTF-8"));
    				loggerVisit.info("请求失败");
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("V2_ACCOUNT_SIMPLE_BAN")){
    				// 定向返回给前端
    				loggerVisit.info("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false");
    				String keyword2 = "您申请提现微信账户尚未实名认证，请更换其他微信账户";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword2,"UTF-8"));
    				loggerVisit.info("请求失败");
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("SYSTEMERROR")){
    				// 定向返回给前端
    				loggerVisit.info("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false");
    				String keyword3 = "系统繁忙，请稍后再试";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword3,"UTF-8"));
    				loggerVisit.info("请求失败");
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("AMOUNT_LIMIT")){
    				// 定向返回给前端
    				loggerVisit.info("提现金额超出限制。低于最小金额1.00元或累计超过20000.00元");
    				loggerVisit.info("请求失败");
    				String  keyword4 = "提现金额超出限制。低于最小金额1.00元或累计超过20000.00元";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword4,"UTF-8"));
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("PARAM_ERROR")){
    				// 定向返回给前端
    				loggerVisit.info("金额格式有误");
    				loggerVisit.info("请求失败");
    				String  keyword5 = "金额格式有误";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword5,"UTF-8"));
    				return;
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("ORDERPAID")){
    				// 定向返回给前端
    				loggerVisit.info("该订单已支付");
    				loggerVisit.info("请求失败");
    				String  keyword6 = "该订单已支付";
    				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword6,"UTF-8"));
    				return;
    			}
    			
    			loggerVisit.info("转账失败：" + (String) resultMap.get("err_code") + ":" + (String) resultMap.get("err_code_des"));
    			// 定向返回给前端
    			String keyword7 = (String) resultMap.get("err_code_des");
				loggerVisit.info("请求失败https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword7,"UTF-8"));
				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?cashback=false&msg="+URLEncoder.encode(keyword7,"UTF-8"));
				return;
			}
			
		} catch (Exception e) {
			loggerVisit.error(e.getMessage(), e);
		}

		loggerVisit.info("方法结束：transferPay");
		
	}

	//企业向个人支付转账  微信外
	@Override
	public LianjiuResult transferPayOther(HttpServletRequest request, HttpServletResponse response, String userId,
			String money, String openid, String name) {
		loggerVisit.info("开始进入方法：transferPayOther 企业向个人支付转账 ");
		//loggerVisit.info("[/transfer/pay]");
		//业务判断 openid是否有收款资格
		loggerVisit.info("支现获取的当下openid:"+openid+"userId:"+userId+"money"+money+"name:"+name);
		WalletLianjiu wallet = walletLianjiuMapper.selectWalletByUserId(userId);
		//金额判断
		if(Double.parseDouble(money)<10){
			return LianjiuResult.build(501,"提现金额最低10元"); 
		}
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
		//保留未变动的余额
		String ormoney = money;
		// 单位为分

		loggerVisit.info("moeny  修改前："+ormoney);
		ormoney = String.valueOf((int) (Double.valueOf(money) * 100));
		loggerVisit.info("moeny  修改后："+ormoney); 
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
			parm.put("amount", ormoney); //转账金额
			parm.put("desc", "转账到个人"); //企业付款描述信息
			parm.put("spbill_create_ip", PayUtil.getLocalIp(request)); //Ip地址
			parm.put("sign", SignTools.buildRequestMysign(parm));
			//先减去钱包里边对应的金额 失败再加回去
			WalletLianjiu wall1 = new WalletLianjiu();
			wall1.setUserId(userId);
			/*money = Double.toString(Double.valueOf(money) / 100);
			Double cash = Double.valueOf(money);
			money = String.format("%.2f",cash);*/
			wall1.setWalletMoney(money);
			
			String moneys = walletLianjiuMapper.selectUserMoney(userId);
			// userMoney = 用户余额    monetary = 用户提现金额
			BigDecimal userMoney = new BigDecimal(moneys);
			loggerVisit.info("userMoney:"+userMoney);
			BigDecimal monetary = new BigDecimal(money);
			loggerVisit.info("monetary:"+monetary);
			moneys = userMoney.subtract(monetary).toString();
			loggerVisit.info("moneys:"+moneys);
			int count = walletLianjiuMapper.updateUserWalletMoney(moneys, userId);
			loggerVisit.info("修改了：wallentLianJiu："+count+"条数据");
//			int i = walletLianjiuMapper.reduceWalletMoney(wall1);
//			while(i<1){
//				int f = walletLianjiuMapper.reduceWalletMoney(wall1);
//				if(f==1){
//					break;
//				}
//			}
//			
			
			String resultXML = HttpClientCustomSSL.httpClientResult(parm);//转账
			//交易结果处理
			Map<String, Object> resultMap =  XMLUtil.doXMLParse(resultXML);
            String return_code = (String) resultMap.get("return_code");
            String result_code = (String) resultMap.get("result_code");
			loggerVisit.info("请求结果:"+resultMap);
			if (return_code.equalsIgnoreCase("SUCCESS") && result_code.equalsIgnoreCase("SUCCESS")){
            	//交易成功
				loggerVisit.info("转账成功：" +return_code+ ":" + result_code);
				
				//微信提现账单
				WxOrder order = new WxOrder();
				order.setOutTradeNo((String) resultMap.get("partner_trade_no"));
				order.setType("3");// 提现
				order.setUserId(userId);
				order.setNonce_str(Common.getUuid());
				order.setUser_ip(request.getRemoteAddr());
				order.setReMessage(resultXML);
				order.setTotalFee(money);
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
				//user.setRecordPrice(String.format("%.2f", Float.parseFloat(ormoney)));
				user.setRecordPrice(money);
				user.setRecordCreated(new Date());
				user.setRecordUpdated(new Date());
				user.setIsIncome((byte) 0);
				user.setRecordName(name);
				user.setUserId(userId);
				userWalletRecordMapper.insertSelective(user);
				
				//添加已提现
				//walletLianjiuMapper.addWalletDrawedMoney(wall1);
				String userDrawedMoney = walletLianjiuMapper.selectUserDrawedMoney(userId);
				BigDecimal bds = new BigDecimal(userDrawedMoney);
				BigDecimal bd2 = new BigDecimal(money);
				bds = bds.add(bd2);
				int count3 = walletLianjiuMapper.updateUserWalletDrawedMoney(bds.toString(), userId);
				loggerVisit.info("调用 updateUserWalletDrawedMoney 修改已提现的金额："+userDrawedMoney);
				loggerVisit.info("调用 updateUserWalletDrawedMoney 修改已提现的金额（添加过后）："+bds);
				loggerVisit.info("调用 updateUserWalletDrawedMoney 修改已提现的金额数据："+count3+"条");
				/*	Map<String, String> transferMap = new HashMap<>();
				transferMap.put("partner_trade_no", (String) resultMap.get("partner_trade_no"));//商户转账订单号
				transferMap.put("payment_no",  (String) resultMap.get("payment_no")); //微信订单号
				transferMap.put("payment_time",  (String) resultMap.get("payment_time")); //微信支付成功时间
				LianjiuResult li = new LianjiuResult(transferMap);*/

				loggerVisit.info("方法结束：transferPayOther");
				return LianjiuResult.ok("提现成功");
            	
            }else{ 
            	//转账失败再把钱加回去
//            	int m = walletLianjiuMapper.addWalletMoney(wall1);
//    			while(m<1){
//    				int n = walletLianjiuMapper.addWalletMoney(wall1);
//    				if(n==1){
//    					break;
//    				}
//    			}
            	int count1 = walletLianjiuMapper.updateUserWalletMoney(userMoney.toString(), userId);
            	loggerVisit.info("/转账失败再把钱加回去:"+userMoney.toString());
            	loggerVisit.info("修改了数据："+count1);
            	if(((String) resultMap.get("err_code_des")).equalsIgnoreCase("余额不足")||((String) resultMap.get("err_code")).equalsIgnoreCase("NOTENOUGH")){
            		loggerVisit.info("转账失败：" + (String) resultMap.get("return_msg") + ":" + (String) resultMap.get("err_code_des"));
            		
            		UserWalletRecord wall = new UserWalletRecord();
            		wall.setRecordId(IDUtils.genWalletLianjiuId());
            		wall.setRecordCreated(new Date());
            		wall.setRecordUpdated(new Date());
            		wall.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));		
            		wall.setIsIncome((byte)1);
            		wall.setRecordStatus("0");
            		wall.setUserId(userId);
            		wall.setRecordName(name);
            		wall.setRelativeId(openid);
            		if(userWalletRecordMapper.insertSelective(wall)!=1){
            			return LianjiuResult.build(501, "微信支现添加失败");
            		}
            		
            		//转账成功再把钱减了
//                	int f = walletLianjiuMapper.reduceWalletMoney(wall1);
//        			while(f<1){
//        				int n = walletLianjiuMapper.reduceWalletMoney(wall1);
//        				if(n==1){
//        					break;
//        				}
//        			}
            		int count2 = walletLianjiuMapper.updateUserWalletMoney(moneys, userId);
                	loggerVisit.info("转账成功再把钱减了:"+moneys.toString());
                	loggerVisit.info("修改了数据："+count2);
        			//walletLianjiuMapper.walletDrawingMoney(wall1);
                	String userDrawingMoney = walletLianjiuMapper.selectUserDrawingMoney(userId);
            		BigDecimal userDrawingMoneys = new BigDecimal(userDrawingMoney);
            		userDrawingMoney = userDrawingMoneys.add(monetary).toString();
                	int count3 = walletLianjiuMapper.updateUserWalletDrawingMoney(userDrawingMoney.toString(), userId);
                	loggerVisit.info("修改用户提现中的钱:"+userDrawingMoney.toString());
                	loggerVisit.info("修改了数据："+count3);
        			return LianjiuResult.ok("提现已处理，金额将在24小时内转入微信零钱");
            	}
            	
            	if(((String) resultMap.get("err_code")).equalsIgnoreCase("NAME_MISMATCH")){
    				// 定向返回给前端
    				loggerVisit.info("绑定的微信真实姓名与所填姓名不一致");
    				return LianjiuResult.build(501,"绑定的微信真实姓名与所填姓名不一致");
    			}
            	
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("V2_ACCOUNT_SIMPLE_BAN")){
    				// 定向返回给前端
    				loggerVisit.info("您申请提现微信账户尚未实名认证，请更换其他微信账户");
    				return LianjiuResult.build(501,"您申请提现微信账户尚未实名认证，请更换其他微信账户");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("SYSTEMERROR")){
    				// 定向返回给前端
    				loggerVisit.info("系统繁忙，请稍后再试");
    				loggerVisit.info("请求失败");
    				return LianjiuResult.build(501,"系统繁忙，请稍后再试");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("AMOUNT_LIMIT")){
    				// 定向返回给前端
    				loggerVisit.info("提现金额超出限制。低于最小金额1.00元或累计超过20000.00元");
    				loggerVisit.info("请求失败");
    				return LianjiuResult.build(501,"提现金额超出限制。低于最小金额1.00元或累计超过20000.00元");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("PARAM_ERROR")){
    				// 定向返回给前端
    				loggerVisit.info("金额格式有误");
    				loggerVisit.info("请求失败");
    				return LianjiuResult.build(501,"金额格式有误");
    			}
    			
    			if(((String) resultMap.get("err_code")).equalsIgnoreCase("ORDERPAID")){
    				// 定向返回给前端
    				loggerVisit.info("该订单已支付");
    				loggerVisit.info("请求失败");
    				return LianjiuResult.build(501,"该订单已支付");
    			}
    			
    			loggerVisit.info("---------------------转账失败(String) resultMap.get(\"return_msg\")：" + (String) resultMap.get("return_msg") + ":" + (String) resultMap.get("err_code_des"));
    			return LianjiuResult.build(501, ""+(String) resultMap.get("err_code_des"));
            }
			
		} catch (Exception e) {
			loggerVisit.error(e.getMessage(), e);
		}
		loggerVisit.info("方法结束：transferPayOther");
		return LianjiuResult.build(501, "传递参数有误");

		/*if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
			loggerVisit.info("转账成功：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			Map<String, String> transferMap = new HashMap<>();
			transferMap.put("partner_trade_no", restmap.get("partner_trade_no"));//商户转账订单号
			transferMap.put("payment_no", restmap.get("payment_no")); //微信订单号
			transferMap.put("payment_time", restmap.get("payment_time")); //微信支付成功时间
			return LianjiuResult.ok(transferMap);
		}else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				loggerVisit.info("转账失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
			return LianjiuResult.build(401, "转账失败:"+new ResponseData());
		} */
	}

	//企业向个人转账查询
	@Override
	public void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno,
			String callback) {
		loggerVisit.info("开始进入方法：orderPayQuery 企业向个人转账查询");
		//loggerVisit.info("[/transfer/pay/query]");
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
			loggerVisit.error(e.getMessage(), e);
		}
		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
			// 订单查询成功 处理业务逻辑
			loggerVisit.info("订单查询：订单" + restmap.get("partner_trade_no") + "支付成功");
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
				loggerVisit.info("订单转账失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "订单转账失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
		loggerVisit.info("方法结束：orderPayQuery");
		
	}

	//银行卡支现
	@Override
	public LianjiuResult unionpay(String userId, String money) {
		loggerVisit.info("银行卡提现:unionpay 银行卡支现");
		loggerVisit.info("开始进入方法：unionpay 银行卡支现");
		if(StringUtil.hasEmpty(userId,money)){
			return LianjiuResult.build(501, "参数不能为空");
		}
		//金额判断
		if(Double.parseDouble(money)<10){
			return LianjiuResult.build(501,"提现金额最低10元"); 
		}

		Double walletMoney = Double.valueOf(walletLianjiuMapper.selectWalletByUserId(userId).getWalletMoney());
		if(Double.valueOf(money)>walletMoney){
			return LianjiuResult.build(401, "提现金额超出钱包余额");
		}
		UserWalletRecord wallet = new UserWalletRecord();
		wallet.setRecordId(IDUtils.genWalletLianjiuId());
		wallet.setRecordCreated(new Date());
		wallet.setRecordUpdated(new Date());
		wallet.setRecordName("银行卡支现");
		wallet.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));		
		wallet.setRecordStatus("0");
		wallet.setIsIncome((byte)9);
		wallet.setUserId(userId);
		if(userWalletRecordMapper.insertSelective(wallet)!=1){
			return LianjiuResult.build(501, "银行卡支现添加失败");
		}
		loggerVisit.info("方法结束：unionpay");
		return LianjiuResult.ok("添加成功");
	}

	//微信H5支现 获取code接口
	@Override
	public LianjiuResult wxCashCode(String userId, HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("开始进入方法：wxCashCode 微信H5支现 获取code接口");
		if (StringUtil.hasEmpty(userId)) {
			return LianjiuResult.build(404, "参数不要为空");
		}
		//本地logo的位置
		String logoPath = "/usr/local/lianjiu-server-cluod/payment-tomcat/webapps/ROOT/LIANJIUlogo.png";
		//String logoPath = "C:/Users/Administrator/Documents/Tencent Files/2968543645/FileRecv/LIANJIUlogo.png";
		String backUri = "https://payment.lianjiuhuishou.com/payment/transferController/wxpayH5return";
		// 授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
		// 最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名,
		// 比如 Sign = %3D%2F%CS%
		// money=String.valueOf((int)(Double.valueOf(money)*100));
		backUri = backUri + "?userId=" + userId;
		// URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		try {
			backUri = URLEncoder.encode(backUri,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + WXPayUtil.APPID + "&redirect_uri="
				+ backUri + "&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		loggerVisit.info("生成二维码钱url:--"+url);
		try {
			InputStream is =  QRCodeUtil.encode(url, logoPath, "0", "LIANJIUqrcode", true);
			loggerVisit.info("生成InputStream:"+is);
			/*// 图片上传
			String imagePath = new DateTime().toString("/yyyy/MM/dd");
			// 取原始文件名
			String oldName = "qrCode";
			// 新文件名+原文件的后缀
			String newName = IDUtils.genImageName() + oldName;
			boolean result = FtpUtil.uploadFile("101.132.38.30", GlobalValueUtil.FTP_PORT, GlobalValueUtil.FTP_USERNAME,
					GlobalValueUtil.FTP_PASSWORD, GlobalValueUtil.FTP_BASE_PATH, imagePath, newName,
					is);*/
			String newURl = "https://upload.lianjiuhuishou.com/pic/upload";
			 //创建HttpClient对象  
		    CloseableHttpClient client = HttpClients.createDefault();  
		    //构建POST请求   请求地址请更换为自己的。  
		    //1)  
		    HttpPost post = new HttpPost(newURl);  
		    MultipartEntityBuilder builder = MultipartEntityBuilder.create();  
	        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);  
	        //第一个参数为 相当于 Form表单提交的file框的name值 第二个参数就是我们要发送的InputStream对象了  
	        //第三个参数是文件名  
	        //3)  
	        builder.addBinaryBody("uploadFile", is, ContentType.create("multipart/form-data"), "text01.jpg");  
	        //4)构建请求参数 普通表单项  
	        StringBody stringBody = new StringBody("12",ContentType.MULTIPART_FORM_DATA);  
	        builder.addPart("id",stringBody);  
	        HttpEntity entity = builder.build();  
	        post.setEntity(entity);  
	        //发送请求  
	        HttpResponse respo = client.execute(post);
	        loggerVisit.info("图片服务器respone:"+respo);
	        entity = respo.getEntity();  
	        loggerVisit.info("图片服务器entity:"+entity);
	        if (entity != null) {  
	            is = entity.getContent();  
	            //转换为字节输入流  
	            BufferedReader br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));  
	            String body = null;  
	            while ((body = br.readLine()) != null) {  
	                loggerVisit.info("结果body:"+body); 
	                JSONObject string_to_json = JSONObject.fromObject(body);
	                Map map = string_to_json;
	                loggerVisit.info("返回的图片链接地址:"+map.get("lianjiuData"));
	        		loggerVisit.info("方法结束：wxCashCode  成功");
	                return LianjiuResult.ok(map.get("lianjiuData"));
	                
	            }  
	        } else{
	        	loggerVisit.info("上传失败");
        		loggerVisit.info("方法结束：wxCashCode 失败");
	        	 return LianjiuResult.build(501,"上传失败");
	        }
            
			
		} catch (Exception e) {
			e.printStackTrace();
			loggerVisit.info("方法结束：wxCashCode 失败");
			return LianjiuResult.build(501,"参数异常");
		}
		loggerVisit.info("方法结束：wxCashCode 失败");
		return LianjiuResult.build(501,"参数异常");
	}

	//微信H5支现
	@Override
	public void wxpayGZHreturn(HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("开始进入方法：wxpayGZHreturn 微信H5支现");
		loggerVisit.info("微信公众号支付:wxpayGZHreturn 微信H5支现");
		try {
			// 网页授权后获取传递的参数
			loggerVisit.info("微信GZH return");
			String code = request.getParameter("code");
			String userId = request.getParameter("userId");
			loggerVisit.info("微信code:" + code);
			if (StringUtil.isEmpty(code)) {
				throw new RuntimeException("获取code失败");
			}
			// 通过code获取公众号对应的用户唯一openid code 5分钟不用就会过期
			String openId = "";
			String access_token = "";
			String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WXPayUtil.APPID + "&secret=" + WXPayUtil.API_SECRET
					+ "&code=" + code + "&grant_type=authorization_code";
			loggerVisit.info("微信openId获取前  userId:"+userId);
			// https请求
			JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
			loggerVisit.info(jsonObject);
			if (null != jsonObject) {
				openId = jsonObject.getString("openid");
				access_token = jsonObject.getString("access_token");
				loggerVisit.info("微信openId:" + openId);
				// 保存用户唯一openId
				UserDetails ud = userDetailsMapper.selectByUserId(userId);
				if(null == ud){
					loggerVisit.info("UserDetails 为 null");
					ud = new UserDetails();
				}
				String checkCode = IDUtils.genPWDId();
				//空指针 error
				if(ud.getCheckTime()==null||ud.getCheckTime().equals("")){
					userDetailsMapper.saveOpenIdH5(openId, userId,new Date(),checkCode);
					// 定向返回给前端
					loggerVisit.info("首次:https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode);
					response.sendRedirect("https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode);
					loggerVisit.info("请求成功");

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
						userDetailsMapper.saveOpenIdH5(openId, userId,new Date(),checkCode1);
						// 定向返回给前端
						loggerVisit.info("超过三分钟:https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode1);
						response.sendRedirect("https://m.lianjiuhuishou.com/weiX?checkCode="+checkCode1);
						loggerVisit.info("请求成功");
			        }else{
			        	userDetailsMapper.saveOpenIdH5(openId, userId,ud.getCheckTime(),ud.getUserCheck());
						// 定向返回给前端
						loggerVisit.info("未超过三分钟:https://m.lianjiuhuishou.com/weiX?checkCode="+ud.getUserCheck());
						response.sendRedirect("https://m.lianjiuhuishou.com/weiX?checkCode="+ud.getUserCheck());
						loggerVisit.info("请求成功");
			        }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		loggerVisit.info("方法结束：wxpayGZHreturn");
	}

	//微信企业支付资格验证
	@Override
	public LianjiuResult wxpayCheckCode(String userId, String checkCode, HttpServletRequest request,
			HttpServletResponse response) {
		loggerVisit.info("开始进入方法：wxpayCheckCode 微信企业支付资格验证");
		UserDetails ud = userDetailsMapper.selectByUserId(userId);

		if(null == ud){
			loggerVisit.info("UserDetails 为 null");
			return LianjiuResult.build(504, "验证时出现异常，请联系客服");
		}
		loggerVisit.info(ud);
		String openid = ud.getUserOpenid();
		Date checkTime = ud.getCheckTime();
		String userCode = ud.getUserCheck();
		if(openid==null||openid.trim().isEmpty()){
			return LianjiuResult.build(501, "验证不通过,请使用微信扫描二维码获取获取验证码~");
		}
		Date date = new Date();
        long l = date.getTime() - checkTime.getTime();  
        long day = l / (24 * 60 * 60 * 1000);  
        long hour = (l / (60 * 60 * 1000) - day * 24);  
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);  
        //long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  
        loggerVisit.info("方法结束：wxpayCheckCode");
        if(min>3){
        	loggerVisit.info("验证码超过三分钟,请重新扫描二维码");
        	return LianjiuResult.build(501, "验证码超过三分钟,请重新扫描二维码");
        }else{
        	if(checkCode.equals(userCode)){
        		loggerVisit.info("验证码通过");
        		String check = IDUtils.genPWDId();
        		int count = userDetailsMapper.updateCheckCode(userId,check);
        		loggerVisit.info("废除验证码："+count);
        		return LianjiuResult.ok("验证码通过");
        	}else{
        		loggerVisit.info("验证码错误");
        		return LianjiuResult.build(501, "验证码错误");
        	}
        }
	}

	// 微信公众平台 成为开发者验证入口
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("开始进入方法：doGet");
		String TOKEN = "123456789";
	     // 微信加密签名  
	     String signature = request.getParameter("signature"); 
	     loggerVisit.info("signature:"+signature);
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
	     loggerVisit.info("digest:"+digest);
	     // 确认请求来至微信  
	     if (digest.equals(signature)) {  
	         try {
				response.getWriter().print(echostr);
			} catch (IOException e) {
				e.printStackTrace();
			}  
	     }  
			loggerVisit.info("方法结束：doGet");
	}
	

}
