package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface QRCodeService {

	void pay(String out_trade_no, String total_fee, String subject, String body, HttpServletResponse response);

	void QRCode(HttpServletRequest request);

}
