package com.lianjiu.sso.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.Qq;
import com.lianjiu.model.User;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.Wechat;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.dao.RecommendDao;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.user.QqMapper;
import com.lianjiu.rest.mapper.user.RecommendMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.WechatMapper;
import com.lianjiu.sso.service.UserService;

/**
 * 用户管理Service
 * <p>
 * Title: UserServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private QqMapper qqMapper;
	@Autowired
	private WechatMapper wechatMapper;
	@Autowired
	private RecommendMapper recommendMapper;
	@Autowired
	private RecommendDao recommendDao;

	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	private static Logger loggerUser = Logger.getLogger("user");

	/**
	 * 校验数据
	 */
	@Override
	public LianjiuResult checkData(String username, String callback) {
		// 如果不存在
		User user = userMapper.selectAllByUsername(username);
		if (null == user) {
			return LianjiuResult.ok(true);
		}
		// 如果存在
		return LianjiuResult.ok(false);
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
			String userDetailsId = IDUtils.genUserDetIDs();
			user.setUserId(IDUtils.genUserIDs());
			user.setUsername(username);
			user.setUserPhone(username);
			user.setUserCreated(new Date());
			user.setUserDetailsId(userDetailsId);
			user.setUserPassword(MD5Util.md5(userPassword));
			loggerUser.info("用户名:" + username + "密码:" + userPassword);
			userMapper.insertSelective(user);
			UserDetails userDetails = new UserDetails();
			userDetails.setUdetailsId(userDetailsId);
			userDetails.setUdetailsRegisterTime(new Date());
			userDetailsMapper.insert(userDetails);
			return LianjiuResult.ok();
		} else {
			return LianjiuResult.build(400, "该账号已经注册");
		}
	}

	/**
	 * 短信验证码不存cookie
	 */
	@Override
	public LianjiuResult sendSms(String phone) {
		loggerUser.info("开始获取短信验证码");
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("3", "+86", phone, code);
		jedisClient.set(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, code);
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, 300);
		return LianjiuResult.ok("发送成功");
	}

	/**
	 * 短信登陆获取验证码
	 */
	@Override
	public LianjiuResult sendSmsLogin(String phone) {
		loggerUser.info("短信登陆获取验证码");
		// 查询该账号是否存在推荐人
		int isReferrer = userDetailsMapper.getReferrerByPhone(phone);
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("3", "+86", phone, code);
		// 把验证码、电话号码写入redis
		jedisClient.set(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, code);
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, 300);
		return LianjiuResult.ok(isReferrer);

	}

	/**
	 * 短信验证码登陆
	 */
	@Override
	public LianjiuResult sMsLogin(String phone, String uCode, String udetailsEquipment, String udetailsReferrer) {
		loggerUser.info("开始短信登陆");
		// 获取短信验证码,验证手机号码
		String code = null;
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerUser.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		code = jedisClient.get(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		// 验证码输入正确
		if (Util.isEmpty(code) || !uCode.equals(code)) {
			loggerUser.info("验证码不正确");
			return LianjiuResult.build(502, "验证码不正确");
		}
		String udetailsEdition = GlobalValueUtil.SMS;
		User user = userMapper.selectUserByphone(phone);
		// 该号码登陆过，已是用户
		if (null != user) {
			loggerUser.info("该用户已是链旧会员用户");
			// 更新登陆设备状态
			UserDetails details = new UserDetails();
			details.setUdetailsEquipment(udetailsEquipment);
			details.setUdetailsId(user.getUserDetailsId());
			details.setUdetailsEdition(udetailsEdition);
			Util.printModelDetails(details);

			userDetailsMapper.modifyRefferByUserId(details);
			user.setUserLogined(new Date());
			user.setUserNickname("");
			userMapper.updateLogined(user);
		} else {
			// 该用户首次登陆
			loggerUser.info("用户首次登陆");
			// 创建用户登陆信息
			user = new User();
			UserDetails userDetails = new UserDetails();
			if (Util.isEmpty(udetailsReferrer) || "aaaaaa".equals(udetailsReferrer) || "".equals(udetailsReferrer)) {
				loggerUser.info("推荐码没传入");
				userDetails.setUdetailsReferrer("aaaaaa");
				user.setUdetailsReferrer("aaaaaa");
			} else if (checkRecomend(udetailsReferrer)) {
				// 把推荐人的推荐码存到用户详情中
				userDetails.setUdetailsReferrer(udetailsReferrer);
				user.setUdetailsReferrer(udetailsReferrer);
			} else {
				loggerUser.info("推荐码不存在");
				return LianjiuResult.build(503, "推荐码不存在");
			}
			String userId = IDUtils.genUserIDs();
			String userDetailsId = IDUtils.genUserDetIDs();
			String photo = GlobalValueUtil.LIANJIU_LOG;
			user.setUserId(userId);
			user.setUserPhone(phone);
			user.setUsername(phone);
			user.setUserNickname("");
			user.setUserDetailsId(userDetailsId);
			user.setUserPhoto(photo);
			user.setUserCreated(new Date());
			user.setUserLogined(new Date());
			userMapper.insert(user);
			// 创建用户基本信息

			userDetails.setUdetailsId(userDetailsId);
			userDetails.setUdetailsPhoto(photo);
			// userDetails.setUdetailsReferrer(udetailsReferrer);
			userDetails.setUdetailsEquipment(udetailsEquipment);
			userDetails.setUdetailsEdition(udetailsEdition);
			userDetails.setUserPhonenum(phone);

			// 首次登陆用户积分+10
			userDetails.setUdetailsIntegral(10);
			userDetails.setUdetailsPhoto(photo);
			userDetails.setUdetailsRegisterTime(new Date());
			userDetailsMapper.insert(userDetails);

			// 创建钱包
			String walletId = IDUtils.genWalletLianjiuId();
			WalletLianjiu walletLianjiu = new WalletLianjiu();
			walletLianjiu.setUserId(userId);
			walletLianjiu.setWalletId(walletId);
			walletLianjiu.setWalletCreated(new Date());
			walletLianjiu.setWalletUpdated(new Date());
			walletLianjiuMapper.insertWallet(walletLianjiu);
			/**
			 * 给推荐人加钱，给推荐商户计数
			 */
			recommendDao.payForRecomend(udetailsReferrer);
		}
		user.setUserPassword(null);
		user.setUdetailsReferrer(null);
		// 删除获取验证码缓存信息
		jedisClient.del(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		// 把用户信息写入redis
		jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + user.getUserId(), JsonUtils.objectToJson(user));
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + user.getUserId(), SSO_SESSION_EXPIRE);

		return LianjiuResult.ok(user);

	}

	/**
	 * 
	 * zhaoxi 2017年12月29日 descrption:推荐码的校验，传入且存在，不传入，返回ture
	 * 
	 * @param udetailsReferrer
	 * @param userDetails
	 * @return LianjiuResult
	 */
	private boolean checkRecomend(String udetailsReferrer) {
		try {
			// 查询推荐码是否存在
			String recommendId = recommendMapper.getIdByNum(udetailsReferrer);
			if (Util.isEmpty(recommendId)) {
				loggerUser.info("推荐码不存在");
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			loggerUser.info("查询推荐码出现sql异常");
			loggerUser.info(e.getMessage());
			loggerUser.info(e.getCause());
			return false;
		}
	}

	/**
	 * token获取短信验证码
	 */
	@Override
	public LianjiuResult getCodeByToken(String token) {
		/*
		 * if (Util.isEmpty(token)) { return LianjiuResult.build(400,
		 * "token为空"); } if (token.startsWith("LS") && token.trim().length() ==
		 * 15) { token = jedisClient.get(token); } // 根据token从redis中查询用户信息
		 * String json = jedisClient.get(REDIS_SMS_SESSION_KEY + ":" + token);
		 * // 判断是否为空 if (StringUtils.isBlank(json)) { return
		 * LianjiuResult.build(400, "用户已掉线"); } // 更新过期时间
		 * jedisClient.expire(REDIS_SMS_SESSION_KEY + ":" + token,
		 * SSO_SESSION_EXPIRE); // 返回用户信息
		 */ return null;
	}

	/**
	 * 获取短信验证码token
	 */
	@Override
	public LianjiuResult getSmsByToken(String token) {
		/*
		 * // 根据token从redis中查询用户信息 String json =
		 * jedisClient.get(REDIS_SMS_SESSION_KEY + ":" + token); // 判断是否为空 if
		 * (StringUtils.isBlank(json)) { return LianjiuResult.build(400,
		 * "用户已掉线"); } // 更新过期时间 jedisClient.expire(REDIS_SMS_SESSION_KEY + ":"
		 * + token, SSO_SESSION_EXPIRE); // 返回用户信息
		 */ return null;
	}

	/**
	 * 用户登录
	 * <p>
	 * Title: userLogin
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public LianjiuResult userLogin(String username, String userPassword, String udetailsEquipment) {
		User user = userMapper.selectAllByUsername(username);
		loggerUser.info("开始链旧普通登陆");
		// 如果根据名称查不到数据
		if (null == user) {
			loggerUser.info("账号不存在");
			return LianjiuResult.build(501, "账号不存在，可使用手机短信登录");
		}
		// 如果密码为空
		if (null == user.getUserPassword() || "" == user.getUserPassword()) {
			loggerUser.info("您好，你还未设置密码，请使用短信登录");
			return LianjiuResult.build(503, "您好，你还未设置密码，请使用短信登录");
		}
		String pwd = DigestUtils.md5DigestAsHex(userPassword.getBytes());
		// 如果输入的密码跟数据库的密码相等
		if (!user.getUserPassword().equals(pwd)) {
			loggerUser.info("密码错误");
			return LianjiuResult.build(503, "密码输入不正确");
		}
		// 登陆成功
		String photo = GlobalValueUtil.LIANJIU_LOG;
		user.setUserLogined(new Date());
		user.setUsername(username);
		user.setUserNickname(username);
		user.setUserPhoto(photo);
		userMapper.updateLogined(user);
		// 更新登陆设备状态
		UserDetails details = new UserDetails();
		String udetailsEdition = GlobalValueUtil.LIANJIU;
		details.setUdetailsEquipment(udetailsEquipment);
		details.setUdetailsId(user.getUserDetailsId());
		details.setUdetailsEdition(udetailsEdition);
		userDetailsMapper.modifyEqEdByUserId(details);
		// 保存用户之前，把用户对象中的密码清空。
		user.setUserPassword(null);
		user.setUdetailsReferrer(null);
		// 把用户信息写入redis
		jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + user.getUserId(), JsonUtils.objectToJson(user));
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + user.getUserId(), SSO_SESSION_EXPIRE);
		return LianjiuResult.ok(user);
	}

	@Override
	public LianjiuResult getUserByToken(String token) {
		// 根据token从redis中查询用户信息
		String json = jedisClient.get(GlobalValueJedis.REDIS_USER_SESSION_KEY + token);
		// 判断是否为空
		if (Util.isEmpty(json) || StringUtils.isBlank(json)) {
			return LianjiuResult.build(400, "用户已掉线");
		}
		// 更新过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + token, SSO_SESSION_EXPIRE);
		// 返回用户信息
		return LianjiuResult.ok(JsonUtils.jsonToPojo(json, User.class));
	}

	/**
	 * 设置用户密码
	 */

	@Override
	public LianjiuResult modifyPwd(String userId, String userPassword) {
		loggerUser.info("开始设置密码");
		String password = MD5Util.md5(userPassword);
		int rowsAffected = userMapper.modifyPwd(userId, password);
		User user = userMapper.selectByPrimaryKey(userId);
		if (null == user) {
			loggerUser.info("用户对象为空");
			return LianjiuResult.build(501, "error user为null");
		}
		user.setUserPassword(null);
		user.setUdetailsReferrer(null);
		// 更新缓存
		jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId, JsonUtils.objectToJson(user));
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId, SSO_SESSION_EXPIRE);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 忘记密码获取验证码
	 */
	@Override
	public LianjiuResult forgetPwdSms(String phone) {
		loggerUser.info("开始忘记密码获取密码");
		// 判断该电话号码是否存在
		String userId = userMapper.getIdByPhone(phone);
		if (Util.isEmpty(userId)) {
			loggerUser.info("账号不存在");
			return LianjiuResult.build(501, "该号码还未注册账号");

		}
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("2", "+86", phone, code);
		jedisClient.set(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, code);
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, 300);
		return LianjiuResult.ok("发送成功");
	}

	/**
	 * 更新用户密码(忘记密码)
	 */
	@Override
	public LianjiuResult updatePwd(String userPassword, String code, String phone) {
		loggerUser.info("开始进入更新用户密码");
		String userId = userMapper.getIdByPhone(phone);
		if (Util.isEmpty(userId)) {
			loggerUser.info("账号不存在");
			return LianjiuResult.build(501, "用户不存在");
		}
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerUser.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		// 判断用户输入的验证码是否匹配
		String aCode = jedisClient.get(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		if (Util.isEmpty(aCode) || !code.equals(aCode)) {
			loggerUser.info("验证码不正确");
			return LianjiuResult.build(502, "验证码不正确");
		}
		/*
		 * // 验证码验证符合,更新用户密码 if (!code.equals(aCode)) {
		 * loggerUser.info("验证码不正确"); return LianjiuResult.build(503, "验证码不正确");
		 * }
		 */
		String password = MD5Util.md5(userPassword);
		int rowsAffected = userMapper.updatePwd(phone, password);
		jedisClient.del(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		return LianjiuResult.ok(rowsAffected);

	}

	/**
	 * 校验原手机号码发送验证码(修改电话号码)
	 */
	@Override
	public LianjiuResult sendSmsPhone(String userId, String phone) {
		loggerUser.info("开始获取短信验证码,校验手机号码是否是当前用户号码");
		String oPhone = userMapper.getPhoneById(userId);
		if (Util.isEmpty(oPhone) || !phone.equals(oPhone)) {
			loggerUser.info("手机号码与当前手机号码不一致");
			return LianjiuResult.build(501, "您输入的手机号码与当前手机号码不一致");
		}
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("9", "+86", phone, code);
		// 把验证码写入redis
		jedisClient.set(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, code);
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, 300);
		return LianjiuResult.ok("发送成功");
	}

	@Override
	public LianjiuResult phoneCheck(String phone, String code) {
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerUser.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		// 去redis中取出验证码信息，匹配code不正确返回502
		String aCode = jedisClient.get(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		if (Util.isEmpty(aCode) || !code.equals(aCode)) {
			return LianjiuResult.build(501, "验证码不正确");
		}
		// 清除缓存
		jedisClient.del(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		// 校验无误返回 状态码200
		return LianjiuResult.build(200, "校验成功");

	}

	/**
	 * 更改密码
	 */
	@Override
	public LianjiuResult changePwd(String phone, String code, String userPassword) {
		loggerUser.info("开始进入更改用户密码");
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerUser.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		// 判断用户输入的验证码是否匹配
		String aCode = jedisClient.get(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		if (Util.isEmpty(aCode) || !code.equals(aCode)) {
			loggerUser.info("验证码不正确");
			return LianjiuResult.build(502, "验证码不正确");
		}
		String password = MD5Util.md5(userPassword);
		String opassword = userMapper.getPwdByPhone(phone);
		if (Util.isEmpty(opassword) || password.equals(opassword)) {
			loggerUser.info("新密码不能与旧密码一致");
			return LianjiuResult.build(503, "新密码不能与旧密码一致");
		}
		userMapper.updatePwd(phone, password);
		jedisClient.del(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("修改成功");
		return LianjiuResult.build(200, "修改成功");
	}

	/**
	 * 向新手机发送验证码
	 */
	@Override
	public LianjiuResult sendNewPhone(String userId, String phone) {
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(500, "电话号码不能为空");
		}
		loggerUser.info("向新手机发送验证码");
		// 判断新手机号码是否存在数据库
		String uId = userMapper.getIdByPhone(phone);
		if (Util.notEmpty(uId)) {
			if (userId.equals(uId)) {
				loggerUser.info("手机号码与当前电话号码一致");
				return LianjiuResult.build(505, "新手机号码不能与当前手机号码一致");
			}
			loggerUser.info("您输入的手机号码已存在");
			return LianjiuResult.build(506, "您输入的手机号码已存在");
		}
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		sendSMS.sendMessage("8", "+86", phone, code);
		jedisClient.set(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, code);
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, 300);
		return LianjiuResult.ok("发送成功");
	}

	// 修改手机号码
	@Override
	public LianjiuResult phoneChange(String userId, String phone, String code) {
		loggerUser.info("开始验证修改");
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerUser.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		// 去redis中取出验证码信息，匹配code不正确返回502
		String uCode = jedisClient.get(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		if (Util.isEmpty(uCode) || !code.equals(uCode)) {
			loggerUser.info("验证码不正确");
			return LianjiuResult.build(504, "验证码不正确");
		}
		// json转对象
		User user = userMapper.selectByPrimaryKey(userId);
		if (null == user) {
			loggerUser.info("用户对象为空");
			return LianjiuResult.build(501, "error user为null");
		}
		loggerUser.info("从缓存中读取用户信息");
		Util.printModelDetails(user);
		String udetailsId = user.getUserDetailsId();
		User users = new User();
		// 修改手机号
		users.setUserPhone(phone);
		users.setUsername(phone);
		users.setUserId(userId);
		// 去数据库更新
		int count = userMapper.updateUserPhone(users);
		if (0 == count) {
			loggerUser.info("更新手机号码失败");
			return LianjiuResult.build(505, "系统繁忙");
		}
		// 更新用户信息表
		UserDetails userDetails = new UserDetails();
		userDetails.setUdetailsId(udetailsId);
		userDetails.setUserPhonenum(phone);
		userDetailsMapper.updatePhone(userDetails);
		// 取出验证码相关信息后直接删除redis中的内容
		jedisClient.del(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId, JsonUtils.objectToJson(users));
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId, SSO_SESSION_EXPIRE);
		loggerUser.info("更新用户信息缓存");
		Util.printModelDetails(users);
		loggerUser.info("修改成功");
		return LianjiuResult.build(200, "修改成功");
	}

	/**
	 * 判断用户是否设置过密码
	 */
	@Override
	public LianjiuResult isPwd(String username) {
		loggerUser.info("开始判断用户是否设置过密码");
		String pwd = userMapper.getPwdByPhone(username);
		if (Util.isEmpty(pwd)) {
			loggerUser.info("未设置过密码");
			return LianjiuResult.build(400, "未设置过密码");
		} else {
			loggerUser.info("已经设置过密码");
			return LianjiuResult.build(200, "已经设置过密码");
		}
	}

	/**
	 * QQ微信授权成功
	 */
	@Override
	public LianjiuResult OtherLogin(String num, String nickname, String picture, int type) {
		if (Util.isEmpty(num) || Util.isEmpty(nickname) || Util.isEmpty(picture)) {
			loggerUser.info("qq基本信息获取失败");
			return LianjiuResult.build(501, "qq基本信息获取失败");
		}
		String qUserId = qqMapper.getUserIdByNum(num);
		String wUserId = wechatMapper.getUserIdByNum(num);
		String userDetailsId = IDUtils.genUserDetIDs();
		String qqId = IDUtils.genQWIDs(1);// qqID生成
		String wechatId = IDUtils.genQWIDs(0);
		String userId = IDUtils.genUserIDs();
		Date date = new Date();
		// 查询该qq号码是否与链旧账号绑定过
		if (type == 1) {
			// 未绑定链旧账号
			if (Util.isEmpty(qUserId)) {
				loggerUser.info("用户QQ未绑定链旧账号，开始创建");
				// qq登陆
				Qq qqMessage = new Qq();
				if (Util.notEmpty(wUserId)) {
					qqMessage.setUserId(wUserId);
				} else {
					qqMessage.setUserId(userId);
				}
				qqMessage.setQqId(qqId);
				qqMessage.setQqNum(num);
				qqMessage.setQqNickname(nickname);
				qqMessage.setQqPicture(picture);
				qqMessage.setQqCreated(date);
				qqMessage.setQqUpdated(date);
				qqMapper.insertSelective(qqMessage);
			}
		}
		// 查询该微信号码是否与链旧账号绑定过
		if (type == 0) {
			if (Util.isEmpty(wUserId)) {
				loggerUser.info("用户微信未绑定链旧账号，开始创建");
				// 创建微信表信息
				Wechat wechat = new Wechat();
				if (Util.notEmpty(qUserId)) {
					wechat.setUserId(qUserId);
				} else {
					wechat.setUserId(userId);
				}
				wechat.setWechatId(wechatId);
				wechat.setWechatNickname(nickname);
				wechat.setWechatNum(num);
				wechat.setWechatPicture(picture);
				wechat.setWechatCreated(new Date());
				wechat.setWechatUpdated(new Date());
				wechatMapper.insertSelective(wechat);
			}
		}
		// 创建用户登陆信息
		if (Util.isEmpty(wUserId) && Util.isEmpty(qUserId)) {
			User user = new User();
			UserDetails userDetails = new UserDetails();
			user.setUserId(userId);
			user.setUserDetailsId(userDetailsId);
			user.setUserNickname(nickname);
			user.setUserCreated(new Date());
			user.setUserLogined(new Date());
			user.setUserPhoto(picture);
			userMapper.insertSelective(user);
			userDetails.setUdetailsId(userDetailsId);
			userDetails.setUdetailsPhoto(picture);
			userDetails.setUdetailsIntegral(10);
			userDetails.setUdetailsRegisterTime(date);
			userDetailsMapper.insert(userDetails);
		}
		// 查该qq/微信账号是否绑定了电话号码
		loggerUser.info("开始判断用户是否绑定了手机号码");
		// User qUser = userMapper.getByQqNum(num);
		// User wUser = userMapper.getByWechatNum(num);
		String qPhone = userMapper.getQphoneByNum(num);
		// 没有绑定电话号码
		if (Util.isEmpty(qPhone)) {
			String wPhone = userMapper.getWphoneByNum(num);
			if (Util.isEmpty(wPhone)) {
				loggerUser.info("账号未绑定手机号码");
				return LianjiuResult.build(502, "该账号还未绑定手机号码");
			}
			loggerUser.info("绑定了手机号码");
			return LianjiuResult.build(200, "已绑定");
		}
		loggerUser.info("已经绑定了手机号码");
		return LianjiuResult.build(200, "已绑定");
	}

	/**
	 * 绑定手机号码
	 */
	@Override
	public LianjiuResult bindingPhone(String userId, String phone, String code) {
		loggerUser.info("绑定手机号码");
		/*
		 * // 查该电话号码是否绑定其他账号-------------发送验证已经判断 List<String> listPhone =
		 * userMapper.getAllPhone(); for (String stringPhone : listPhone) { if
		 * (stringPhone.equals(phone)) { return LianjiuResult.build(501,
		 * "该电话号码已经绑定其他账号"); } }
		 */
		// 去redis中取出验证码信息，匹配code不正确返回502
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerUser.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		String aCode = jedisClient.get(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		if (Util.isEmpty(aCode) || !code.equals(aCode)) {
			loggerUser.info("验证码不正确");
			return LianjiuResult.build(502, "验证码不正确");
		}
		User user = new User();
		user.setUserId(userId);
		user.setUserPhone(phone);
		user.setUsername(phone);
		int count = userMapper.updateUserPhone(user);
		if (count == 0) {
			loggerUser.info("绑定失败，更新语句报错");
			return LianjiuResult.build(504, "绑定失败");
		}
		// 清除缓存
		jedisClient.del(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone);
		loggerUser.info("绑定手机号成功");
		return LianjiuResult.build(200, "绑定成功");

	}

	/**
	 * 绑定手机获取验证码
	 */
	@Override
	public LianjiuResult sendSmsByBinding(String phone) {
		loggerUser.info("开始获取短信验证码");
		// 查该电话号码是否绑定其他账号
		/*
		 * String userId = userMapper.getIdByPhone(phone); if
		 * (Util.notEmpty(userId)) { loggerUser.info("该号码已经绑定过其他账号"); return
		 * LianjiuResult.build(501, "该号码已经绑定过其他账号"); }
		 */

		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("10", "+86", phone, code);
		// 把验证码、电话号码写入redis
		jedisClient.set(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, code);
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, 300);
		return LianjiuResult.ok("发送成功");
	}

	/**
	 * 通过userId查用户信息
	 */
	@Override
	public LianjiuResult getUserMessage(String userId) {
		/**
		 * 从redis中取出用户
		 */
		String userJson = jedisClient.get(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId);
		loggerUser.info("userJson" + userJson);
		if (Util.isEmpty(userJson)) {
			loggerUser.info("用户已掉线");
			return LianjiuResult.build(501, "用户已经掉线");
		}
		User user = JsonUtils.jsonToPojo(userJson, User.class);
		Util.printModelDetails(user);
		if (null != user) {
			return LianjiuResult.ok(user);
		}
		loggerUser.info("通过用户编号查询redis失败");
		return LianjiuResult.build(500, "请传入正确的用户ID");
	}

	/**
	 * qq微信授权校验登陆
	 */
	@Override
	public LianjiuResult QWLogin(String openId, Integer type, String udetailsEquipment) {
		// 查type对应的openId是否存在
		// qq授权
		if (GlobalValueUtil.QQ_LOGIN == type) {
			// 查qq表是否存在openId
			Qq qq = qqMapper.getByNum(openId);
			// qq未绑定链旧账号，查询微信是否绑定了链旧账号
			if (null == qq) {
				loggerUser.info("用户未绑定链旧账号");
				return LianjiuResult.build(500, "用户未绑定链旧账号");// 跳转到绑定手机页面
			}
			// qq已绑定链旧账号，返回用户登陆信息
			User user = userMapper.selectUserByUserId(qq.getUserId());
			if (null == user) {
				loggerUser.info("用户对象为空");
				return LianjiuResult.build(501, "error user为null");
			}
			// 更新登陆设备状态
			UserDetails details = new UserDetails();
			String udetailsEdition = GlobalValueUtil.QQ;
			details.setUdetailsEquipment(udetailsEquipment);
			details.setUdetailsId(user.getUserDetailsId());
			details.setUdetailsEdition(udetailsEdition);
			userDetailsMapper.modifyEqEdByUserId(details);
			// 把用户信息写入redis
			jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + qq.getUserId(), JsonUtils.objectToJson(user));
			// 设置session的过期时间
			jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + qq.getUserId(), SSO_SESSION_EXPIRE);
			return LianjiuResult.ok(user);
		}
		// 微信授权
		if (GlobalValueUtil.WECHAT_LOGIN == type) {
			// 查wehat表是否存在openId
			Wechat wechat = wechatMapper.getByNum(openId);
			if (null == wechat) {
				loggerUser.info("用户未绑定链旧账号");
				return LianjiuResult.build(501, "用户未绑定链旧账号");// 跳转到绑定手机页面
			}
			// 微信已绑定链旧账号，返回用户登陆信息
			User user = userMapper.selectUserByUserId(wechat.getUserId());
			if (null == user) {
				loggerUser.info("用户对象为空");
				return LianjiuResult.build(501, "error user为null");
			}
			// 更新登陆设备状态
			UserDetails details = new UserDetails();
			String udetailsEdition = GlobalValueUtil.WECHAT;
			loggerUser.info("udetailsEquipment" + udetailsEquipment);
			details.setUdetailsEquipment(udetailsEquipment);
			details.setUdetailsId(user.getUserDetailsId());
			details.setUdetailsEdition(udetailsEdition);
			Util.printModelDetails(details);
			userDetailsMapper.modifyEqEdByUserId(details);
			// 把用户信息写入redis
			jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + wechat.getUserId(), JsonUtils.objectToJson(user));
			// 设置session的过期时间
			jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + wechat.getUserId(), SSO_SESSION_EXPIRE);
			return LianjiuResult.ok(user);
		}
		return LianjiuResult.build(502, "type传入值有误");
	}

	/**
	 * 绑定校验手机号码，创建用户
	 */
	@Override
	public LianjiuResult CreateUser(String openId, String userPhone, String code, String nickname, String picture,
			Integer type, String udetailsEquipment) {
		loggerUser.info("第三方登陆");
		// 校验手机号码，创建用户
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + userPhone);
		loggerUser.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerUser.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		// 去redis中取出验证码信息，匹配code不正确返回502
		String aCode = jedisClient.get(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + userPhone);
		Date date = new Date();
		if (Util.isEmpty(aCode)) {
			loggerUser.info("验证码不正确");
			return LianjiuResult.build(502, "验证码不正确");
		}
		// 查QQ/微信表查看是否存在该电话号码，
		// user表存在的情况
		User users = userMapper.selectUserByphone(userPhone);
		// 该电话号码未绑定链旧账号
		if (null == users) {
			if (code.equals(aCode)) {
				// 清除缓存
				jedisClient.del(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + userPhone);
				String userId = IDUtils.genUserIDs();
				String userDetailsId = IDUtils.genUserDetIDs();
				// 创建用户登陆信息
				User user = new User();
				user.setUserId(userId);
				user.setUserPhone(userPhone);
				user.setUsername(userPhone);
				user.setUserDetailsId(userDetailsId);
				user.setUserCreated(date);
				user.setUserLogined(date);
				userMapper.insertSelective(user);
				// 创建钱包
				String walletId = IDUtils.genWalletLianjiuId();
				WalletLianjiu walletLianjiu = new WalletLianjiu();
				walletLianjiu.setUserId(userId);
				walletLianjiu.setWalletId(walletId);
				walletLianjiu.setWalletCreated(date);
				walletLianjiu.setWalletUpdated(date);
				walletLianjiuMapper.insertWallet(walletLianjiu);
				// 创建qq表
				if (GlobalValueUtil.QQ_LOGIN == type) {
					loggerUser.info("新用户QQ登陆");
					Qq qq = new Qq();
					qq.setUserId(userId);
					qq.setQqId(IDUtils.genQWIDs(1));
					qq.setQqNickname(nickname);
					qq.setQqNum(openId);
					qq.setQqPicture(picture);
					qq.setQqCreated(date);
					qq.setQqUpdated(date);
					qqMapper.insertSelective(qq);
					// 创建用户基本信息
					UserDetails userDetails = new UserDetails();
					userDetails.setUdetailsId(userDetailsId);
					// 首次登陆用户积分+10
					userDetails.setUdetailsIntegral(10);
					// 更新登陆设备状态
					String udetailsEdition = GlobalValueUtil.QQ;
					userDetails.setUdetailsEquipment(udetailsEquipment);
					userDetails.setUdetailsId(user.getUserDetailsId());
					userDetails.setUdetailsEdition(udetailsEdition);
					userDetailsMapper.insert(userDetails);
					// 设置基本信息表头像为qq头像
				}
				if (GlobalValueUtil.WECHAT_LOGIN == type) {
					loggerUser.info("新用户微信登陆");
					Wechat wechat = new Wechat();
					wechat.setUserId(userId);
					wechat.setWechatNum(openId);
					wechat.setWechatId(IDUtils.genQWIDs(0));
					wechat.setWechatNickname(nickname);
					wechat.setWechatPicture(picture);
					wechat.setWechatCreated(date);
					wechat.setWechatUpdated(date);
					wechatMapper.insertSelective(wechat);
					// 创建用户基本信息
					UserDetails userDetails = new UserDetails();
					userDetails.setUdetailsId(userDetailsId);
					// 首次登陆用户积分+10
					userDetails.setUdetailsIntegral(10);
					// 更新登陆设备状态
					String udetailsEdition = GlobalValueUtil.WECHAT;
					userDetails.setUdetailsEquipment(udetailsEquipment);
					userDetails.setUdetailsId(user.getUserDetailsId());
					userDetails.setUdetailsEdition(udetailsEdition);
					userDetailsMapper.insert(userDetails);
				}
				// 生成token
				String token = UUID.randomUUID().toString();
				// 把用户信息写入redis
				jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId, JsonUtils.objectToJson(user));
				// 设置session的过期时间
				jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId, SSO_SESSION_EXPIRE);
				GlobalValueUtil.loginMessageToken = token;
				return LianjiuResult.ok(user);
				// 创建qq表信息
			} else {
				// 筛选出用户手机号与当前传入的手机号匹配，匹配不正确返回503
				loggerUser.info("验证码不正确");
				return LianjiuResult.build(503, "验证码不正确");
			}
		}
		// 用户号码已经注册了链旧账号，再查该用户是否绑定了QQ微信，将用户id关联到qq/微信表
		// qq登陆，查qq表
		String userId = users.getUserId();
		if (1 == type) {
			Qq qq = qqMapper.getNpByUserId(userId);
			if (null != qq) {
				loggerUser.info("该电话号码已经绑定其他qq账号");
				return LianjiuResult.build(504, "该电话号码已经绑定其他qq账号");
			}
		}
		// 微信登陆，查微信表
		if (0 == type) {
			Wechat wechat = wechatMapper.getNpByUserId(userId);
			if (null != wechat) {
				loggerUser.info("该电话号码已经绑定其他微信账号");
				return LianjiuResult.build(505, "该电话号码已经绑定其他微信账号");
			}
		}

		// 更新用户登陆表
		User aUser = new User();
		aUser.setUserId(users.getUserId());
		int cAuser = userMapper.modifyNickById(aUser);
		if (cAuser == 0) {
			loggerUser.info("更新用户信息出错");
			return LianjiuResult.build(506, "用户信息错误");
		}
		if (GlobalValueUtil.QQ_LOGIN == type) {
			Qq qq = new Qq();
			qq.setUserId(userId);
			qq.setQqId(IDUtils.genQWIDs(1));
			qq.setQqNickname(nickname);
			qq.setQqNum(openId);
			qq.setQqPicture(picture);
			qq.setQqCreated(date);
			qq.setQqUpdated(date);
			qqMapper.insertSelective(qq);
		}
		if (GlobalValueUtil.WECHAT_LOGIN == type) {
			Wechat wechat = new Wechat();
			wechat.setUserId(userId);
			wechat.setWechatNum(openId);
			wechat.setWechatId(IDUtils.genQWIDs(0));
			wechat.setWechatNickname(nickname);
			wechat.setWechatPicture(picture);
			wechat.setWechatCreated(date);
			wechat.setWechatUpdated(date);
			wechatMapper.insertSelective(wechat);
		}
		// 把用户信息写入redis
		jedisClient.set(GlobalValueJedis.REDIS_USER_SESSION_KEY + users.getUserId(), JsonUtils.objectToJson(users));
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SESSION_KEY + users.getUserId(), SSO_SESSION_EXPIRE);
		return LianjiuResult.ok(users);
	}

	/**
	 * 退出登陆
	 */
	@Override
	public LianjiuResult loginOut(String userId) {
		loggerUser.info("退出登陆");
		String oLoginJson = jedisClient.get(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId);
		loggerUser.info("当前缓存信息：" + oLoginJson);
		jedisClient.del(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId);
		String loginJson = jedisClient.get(GlobalValueJedis.REDIS_USER_SESSION_KEY + userId);
		loggerUser.info("退出后的登陆信息" + loginJson);
		return LianjiuResult.build(200, "退出登陆成功");
	}

	/**
	 * 
	 * 修改密码获取验证码
	 */
	@Override
	public LianjiuResult sendSMSPwd(String userId) {
		String phone = userMapper.getPhoneById(userId);
		String code = IDUtils.genPWDId();
		sendSMS.sendMessage("14", "+86", phone, code);
		// 把验证码、电话号码写入redis
		jedisClient.set(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, code);
		// 设置session的过期时间
		jedisClient.expire(GlobalValueJedis.REDIS_USER_SMS_SESSION_KEY + phone, 300);
		return LianjiuResult.ok("发送成功");
	}

}
