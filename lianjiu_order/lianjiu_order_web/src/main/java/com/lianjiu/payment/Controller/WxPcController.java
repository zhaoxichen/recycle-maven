package com.lianjiu.payment.Controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.internal.util.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.config.PayUtil;
import com.lianjiu.payment.config.WXUtil;
import com.lianjiu.payment.wxh5.CommonUtil;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.WxOrderMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.WechatMapper;

import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;



@Controller
@RequestMapping("/WeixinPc/")
public class WxPcController {
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private WxOrderMapper wxOrderMapper;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private WechatMapper  wechatMapper;
	/**
	 * 微信公众号支付
	 * 获取code接口
	 * @param money
	 * @param orderNo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="pcpay", method = RequestMethod.POST)
	@ResponseBody
	public void wxpayGZH(String money,HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//共账号及商户相关参数
		String appid = "wx77f8b42d3163922d";
		String orderNo =  Common.getczid();
		String backUri = "http://www.lianjiuhuishou.com/WeixinPc/wxpayreturn";
		//授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
		//最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名,
		//比如 Sign = %3D%2F%CS% 
		backUri = backUri+"?userId=b88001&orderNo="+orderNo+"&describe=test&money="+money;
		//URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		backUri = URLEncoder.encode(backUri);
		//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
				"appid=" + appid+
				"&redirect_uri=" +
				 backUri+
				"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		response.sendRedirect(url);
	}
	
	
	// 微信pc支付
		@RequestMapping("pcpayreturn")
		public void wxpay(HttpServletRequest request, HttpServletResponse response)  {
			//网页授权后获取传递的参数
			String userId = request.getParameter("userId"); 	
			String orderNo = request.getParameter("orderNo"); 
			String money = request.getParameter("money");
			String code = request.getParameter("code");
			try{
				//保存openid
				String appsecret = "ad72a5189014adc5dbcb92fe97cb7f3b";
				String openId ="";
				String appid = "";
				String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
				JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
				System.out.println(jsonObject);
				if (null != jsonObject) {
					openId = jsonObject.getString("openid");
					//保存用户唯一openId
					userDetailsMapper.saveOpenId(openId,userId);
					
				}
				
				
				if (!StringUtil.isNotBlank(money)) {
					return;
				}
				String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
				//单位为分
				money=String.valueOf((int)(Double.valueOf(money)*100));
				String notifyUrl = "";
				String out_trade_no = Common.getczid();
				WxOrder order = new WxOrder();
				order.setOutTradeNo(out_trade_no);
				order.setType("1");//充值
				order.setTotalFee(money);
				order.setUserId(userId);
				String data=WXUtil.getrechargeXMLpc(notifyUrl, order);//获取XML报文
				order.setMessage(data);
				order.setDate(DateUtils.getTime());
				
				HttpClient client = new HttpClient();
				PostMethod  method=new PostMethod(url);
				method.setRequestHeader("Connection","keep-alive");
				method.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
//				HttpMethodParams p = new HttpMethodParams(); 
//				method.setParams(p);
				System.out.println(data);
				method.setRequestBody(data);
						//			NameValuePair[] data = {new NameValuePair("tId",tId),new NameValuePair("time",date),new NameValuePair("releaseId",fbr.getVip()),new NameValuePair("buyerId",tbr.getVip())};
//				method.setQueryString(data);
				client.executeMethod(method);
				byte[] b=method.getResponseBody(); 
//				System.out.println();
				String message=new String(b,"utf-8");
				System.out.println("返回消息："+message);
				method.releaseConnection();
				client.getHttpConnectionManager().closeIdleConnections(0);
				if(!StringUtil.isNotBlank(message)){
					return;
				}
				Document document = DocumentHelper.parseText(message);
				System.out.println(document.getRootElement().element("return_code").getText());
				if("SUCCESS".equals(document.getRootElement().element("return_code").getText())&&"SUCCESS".equals(document.getRootElement().element("result_code").getText())){
						jedisClient.hset("wxOrderCache", out_trade_no, "1");
						
						order.setState("2");//请求成功
						//保存订单信息
						wxOrderMapper.insertWXOrder(order);
						//生成二维码
						int width = 200; // 图像宽度  
				        int height = 200; // 图像高度  
				        String format = "png";// 图像类型  
				        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
				        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
				        BitMatrix bitMatrix = new MultiFormatWriter().encode(document.getRootElement().element("code_url").getText(),  
				                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵  
				        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
				        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
						ImageIO.write(image, format, baos);  
						byte[] bytes = baos.toByteArray();  
						String base64 =  new BASE64Encoder().encode(bytes);
//				        MatrixToImageWriter.writeToStream(bitMatrix, format, response.getOutputStream());
//				        MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像  
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("img", base64);
						map.put("out_trade_no", out_trade_no);
				        System.out.println("输出成功.");  
				        return;
				}else{
					order.setState("1");//请求失败
					//保存订单信息
					wxOrderMapper.insertWXOrder(order);
					return;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		//回调
		@RequestMapping("wxRollBack")
		public void wxRollBack(HttpServletRequest request, HttpServletResponse response)  {
			 	StringBuffer data = new StringBuffer();
		        String line = null;
		        BufferedReader reader = null;
		        SortedMap<Object, Object> parameter = new TreeMap<Object, Object>();
		        try {
		            reader = request.getReader();
		            while (null != (line = reader.readLine())){
		                data.append(line);
		                }
		            Document document = DocumentHelper.parseText(data.toString());
					System.out.println(document.getRootElement().element("return_code").getText());
					Element element = document.getRootElement();
					if("SUCCESS".equals(element.element("return_code").getText())){
						//商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
						//同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
						String outTradeNo=element.element("out_trade_no").getText();
						String token=jedisClient.set("orderCache", outTradeNo);
						WxOrder order = new WxOrder();
						order.setOutTradeNo(outTradeNo);
						order = wxOrderMapper.queryWXOrder(order);
						if(!"SUCCESS".equals(element.element("result_code").getText())){
							jedisClient.del("orderCache");
							//ehCacheMananger.remove("orderCache", outTradeNo);
							parameter.put("return_code", "SUCCESS");
							parameter.put("return_msg", "OK");
							order.setState("3");//回调失败
							order.setReMessage(data.toString());
							wxOrderMapper.updateByPrimaryKeySelective(order);
							jedisClient.hset("wxOrderCache", order.getOutTradeNo(),"3");
							return;
						}
						//这里幂等性验证也可以通过修改业务来避免
						if(!StringUtils.isEmpty(token)){
							jedisClient.del("orderCache");
							parameter.put("return_code", "SUCCESS");
							parameter.put("return_msg", "OK");
							return;
							
						}
						jedisClient.hset("orderCache", outTradeNo, outTradeNo);
						String totalFee= document.getRootElement().element("total_fee").getText();
						if(!order.getTotalFee().equals(totalFee)){
							parameter.put("return_code", "FAIL");
							parameter.put("return_msg", "金额错误");
							order.setState("3");//回调失败
							order.setReMessage(data.toString());
							wxOrderMapper.updateByPrimaryKeySelective(order);
							jedisClient.del("orderCache");
							return;
						}
						String sign1= document.getRootElement().element("sign").getText();
						SortedMap<Object, Object> map = new TreeMap<Object, Object>();
						for (Iterator iterator = document.getRootElement().elementIterator(); iterator.hasNext();) {  
					         Element e = (Element) iterator.next();  
					         map.put(e.getName(), e.getText());  
					      }
						map.remove("sign");
						String sign2 = MD5Util.wxmd5(map);
//								String notifyUrl = userService.manageUrl("wxcallbackURL", "wxcallbackURL");
//								String sign1=order.getSign();//报文签名
//								WXUtil.getrechargeXML(notifyUrl, order);
//								String sign2=order.getSign();//返回报文重新生成签名
						if(!sign1.equals(sign2)){
							parameter.put("return_code", "FAIL");
							parameter.put("return_msg", "签名失败");
							order.setSign(sign1);
							order.setState("3");//回调失败
							order.setReMessage(data.toString());
							wxOrderMapper.updateByPrimaryKeySelective(order);
							jedisClient.del("orderCache");
							return;
						}
						
						//更改订单表的订单状态
						OrdersFaceface orders = new OrdersFaceface();
						orders.setOrFacefaceId(outTradeNo);
						orders.setOrFacefaceStatus((byte)3);
						OrdersExcellent excellent = new OrdersExcellent();
						excellent.setOrExcellentStatus((byte)4);
						excellent.setOrExcellentId(outTradeNo);
						excellent.setOrExcellentUpdated(new Date());
						if(ordersFacefaceMapper.updateFaceFaceState(orders)+ordersExcellentMapper.modifyExcellentState(excellent)!=1){
							throw new RuntimeException("订单状态修改失败");
						}
						
						
						
						order.setState("4");//回调成功
						String vip = order.getUserId();
						Double money = Double.parseDouble(String.valueOf((Double.valueOf(order.getTotalFee())/100)));//转化为单位元字符串
						//校验钱数
						
						String bank= document.getRootElement().element("bank_type").getText();
						
					/*	TenderMiddle tm=new TenderMiddle(Common.getUuid(),vip,"1",order.getOutTradeNo(),DateUtils.getTime(),"2","微信支付",bank,"","","","",money,"");
						vipService.recharge(tm);
						//为账户增加金额
						tenderService.addAccountBalance(vip,money);
						//为统计表充值字段增加金额
						tenderService.addAmountCount(money);
						order.setRe_message(data.toString());
						userService.updateWXOrder(order);
						
						//推送
						List<String> list=new ArrayList<String>();
						list.add(order.getOut_trade_no());
						list.add(String.valueOf(money));
						String str=PushModel.pushModel("cn","wxchongzhi", list);
						Push p=new Push(Common.getUuid(), str.split("/")[0], str.split("/")[1], vip, "1", DateUtils.getTime(),"cn");
						pushService.SetPush(p);
						
						
						ehCacheMananger.put("wxOrderCache", order.getOut_trade_no(),"2");
						ehCacheMananger.remove("orderCache", outTradeNo);
						parameter.put("return_code", "SUCCESS");
						parameter.put("return_msg", "OK");*/
						
					}else{
						parameter.put("return_code", "SUCCESS");
						parameter.put("return_msg", "OK");
						
					}
					
