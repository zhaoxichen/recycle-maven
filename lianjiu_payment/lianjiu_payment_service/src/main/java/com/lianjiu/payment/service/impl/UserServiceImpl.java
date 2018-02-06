package com.lianjiu.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.model.User;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.payment.service.UserService;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AllianceBusinessDetailsMapper allianceBusinessDetailsMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;

	@Override
	public User selectByPrimaryKey(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int modifyWeeklyState(String outTradeNo) {
		return allianceBusinessDetailsMapper.modifyWeeklyState(outTradeNo);
	}

	@Override
	public void insertWalletRecord(UserWalletRecord user) {
		userWalletRecordMapper.insertSelective(user);
	}

	@Override
	public WalletLianjiu selectWalletByUserId(String userId) {
		return walletLianjiuMapper.selectWalletByUserId(userId);
	}

	@Override
	public void insertWalletLianjiu(WalletLianjiu walletLianjiu) {
		walletLianjiuMapper.insert(walletLianjiu);
	}

	@Override
	public int updateWalletMoney(WalletLianjiu walletLianjiu) {
		return walletLianjiuMapper.updateWalletMoney(walletLianjiu);
	}

}
