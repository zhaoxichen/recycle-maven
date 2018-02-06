package com.lianjiu.rest.mapper.user;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.Power;
import com.lianjiu.model.Role;
import com.lianjiu.model.User;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.vo.UserCustom;

public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);

	void insertUserDetails(UserDetails userDetails);

	void insertUser(User user);

	void updateUser(User user);

	void updateUserDetails(UserDetails userDetails);

	void delete(User user);

	List<Power> findByPower(String userId);

	List<Role> getByRoleId(String userId);
	
	int selectAllByUsernameCount(String username);

	void updateLogined(User userLogined);
	
	String selectNickNameByUserId (String userId);

	int modifyPwd(@Param(value = "userId")String userId, @Param(value = "password") String password);

	int selectCountByphone(String phone);

	int updatePwd( @Param(value = "phone") String phone, @Param(value = "password") String password);

	User selectUserByphone(String phone);

	User selectUserByUserId(String userId);

	List<OrdersExpress> selectExpressByUserId(String userId);

	String getPhoneById(String userId);
	
	int relativePrice(@Param(value = "userId") String userId, @Param(value = "price") String price,@Param(value = "date") Date date);

	int getUserMoneyCheck(@Param(value = "userId") String userId, @Param(value = "price") String price);
	
	String getDetailsId(String userId);

	int updateUserPhone(User users);

	String getPwdByPhone(String oPhone);

	List<String> getAllPhone();

	User getByQqNum(@Param(value = "qqNum") String num);

	User getByWechatNum(@Param(value = "wechatNum") String num);

	String getQphoneByNum(@Param(value = "qqNum") String num);

	String getWphoneByNum(@Param(value = "wechatNum") String num);

	int modifyPhone(String userId, String phone);

	int modifyNickById(User aUser);

	String getUserPrice(String userId);

	String getIdByPhone(String phone);

	List<UserCustom> selectAllUser();

	UserDetails selectByDetailsId(String detailsId);

	List<UserCustom> vagueQuery(@Param(value = "user") User user,@Param(value = "cratedStart") String createdStart,@Param(value = "cratedOver") String cratedOver);

	User selectAllByUsername(@Param(value="username") String username);

	List<User> getUserByReferrer(@Param(value="udetailsReferrer") String referrer,@Param(value="startTime") String startTime,@Param(value="endTime") String endTime);

}