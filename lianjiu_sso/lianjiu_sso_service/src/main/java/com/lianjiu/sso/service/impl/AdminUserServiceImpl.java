package com.lianjiu.sso.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.CookieUtils;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.model.Admin;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.user.AdminMapper;
import com.lianjiu.sso.service.AdminUserService;

@Service
public class AdminUserServiceImpl implements AdminUserService {

	public AdminUserServiceImpl() {

	}

	@Autowired
	private AdminMapper adminMapper;

	@Value("${REDIS_ADMIN_SESSION_KEY}")
	private String REDIS_ADMIN_SESSION_KEY;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;
	@Autowired
	private JedisClient jedisClient;

	@Override
	public LianjiuResult checkData(String content, Integer type) {
		Admin adminUser = new Admin();
		// 如果content为空则提示用户名或者密码错误
		if (null == content) {
			return LianjiuResult.ok(false);
		}
		// 判断是否是11位数的电话号码
		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(content);
		System.out.println(m.matches());
		if (m.matches() == true) {
			adminUser.setAdminPhone(content);
		}
		// 否则字母加数字校验（用户名）
		else {
			adminUser.setAdminUsername(content);
		}

		adminUser.setAdminId(IDUtils.genGUIDs());
		// 执行查询，查询注册填写的信息是否存在数据库
		SearchObjecVo vo = new SearchObjecVo(adminUser);
		List<Admin> list = adminMapper.selectBySearchObjecVo(vo);
		if (list == null || list.size() == 0) {
			return LianjiuResult.ok(true);
		}
		return LianjiuResult.ok(false);
	}

	@Override
	public LianjiuResult adminUserLogin(Admin admin, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("开始登陆");

		Admin adminUser = new Admin();
		// 判断用户名是否存在
		adminUser.setAdminUsername(admin.getAdminUsername());
		SearchObjecVo vo = new SearchObjecVo(adminUser);
		List<Admin> list = adminMapper.selectBySearchObjecVo(vo);
		// 存在，判断用户名是否与密码相匹配
		if (list != null && list.size() != 0) {
			// 对填写的密码进行MD5加密
			String pwd = DigestUtils.md5DigestAsHex(admin.getAdminPassword().getBytes());
			boolean isPwdErroFlag = true;
			// 如果密码跟数据库匹配跳出循环
			for (Admin listAdminUser : list) {
				if (listAdminUser.getAdminPassword().equals(pwd)) {
					isPwdErroFlag = false;
					// 生成token
					String token = UUID.randomUUID().toString();
					System.out.println("Admintaken:" + token);
					// 保存用户之前，把用户对象中的密码清空。
					listAdminUser.setAdminPassword(null);
					// 把用户信息写入redis
					jedisClient.set(REDIS_ADMIN_SESSION_KEY + ":" + token, JsonUtils.objectToJson(list));
					// 设置session的过期时间
					jedisClient.expire(REDIS_ADMIN_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);

					// 添加写cookie的逻辑，cookie的有效期是关闭浏览器就失效。
					CookieUtils.setCookie(request, response, "TT_TOKEN", token);
					return LianjiuResult.ok(token);
				}
				else if (isPwdErroFlag) {
					return LianjiuResult.build(400, "密码错误");
			}
			}
		}
		// 不存在
		else if (list == null || list.size() == 0) {
			return LianjiuResult.build(400, "用户名不存在");
		}
		// 返回token
		return null;
	}

	@Override
	public LianjiuResult getAdminUserByToken(String token) {
		// 根据token从redis中查询用户信息
		String json = jedisClient.get(REDIS_ADMIN_SESSION_KEY + ":" + token);
		// 判断是否为空
		if (StringUtils.isBlank(json)) {
			return LianjiuResult.build(400, "此session已经过期，请重新登录");
		}
		// 更新过期时间
		jedisClient.expire(REDIS_ADMIN_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		// 返回用户信息
		return LianjiuResult.ok(JsonUtils.jsonToList(json, Admin.class));
	}

}
