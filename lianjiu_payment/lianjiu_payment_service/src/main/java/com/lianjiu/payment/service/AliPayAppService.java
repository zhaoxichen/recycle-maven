package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.common.pojo.LianjiuResult;

public interface AliPayAppService {

	LianjiuResult orderPay(HttpServletRequest request, HttpServletResponse response, String cashnum, String userId,
			String outTradeNo);

	LianjiuResult orderPayJP(HttpServletRequest request, HttpServletResponse response, String cashnum, String userId,
			String outTradeNo);

	LianjiuResult iosOrderPay(HttpServletRequest request, HttpServletResponse response, String cashnum, String userId,
			String outTradeNo);

	LianjiuResult iosOrderPayJP(HttpServletRequest request, HttpServletResponse response, String cashnum, String userId,
			String outTradeNo);

	void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno, String orderno,
			String callback);

	void orderPayNotify(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult orderPayRefund(HttpServletRequest request, HttpServletResponse response, String tradeno,
			String orderno, double amount);

	void orderPayRefundQuery(HttpServletRequest request, HttpServletResponse response, String orderno, String tradeno,
			String callback);


}
