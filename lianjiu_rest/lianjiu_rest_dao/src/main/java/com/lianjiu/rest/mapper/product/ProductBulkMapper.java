package com.lianjiu.rest.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.ProductBulk;
import com.lianjiu.rest.model.BulkVo;

public interface ProductBulkMapper {
	int deleteByPrimaryKey(String bulkId);

	int insert(ProductBulk record);

	ProductBulk selectByPrimaryKey(String bulkId);

	int updateByPrimaryKeySelective(ProductBulk record);

	List<ProductBulk> getBulkByCid(Long cid);

	List<ProductBulk> getAll();

	List<BulkVo> selectBulkByKeyword(String keyword);

	List<BulkVo> selectBulkAll();

	List<BulkVo> selectOthersByKeyword(String keyword);

	List<BulkVo> selectBulkByCid(@Param("cid") Long categoryId);

	List<BulkVo> selectBulkByCidByKeyword(@Param("cid") Long categoryId, @Param("keyword") String keyword);

	List<BulkVo> selectOthersByCidByKeyword(@Param("cid") Long categoryId, @Param("keyword") String keyword);

	String getDetailedAddress(Long cid);

	int addressCheck(Long cid);

	int modifyAddress(@Param("cid") Long cid, @Param("address") String address);

	int insertAddress(@Param("id") String id, @Param("cid") Long cid, @Param("address") String address);

	long getCidByPid(@Param("bulkId") String pid);

}