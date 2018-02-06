package com.lianjiu.rest.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.UserAddress;
import com.lianjiu.model.UserDetails;
import com.lianjiu.model.vo.SearchObjecVo;

public interface UserAddressMapper {

	int deleteByPrimaryKey(String userAddressId);

    int insert(UserAddress address);

    int insertSelective(UserAddress address);

    UserAddress selectByPrimaryKey(String userAddressId);

    int updateByPrimaryKeySelective(UserAddress address);

    int updateByPrimaryKey(UserAddress address);

	List<UserAddress> selectBySearchObjecVo(SearchObjecVo vo);

	List<UserAddress> selectAllByUser(String userId);

	UserAddress selectByAddressId(String userAddressId, String userId);

	void setDefaultFalse(String userId);

	int setDefaultTrue(String userAddressId);

	UserAddress selectDefault(String userId);

	UserAddress selectAddressByAddressId(String userAddressId);

	UserAddress getAddressDefultByUser(String userId);

	UserDetails selectCardById(String udetailsId);

	List<UserAddress> selectLongLati();

	UserAddress getAddressByFaceId(@Param("orderFaceId") String ordersId);

	int deleteById(String userAddressId);

	UserAddress getLoLaById(String addressId);

	int modifyDefultByUser(String userId);

	UserAddress getLoLaByOrdersId(String ordersId);

}