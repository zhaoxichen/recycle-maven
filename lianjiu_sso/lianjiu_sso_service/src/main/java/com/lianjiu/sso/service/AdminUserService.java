package com.lianjiu.sso.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Admin;

public interface AdminUserService {

	LianjiuResult checkData(String content, Integer type);
	LianjiuResult getAdminUserByToken(String token);
	LianjiuResult adminUserLogin(Admin admin, HttpServletRequest request, HttpServletResponse response);
}
