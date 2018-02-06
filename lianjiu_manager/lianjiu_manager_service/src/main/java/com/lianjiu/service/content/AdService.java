package com.lianjiu.service.content;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AdCarousel;
import com.lianjiu.model.AdElectronic;
import com.lianjiu.model.AdEssenceBrand;
import com.lianjiu.model.AdEssenceHot;
import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.AdSecond;
import com.lianjiu.model.AdTheme;
import com.lianjiu.model.AdThird;

public interface AdService {

	LianjiuResult insertCarousel(AdCarousel record);

	LianjiuResult deleteCararouse(String caId);

	LianjiuResult updateByPrimaryKeySelective(AdCarousel record);

	LianjiuResult selectByPrimaryKey(String caId);

	LianjiuResult selectCararouseList(int pageNum, int pageSize);

	LianjiuResult insertElectronic(AdElectronic record);

	LianjiuResult deleteElectronic(String eleId);

	LianjiuResult updateElectronic(AdElectronic record);

	LianjiuResult selectElectronicById(String eleId);

	LianjiuResult selectElectronicList(int pageNum, int pageSize);

	LianjiuResult insertHotProduct(AdHotProduct record);

	LianjiuResult deleteHotProduct(String hotId);

	LianjiuResult updateHotProduct(AdHotProduct record);

	LianjiuResult selectHotProductById(String hotId);

	LianjiuResult selectHotProductList(int pageNum, int pageSize);


	LianjiuResult insertAdSecond(AdSecond record);

	LianjiuResult deleteAdSecond(String secId);

	LianjiuResult updateAdSecond(AdSecond record);

	LianjiuResult selectAdSecondById(String secId);

	LianjiuResult selectAdSecondList(int pageNum, int pageSize);

	LianjiuResult insertAdTheme(AdTheme record);

	LianjiuResult deleteAdTheme(String theId);

	LianjiuResult updateAdTheme(AdTheme record);

	LianjiuResult selectAdThemeById(String theId);

	LianjiuResult selectAdThemeList(int pageNum, int pageSize);

	LianjiuResult insertAdThird(AdThird record);

	LianjiuResult deleteAdThird(String thiId);

	LianjiuResult updateAdThird(AdThird record);

	LianjiuResult selectAdThirdList(int pageNum, int pageSize);

	LianjiuResult selectAdThirdById(String thiId);

	LianjiuResult insertAdEssenceHot(AdEssenceHot record);

	LianjiuResult deleteAdEssenceHot(String essId);

	LianjiuResult updateAdEssenceHot(AdEssenceHot record);

	LianjiuResult selectAdEssenceHotById(String essId);

	LianjiuResult selectAdEssenceHotList(int pageNum, int pageSize);

	LianjiuResult insertAdEssenceBrand(AdEssenceBrand record);

	LianjiuResult deleteAdEssenceBrand(String brId);

	LianjiuResult updateAdEssenceBrand(AdEssenceBrand record);

	LianjiuResult selectAdEssenceBrandById(String brId);

	LianjiuResult selectAdEssenceBrandList(int pageNum, int pageSize);
	
	
}
