package com.lianjiu.sso.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.AllianceBusiness;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessMapper;
import com.lianjiu.sso.service.AllianceUserService;

@Service
public class AllianceUserServiceImpl implements AllianceUserService {

	@Autowired
	private AllianceBusinessMapper allianceBusinessMapper;

	@Autowired
	private AllianceBusinessDetailsMapper detailsMapper;

	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	@Autowired
	private JedisClient jedisClient;

	private static Logger loggerAlliance = Logger.getLogger("alliance");

	/**
	 * 加盟商登陆
	 * <p>
	 * Title: userLogin
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 */
	@Override
	public LianjiuResult allianceUserLogin(String username, String password, String token) {
		AllianceBusiness alliance = allianceBusinessMapper.selectAllByabusName(username);
		// 存在，判断用户名是否与密码相匹配
		if (null == alliance) {
			loggerAlliance.info("加盟商登陆error allianceBusiness为null");
			return LianjiuResult.build(501, "您还未申请加盟商，或者申请还未通过");
		}
		// 对填写的密码进行MD5加密
		String pwd = MD5Util.md5(password);
		if (!pwd.equals(alliance.getAllianceBusinessPassword())) {
			loggerAlliance.info("密码错误");
			return LianjiuResult.build(502, "密码错误");
		}
		// 登陆成功
		alliance.setAllianceBusinessUpdated(new Date());
		alliance.setAllianceBusinessName(username);
		alliance.setAllianceServiceToken(token);
		Util.printModelDetails(alliance);
		allianceBusinessMapper.updateLogined(alliance);
		alliance.setAllianceServiceToken(null);
		String allianceId = alliance.getAllianceBusinessId();
		jedisClient.set(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId, JsonUtils.objectToJson(alliance));
		// 保存用户之前，把用户对象中的密码清空。
		alliance.setAllianceBusinessPassword(null);
		jedisClient.expire(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId, SSO_SESSION_EXPIRE);
		return LianjiuResult.ok(alliance);
	}

	@Override
	public LianjiuResult getAllianceUserByToken(String token) {
		/*
		 * // 根据token从redis中查询用户信息 String json =
		 * jedisClient.get(REDIS_ALLINCE_SESSION_KEY + ":" + token); // 判断是否为空
		 * if (StringUtils.isBlank(json)) { return LianjiuResult.build(400,
		 * "此session已经过期，请重新登录"); } // 更新过期时间
		 * jedisClient.expire(REDIS_ALLINCE_SESSION_KEY + ":" + token,
		 * SSO_SESSION_EXPIRE); // 返回用户信息 return
		 * LianjiuResult.ok(JsonUtils.jsonToPojo(json, AllianceBusiness.class));
		 */
		return null;
	}

	/**
	 * 修改手机短信验证码不存cookie（新手机）
	 */
	@Override
	public LianjiuResult sendSmsAlliance(String allianceId, String phone) {
		loggerAlliance.info("开始获取短信验证码");
		/*
		 * // 去redis中取出用户信息，转换成 AllianceBusiness 对象 String loginRedis =
		 * jedisClient.get(REDIS_ALLINCE_SESSION_KEY + ":" +
		 * GlobalValueUtil.loginMessageToken); if (Util.isEmpty(loginRedis)) {
		 * return LianjiuResult.build(400, "用户登录信息不存在，请重新登陆"); }
		 */
		// json转对象
		String oPhone = allianceBusinessMapper.getPhoneById(allianceId);
		if (phone.equals(oPhone)) {
			loggerAlliance.info("新手机号码与原手机号码一致了");
			return LianjiuResult.build(501, "新手机号码不能与原手机号码一致");
		}
		// 判断新手机号码是否存在数据库
		List<String> listPhone = allianceBusinessMapper.getAllPhone();
		if (null == listPhone || 0 == listPhone.size()) {
			loggerAlliance.info("数据库中电话号码为空");
			return LianjiuResult.build(502, "电话号码为null");
		}
		loggerAlliance.info("listPhone" + listPhone);
		for (String lPhone : listPhone) {
			if (lPhone.equals(phone)) {
				loggerAlliance.info("新手机号码已存在");
				return LianjiuResult.build(503, "您输的手机号码已存在");
			}
		}
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("4", "+86", phone, code);
		// 把验证码、电话号码写入redis
		jedisClient.set(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId, code);
		jedisClient.expire(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId, 300);
		return LianjiuResult.ok("发送成功");
	}

