package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.Power;
import com.lianjiu.model.Role;
import com.lianjiu.model.User;
import com.lianjiu.model.vo.SearchObjecVo;

public interface RoleMapper {

    int deleteByPrimaryKey(String roleId);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

	List<RoleMapper> selectBySearchObjecVo(SearchObjecVo vo);
	
	List<Power> getRoleByPowerId(String roleId);

	List<User> getRoleByUserId(String roleId);

}