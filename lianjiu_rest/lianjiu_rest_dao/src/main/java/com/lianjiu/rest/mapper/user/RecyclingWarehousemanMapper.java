package com.lianjiu.rest.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.RecyclingWarehouseman;

public interface RecyclingWarehousemanMapper {
    int deleteByPrimaryKey(String recyclerId);

    int insert(RecyclingWarehouseman recyclingWarehouseman);

    RecyclingWarehouseman selectByPrimaryKey(String recyclingWarehouseman);

    int updateByPrimaryKeySelective(RecyclingWarehouseman recyclingWarehouseman);

	String getIdByPhone(String recyclerPhone);

	List<RecyclingWarehouseman> getAll();

	String getIdByName(String recyclerName);

	int modifyIsExsit(String recyclerId);

	String getPwdById(String recyclerId);

	int modifyPwd(@Param(value = "recyclerId") String recyclerId, @Param(value = "recyclerPassword") String nPwd);

	RecyclingWarehouseman getWarehousemanByPhone(@Param(value = "recyclerPhone") String username);

	int getCountByOphone(String recyclerPhone);
}