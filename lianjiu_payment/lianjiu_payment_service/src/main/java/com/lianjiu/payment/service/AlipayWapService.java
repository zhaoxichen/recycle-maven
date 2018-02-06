package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AlipayWapService {
	public void pay(String total_amount, String userId, String outTradeNo, HttpServletResponse response);

	public void wapPay(HttpServletRequest request, HttpServletResponse response);
	
}
