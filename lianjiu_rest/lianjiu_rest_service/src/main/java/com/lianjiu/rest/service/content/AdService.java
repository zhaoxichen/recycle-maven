package com.lianjiu.rest.service.content;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AdCarousel;
import com.lianjiu.model.AdElectronic;
import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.AdHotTopic;
import com.lianjiu.model.AdSecond;
import com.lianjiu.model.AdTheme;
import com.lianjiu.model.AdThird;

public interface AdService {

	LianjiuResult selectByPrimaryKey(String caId);

	LianjiuResult selectCararouseList(int pageNum, int pageSize);


	LianjiuResult selectElectronicById(String eleId);

	LianjiuResult selectElectronicList(int pageNum, int pageSize);

	LianjiuResult selectHotProductById(String hotId);

	LianjiuResult selectHotProductList(int pageNum, int pageSize);



	LianjiuResult selectAdSecondById(String secId);

	LianjiuResult selectAdSecondList(int pageNum, int pageSize);


	LianjiuResult selectAdThemeById(String theId);

	LianjiuResult selectAdThemeList(int pageNum, int pageSize);


	LianjiuResult selectAdThirdList(int pageNum, int pageSize);

	LianjiuResult selectAdThirdById(String thiId);

	LianjiuResult selectHotProductFour();

	LianjiuResult selectCararouseFour();

	LianjiuResult selectAdEssenceHotFour();

	LianjiuResult selectCararouseFourType(int type);
	

	
	
}
