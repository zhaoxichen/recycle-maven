package com.lianjiu.rest.mapper.product;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.ProductBulkAddress;

public interface ProductBulkAddressMapper {
	int deleteByPrimaryKey(String bulkAddressId);

	int insert(ProductBulkAddress record);

	ProductBulkAddress selectByPrimaryKey(String bulkAddressId);

	int updateByPrimaryKeySelective(ProductBulkAddress record);

	String getAddressNameByPid(String pid);

	String getAddressNameByCid(@Param(value = "categoryId") long cid);

}