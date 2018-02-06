package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.common.pojo.LianjiuResult;

public interface TransferService {

	LianjiuResult wxpayCode(String userId, String money, String name, HttpServletRequest request,
			HttpServletResponse response);

	void transferPay(HttpServletRequest request, HttpServletResponse response);

	void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno, String callback);

	LianjiuResult unionpay(String userId, String money);

	LianjiuResult wxCashCode(String userId, HttpServletRequest request, HttpServletResponse response);

	void wxpayGZHreturn(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult wxpayCheckCode(String userId, String checkCode, HttpServletRequest request,
			HttpServletResponse response);

	void doGet(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult transferPayOther(HttpServletRequest request, HttpServletResponse response, String userId,
			String money, String openid, String name);

	LianjiuResult wxpayCheck(String userId, HttpServletRequest request, HttpServletResponse response);


}
