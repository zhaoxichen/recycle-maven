package com.lianjiu.payment.service;

import com.lianjiu.model.User;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;

public interface UserService {

	User selectByPrimaryKey(String userId);

	int modifyWeeklyState(String outTradeNo);

	void insertWalletRecord(UserWalletRecord user);

	WalletLianjiu selectWalletByUserId(String userId);

	void insertWalletLianjiu(WalletLianjiu walletLianjiu);

	int updateWalletMoney(WalletLianjiu walletLianjiu);

}