	/**
	 * 获取短信验证码,校验手机号码是否是当前用户号码
	 */
	@Override
	public LianjiuResult sendSms(String allianceId, String phone) {
		loggerAlliance.info("开始获取短信验证码,校验手机号码是否是当前用户号码");
		/*
		 * loggerAlliance.info(GlobalValueUtil.loginMessageToken); //
		 * 去redis中取出当前用户登陆信息 String loginMessage =
		 * jedisClient.get(REDIS_ALLINCE_SESSION_KEY + ":" +
		 * GlobalValueUtil.loginMessageToken); if (Util.isEmpty(loginMessage)) {
		 * loggerAlliance.info("用户已掉线"); return LianjiuResult.build(500,
		 * "用户已掉线"); } loggerAlliance.info("loginMessage=" + loginMessage);
		 */
		// json转对象
		AllianceBusiness allianceBusiness = allianceBusinessMapper.selectAllianceByUserId(allianceId);
		if (null == allianceBusiness) {
			loggerAlliance.info("通过加盟商编号查加盟商信息报错，查出对象为空");
			return LianjiuResult.build(501, "error allianceBusiness为null");
		}
		Util.printModelDetails(allianceBusiness);
		String oPhone = allianceBusiness.getAllianceBusinessPhone();
		loggerAlliance.info("用户输入的手机号码：" + phone);
		loggerAlliance.info("当前手机号码" + oPhone);
		if (phone.equals(oPhone)) {
			// 获取随机短信验证码
			String code = IDUtils.genPWDId();
			// 给用户发送随机验证码
			sendSMS.sendMessage("9", "+86", phone, code);
			jedisClient.set(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId, code);
			jedisClient.expire(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId, 300);
			return LianjiuResult.ok("发送成功");
		} else {
			loggerAlliance.info("电话号码与当前用户电话号码不匹配");
			return LianjiuResult.build(502, "电话号码与当前用户电话号码不匹配");
		}
	}

