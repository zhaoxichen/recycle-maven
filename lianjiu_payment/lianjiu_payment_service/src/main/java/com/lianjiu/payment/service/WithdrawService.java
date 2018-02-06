package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.common.pojo.LianjiuResult;

public interface WithdrawService {

	LianjiuResult wxpayCheck(String userId);

	LianjiuResult wxpayCode(String userId, String money, String name);

	void transferPay(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult transferPayOther(HttpServletRequest request, HttpServletResponse response, String userId,
			String money, String openid, String name);

	void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno, String callback);

	LianjiuResult unionpay(String userId, String money, HttpServletRequest request, HttpServletResponse response);

	void wxpayGZHreturn(HttpServletRequest request, HttpServletResponse response);

	LianjiuResult wxpayCheckCode(String userId, String checkCode, HttpServletRequest request,
			HttpServletResponse response);

	void invisit(HttpServletRequest request, HttpServletResponse response);

}
