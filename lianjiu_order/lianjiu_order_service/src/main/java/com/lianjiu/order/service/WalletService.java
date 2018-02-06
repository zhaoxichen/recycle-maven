package com.lianjiu.order.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.WalletLianjiu;

public interface WalletService {

	LianjiuResult insertWallet(WalletLianjiu walletLianjiu);

	LianjiuResult selectWalletByUserId(String userId);

	LianjiuResult reduceWallet(WalletLianjiu walletLianjiu);

	LianjiuResult sendSms(String phone);

	LianjiuResult checkSms(String excellentId,String userId,String check,String price);

	LianjiuResult wSendSms(String userId);

	LianjiuResult checkWsms(String userId, String code);

	
}
