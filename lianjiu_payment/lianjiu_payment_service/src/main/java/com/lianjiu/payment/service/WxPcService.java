package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WxPcService {

	void wxpayGZH(String money, HttpServletRequest request, HttpServletResponse response);

	void wxpay(HttpServletRequest request, HttpServletResponse response);

	void wxRollBack(HttpServletRequest request, HttpServletResponse response);
	
}
