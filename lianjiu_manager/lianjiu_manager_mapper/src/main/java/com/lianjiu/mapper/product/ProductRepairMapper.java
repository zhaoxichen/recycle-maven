package com.lianjiu.mapper.product;

import java.util.List;

import com.lianjiu.model.ProductRepair;
import com.lianjiu.model.vo.SearchObjecVo;

public interface ProductRepairMapper {

	int deleteByPrimaryKey(String repairId);

	int insert(ProductRepair record);

	int insertSelective(ProductRepair record);

	ProductRepair selectByPrimaryKey(String repairId);

	int updateByPrimaryKeySelective(ProductRepair record);

	int updateByPrimaryKeyWithBLOBs(ProductRepair record);

	int updateByPrimaryKey(ProductRepair record);

	List<ProductRepair> selectBySearchObjecVo(SearchObjecVo vo);
}