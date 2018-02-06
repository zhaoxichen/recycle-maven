package com.lianjiu.service.admin;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Admin;

public interface AdminUserService {

	LianjiuResult selectAllAdminUser(int pageNum, int pageSize);

	Admin getAdminUserByToken(String token);

	//User getUserByToken(String token);

}
