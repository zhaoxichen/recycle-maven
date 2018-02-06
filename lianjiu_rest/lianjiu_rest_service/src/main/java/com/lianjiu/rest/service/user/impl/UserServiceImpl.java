package com.lianjiu.rest.service.user.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.AdActivity;
import com.lianjiu.model.Coupon;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.OrdersExpressItem;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.OrdersRepairItem;
import com.lianjiu.model.Power;
import com.lianjiu.model.Qq;
import com.lianjiu.model.Recommend;
import com.lianjiu.model.Role;
import com.lianjiu.model.User;
import com.lianjiu.model.UserAddress;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.Waste;
import com.lianjiu.model.Wechat;
import com.lianjiu.model.vo.UserCustom;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CouponMapper;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.content.AdActivityMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExpressMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.orders.OrdersRepairItemMapper;
import com.lianjiu.rest.mapper.orders.OrdersRepairMapper;
import com.lianjiu.rest.mapper.user.QqMapper;
import com.lianjiu.rest.mapper.user.RecommendMapper;
import com.lianjiu.rest.mapper.user.UserAddressMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserRoleMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.rest.mapper.user.WechatMapper;
import com.lianjiu.rest.model.OrdersExpressItemVo;
import com.lianjiu.rest.model.OrdersFacefaceItemVo;
import com.lianjiu.rest.model.OrdersRepairItemVo;
import com.lianjiu.rest.model.OrdersView;
import com.lianjiu.rest.service.user.UserService;
import com.lianjiu.rest.tool.JedisTool;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private UserAddressMapper userAddressMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private OrdersExpressMapper ordersExpressMapper;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private AdActivityMapper adActivityMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private OrdersRepairMapper ordersRepairMapper;
	@Autowired
	private OrdersRepairItemMapper ordersRepairItemMapper;
	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
	@Autowired
	private QqMapper qqMapper;
	@Autowired
	private WechatMapper wechatMapper;
	@Autowired
	private RecommendMapper recommendMapper;
	private static Logger loggerOperation = Logger.getLogger("operation");

	@Value("${REDIS_SMS_SESSION_KEY}")
	private String REDIS_SMS_SESSION_KEY;

	@Value("${REDIS_PHONE_SESSION_KEY}")
	private String REDIS_PHONE_SESSION_KEY;

	private static Logger loggerUser = Logger.getLogger("user");

	public LianjiuResult selectAllUser(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 去数据查询
		List<UserCustom> users = userMapper.selectAllUser();
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<UserCustom> listUser = (Page<UserCustom>) users;
		// 封装返回
		LianjiuResult result = new LianjiuResult(users);
		loggerUser.info("总页数：" + listUser.getTotal());
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
		LianjiuResult result = new LianjiuResult(listPower);
		return result;
	}

	@Override
	public LianjiuResult getByRoleId(String userId) {
		// 执行查询
		List<Role> role = userMapper.getByRoleId(userId);
		LianjiuResult result = new LianjiuResult(role);
		return result;
	}

	@Override
	public LianjiuResult deleteUser(String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "请指定要用户id");
		}
		// 去数据库删除
		int rowsAffected = userMapper.deleteByPrimaryKey(userId);
		int userRoleCount = userRoleMapper.deleteByUserId(userId);
		loggerUser.info("删除user_role：" + userRoleCount + "条数据;");
		return LianjiuResult.ok(rowsAffected);
	}

	// 查默认地址
	@Override
	public LianjiuResult selectAddressDefault(String userId) {
		UserAddress address = userAddressMapper.selectDefault(userId);
		if (null == address) {
			loggerUser.info("用户未设置默认地址");
			return LianjiuResult.build(502, "没有设置默认地址");
		}
		return LianjiuResult.ok(address);
	}

	/**
	 * 获取用户基本信息
	 */
	@Override
	public LianjiuResult selectAsset(String userId) {
		// 根据用户id获取其钱包 未做）
		if (StringUtil.isEmpty(userId)) {
			return LianjiuResult.build(503, "请传递指定的用户");
		}
		User user = userMapper.selectUserByUserId(userId);
		if (null == user) {
			loggerUser.info("user对象为空");
			return LianjiuResult.build(504, "请传递指定的用户");
		}
		String udetailsId = user.getUserDetailsId();
		if (Util.isEmpty(udetailsId)) {
			loggerUser.info("账号存在异常");
			return LianjiuResult.build(505, "账户存在异常，请退出重新登陆");
		}
		String grade = null;
		Integer integral = userDetailsMapper.selectIntegralById(udetailsId);
		loggerUser.info("积分" + integral);
		if (null == integral) {
			integral = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询是否存在热门活动
		int count = adActivityMapper.getCount();
		if (0 < count) {
			map.put("isActivity", 1);
		}else {
			map.put("isActivity", 0);
		}
		// int integral = Integer.parseInt(Integral);
		if (integral > 0 && integral < 101) {
			grade = "1";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		if (integral > 100 && integral < 301) {
			grade = "2";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		if (integral > 300 && integral < 401) {
			grade = "3";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		if (integral > 400 && integral < 501) {
			grade = "4";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		if (integral > 500 && integral < 701) {
			grade = "5";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		if (integral > 700 && integral < 801) {
			grade = "6";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		if (integral > 800 && integral < 901) {
			grade = "7";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		if (integral > 900) {
			grade = "8";
			map.put("grade", grade);
			map.put("integral", integral);
		}
		loggerUser.info("开始判断用户是否设置过密码");
		String pwd = userMapper.getPwdByPhone(user.getUserPhone());
		if (Util.isEmpty(pwd)) {
			map.put("isPwd", "0");
		} else {
			map.put("isPwd", "1");
		}
		String edition = userDetailsMapper.getEdById(udetailsId);// 登陆方式
		if (Util.isEmpty(edition)) {
			loggerUser.info("登陆方式为空");
			return LianjiuResult.build(501, "登陆方式为null");
		}
		// 登陆方式为链旧或者短信登陆
		if (GlobalValueUtil.LIANJIU.equals(edition) || GlobalValueUtil.SMS.equals(edition)) {
			map.put("username", user.getUsername());
			map.put("userphoto", user.getUserPhoto());
		}
		// qq登陆
		if (GlobalValueUtil.QQ.equals(edition)) {
			loggerUser.info("QQ登陆");
			Qq qq = qqMapper.getNpByUserId(userId);// 查昵称头像
			if (null == qq) {
				loggerUser.info("查询qq表信息出错");
				return LianjiuResult.build(502, "系统繁忙");
			}
			map.put("nickName", qq.getQqNickname());
			map.put("userphoto", qq.getQqPicture());
			map.put("username", "");
		}
		// 微信登陆
		if (GlobalValueUtil.WECHAT.equals(edition)) {
			loggerUser.info("微信登陆");
			Wechat wechat = wechatMapper.getNpByUserId(userId);// 查昵称头像
			if (null == wechat) {
				loggerUser.info("查询wechat表信息出错");
				return LianjiuResult.build(503, "系统繁忙");
			}
			map.put("nickName", wechat.getWechatNickname());
			map.put("userphoto", wechat.getWechatPicture());
			map.put("username", "");
		}
		map.put("userPhone", user.getUserPhone());
		WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(userId);
		if (walletLianjiu != null) {

			String userAsset = walletLianjiu.getWalletMoney();
			if (Util.isEmpty(userAsset)) {
				loggerUser.info("查询我的资产错误，返回错误数据0");
				map.put("userAsset", 0);
			}
			String accumulatedAmount = walletLianjiu.getWalletTotalcount();
			if (Util.isEmpty(accumulatedAmount)) {
				loggerUser.info("查询总资产错误，返回错误数据0");
				map.put("accumulatedAmount", 0);
			}
			// 查询之后返回用户的资产 累计赚取 环保达人等级
			map.put("userAsset", userAsset);
			map.put("accumulatedAmount", accumulatedAmount);
			return LianjiuResult.ok(map);
		} else {
			// 未开通钱包的状态
			loggerUser.info("创建用户时，创建钱包错误");
			map.put("userAsset", 0);
			map.put("accumulatedAmount", 0);
			return LianjiuResult.ok(map);
		}
	}

	/**
	 * 钱包信息
	 */
	@Override
	public LianjiuResult selectAssetDetails(String userId) {
		// 根据用户id获取其钱包）
		WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(userId);
		// 根据用户ID查账单
		List<UserWalletRecord> list = userWalletRecordMapper.selectRecordByUserId(userId);
		if (null != walletLianjiu || !"".equals(walletLianjiu)) {
			String userAsset = walletLianjiu.getWalletMoney();
			String accumulatedAmount = walletLianjiu.getWalletTotalcount();
			String walletDrawing = walletLianjiu.getWalletDrawing();
			// 查询之后返回用户的资产 累计赚取 环保达人等级
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("userAsset", userAsset);
			map.put("walletDrawing", walletDrawing);
			map.put("accumulatedAmount", accumulatedAmount);
			map.put("grade", 1);
			map.put("list", list);
			return LianjiuResult.ok(map);
		} else {
			// 没有账单
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userAsset", 0);
			map.put("withdrawCash", 0);
			map.put("accumulatedAmount", 0);
			map.put("grade", 1);
			map.put("recordName", list);
			return LianjiuResult.ok(map);
		}
	}

	/**
	 * 实名认证(审核后绑定身份证)---后台管理
	 */
	@Override
	public LianjiuResult userCertification(String udetailsId, String udetailsIdCard, String userName) {
		if (Util.isEmpty(udetailsId)) {
			return LianjiuResult.build(502, "用户id不能为空！");
		}
		UserDetails ud = new UserDetails();
		ud.setUdetailsId(udetailsId);
		ud.setUserName(userName);
		ud.setUdetailsIdCard(udetailsIdCard);
		ud.setUdetailsRealnameTime(new Date());
		int count = userDetailsMapper.updateUserIdentity(ud);
		return LianjiuResult.ok(count);
	}

	/**
	 * 插入用户地址
	 * 
	 * @param address
	 * @return
	 */
	@Override
	public LianjiuResult insertAddressByUser(UserAddress address) {
		if (null == address) {
			return LianjiuResult.build(502, "传入的对象为空");
		}

		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(address.getUserId());
		if (listAddress.size() <= 0) {
			address.setUserDefault(true);
		}

		if (listAddress.size() > 0) {
			// 如果添加地址时为默认地址
			if (address.getUserDefault().equals(true)) {
				// 取消默认地址状态
				userAddressMapper.setDefaultFalse(address.getUserId());
				// 设置默认地址状态
				userAddressMapper.setDefaultTrue(address.getUserAddressId());
			}
		}
		address.setUserAddressId(IDUtils.genUserAddressId());
		address.setUserCreated(new Date());
		address.setUserUpdated(new Date());
		// 去数据库添加
		userAddressMapper.insertSelective(address);
		// 执行查询
		listAddress = userAddressMapper.selectAllByUser(address.getUserId());
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	/**
	 * 更新用户地址
	 * 
	 * @param address
	 * @return
	 */
	@Override
	public LianjiuResult updateAddress(UserAddress address) {
		if (null == address) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		address.setUserUpdated(new Date());
		Util.printModelDetails(address);
		// 保存更新设置默认地址
		if (address.getUserDefault().equals(true)) {
			// 取消默认地址状态
			userAddressMapper.setDefaultFalse(address.getUserId());
			// 设置默认地址状态
			userAddressMapper.setDefaultTrue(address.getUserAddressId());
		}
		// 去数据库更新
		userAddressMapper.updateByPrimaryKeySelective(address);
		// 执行查询
		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(address.getUserId());
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	/**
	 * 更改默认
	 * 
	 * @param userAddressId,userId,userDefault
	 * @return
	 */
	@Override
	public LianjiuResult setDefault(String userAddressId, String userId) {
		// UserAddress userAddress = new UserAddress();
		// userAddress.setUserAddressId(userAddressId);
		// userAddress.setUserId(userId);
		// 将默认状态为true的地址，设为false
		userAddressMapper.setDefaultFalse(userId);
		// 将当前地址默认状态设为true
		int rowsAffected = userAddressMapper.setDefaultTrue(userAddressId);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 查询当前地址
	 */
	@Override
	public LianjiuResult selectByAddressId(String userAddressId, String userId) {
		UserAddress address = userAddressMapper.selectByAddressId(userAddressId, userId);
		if (null == address) {
			loggerUser.info("查询地址出错");
			return LianjiuResult.build(501, "地址error");
		}
		return LianjiuResult.ok(address);
	}

	/**
	 * 根据userID删除用户地址
	 */
	@Override
	public LianjiuResult deleteAdressByUser(String userAddressId, String userId) {
		if (Util.isEmpty(userAddressId)) {
			return LianjiuResult.build(501, "请指定企业回收订单id");
		}
		// 查地址是否是默认地址
		UserAddress userAddress = userAddressMapper.getAddressDefultByUser(userId);
		if (null == userAddress) {
			loggerUser.info("用户地址为空");
			return LianjiuResult.build(401, "删除失败");
		}
		// 去数据库查询
		int count = userAddressMapper.deleteById(userAddressId);
		if (0 == count) {
			loggerUser.info("删除地址error");
			return LianjiuResult.build(502, "删除失败");
		}
		System.out.println("默认地址" + userAddress.getUserAddressId());
		if (userAddressId.equals(userAddress.getUserAddressId())) {
			// 删除地址为默认地址，设置最新添加地址为默认地址
			System.out.println("删除地址为默认地址");
			userAddressMapper.modifyDefultByUser(userId);
		}
		// 执行查询
		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(userId);
		/*
		 * if (null == listAddress || 0 == listAddress.size()) {
		 * loggerUser.info("删除当前地址后，用户地址为空"); return LianjiuResult.build(503,
		 * "地址列表空"); }
		 */
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	/**
	 * 根据userID查用户地址
	 */
	@Override
	public LianjiuResult chooseAdressByUser(String userId) {
		// 执行查询
		List<UserAddress> listAddress = userAddressMapper.selectAllByUser(userId);
		if (null == listAddress || 0 == listAddress.size()) {
			loggerUser.info("用户地址为空");
			return LianjiuResult.build(501, "地址列表空");
		}
		LianjiuResult result = new LianjiuResult(listAddress);
		return result;
	}

	@Override
	public LianjiuResult selectAddressByAddressId(String userAddressId) {
		UserAddress address = userAddressMapper.selectAddressByAddressId(userAddressId);
		if (null == address) {
			loggerUser.info("用户地址为空");
			return LianjiuResult.build(501, "地址列表空");
		}
		return LianjiuResult.ok(address);
	}

	/**
	 * 创建用户
	 */
	@Override
	public LianjiuResult createUser(String username, String userPassword) {
		loggerUser.info("开始创建用户");
		User users = userMapper.selectAllByUsername(username);
		// 如果输入的用户名(电话号码)表中不存在
		if (null == users) {
			// md5加密
			// 生成UserID
			User user = new User();
			Util.printModelDetails(user);
			user.setUserId(IDUtils.genUserIDs());
			user.setUsername(username);
			user.setUserPhone(username);
			user.setUserCreated(new Date());
			user.setUserDetailsId(IDUtils.genUserDetIDs());
			user.setUserPassword(MD5Util.md5(userPassword));
			loggerUser.info("用户名:" + username + "密码:" + userPassword);
			userMapper.insertSelective(user);
			return LianjiuResult.ok();
		} else {
			loggerUser.info("改账号未注册");
			return LianjiuResult.build(400, "该账号注册");
		}
	}

	/**
	 * 快递回收订单
	 */
	@Override
	public LianjiuResult getExpressOrders(String userId) {
		loggerUser.info("开始进入快递回收订单查询");
		// 查询快递订单信息
		List<OrdersExpress> listOrders = ordersExpressMapper.selectExpressItemByOrdersId(userId);
		if (null == listOrders || 0 == listOrders.size()) {
			loggerUser.info("快递回收订单为空");
			return LianjiuResult.build(501, "快递回收订单为空");
		}
		String ordersId = null;
		String productName = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listOrders", listOrders);
		// 查询订单所对应的商品信息
		for (OrdersExpress list : listOrders) {
			ordersId = list.getOrExpressId();
			List<OrdersExpressItem> listProducts = ordersItemMapper.selectExpressItemByOrdersId(ordersId);
			for (OrdersExpressItem listProduct : listProducts) {
				productName = listProduct.getProductName();
				loggerUser.info(productName);
				map.put("productName", listProduct.getProductName());
			}
		}
		return LianjiuResult.ok(map);
	}

	/**
	 * 用户优惠券查询
	 */
	@Override
	public LianjiuResult selectCouponByUserId(String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(502, "请传入用户编号");
		}
		List<Coupon> listCoupon = couponMapper.selectByUserId(userId);
		if (null == listCoupon || 0 == listCoupon.size()) {
			loggerUser.info("用户优惠券为空");
			return LianjiuResult.build(501, "用户优惠券为空");
		}
		// LianjiuResult result = new LianjiuResult(listCoupon);
		return LianjiuResult.ok(listCoupon);
	}

	/**
	 * 热门活动
	 */
	@Override
	public LianjiuResult chooseAdressByUser(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdActivity> adActivity = adActivityMapper.selectActivityList();
		if (null == adActivity || 0 == adActivity.size()) {
			loggerUser.info("热门活动为空");
			return LianjiuResult.build(501, "热门活动为空");
		}
		Page<AdActivity> list = (Page<AdActivity>) adActivity;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list.getTotal());
		result.setLianjiuData(adActivity);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/*
	 * 
	 * 查询回收车 一个订单 对应多个商品记录
	 * 
	 */

	@Override
	public LianjiuResult getUserCare(String userId) {
		if (null == userId || userId.length() == 0 || userId.equals("{userId}")) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		String userCart = jedisClient.get(userId + "cart");
		if (Util.isEmpty(userCart)) {
			return LianjiuResult.build(400, "您的回收车订单为空");
		}
		List<Waste> list = null;
		if (null == userCart || "".equals(userCart) || "null".equals(userCart)) {

		} else {
			list = JsonUtils.jsonToList(userCart, Waste.class);
		}
		return LianjiuResult.ok(list);
	}

	/*
	 * 
	 * 查询精品订单 一个订单 只对应一个商品记录
	 * 
	 */
	@Override
	public LianjiuResult getExcellentItem(String userId) {
		if (null == userId || userId.length() == 0 || userId.equals("{userId}")) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		// 设置分页信息
		// PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersView> ordersList = ordersExcellentMapper.getExcellentByUserId(userId);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		return LianjiuResult.ok(ordersList);
	}

	/*
	 * 
	 * 查询手机维修 一个订单 只对应一个商品记录
	 * 
	 */
	@Override
	public LianjiuResult getRepair(String userId) {
		if (null == userId || userId.length() == 0 || userId.equals("{userId}")) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}

		List<OrdersRepair> repairList = ordersRepairMapper.getRepairByUserId(userId);
		List<OrdersRepairItemVo> list = new ArrayList<OrdersRepairItemVo>();
		for (OrdersRepair or : repairList) {
			OrdersRepairItemVo ordersRepairItemVo = new OrdersRepairItemVo();
			ordersRepairItemVo.setOrdersRepair(or);
			List<OrdersRepairItem> itemList = ordersRepairItemMapper.getItemByordersId(or.getOrRepairId());
			if (null != itemList && itemList.size() > 0) {
				ordersRepairItemVo.setOrdersRepairItem(itemList.get(0));
			}
			list.add(ordersRepairItemVo);
		}
		return LianjiuResult.ok(list);
	}

	/*
	 * 
	 * 查询快递 一个订单 对应多个商品记录
	 * 
	 */
	@Override
	public LianjiuResult getExpressItem(String userId) {
		if (null == userId || userId.length() == 0 || userId.equals("{userId}")) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		loggerUser.info("userId:" + userId);
		List<OrdersExpressItemVo> list = ordersExpressMapper.selectExpressByUserId(userId);
		List<OrdersExpressItemVo> lists = new ArrayList<OrdersExpressItemVo>();
		for (OrdersExpressItemVo vo : list) {
			lists.add(vo);
			vo.setItemList(ordersItemMapper.getItemByOrdersId(vo.getOrExpressId()));
		}
		return LianjiuResult.ok(list);
	}

	/**
	 * 废品订单
	 */
	@Override
	public LianjiuResult getFacaface(String userId) {
		if (null == userId || userId.length() == 0 || userId.equals("{userId}")) {
			return LianjiuResult.build(501, "error 用户id不能为空！");
		}
		loggerUser.info("userId:" + userId);
		List<OrdersFaceface> facefaceList = ordersFacefaceMapper.findByUserId(userId);

		List<OrdersFacefaceItemVo> list = new ArrayList<OrdersFacefaceItemVo>();
		for (OrdersFaceface vo : facefaceList) {
			OrdersFacefaceItemVo ordersFacefaceItemVo = new OrdersFacefaceItemVo();
			ordersFacefaceItemVo.setOrdersFaceface(vo);
			ordersFacefaceItemVo.setItemList(ordersItemMapper.getItemByOrdersId(vo.getOrFacefaceId()));
			list.add(ordersFacefaceItemVo);
		}
		return LianjiuResult.ok(list);
	}

	/**
	 * 根据用户id查默认地址
	 */
	@Override
	public LianjiuResult getAddressDefultByUser(String userId) {
		UserAddress address = userAddressMapper.getAddressDefultByUser(userId);
		if (null == address) {
			return LianjiuResult.build(502, "没有默认地址");
		}
		return LianjiuResult.ok(address);
	}

	/**
	 * 校验实名认证
	 */
	@Override
	public LianjiuResult isCertification(String userId) {
		Integer state = userDetailsMapper.getIsIdCard(userId);
		if(null == state){
			loggerUser.error("查询用户是否实名认证失败");
			return LianjiuResult.build(501, "用户信息错误");
			
		}
		String uDetailsId = userMapper.getDetailsId(userId);
		loggerUser.info("state:" + state);
		if (0 == state) {
			return LianjiuResult.build(400, "未实名认证");
		}
		if (1 == state) {
			UserDetails uDetails = userDetailsMapper.getNameCardById(uDetailsId);
			return LianjiuResult.ok(uDetails);
		} else {
			return LianjiuResult.build(500, "系统繁忙");
		}

	}

	/**
	 * 校验是否绑定银行卡
	 */
	@Override
	public LianjiuResult isUserbankCard(String userId) {
		loggerUser.info("开始校验是否绑定银行卡");
		String uDetailsId = userMapper.getDetailsId(userId);
		UserDetails uDetails = userDetailsMapper.getNameCardById(uDetailsId);
		String cardNo = uDetails.getUdetailsCardNo();
		// UserDetails userDetails =
		// userDetailsMapper.getBankCardById(uDetailsId);
		if (Util.isEmpty(cardNo)) {
			return LianjiuResult.build(400, "未绑定银行卡");
		} else {
			return LianjiuResult.ok(uDetails);
		}

	}

	/**
	 * 绑定银行卡
	 */
	@Override
	public LianjiuResult modifyUserbankCard(String userId, String udetailsCardNo, String udetailsCardBank,
			String code) {
		loggerUser.info("开始进入绑定银行卡");
		// UserDetails uDetails = userDetailsMapper.selectCardById(udetailsId);
		String uCode = null;
		String oPhone = null;
		uCode = jedisClient.get(REDIS_SMS_SESSION_KEY + ":" + GlobalValueUtil.phoneCodeToken);
		oPhone = jedisClient.get(REDIS_PHONE_SESSION_KEY + ":" + GlobalValueUtil.phoneToken);
		loggerUser.info(code + oPhone);
		if (Util.isEmpty(uCode) || Util.isEmpty(oPhone)) {
			return LianjiuResult.build(501, "验证码不正确");
		}
		if (!code.equals(uCode)) {
			return LianjiuResult.build(501, "验证码不正确");
		}
		String uDetailsId = userMapper.getDetailsId(userId);
		UserDetails uDetails = new UserDetails();
		uDetails.setUdetailsId(uDetailsId);
		uDetails.setUdetailsCardNo(udetailsCardNo);
		uDetails.setUdetailsCardBank(udetailsCardBank);
		int count = userDetailsMapper.updateUserbankCard(uDetails);
		if (count > 0) {
			loggerUser.info("绑定成功");
			jedisClient.del(REDIS_SMS_SESSION_KEY + ":" + GlobalValueUtil.phoneCodeToken);
			jedisClient.del(REDIS_PHONE_SESSION_KEY + ":" + GlobalValueUtil.phoneToken);
			return LianjiuResult.ok(uDetails);
		} else {

			return LianjiuResult.build(400, "绑定失败");
		}

	}

	/**
	 * 实名认证（绑定身份证）
	 */
	@Override
	public LianjiuResult subEnvirProtector(String userId, String username, String idCard) {
		loggerUser.info("开始进入实名认证");
		String udetailsId = userMapper.getDetailsId(userId);
		if (Util.isEmpty(udetailsId)) {
			return LianjiuResult.build(400, "请传入正确的用户ID");
		}
		UserDetails userDetails = new UserDetails();
		userDetails.setUdetailsId(udetailsId);
		userDetails.setUserName(username);
		userDetails.setUdetailsIdCard(idCard);
		userDetails.setUdetailsRealnameTime(new Date());
		int count = userDetailsMapper.upIdCardById(userDetails);
		return LianjiuResult.ok(count);
	}

	// 删除银行卡
	@Override
	public LianjiuResult removeUserbankCard(String udetailsId) {
		if (Util.isEmpty(udetailsId)) {
			return LianjiuResult.build(502, "用户id不能为空！");
		}
		int count = userDetailsMapper.removeUserbankCard(udetailsId);
		if (count > 0) {
			return LianjiuResult.build(200, "删除成功");
		} else {
			return LianjiuResult.build(400, "删除失败");
		}
	}

	// 修改银行卡（与绑定银行卡接口一致）
	@Override
	public LianjiuResult updateUserbankCard(UserDetails userDetails, String code) {
		loggerUser.info("开始修改银行卡");
		String uCode = null;
		String oPhone = null;
		uCode = jedisClient.get(REDIS_SMS_SESSION_KEY + ":" + GlobalValueUtil.phoneCodeToken);
		oPhone = jedisClient.get(REDIS_PHONE_SESSION_KEY + ":" + GlobalValueUtil.phoneToken);
		loggerUser.info(code + oPhone);
		if (Util.isEmpty(uCode) || Util.isEmpty(oPhone) || !code.equals(uCode)) {
			return LianjiuResult.build(501, "验证码不正确");
		}
		int count = userDetailsMapper.updateUserbankCard(userDetails);
		if (count > 0) {
			jedisClient.del(REDIS_SMS_SESSION_KEY + ":" + GlobalValueUtil.phoneCodeToken);
			jedisClient.del(REDIS_PHONE_SESSION_KEY + ":" + GlobalValueUtil.phoneToken);
			return LianjiuResult.build(200, "修改成功");
		} else {
			return LianjiuResult.build(400, "修改失败");
		}

	}

	@Override
	public String isCertificationPort(String appkey, String name, String cardno, String output) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appkey", appkey);
		map.put("name", name);
		map.put("cardno", cardno);
		map.put("output", output);
		String url = "http://api.id98.cn/api/idcard";
		String httpResponse = HttpClientUtil.doGet(url, map);
		loggerUser.info(httpResponse);
		return httpResponse;
	}

	/**
	 * 绑定身份证获取验证码
	 */
	@Override
	public LianjiuResult sendSmsForBankCard(String phone) {
		loggerUser.info("开始获取短信验证码");
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("11", "+86", phone, code);
		// 生成token
		String token = UUID.randomUUID().toString();
		// 把验证码、电话号码写入redis
		jedisClient.set(REDIS_SMS_SESSION_KEY + ":" + token, code);
		jedisClient.set(REDIS_PHONE_SESSION_KEY + ":" + token, phone);
		// 设置session的过期时间
		jedisClient.expire(REDIS_SMS_SESSION_KEY + ":" + token, 300);
		jedisClient.expire(REDIS_PHONE_SESSION_KEY + ":" + token, 300);
		// 设置验证码redis键为全局变量
		GlobalValueUtil.phoneCodeToken = token;
		GlobalValueUtil.phoneToken = token;
		return LianjiuResult.ok("发送成功");
	}

	/*
	 * 提现获取用户基本信息
	 */
	@Override
	public LianjiuResult getUserDetailsByUserId(String userId) {
		String udetailsId = userMapper.getDetailsId(userId);
		UserDetails details = userDetailsMapper.getDetailsByUserId(udetailsId);
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "提现异常");
		}
		return LianjiuResult.ok(details);
	}

	/**
	 * 自动生成推荐人
	 */
	@Override
	public synchronized LianjiuResult autoAddRecommend(String userId) {
		/**
		 * 查询用户是否已经存在推荐码
		 */
		String recommendNum = recommendMapper.getRecommendNumByUser(userId);
		if (Util.notEmpty(recommendNum)) {
			return LianjiuResult.ok(recommendNum);
		}
		/**
		 * 生成一个推荐码
		 */
		List<String> recommends = recommendMapper.getRecommendNumList();
		if (null == recommends) {
			return LianjiuResult.build(501, "数据异常");
		}
		Recommend recommend = new Recommend();
		recommend.setRecommendId(IDUtils.genUserIDs());
		// 从redis中取出用户
		User user = JedisTool.checkObjectFormRedis(jedisClient, GlobalValueJedis.REDIS_USER_SESSION_KEY + userId,
				User.class);
		if (null == user) {
			// 发个短信通知工作人员
			sendSMS.sendMessage("16", "+86", GlobalValueJedis.ADMIN_PHONE,
					GlobalValueJedis.REDIS_USER_SESSION_KEY + userId + "--生成推荐码");
			loggerOperation.info("掉线用户的key：" + GlobalValueJedis.REDIS_USER_SESSION_KEY + userId);
			loggerOperation.info("掉线用户的value：" + jedisClient.get(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId));
			return LianjiuResult.build(503, "用户已经掉线");
		} else {
			recommend.setRecommendName(user.getUserNickname());
			recommend.setRecommendPhone(user.getUsername());
		}
		recommend.setUserId(userId);
		// 随机生成六位数字， 校验是否重复
		int i = 0;
		while (true) {
			recommendNum = Integer.toString((int) ((Math.random() * 9 + 1) * 100000));
			// 7开头的是商户推荐码 8,9开头的预留
			if (recommendNum.matches("[7-9][0-9]{5}")) {
				continue;
			}
			if (!recommends.contains(recommendNum)) {
				break;
			}
			// 防止死循环
			if (500000 < i++) {
				return LianjiuResult.build(502, "推荐码暂时无法生成，请联系工作人员");
			}
		}
		recommend.setRecommendNum(recommendNum);
		// 存入数据库
		try {
			recommendMapper.insert(recommend);
		} catch (RuntimeException e) {
			return LianjiuResult.build(504, "数据异常");
		} catch (Exception e) {
			return LianjiuResult.build(505, "其他异常");
		}
		return LianjiuResult.ok(recommendNum);
	}

	/**
	 * 获取用户的推荐码
	 */
	@Override
	public LianjiuResult getRecommend(String userId) {
		/**
		 * 查询用户是否已经存在推荐码
		 */
		String recommendNum = recommendMapper.getRecommendNumByUser(userId);
		if (Util.notEmpty(recommendNum)) {
			return LianjiuResult.ok(recommendNum);
		}

		return LianjiuResult.build(501, "还没有生成推荐码");
	}
}
