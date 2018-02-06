package com.lianjiu.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.payment.dao.config.AlipayConfig;

public interface AliTansService {

	void alipayNotify(HttpServletRequest request, HttpServletResponse res);

	void alipayApi(HttpServletRequest request, HttpServletResponse response, AlipayConfig alipayConfig);

}