	/**
	 * 校验验证码
	 */
	@Override
	public LianjiuResult checkAlliancePhone(String phone, String code) {
		String allianceId = allianceBusinessMapper.getIdByPhone(phone);
		if (Util.isEmpty(allianceId)) {
			loggerAlliance.info("该电话号码错误，提示验证码不正确");
			return LianjiuResult.build(501, "验证码不正确");
		}
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		loggerAlliance.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerAlliance.info("验证码超时，请重新登陆");
			return LianjiuResult.build(502, "验证码超时，请重新获取");
		}
		// 去redis中取出验证码信息，匹配code不正确返回502
		String aCode = jedisClient.get(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		// 验证码为空或者验证码不对
		if (Util.isEmpty(aCode) || !code.equals(aCode)) {
			loggerAlliance.info("验证码不正确");
			return LianjiuResult.build(501, "验证码不正确");
		}
		// 取完后立即清掉
		jedisClient.del(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		// 校验无误返回 状态码200
		loggerAlliance.info("校验成功");
		return LianjiuResult.build(200, "校验成功");
	}

	/**
	 * 校验新手机号码验证码，更新用户电话号码
	 */
	@Override
	public LianjiuResult updateAlliancePhone(String allianceId, String phone, String code) {
		loggerAlliance.info("开始验证修改");
		// 去redis中取出验证码信息，匹配code不正确返回502
		String aCode = jedisClient.get(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		if (Util.isEmpty(aCode)) {
			loggerAlliance.info("查不到缓存信息，验证码不正确");
			return LianjiuResult.build(501, "验证码不正确");
		}
		if (!code.equals(aCode)) {
			loggerAlliance.info("输入的验证码跟缓存中取出的验证码不一致");
			return LianjiuResult.build(502, "验证码不正确");
		}
		jedisClient.del(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		AllianceBusiness alliance = allianceBusinessMapper.selectAllianceByUserId(allianceId);
		if (null == alliance) {
			loggerAlliance.info("通过传入的加盟商编号查加盟商对象为空");
			return LianjiuResult.build(503, "error alliance为null");
		}
		Util.printModelDetails(alliance);
		String username = alliance.getAllianceBusinessName();
		AllianceBusiness allianceBusiness = allianceBusinessMapper.selectAllByabusName(username);
		if (null == allianceBusiness) {
			loggerAlliance.info("通过加盟商用户名查加盟商对象为空");
			return LianjiuResult.build(504, "error allianceBusiness为null");
		}
		// 修改手机号
		allianceBusiness.setAllianceBusinessPhone(phone);
		allianceBusiness.setAllianceBusinessName(phone);
		allianceBusiness.setAllianceBusinessId(allianceId);
		allianceBusiness.setAllianceBusinessUpdated(new Date());
		// 去数据库更新
		allianceBusinessMapper.updateAlliance(allianceBusiness);
		allianceBusiness.setAllianceBusinessPassword(null);
		AllianceBusinessDetails allianceBusinessDetails = new AllianceBusinessDetails();
		allianceBusinessDetails.setAbunesId(allianceId);
		allianceBusinessDetails.setAbunesPhone(phone);
		loggerAlliance.info("开始更新加盟商信息表");
		detailsMapper.updatePhone(allianceBusinessDetails);
		jedisClient.set(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId,
				JsonUtils.objectToJson(allianceBusiness));
		jedisClient.expire(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId, SSO_SESSION_EXPIRE);
		loggerAlliance.info("更新电话号码成功");
		return LianjiuResult.ok(allianceBusiness);
	}

	/**
	 * 修改密码获取验证吗
	 */
	@Override
	public LianjiuResult sendSMSPwd(String allianceId) {
		String phone = allianceBusinessMapper.getPhoneById(allianceId);
		if (Util.isEmpty(phone)) {
			loggerAlliance.info("通过传入的加盟商编号查询加盟商手机号码失败");
			return LianjiuResult.build(501, "手机号码获取失败");
		}
		String code = IDUtils.genPWDId();
		sendSMS.sendMessage("14", "+86", phone, code);
		// 把验证码写入redis
		jedisClient.set("REDIS_SMS_SESSION_KEY_" + phone, code);
		// 设置session的过期时间
		jedisClient.expire("REDIS_SMS_SESSION_KEY_" + phone, 300);

		jedisClient.set(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId, code);
		jedisClient.expire(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId, 300);
		return LianjiuResult.ok("发送成功");
	}

	/**
	 * 更改密码
	 */
	@Override
	public LianjiuResult updateAlliancePassword(String allianceId, String password, String code) {
		// 判断redis中的验证码是过期，过期提示验证码超时
		Long time = jedisClient.ttl(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		loggerAlliance.info("验证码剩余时间" + time);
		if (0 >= time) {
			// System.out.println("验证码超时，请重新登陆");
			loggerAlliance.info("验证码超时，请重新登陆");
			return LianjiuResult.build(501, "验证码超时，请重新获取");
		}
		// 去redis中取出验证码信息，匹配code不正确返回502
		String codeRedis = jedisClient.get(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		if (Util.isEmpty(codeRedis)) {
			loggerAlliance.info("缓存中没有验证码");
			return LianjiuResult.build(501, "验证码不正确");
		}
		if (!code.equals(codeRedis)) {
			loggerAlliance.info("验证码匹配错误");
			return LianjiuResult.build(502, "验证码不正确");
		}
		jedisClient.del(GlobalValueJedis.REDIS_ALLIANCE_SMS_SESSION_KEY + allianceId);
		AllianceBusiness allianceBusiness = allianceBusinessMapper.selectAllianceByUserId(allianceId);
		if (null == allianceBusiness) {
			loggerAlliance.info("加盟商对象为空");
			return LianjiuResult.build(504, "error allianceBusiness为null");
		}
		String username = allianceBusiness.getAllianceBusinessName();
		AllianceBusiness alliance = allianceBusinessMapper.selectAllByabusName(username);
		if (null == alliance) {
			loggerAlliance.info("通过加盟商用户名查加盟商对象为空");
			return LianjiuResult.build(504, "error alliance为null");
		}
		String oPassword = alliance.getAllianceBusinessPassword();
		String nPassword = MD5Util.md5(password);
		loggerAlliance.info("oPassword" + oPassword);
		loggerAlliance.info("nPassword" + nPassword);
		if (oPassword.equals(nPassword)) {
			loggerAlliance.info("密码与原密码一致");
			return LianjiuResult.build(503, "密码不能与原密码一样");
		} else {
			// 修改密码，加密后
			allianceBusiness.setAllianceBusinessPassword(MD5Util.md5(password));
			// 去数据库更新
			allianceBusiness.setAllianceBusinessUpdated(new Date());
			int rowsAffected = allianceBusinessMapper.updateByPrimaryKeySelective(allianceBusiness);
			/*
			 * // 更新redis的值 jedisClient.set(REDIS_ALLINCE_SESSION_KEY + ":" +
			 * GlobalValueUtil.loginMessageToken,
			 * JsonUtils.objectToJson(allianceBusiness));
			 * jedisClient.expire(REDIS_ALLINCE_SESSION_KEY + ":" +
			 * GlobalValueUtil.loginMessageToken, SSO_SESSION_EXPIRE);
			 */
			return LianjiuResult.ok(rowsAffected);
		}
	}

	@Override
	public LianjiuResult allianceLogin(String username, String password) {
		// 假设已经查询到该加盟商用户
		AllianceBusiness alliance = new AllianceBusiness();
		alliance.setAllianceBusinessName("张三");
		alliance.setAllianceBusinessPhone("123456789010");
		return LianjiuResult.ok(alliance);
	}

	// 退出登陆
	@Override
	public LianjiuResult Logout(String allianceId) {
		// 退出登陆，清除加盟商缓存信息
		String oLoginJson = jedisClient.get(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId);
		loggerAlliance.info("当前登陆信息：" + oLoginJson);
		jedisClient.del(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId);
		String loginJson = jedisClient.get(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId);
		loggerAlliance.info("退出登陆后的信息：" + loginJson);
		return LianjiuResult.build(200, "退出成功");
	}

}
