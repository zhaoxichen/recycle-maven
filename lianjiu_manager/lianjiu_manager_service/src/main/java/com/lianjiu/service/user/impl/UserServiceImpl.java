package com.lianjiu.service.user.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.MathUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Power;
import com.lianjiu.model.Recommend;
import com.lianjiu.model.Role;
import com.lianjiu.model.User;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.UserRole;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.vo.UserCustom;
import com.lianjiu.rest.mapper.user.RecommendMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserRoleMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RecommendMapper recommendMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;

	private static Logger loggerUser = Logger.getLogger("user");

	public LianjiuResult selectAllUser(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 去数据查询
		List<UserCustom> users = userMapper.selectAllUser();
		if (null == users || 0 == users.size()) {
			loggerUser.info("对象为空");
			return LianjiuResult.build(501, "暂无数据");
		}
		// 去掉aaaaaa
		for (UserCustom userCustom : users) {
			if ("aaaaaa".equals(userCustom.getUdetailsReferrer()) || null == userCustom.getUdetailsReferrer()) {
				userCustom.setUdetailsReferrer("");
			}
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<UserCustom> listUser = (Page<UserCustom>) users;
		System.out.println("总页数：" + listUser.getPages());
		// 封装返回
		LianjiuResult result = new LianjiuResult(users);
		result.setStatus(200);
		result.setMsg("ok");
		result.setTotal(listUser.getTotal());
		return result;
	}

	public User getUserByToken(String token) {
		try {
			// 调用sso系统的服务，根据token取用户信息
			String json = HttpClientUtil.doGet(GlobalValueUtil.SSO_BASE_URL + "/user/token/" + token);
			// 把json转换成LianjiuResult
			LianjiuResult result = LianjiuResult.formatToPojo(json, User.class);
			if (result.getStatus() == 200) {
				User user = (User) result.getLianjiuData();
				return user;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public LianjiuResult getUserByPowerId(String userId) {
		// 执行查询
		List<Power> listPower = userMapper.findByPower(userId);
		if (null == listPower || 0 == listPower.size()) {
			loggerUser.info("对象列表为空");
			return LianjiuResult.build(501, "对象列表为空");
		}
		LianjiuResult result = new LianjiuResult(listPower);
		return result;
	}

	@Override
	public LianjiuResult getByRoleId(String userId) {
		// 执行查询
		List<Role> role = userMapper.getByRoleId(userId);
		if (null == role || 0 == role.size()) {
			loggerUser.info("对象列表为空");
			return LianjiuResult.build(501, "对象列表为空");
		}
		LianjiuResult result = new LianjiuResult(role);
		return result;
	}

	@Override
	public LianjiuResult deleteUser(String userId) {
		try {
			// 去数据库删除
			int rowsAffected = userMapper.deleteByPrimaryKey(userId);
			userRoleMapper.deleteByUserId(userId);
			loggerUser.info("删除成功");
			return LianjiuResult.ok(rowsAffected);
		} catch (Exception e) {
			loggerUser.info("数据异常:" + e.getMessage());
			return LianjiuResult.build(502, "数据异常");
		}
	}

	@Override
	public LianjiuResult updateRoleAllot(String userId, List<Role> userList) {
		// 判断用户id是否为空
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "Error");
		}
		// 删除中间表所有的
		int rolePowerCount = userRoleMapper.deleteByUserId(userId);
		System.out.println("删除user_role表中：" + rolePowerCount + "条数据");
		if (userList.size() <= 0) {
			return LianjiuResult.ok(1);
		}
		for (Role p : userList) {
			UserRole ur = new UserRole();
			ur.setUserRoleId(IDUtils.genPowerModuleId());
			ur.setUserId(userId);
			ur.setRoleId(p.getRoleId());
			userRoleMapper.insert(ur);
		}
		return LianjiuResult.ok(1);
	}

	@Override
	public LianjiuResult selectDetailsByUserId(String detailsId) {
		UserDetails details = userMapper.selectByDetailsId(detailsId);
		if (null == details || "".equals(details)) {
			return LianjiuResult.build(501, "对象为空");
		}
		if ("aaaaaa".equals(details.getUdetailsReferrer()) || null == details.getUdetailsReferrer()) {
			details.setUdetailsReferrer("");
		}
		return LianjiuResult.ok(details);
	}

	@Override
	public LianjiuResult selectUserByUserId(String userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		if (null == user || "".equals(user)) {
			return LianjiuResult.build(501, "对象为空");
		}
		return LianjiuResult.ok(user);
	}

	/**
	 * 商户推荐人添加
	 */
	@Override
	public synchronized LianjiuResult addRecommend(Recommend recommend) {
		System.out.println("开始进入推荐人添加");
		List<String> recommends = recommendMapper.getRecommendNumList();
		if (null == recommends) {
			loggerUser.info("查询推荐人列表出错");
			return LianjiuResult.build(501, "数据异常");
		}
		// 判断用户电话号码是否重复
		int count = recommendMapper.getCountByPhone(recommend.getRecommendPhone());
		if (count > 0) {
			loggerUser.info("推荐人电话号码已存在");
			return LianjiuResult.build(501, "电话号码已存在");
		}
		// 随机生成五位数字
		String recommendNum = null;
		// 校验是否重复
		int i = 0;
		while (true) {
			recommendNum = "7" + (int) ((Math.random() * 9 + 1) * 10000);
			if (!recommends.contains(recommendNum)) {
				break;
			}
			// 防止死循环
			if (100000 < i++) {
				return LianjiuResult.build(501, "推荐码暂时无法生成，请联系工作人员");
			}
		}
		recommend.setRecommendId(IDUtils.genUserIDs());
		recommend.setRecommendNum(recommendNum);
		// 存入数据库
		try {
			recommendMapper.insert(recommend);
		} catch (RuntimeException e) {
			loggerUser.info("插入语句异常");
			return LianjiuResult.build(504, "数据异常");
		} catch (Exception e) {
			return LianjiuResult.build(505, "其他异常");
		}
		return LianjiuResult.ok(recommend);
	}

	@Override
	public LianjiuResult vagueQuery(User user, int pageNum, int pageSize, String createdStart, String cratedOver) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 去数据查询
		List<UserCustom> users = userMapper.vagueQuery(user, createdStart, cratedOver);
		if (null == users || 0 == users.size()) {
			return LianjiuResult.build(501, "对象为空");
		}
		// 去掉aaaaaa
		for (UserCustom userCustom : users) {
			if ("aaaaaa".equals(userCustom.getUdetailsReferrer()) || null == userCustom.getUdetailsReferrer()) {
				userCustom.setUdetailsReferrer("");
			}
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<UserCustom> listUser = (Page<UserCustom>) users;
		System.out.println("总页数：" + listUser.getPages());
		// 封装返回
		LianjiuResult result = new LianjiuResult(users);
		result.setStatus(200);
		result.setMsg("ok");
		result.setTotal(listUser.getTotal());
		return result;
	}

	/**
	 * 推荐人修改
	 */
	@Override
	public LianjiuResult modifyRecommend(Recommend recommend) {
		loggerUser.info("开始修改推荐人");
		/**
		 * 判断用户电话号码是否重复,查询当前电话号码
		 */
		String phone = recommendMapper.getPhoneById(recommend.getRecommendId());
		// 电话号码更改了，判断重复
		if (!phone.equals(recommend.getRecommendPhone())) {
			int count = recommendMapper.getCountByPhone(recommend.getRecommendPhone());
			if (count > 0) {
				loggerUser.info("推荐人电话号码已存在");
				return LianjiuResult.build(501, "电话号码已存在");
			}
		}
		try {
			recommendMapper.updateByPrimaryKeySelective(recommend);
			loggerUser.info("修改成功");
			return LianjiuResult.ok(recommend);
		} catch (RuntimeException e) {
			loggerUser.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(502, "数据异常");
		}
	}

	/**
	 * 模糊搜索订单
	 */
	@Override
	public LianjiuResult searchRecommend(String parameter, int pageSize, int pageNum) {
		loggerUser.info("开始订单搜索功能");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<Recommend> listRecommend = recommendMapper.getRecommeByParm(parameter);
			if (null == listRecommend) {
				loggerUser.info("订单对象为空");
				return LianjiuResult.build(501, "暂无数据");
			}
			Page<Recommend> listRecommends = (Page<Recommend>) listRecommend;
			LianjiuResult result = new LianjiuResult(listRecommend);
			result.setTotal(listRecommends.getTotal());
			return result;
		} catch (RuntimeException e) {
			loggerUser.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(502, "数据异常");
		}
	}

	/**
	 * 通过推荐人查用户信息
	 */
	@Override
	public LianjiuResult getUserByRecommend(String referrer, String startTime, String endTime, int pageNum,
			int pageSize) {
		loggerUser.info("通过推荐人查用户信息");
		/**
		 * 当填写推荐人的时候查询推荐人是否存在
		 */
		if (Util.notEmpty(referrer)) {
			String reId = recommendMapper.getIdByNum(referrer);
			if (Util.isEmpty(reId)) {
				loggerUser.info("推荐人查询失败");
				return LianjiuResult.build(501, "推荐人不存在");
			}
		}
		/**
		 * 根据推荐码查询用户
		 */
		PageHelper.startPage(pageNum, pageSize);
		List<User> listUser = userMapper.getUserByReferrer(referrer, startTime, endTime);
		if (null == listUser || 0 == listUser.size()) {
			loggerUser.info("无可查询数据");
			return LianjiuResult.build(502, "无可查数据");
		}
		Page<User> listUsers = (Page<User>) listUser;
		LianjiuResult result = new LianjiuResult(listUser);
		result.setTotal(listUsers.getTotal());
		return result;
	}

	/**
	 * 推荐订单统计
	 */
	@Override
	public LianjiuResult getRecordByFaceface(String referrer, String startTime, String endTime, int categoryId,
			int pageNum, int pageSize) {
		loggerUser.info("开始推荐订单统计");
		/**
		 * 当填写推荐人的时候查询推荐人是否存在
		 */
		if (Util.notEmpty(referrer)) {
			String reId = recommendMapper.getIdByNum(referrer);
			if (Util.isEmpty(reId)) {
				loggerUser.info("推荐人查询失败");
				return LianjiuResult.build(501, "推荐人不存在");
			}
		}
		/**
		 * 根据推荐人查询流水单
		 */
		switch (categoryId) {
		// 上门回收订单
		case GlobalValueUtil.FACEFACE_ORDERS:
			categoryId = GlobalValueUtil.INCOME_FACEFACE_RETRIEVE_EARN;
			break;
		// 快递回收订单
		case GlobalValueUtil.EXPRESS_ORDERS:
			categoryId = GlobalValueUtil.INCOME_EXPRESS_RETRIEVE_EARN;
			break;
		// 大宗回收订单
		case GlobalValueUtil.WAREHOUSE_ORDERS:
			categoryId = GlobalValueUtil.INCOME_WAREHOUSE_REFUND_EARN;
			break;
		default:
			loggerUser.info("暂无数据");
			return LianjiuResult.build(503, "暂无数据");
		}
		// 查总价
		List<UserWalletRecord> listWalletRecord = userWalletRecordMapper.getByReferrer(referrer, categoryId, startTime,
				endTime);
		Double totalPrices = 0.00;
		for (UserWalletRecord walletRecord : listWalletRecord) {
			String price = walletRecord.getRecordPrice();

			Double prices = Double.parseDouble(price);
			totalPrices = MathUtil.add(totalPrices, prices);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<UserWalletRecord> userWalletRecord = userWalletRecordMapper.getByReferrer(referrer, categoryId, startTime,
				endTime);
		if (null == userWalletRecord) {
			loggerUser.info("暂无数据");
			return LianjiuResult.build(502, "暂无数据");
		}
		loggerUser.info("总价：" + totalPrices);
		String totalPrice = totalPrices + "";
		Page<UserWalletRecord> pageFurniture = (Page<UserWalletRecord>) userWalletRecord;
		LianjiuResult result = new LianjiuResult(userWalletRecord);
		result.setTotal(pageFurniture.getTotal());
		result.setMsg(totalPrice);
		return result;
	}

	/*
	 * 用户分配角色，测试
	 * 
	 * @Override public LianjiuResult updateRoleAllot(String userId, String
	 * userList) { //判断角色id是否为空 if (Util.isEmpty(userId)) { return
	 * LianjiuResult.build(501, "Error"); }
	 * 
	 * //删除中间表所有的 int rolePowerCount = userRoleMapper.deleteByUserId(userId);
	 * System.out.println("删除userRoleMapper表中："+rolePowerCount+"条数据");
	 * 
	 * String [] list= userList.split("-"); int count = list.length;
	 * 
	 * if (count <= 0){ return LianjiuResult.ok(1); } for (int i=0 ;
	 * i<=count-1;i++){ UserRole ur = new UserRole();
	 * ur.setUserRoleId(IDUtils.genPowerModuleId()); ur.setUserId(userId);
	 * ur.setRoleId(list[i]); System.out.println("正在添加第："+count+"条数据；");
	 * System.out.println("添加的数据  用户id："+userId+"-------------------");
	 * System.out.println("添加的数据  权限id："+list[i]+"-------------------");
	 * System.out.println("-------------------------------------------------");
	 * userRoleMapper.insert(ur); } System.out.println("完成！"); return
	 * LianjiuResult.ok(1); }
	 */

}
