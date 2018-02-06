package com.lianjiu.sso.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.RecyclingWarehouseman;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.user.RecyclingWarehousemanMapper;
import com.lianjiu.sso.service.WarehousemanService;

@Service
public class WarehousemanServiceImpl implements WarehousemanService {
	@Autowired
	RecyclingWarehousemanMapper warehousemanMapper;

	private static Logger loggerWarehouseman = Logger.getLogger("warehouseman");
	@Autowired
	private JedisClient jedisClient;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	@Override
	public LianjiuResult userLogin(String username, String userPassword) {
		loggerWarehouseman.info("开始登陆");
		RecyclingWarehouseman warehouseman = warehousemanMapper.getWarehousemanByPhone(username);
		if (null == warehouseman || "".equals(warehouseman)) {
			loggerWarehouseman.info("用户对象为空");
			return LianjiuResult.build(501, "用户名不存在");
		}
		String password = MD5Util.md5(userPassword);
		String pwd = warehouseman.getRecyclerPassword();
		if (!password.equals(pwd)) {
			loggerWarehouseman.info("密码不正确");
			return LianjiuResult.build(501, "密码不正确");
		}
		try {
			int count = warehousemanMapper.updateByPrimaryKeySelective(warehouseman);
			if (0 == count) {
				loggerWarehouseman.info("更新用户信息导致登陆失败");
				LianjiuResult.build(502, "登陆失败");
			}
			// 把用户信息写入redis
			jedisClient.set(GlobalValueJedis.REDIS_SEMAN_SNSESSION_KEY + warehouseman.getRecyclerId(),
					JsonUtils.objectToJson(warehouseman));
			// 设置session的过期时间
			jedisClient.expire(GlobalValueJedis.REDIS_SEMAN_SNSESSION_KEY + warehouseman.getRecyclerId(),
					SSO_SESSION_EXPIRE);

			loggerWarehouseman.info("登陆成功");
			warehouseman.setRecyclerPassword(null);
			return LianjiuResult.ok(warehouseman);
		} catch (RuntimeException e) {
			loggerWarehouseman.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(503, "数据异常");
		}
	}

	/**
	 * 修改密码
	 */
	@Override
	public LianjiuResult modifyPwd(String recyclerId, String oldPassword, String newPassword) {
		loggerWarehouseman.info("开始修改密码");
		// 查询旧密码是否正确
		String pwd = MD5Util.md5(oldPassword); // 加密后的旧密码
		// 查询旧密码
		String oPwd = warehousemanMapper.getPwdById(recyclerId);
		if (Util.isEmpty(oPwd)) {
			loggerWarehouseman.info("查询旧密码失败");
			return LianjiuResult.build(501, "修改密码失败");
		}
		if (!oPwd.equals(pwd)) {
			loggerWarehouseman.info("原密码输入错误");
			return LianjiuResult.build(502, "原密码输入错误");
		}
		// 新密码是否与旧密码一致
		if (oldPassword.equals(newPassword)) {
			loggerWarehouseman.info("新密码与旧密码一致");
			return LianjiuResult.build(503, "新密码不能与旧密码一致");
		}
		// 更新密码
		try {
			String nPwd = MD5Util.md5(newPassword); // 加密后的旧密码
			int count = warehousemanMapper.modifyPwd(recyclerId, nPwd);
			if (0 >= count) {
				loggerWarehouseman.info("更新失败");
				return LianjiuResult.build(504, "修改密码失败");
			}
			return LianjiuResult.ok(count);
		} catch (RuntimeException e) {
			loggerWarehouseman.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(505, "数据异常！");
		}
	}
	/**
	 * 退出登陆
	 */
	@Override
	public LianjiuResult loginOut(String recyclerId) {
		loggerWarehouseman.info("退出登陆");
		String oLoginJson = jedisClient.get(GlobalValueJedis.REDIS_SEMAN_SNSESSION_KEY + recyclerId);
		loggerWarehouseman.info("当前缓存信息：" + oLoginJson);
		jedisClient.del(GlobalValueJedis.REDIS_SEMAN_SNSESSION_KEY + recyclerId);
		String loginJson = jedisClient.get(GlobalValueJedis.REDIS_SEMAN_SNSESSION_KEY + recyclerId);
		loggerWarehouseman.info("退出后的登陆信息" + loginJson);
		return LianjiuResult.build(200, "退出登陆成功");
	}

}
