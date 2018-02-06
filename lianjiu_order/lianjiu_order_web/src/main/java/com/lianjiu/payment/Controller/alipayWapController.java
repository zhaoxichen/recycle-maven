package com.lianjiu.payment.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.config.AlipayUtil;
import com.lianjiu.payment.config.WapPayConfig;
import com.lianjiu.rest.mapper.WxOrderMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;

/**
 * @author WCS.Wang
 * @version V1.0
 * @Package com.handsure.pay.controller
 * @Name ali-pay
 * @Description: 手机网站支付
 * @date 2017/8/15
 */
@Controller
@RequestMapping("/alipayweb/")
@Transactional
public class alipayWapController {

	@Autowired
	private WxOrderMapper wxOrderMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private OrdersFacefaceDetailsMapper ordersFacefaceDetailsMapper;
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private AllianceBusinessDetailsMapper allianceBusinessDetailsMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	private static Logger loggerAlipay = Logger.getLogger("alipay");

	/**
	 * 支付接口
	 *
	 * @param out_trade_no
	 *            商户订单号，必填
	 * @param total_amount
	 *            交易金额，必填
	 * @param subject
	 *            商品描述，必填
	 * @param body
	 *            商品描述，可空
	 * @param timeout
	 *            超时时间，可空
	 * @param response
	 */
	@RequestMapping(value = "wapPay", method = RequestMethod.POST)
	public synchronized void pay(String total_amount, String userId, String outTradeNo, HttpServletResponse response) {
		try {
			if (StringUtil.hasEmpty(total_amount, userId, outTradeNo)) {
				throw new RuntimeException("参数不要为空");
			}
			String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
			String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
			if (ePrice == null) {
				ePrice = "0";
			}
			if (fPrice == null) {
				fPrice = "0";
			}
			if (!Double.valueOf(fPrice).equals(Double.valueOf(total_amount))
					&& !Double.valueOf(ePrice).equals(Double.valueOf(total_amount))) {
				throw new RuntimeException("数据不正确,请重新输入");
			}

			AlipayClient alipayClient = new DefaultAlipayClient(WapPayConfig.order_url, WapPayConfig.app_id,
					WapPayConfig.app_private_key, WapPayConfig.data_format, WapPayConfig.input_charset,
					WapPayConfig.alipay_public_key, WapPayConfig.sign_type);
			// 创建API对应的request类
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
			alipayRequest.setReturnUrl(WapPayConfig.return_url);
			alipayRequest.setNotifyUrl(WapPayConfig.notify_url);
			// 封装请求支付信息
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			String out_trade_no = outTradeNo;
			model.setBody("wap支付");
			model.setOutTradeNo(out_trade_no);
			model.setProductCode("QUICK_WAP_WAY");
			model.setSubject("链旧支付宝支付");
			model.setTimeoutExpress("30");
			model.setTotalAmount(total_amount);
			alipayRequest.setBizModel(model);
			// 调用SDK生成表单
			String form = alipayClient.pageExecute(alipayRequest).getBody();
			// 保存数据库数据
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(total_amount);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(form);
			wxOrder.setState("2");
			wxOrder.setResource(AlipayUtil.AlipayH5);
			WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
			if (wo == null) {
				wxOrderMapper.insertWXOrder(wxOrder);
			} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
				throw new RuntimeException("订单已支付成功,请勿重复提交");
			} else {
				wxOrderMapper.updateByPrimaryKeySelective(wxOrder);
			}
			// 直接写入页面
			response.setContentType("text/html;charset=" + WapPayConfig.input_charset);
			// 直接将完整的表单html输出到页面
			response.getWriter().write(form);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (AlipayApiException e) {
			loggerAlipay.error("生成订单失败(AliPay)：" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			loggerAlipay.error("生成订单失败(IO)：" + e.getMessage());
		}

	}

	/**
	 * wap支付接口 链旧精品
	 *
	 * @param out_trade_no
	 *            商户订单号，必填
	 * @param total_amount
	 *            交易金额，必填
	 * @param subject
	 *            商品描述，必填
	 * @param body
	 *            商品描述，可空
	 * @param timeout
	 *            超时时间，可空
	 * @param response
	 */
	/*
	 * @RequestMapping(value="wapPayJP",method = RequestMethod.POST) public void
	 * payJP(String total_amount,String userId, String
	 * outTradeNo,HttpServletResponse response) { try { if
	 * (StringUtil.hasEmpty(total_amount,userId,outTradeNo)) { throw new
	 * RuntimeException("参数不要为空"); } String ePrice =
	 * ordersExcellentMapper.slectPriceByOrderNo(outTradeNo); String fPrice =
	 * ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
	 * if(ePrice==null){ ePrice = "0"; } if(fPrice==null){ fPrice = "0"; }
	 * if(!Double.valueOf(fPrice).equals(Double.valueOf(total_amount))&&!Double.
	 * valueOf(ePrice).equals(Double.valueOf(total_amount))){ throw new
	 * RuntimeException("数据不正确,请重新输入"); } //增加10%的汇率 Double cash =
	 * Double.valueOf(total_amount); cash = cash * 1.1; total_amount =
	 * Double.toString(cash); AlipayClient alipayClient = new
	 * DefaultAlipayClient(WapPayConfig.order_url, WapPayConfig.app_id,
	 * WapPayConfig.app_private_key, WapPayConfig.data_format,
	 * WapPayConfig.input_charset, WapPayConfig.alipay_public_key,
	 * WapPayConfig.sign_type); //创建API对应的request类 AlipayTradeWapPayRequest
	 * alipayRequest = new AlipayTradeWapPayRequest();
	 * alipayRequest.setReturnUrl(WapPayConfig.return_url);
	 * alipayRequest.setNotifyUrl(WapPayConfig.notify_url); //封装请求支付信息
	 * AlipayTradeWapPayModel model = new AlipayTradeWapPayModel(); String
	 * out_trade_no = outTradeNo; model.setBody("wap支付");
	 * model.setOutTradeNo(out_trade_no); model.setProductCode("QUICK_WAP_WAY");
	 * model.setSubject("链旧支付宝支付"); model.setTimeoutExpress("30");
	 * model.setTotalAmount(total_amount); alipayRequest.setBizModel(model);
	 * //调用SDK生成表单 String form =
	 * alipayClient.pageExecute(alipayRequest).getBody(); //保存数据库数据 WxOrder
	 * wxOrder = new WxOrder(); wxOrder.setOutTradeNo(out_trade_no);
	 * wxOrder.setTotalFee(total_amount); wxOrder.setUserId(userId);
	 * wxOrder.setType("1"); wxOrder.setDate(DateUtils.getTime());
	 * wxOrder.setMessage(form); wxOrder.setState("2");
	 * wxOrder.setResource("alipay/H5"); WxOrder wo =
	 * wxOrderMapper.selectByPrimaryKey(out_trade_no); if(wo==null){
	 * wxOrderMapper.insertWXOrder(wxOrder); }else
	 * if(wo.getState().equals("4")&&wo.getType().equals("1")){ throw new
	 * RuntimeException("订单已支付成功,请勿重复提交"); }else{
	 * wxOrderMapper.updateByPrimaryKeySelective(wxOrder); } //直接写入页面
	 * response.setContentType("text/html;charset=" +
	 * WapPayConfig.input_charset); //直接将完整的表单html输出到页面
	 * response.getWriter().write(form); response.getWriter().flush();
	 * response.getWriter().close(); } catch (AlipayApiException e) {
	 * loggerAlipay.error("生成订单失败(AliPay)：" + e.getMessage());
	 * e.printStackTrace(); } catch (IOException e) {
	 * loggerAlipay.error("生成订单失败(IO)：" + e.getMessage()); }
	 * 
	 * }
	 */

	/**
	 * 手机网站支付回调接口
	 *
	 * @param request
	 */
	@RequestMapping(value = "wapPayreturn")
	public void wapPay(HttpServletRequest request, HttpServletResponse response) {
		loggerAlipay.debug("-------------------手机网站支付回调接口:WayPAY-------------------");
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}
			// 商户订单号
			String out_trade_no = request.getParameter("out_trade_no");
			// 支付宝交易号
			String trade_no = request.getParameter("trade_no");
			// 交易状态
			String trade_status = request.getParameter("trade_status");
			// 计算得出通知验证结果
			if (StringUtils.isNotBlank(out_trade_no) && StringUtils.isNotBlank(trade_no)
					&& StringUtils.isNotBlank(trade_status)) {
				boolean verify_result = AlipaySignature.rsaCheckV1(params, WapPayConfig.alipay_public_key,
						WapPayConfig.input_charset, WapPayConfig.sign_type);
				if (verify_result) {
					// 验证成功
					loggerAlipay.info("支付成功！订单号：" + out_trade_no + "，支付宝交易号：" + trade_no);
					if (trade_status.equals("TRADE_FINISHED")) {
						// 判断该笔订单是否在商户网站中已经做过处理
						// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						// 如果有做过处理，不执行商户的业务程序

						// 改变支付表的记录 状态
						WxOrder wxOrder = new WxOrder();
						wxOrder.setOutTradeNo(out_trade_no);
						wxOrder.setState("4");
						wxOrder.setReMessage(JSON.toJSONString(params));
						wxOrder.setSign(request.getParameter("sign"));
						wxOrderMapper.updateByPrimaryKeySelective(wxOrder);

						// 更改订单表的订单状态
						OrdersFaceface orders = new OrdersFaceface();
						orders.setOrFacefaceId(out_trade_no);
						orders.setOrFacefaceStatus((byte) 3);
						OrdersExcellent excellent = new OrdersExcellent();
						excellent.setOrExcellentStatus((byte) 4);
						excellent.setOrExcellentId(out_trade_no);
						excellent.setOrExcellentUpdated(new Date());
						int f = ordersFacefaceMapper.updateFaceFaceState(orders);
						int e = ordersExcellentMapper.modifyExcellentState(excellent);
						if (f + e != 1) {
							throw new RuntimeException("订单状态修改失败");
						}
						// 修改加盟商订单每星期状态
						if (f == 1) {
							allianceBusinessDetailsMapper.modifyWeeklyState(out_trade_no);
							// 增加加盟商流水单
							UserWalletRecord user = new UserWalletRecord();
							user.setRecordId(IDUtils.genGUIDs());
							user.setRecordPrice(request.getParameter("total_fee"));
							user.setRelativeId(out_trade_no);
							user.setRecordCreated(new Date());
							user.setRecordUpdated(new Date());
							user.setIsIncome((byte) 3);
							OrdersFaceface order1 = ordersFacefaceMapper.selectByPrimaryKey(out_trade_no);
							user.setUserId(order1.getOrFacefaceAllianceId());
							user.setRecordName(order1.getOrItemsNamePreview());
							userWalletRecordMapper.insertSelective(user);
							// 增加用户流水单
							user.setRecordId(IDUtils.genGUIDs());
							user.setUserId(order1.getUserId());
							user.setIsIncome((byte) 5);
							userWalletRecordMapper.insertSelective(user);
						}
						response.getWriter().write("success");
						response.getWriter().close();
						// 注意：
						// 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
						// 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
					} else if (trade_status.equals("TRADE_SUCCESS")) {
						// 判断该笔订单是否在商户网站中已经做过处理
						// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						// 如果有做过处理，不执行商户的业务程序
						// 改变支付表的记录 状态
						loggerAlipay.info("支付失败！订单号：" + out_trade_no + "，支付宝交易号：" + trade_no);
						WxOrder wxOrder = new WxOrder();
						wxOrder.setOutTradeNo(out_trade_no);
						wxOrder.setState("3");
						wxOrder.setReMessage(JSON.toJSONString(params));
						wxOrder.setSign(request.getParameter("sign"));
						wxOrderMapper.updateByPrimaryKeySelective(wxOrder);

						response.getWriter().write("fail");
						response.getWriter().close();
						// 注意：
						// 如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
					}
				}
			}
		} catch (Exception e) {
			loggerAlipay.error("错误：" + e.getMessage());
			e.printStackTrace();
		}
	}

}
