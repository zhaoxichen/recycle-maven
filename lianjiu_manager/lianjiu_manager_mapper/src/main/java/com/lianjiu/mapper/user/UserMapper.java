package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.Power;
import com.lianjiu.model.Role;
import com.lianjiu.model.User;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.model.vo.UserCustom;

public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);

	List<User> selectBySearchObjecVo(SearchObjecVo vo);

	void insertUserDetails(UserDetails userDetails);

	void insertUser(User user);

	void updateUser(User user);

	void updateUserDetails(UserDetails userDetails);

	void delete(User user);

	List<Power> findByPower(String userId);

	List<Role> getByRoleId(String userId);

	List<UserCustom> selectAllUser();

	UserDetails selectByDetailsId(String userId);
}