package com.lianjiu.mapper;

import com.lianjiu.model.WalletLianjiu;

public interface WalletLianjiuMapper {
	int insert(WalletLianjiu record);

	WalletLianjiu selectWalletByUserId(String userId);

	String selectPaymentByUserId(String userId);

	int addWalletMoney(WalletLianjiu walletLianjiu);

	int reduceWalletMoney(WalletLianjiu walletLianjiu);

	void insertWallet(WalletLianjiu walletLianjiu);

	int updateWalletMoney(WalletLianjiu walletLianjiu);

}