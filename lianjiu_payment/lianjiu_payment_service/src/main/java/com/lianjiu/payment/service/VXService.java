package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.common.pojo.LianjiuResult;

public interface VXService {

	LianjiuResult vxOutPay(HttpServletRequest request, HttpServletResponse response, String money, String userId,
			String orderNo);

	LianjiuResult vxPayReturn(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult vxPayQuery(String out_trade_no,HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult vxInnerPay(HttpServletRequest request, HttpServletResponse response, String money, String userId,
			String orderNo);

	LianjiuResult vxInnerPayReturn(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult ModifyStatus(String out_trade_no, String status, HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult wxapppayAndroid(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult wxapppayAndroidJP(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult wxpayappJP(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);

	LianjiuResult wxpayapp(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response);
		
}
