package com.lianjiu.rest.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Recommend;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.rest.dao.RecommendDao;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.user.RecommendMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.rest.tool.WalletLianjiuTool;

public class RecommendDaoImpl implements RecommendDao {
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private RecommendMapper recommendMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	private static Logger loggerLogin = Logger.getLogger("login");

	/**
	 * 推荐成功
	 */
	@Override
	public boolean payForRecomend(String udetailsReferrer) {
		if (Util.isEmpty(udetailsReferrer)) {
			loggerLogin.info("推荐码还没传入");
			return false;
		}
		/**
		 * 根据推荐码查询推荐码对象，包含用户id，推荐码使用次数
		 */
		Recommend recommend = recommendMapper.getRecomByNum(udetailsReferrer);
		if (null == recommend) {
			return false;
		}
		/**
		 * 更新推荐码使用次数
		 */
		Integer total = recommend.getRecommendTotalNum();
		loggerLogin.info("该推荐码使用总次数：" + total);
		if (null == total) {
			return false;
		}
		total = total + 1;
		recommend.setRecommendTotalNum(total);
		recommendMapper.modifyTotalNum(recommend);
		/**
		 * 判断是否为商户推荐码
		 */
		if (udetailsReferrer.matches("[7-9][0-9]{5}")) {
			loggerLogin.info("商户推荐码：" + udetailsReferrer);
			return true;
		} else {
			loggerLogin.info("用户推荐码：" + udetailsReferrer);
		}
		/**
		 * 给推荐人的钱包加两块钱，给推荐人生成加两块钱的流水明细
		 */
		//String userId = recommend.getUserId();
		// 流水
		//UserWalletRecord walletRecord = new UserWalletRecord(userId, "推荐注册奖励", "2.00", userId, (byte) 10);
		//WalletLianjiuTool.walletAdd(walletRecord, walletLianjiuMapper, userWalletRecordMapper);
		return true;
	}

	/**
	 * 推荐成功下单快递回收，首单
	 */
	@Override
	public boolean payForOrdersExpressFirst(String udetailsReferrer) {
		if (Util.isEmpty(udetailsReferrer)) {
			loggerLogin.info("推荐码还没传入");
			return false;
		}
		/**
		 * 根据推荐码查询推荐码对象，包含用户id，推荐码使用次数
		 */
		Recommend recommend = recommendMapper.getRecomByNum(udetailsReferrer);
		if (null == recommend) {
			return false;
		}
		/**
		 * 给推荐人的钱包加两块钱，给推荐人生成加两块钱的流水明细
		 */
		String userId = recommend.getUserId();
		// 流水
		UserWalletRecord walletRecord = new UserWalletRecord(userId, "推荐快递回收", "10.00", userId, (byte) 11);
		WalletLianjiuTool.walletAdd(walletRecord, walletLianjiuMapper, userWalletRecordMapper);
		return true;
	}

}
