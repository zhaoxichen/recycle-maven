package com.lianjiu.rest.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductRepair;
import com.lianjiu.rest.model.ProductInfo;
import com.lianjiu.rest.model.ProductParamInfo;

public interface ProductRepairMapper {

	ProductRepair selectByPrimaryKey(String repairId);

	List<ProductInfo> selectByCid(Long cid);
	
	ProductParamInfo selectParamById(String repairId);

	List<ProductRepair> selectBySearchObjecVo(Object object);

	int insert(ProductRepair repair);

	int updateByPrimaryKeySelective(ProductRepair repair);

	int deleteByPrimaryKey(String repairId);
}