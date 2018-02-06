package com.lianjiu.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.UserWalletRecord;

public interface RefoundService {

	LianjiuResult wxRefund(String money, String userId, String outTradeNo, HttpServletRequest request, HttpServletResponse response);

	LianjiuResult orderPayRefund(String tradeNo, String outTradeNo, String money);

	LianjiuResult walletPayBack(UserWalletRecord use,String money);

	LianjiuResult payBack(String outTradeNo, String money, HttpServletRequest request, HttpServletResponse response);

}
