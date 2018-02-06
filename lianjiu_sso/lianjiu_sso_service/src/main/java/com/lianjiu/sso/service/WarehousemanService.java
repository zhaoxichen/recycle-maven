package com.lianjiu.sso.service;

import com.lianjiu.common.pojo.LianjiuResult;

public interface WarehousemanService {

	LianjiuResult userLogin(String username, String userPassword);

	LianjiuResult modifyPwd(String recyclerId, String oldPassword, String newPassword);

	LianjiuResult loginOut(String recyclerId);

}
