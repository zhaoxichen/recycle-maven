package com.lianjiu.service.user;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Recommend;
import com.lianjiu.model.Role;
import com.lianjiu.model.User;

public interface UserService {

	LianjiuResult selectAllUser(int pageNum, int pageSize);

	User getUserByToken(String token);

	LianjiuResult getUserByPowerId(String userId);

	LianjiuResult getByRoleId(String userId);

	LianjiuResult deleteUser(String userId);
	
	LianjiuResult updateRoleAllot(String userId, List<Role> roleList);

	LianjiuResult selectDetailsByUserId(String detailsId);

	LianjiuResult selectUserByUserId(String userId);

	LianjiuResult addRecommend(Recommend recommend);

	LianjiuResult vagueQuery(User user, int pageNum, int pageSize, String cratedStart, String cratedOver);

	LianjiuResult modifyRecommend(Recommend recommend);

	LianjiuResult searchRecommend(String parameter, int pageSize, int pageNum);

	LianjiuResult getUserByRecommend(String referrer, String startTime, String endTime, int pageNum, int pageSize);

	LianjiuResult getRecordByFaceface(String referrer, String startTime, String endTime, int categoryId,int pageNum, int pageSize);

	//LianjiuResult updateRoleAllot(String userId, String roleList);
}