//				    <xml>
		//  <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
		//  <attach><![CDATA[支付测试]]></attach>
		//  <bank_type><![CDATA[CFT]]></bank_type>
		//  <fee_type><![CDATA[CNY]]></fee_type>
		//  <is_subscribe><![CDATA[Y]]></is_subscribe>
		//  <mch_id><![CDATA[10000100]]></mch_id>
		//  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
		//  <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
		//  <out_trade_no><![CDATA[1409811653]]></out_trade_no>
		//  <result_code><![CDATA[SUCCESS]]></result_code>
		//  <return_code><![CDATA[SUCCESS]]></return_code>
		//  <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
		//  <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
		//  <time_end><![CDATA[20140903131540]]></time_end>
		//  <total_fee>1</total_fee>
		//  <trade_type><![CDATA[JSAPI]]></trade_type>
		//  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
		//	</xml>
		            
		            
		            
		        } catch (Exception e) {
		        	e.printStackTrace();
		        } 
		}

		
		/*//查询退款金额
		@RequestMapping("queryRefundMoney")
		public void queryRefundMoney(HttpServletRequest request, HttpServletResponse response)  {
			User user = (User) request.getSession().getAttribute("user");
	//
			if (user == null) {
				return;
			} 
			try{
				WalletLianjiu wallet = walletLianjiuMapper.getAccountInfo(userId);
				WXOrder order = new WXOrder();
				order.setUser_id(user.getVip());
				List<WXOrder> wxList = userService.queryWXOrderList(order);
				Double accountMoney = u.getAccount_balance();
				Double wxRefundMoney=0.00;//微信可提现金额
				for (WXOrder wxOrder : wxList) {
					Double money = (Double.valueOf(wxOrder.getTotal_fee())/100);//转化为单位元字符串
					if("1".equals(wxOrder.getType())){
						wxRefundMoney=MathUtil.add(wxRefundMoney, money);
//						wxRefundMoney+=money;
					}else if("2".equals(wxOrder.getType())){
						wxRefundMoney=MathUtil.sub(wxRefundMoney, money);
//						wxRefundMoney-=money;
					}
				}
				if(accountMoney<wxRefundMoney){
					wxRefundMoney=accountMoney;
				}
				Map<String, Double> map = new HashMap<String, Double>();
				map.put("accountMoney", accountMoney);
				map.put("wxRefundMoney", wxRefundMoney);
				result = iresult.getResult(ResultBiz.SUCCESS,map);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				jsonObj(response,result.toJson());
			}
			}
		// 微信退款
			@RequestMapping("wxRefund")
			public void wxRefund(String money, String payment,
					HttpServletRequest request, HttpServletResponse response)  {
				LOG.info("时间：{}", new String[] { DateUtils.getTime() });
				LOG.info("==============>开始查询积分明细");
				LOG.info("==============>请求参数[money:{}]", new String[] { money });
				User user = (User) request.getSession().getAttribute("user");
		//
				if (user == null) {
					result = iresult.getResult(ResultBiz.illegal);
					return;
				} 
				if(StringUtil.isEmpty(userService.checkPayment(user.getVip(), MD5Util.md5(payment)))){
					result = iresult.getResult(ResultBiz.Payment_Error);
					return;
				}
				
				try{
					User u = tenderService.selectAccount(user.getVip());
					WXOrder order = new WXOrder();
					order.setUser_id(user.getVip());
					List<WXOrder> wxList = userService.queryWXOrderList(order);
					
					Double refee=Double.valueOf(money);//退款金额
					Double ReMoney=0.00;//已退款金额
					Double accountMoney = u.getAccount_balance();
					Double wxRefundMoney=0.00;
					if(refee>accountMoney){
						//退款金额错误
						result = iresult.getResult(ResultBiz.money);
						return;
					}
					List<WXOrder> chOrders= new ArrayList<WXOrder>();//需退款订单
					List<WXOrder> reOrders= new ArrayList<WXOrder>();//退款订单
					//计算当前所有已经退款的订单
					for (int i = 0; i < wxList.size(); i++) {
						Double totalFee = (Double.valueOf(wxList.get(i).getTotal_fee())/100);//转化为单位元字符串
						if("1".equals(wxList.get(i).getType())){
							chOrders.add(wxList.get(i));
							wxRefundMoney=MathUtil.add(wxRefundMoney, totalFee);
//							wxRefundMoney+=totalFee;
						}else if("2".equals(wxList.get(i).getType())){
							ReMoney=MathUtil.add(ReMoney, totalFee);
							wxRefundMoney=MathUtil.sub(wxRefundMoney, totalFee);
//							ReMoney+=totalFee;
//							wxRefundMoney-=totalFee;
						}
					}
					if(refee>wxRefundMoney){
						//退款金额错误
						result = iresult.getResult(ResultBiz.money);
						return;
					}
					//根据退款金额和已退款金额，计算需要退款的订单
					for (int i = 0; i < chOrders.size(); i++) {
						//排除已退款订单
						Double totalFee = (Double.valueOf(chOrders.get(i).getTotal_fee())/100);//转化为单位元
						if(totalFee<=ReMoney){
							//判断该订单是否已退
							chOrders.remove(i);
							ReMoney=MathUtil.sub(ReMoney, totalFee);
//							ReMoney-=totalFee;
							i--;//保持下标不动
						}else if(ReMoney>0){
							//判断是否是某订单退了一部分,记录该订单到退款订单
							//如果退款金额小于该订单剩下金额
							if(refee<=(MathUtil.sub(totalFee,ReMoney))){
								WXOrder wxOrder = new WXOrder();
								wxOrder.setOut_trade_no("TX"+DateUtils.getID());//退款单号
								wxOrder.setTotal_fee(String.valueOf(refee));//退款金额
								ReMoney=0.00;
								reOrders.add(wxOrder);
								refee=0.00;
								break;//跳出循环
							}else{
								WXOrder wxOrder = new WXOrder();
								wxOrder.setOut_trade_no("TX"+DateUtils.getID());//退款单号
								wxOrder.setTotal_fee(String.valueOf(totalFee-ReMoney));//退款金额
								ReMoney=0.00;
								reOrders.add(wxOrder);
								refee=MathUtil.sub(refee, MathUtil.sub(totalFee,ReMoney));
//								refee-=(totalFee-ReMoney);
							}
						}else if(totalFee<=refee){//如果退款金额大于等于该订单总金额
							WXOrder wxOrder = new WXOrder();
							wxOrder.setOut_trade_no("TX"+DateUtils.getID());//退款单号
							wxOrder.setTotal_fee(String.valueOf(totalFee));//退款金额
							reOrders.add(wxOrder);
							refee=MathUtil.sub(refee, totalFee);
//							refee-=totalFee;
						}else if(refee>0){
							WXOrder wxOrder = new WXOrder();
							wxOrder.setOut_trade_no("TX"+DateUtils.getID());//退款单号
							wxOrder.setTotal_fee(String.valueOf(refee));//退款金额
							refee=0.00;
							reOrders.add(wxOrder);
						}
					}
					
					//指定读取证书格式为PKCS12
					KeyStore keyStore = KeyStore.getInstance("PKCS12");
					//读取本机存放的PKCS12证书文件
					String keyStoreurl = userService.manageUrl("keyStore", "keyStore");
					FileInputStream instream = new FileInputStream(new File(keyStoreurl));
					try {
					//指定PKCS12的密码(商户ID)
					keyStore.load(instream, "1456215002".toCharArray());
					} finally {
					instream.close();
					}
					SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1456215002".toCharArray()).build();
					//指定TLS版本
					SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
					//设置httpclient的SSLSocketFactory
					CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
					
					String url="https://api.mch.weixin.qq.com/secapi/pay/refund";
					
					 HttpPost httpPost = new HttpPost(url);//退款接口  
//			         System.out.println("executing request" + httpPost.getRequestLine());  
			       
			         // 设置类型  
			        
			        Double ReMoneySu = 0.00; 
					for (int i = 0; i < reOrders.size(); i++) {
						reOrders.get(i).setTotal_fee(String.valueOf((int)(Double.valueOf(reOrders.get(i).getTotal_fee())*100)));
						String data=WXUtil.getRefundXML(reOrders.get(i), chOrders.get(i));
						reOrders.get(i).setType("2");//退款
						reOrders.get(i).setUser_id(user.getVip());
						reOrders.get(i).setMessage(data);
						reOrders.get(i).setDate(DateUtils.getTime());
						System.out.println(data);
						
						  StringEntity  reqEntity  = new StringEntity(data);						 
						  reqEntity.setContentType("text/xml;charset=UTF-8");  
						httpPost.setEntity(reqEntity);  
				         CloseableHttpResponse responses = httpclient.execute(httpPost);  
				         String message=EntityUtils.toString(responses.getEntity(),"utf-8");
						System.out.println("返回消息："+message);
						reOrders.get(i).setRe_message(message);
//							method.releaseConnection();
//							client.getHttpConnectionManager().closeIdleConnections(0);
						Document document = DocumentHelper.parseText(message);
						System.out.println(document.getRootElement().element("return_code").getText());
						if("SUCCESS".equals(document.getRootElement().element("return_code").getText())){
							reOrders.get(i).setState("2");//请求成功
							//保存订单信息
							userService.insertWXOrder(reOrders.get(i));
							ReMoneySu=MathUtil.add(ReMoneySu, (Double.valueOf(reOrders.get(i).getTotal_fee())/100));
							result = iresult.getResult(ResultBiz.SUCCESS);
						}else{
							reOrders.get(i).setState("1");//请求失败
							//保存订单信息
							userService.insertWXOrder(reOrders.get(i));
							result = iresult.getResult(ResultBiz.SYSTEM_BUSY);
							//发生错误停止
							return;
						}
					}
					//记录
					TenderMiddle tm=new TenderMiddle(Common.getUuid(),user.getVip(),"2","TX"+DateUtils.getID(),DateUtils.getTime(),"2","","微信","","","","",ReMoneySu);
					//消息
					//推送
					List<String> list=new ArrayList<String>();
					list.add(tm.getD_id());
					list.add(String.valueOf(ReMoneySu));
					String str=PushModel.pushModel("cn","wxtixian", list);
					Push p=new Push(Common.getUuid(), str.split("/")[0], str.split("/")[1], user.getVip(), "1", DateUtils.getTime(),"cn");
					pushService.SetPush(p);
					
					
					vipService.cash(tm);
					tenderService.deductmoney(user.getVip(), ReMoneySu);
					user.setAccount_balance(MathUtil.sub(user.getAccount_balance(),ReMoneySu));
				}catch(Exception e){
					result = iresult.getResult(ResultBiz.SYSTEM_BUSY);
					e.printStackTrace();
				}finally{
					jsonObj(response,result.toJson());
				}
				}*/
		/*
		//微信支付查询
		@RequestMapping("wxOrderQuery")
		public void wxOrderQuery(String out_trade_no,
				HttpServletRequest request, HttpServletResponse response)  {
			//查询订单支付情况
			String no = ehCacheMananger.get("wxOrderCache", out_trade_no);
			if(StringUtils.isEmpty(no)){
				//未发起支付或已失效
				result = iresult.getResult(ResultBiz.SUCCESS,"0");
			}else if("1".equals(no)){
				//支付中
				result = iresult.getResult(ResultBiz.SUCCESS,"1");
			}else if("2".equals(no)){
				//支付成功
				result = iresult.getResult(ResultBiz.SUCCESS,"2");
				ehCacheMananger.remove("wxOrderCache", out_trade_no);
			}else if("3".equals(no)){
				//支付失败
				result = iresult.getResult(ResultBiz.SUCCESS,"3");
				ehCacheMananger.remove("wxOrderCache", out_trade_no);
			}
			jsonObj(response, result.toJson());
		}*/
		

}
