package com.lianjiu.rest.tool;

import java.util.Date;

import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;

public class WalletLianjiuTool {
	public static void walletAdd(UserWalletRecord walletRecord,WalletLianjiuMapper walletLianjiuMapper,
			UserWalletRecordMapper userWalletRecordMapper) {
		String userId = walletRecord.getUserId();
		/**
		 * 查询用户的钱包
		 */
		WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(userId);
		if (null == walletLianjiu) {
			return;
		}
		walletLianjiu.setWalletMoney(walletRecord.getRecordPrice());
		int i = walletLianjiuMapper.updateWalletMoney(walletLianjiu);
		if (i == 0) {
			return;
		}
		// 添加一条记录到流水单
		walletRecord.setRecordId(IDUtils.genGUIDs());
		walletRecord.setRecordCreated(new Date());
		walletRecord.setRecordUpdated(new Date());
		userWalletRecordMapper.insertSelective(walletRecord);
	}
}
