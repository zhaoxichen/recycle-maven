package com.lianjiu.rest.service;

import com.lianjiu.common.pojo.LianjiuResult;

public interface IndexService {

	LianjiuResult getIndexLianjiu(int type);

	LianjiuResult getIndexExcellent(int type);

	LianjiuResult getIndexHousehold();

	LianjiuResult getIndexElectronic();

	LianjiuResult getIndexWaste();

	LianjiuResult getIndexRepair();

	LianjiuResult getIndexFurniture();
	
	LianjiuResult getBrands();

	LianjiuResult getProductByBrand(String brand, int orderBy, int pageNum, int pageSize);

	LianjiuResult getIndexBulk();
	
	LianjiuResult getIndexBulkCategory(String TAKEN,Long categoryId);


}
