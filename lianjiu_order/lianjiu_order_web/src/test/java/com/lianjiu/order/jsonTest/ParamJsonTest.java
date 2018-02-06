package com.lianjiu.order.jsonTest;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Product;
import com.lianjiu.rest.mapper.product.ProductMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ParamJsonTest {

	@SuppressWarnings("resource")
	@Test
	public void jsonAnalysis() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		ProductMapper productMapper = (ProductMapper) applicationContext.getBean("productMapper");
		// 取出产品
		Product p = productMapper.selectByPrimaryKey("1505987628494-27e8ed15-2f99-4aaa-8c83-e14166b33536");
		// 取出json数据
		String productParamData = p.getProductParamData();
		System.err.println(productParamData);
		// 筛选出具体的参数
		String price = null;
		JSONArray paramDataArray1 = JSONArray.fromObject(productParamData);// json串转为json数组
		for (Object object1 : paramDataArray1) {
			JSONArray paramDataArray2 = JSONArray.fromObject(object1);// json串转为json数组
			for (Object object2 : paramDataArray2) {
				JSONArray paramDataArray3 = JSONArray.fromObject(object2);// json串转为json数组
				for (Object children1 : paramDataArray3) {
					JSONObject majorDataJsonObject1 = JSONObject.fromObject(children1);// json串转为json对象
					price = (String) majorDataJsonObject1.get("price");
					if (Util.isEmpty(price)) {
						continue;
					}
					JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
					for (Object children : children2) {
						JSONObject majorDataJsonObject = JSONObject.fromObject(children);// json串转为json对象
						price = (String) majorDataJsonObject.get("price");
						System.out.println(price);
						if (Util.isEmpty(price)) {
							continue;
						}
					}
				}
			}
		}
	}
}
