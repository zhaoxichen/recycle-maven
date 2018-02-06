package com.lianjiu.rest.mapper;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.WalletLianjiu;

public interface WalletLianjiuMapper {


    int insert(WalletLianjiu record);

    WalletLianjiu selectWalletByUserId(String userId);

	String selectPaymentByUserId(String userId);
	//用户链旧钱包加钱
	int addWalletMoney(WalletLianjiu walletLianjiu);

	//用户链旧钱包减钱 
	int reduceWalletMoney(WalletLianjiu walletLianjiu);
	
	//添加提现中的钱
	int walletDrawingMoney(WalletLianjiu wall);

	
	int insertWallet(WalletLianjiu walletLianjiu);
	
	//用户链旧钱包加钱,加上累计赚取
	int updateWalletMoney(WalletLianjiu walletLianjiu);
	//添加已提现中的钱
	int addWalletDrawedMoney(WalletLianjiu wall);
	//修改用户余额
	int updateUserWalletMoney(@Param("money") String money,@Param("userId") String userId);
	//修改用户提现中的钱
	int updateUserWalletDrawingMoney(@Param("money") String money,@Param("userId") String userId);
	//修改用户已提现的金额
	int updateUserWalletDrawedMoney(@Param("money") String money,@Param("userId") String userId);
	//查询用户余额
	String selectUserMoney(String userId);
	//查询用户提现中的钱
	String selectUserDrawingMoney(String userId);
	//查询用户已提现的钱
	String selectUserDrawedMoney(String userId);
}