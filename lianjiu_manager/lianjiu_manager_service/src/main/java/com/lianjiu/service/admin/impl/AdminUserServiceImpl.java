package com.lianjiu.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Admin;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.user.AdminMapper;
import com.lianjiu.service.admin.AdminUserService;

@Service
public class AdminUserServiceImpl implements AdminUserService {
	@Autowired
	private AdminMapper adminMapper;

	@Override
	public LianjiuResult selectAllAdminUser(int pageNum, int pageSize) {
		SearchObjecVo vo = new SearchObjecVo();
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 去数据查询
		List<Admin> adminUsers = adminMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Admin> listAdminUsers = (Page<Admin>) adminUsers;
		System.out.println("总页数：" + listAdminUsers.getPages());
		// 封装返回
		LianjiuResult result = new LianjiuResult(adminUsers);
		return result;
	}

	/*@Override
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
	}*/

	@Override
	public Admin getAdminUserByToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
