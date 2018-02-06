package com.lianjiu.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.pojo.ProductPriceGroup;
import com.lianjiu.model.Product;

public interface ProductService {

	LianjiuResult selectProductByPid(long cId, int pageNum, int pageSize);

	LianjiuResult addProduct(Product product);
	
	//从分类表中取出参数模板
	LianjiuResult getParamTemplateFromCategory(String cid);

	LianjiuResult updateProduct(Product product);

	LianjiuResult deleteProductByid(String id);

	LianjiuResult selectProductByid(String productId);

	LianjiuResult addPriceGroup(ProductPriceGroup priceGroup);

	LianjiuResult updatePriceGroup(ProductPriceGroup priceGroup);

	LianjiuResult deletePriceGroupById(String priceId);

	LianjiuResult getPriceGroupByPid(String productId);

	LianjiuResult getPriceGroupById(String priceId);

}
