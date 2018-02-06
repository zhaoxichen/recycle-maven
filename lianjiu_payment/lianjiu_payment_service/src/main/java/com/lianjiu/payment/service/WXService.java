package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.common.pojo.LianjiuResult;

public interface WXService {

	LianjiuResult wxpayGZH(String userId, String money, String orderNo, HttpServletRequest request,
			HttpServletResponse response);

	void wxpayGZHreturn(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult wxpayH5return(HttpServletRequest request, HttpServletResponse response, String money, String userId,
			String orderNo);

	LianjiuResult wxpayapp(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult wxpayappJP(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult wxapppayAndroid(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult wxapppayAndroidJP(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

	void wxRollBack(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult wxRefund(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

}

