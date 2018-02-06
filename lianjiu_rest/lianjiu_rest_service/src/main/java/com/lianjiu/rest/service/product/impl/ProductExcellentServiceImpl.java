package com.lianjiu.rest.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AdEssenceBrand;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.ProductExcellent;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.content.AdEssenceBrandMapper;
import com.lianjiu.rest.mapper.product.ProductExcellentMapper;
import com.lianjiu.rest.model.CategoryInfo;
import com.lianjiu.rest.service.product.ProductExcellentService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 精品信息
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductExcellentServiceImpl implements ProductExcellentService {
	@Autowired
	private ProductExcellentMapper productExcellentMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	AdEssenceBrandMapper adEssenceBrandMapper;
	@Autowired
	JedisClient jedisClient;

	@Override
	public LianjiuResult getExcellentAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<ProductExcellent> excellents = productExcellentMapper.selectAll();
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductExcellent> listExcellents = (Page<ProductExcellent>) excellents;
		System.out.println("总页数：" + listExcellents.getPages());
		LianjiuResult result = new LianjiuResult(excellents);
		result.setTotal(listExcellents.getTotal());
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 查看精品详情
	 */
	@Override
	public LianjiuResult getExcellentById(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(501, "请指定精品的id");
		}
		// 去product_excellent查询精品
		ProductExcellent excellent = productExcellentMapper.selectByPrimaryKey(excellentId);
		if (null == excellent) {
			return LianjiuResult.build(502, "没有查到该精品");
		}
		// 存产品对象到redis
		OrdersItem excellentItem = new OrdersItem();
		String orItemsId = IDUtils.genOrdersId();
		excellentItem.setOrItemsId(orItemsId);
		excellentItem.setOrItemsName(excellent.getExcellentName());
		excellentItem.setOrItemsProductId(excellentId);
		excellentItem.setOrItemsPrice(excellent.getExcellentPrice());
		excellentItem.setOrItemsNum("1");
		excellentItem.setOrItemsPicture(excellent.getExcellentAttributePicture());
		excellentItem.setOrItemsParam(excellent.getExcellentParamData());
		jedisClient.set(excellentId + "excellentItem", JsonUtils.objectToJson(excellentItem));
		jedisClient.expire(excellentId + "excellentItem", 3600);
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(excellent);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 从内容管理的精品信息中获取品牌墙
	 */
	@Override
	public LianjiuResult getBrands() {
		// 精品的分类id 品牌墙
		List<AdEssenceBrand> list = adEssenceBrandMapper.findAll();
		return LianjiuResult.ok(list);
	}

	/**
	 * 获取当前分类下的子分类
	 */
	@Override
	public LianjiuResult getCategoryByName(String categoryName) {
		Long categoryParentId = categoryMapper.selectByCategoryName(categoryName);
		if (null == categoryParentId) {
			return LianjiuResult.build(501, "精品信息类目不存在");
		}
		List<CategoryInfo> categories = categoryMapper.selectCategoryInfoByPId(categoryParentId);
		if (null == categories || 0 == categories.size()) {
			return LianjiuResult.build(502, "精品信息类目下没有产品");
		}
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(categories);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 筛选出品牌
	 */
	@Override
	public LianjiuResult getBrandsByPid(List<Long> categoryPids) {
		if (null == categoryPids || 0 == categoryPids.size()) {
			return LianjiuResult.build(501, "传入要筛选产品的id");
		}
		List<CategoryInfo> categories = categoryMapper.selectCategoryInfoByPIds(categoryPids);
		if (null == categories || 0 == categories.size()) {
			return LianjiuResult.build(502, "产品类目下没有品牌信息");
		}
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(categories);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 高级查询
	 */
	@Override
	public LianjiuResult getProductByBrands(List<Long> categoryIds, List<String> brands, int orderBy, int pageNum,
			int pageSize) {
		if (null == brands || 0 == brands.size()) {
			return LianjiuResult.build(501, "请选择一个品牌");
		}
		List<Long> cIds = null;
		if (null == categoryIds || 0 == categoryIds.size()) {
			// 没有传入父分类id,只传入品牌
			cIds = categoryMapper.selectCidsByCategoryName(brands);
		} else {
			// 传入品牌和父分类id
			cIds = categoryMapper.selectParentIdByFilter(categoryIds, brands);
		}
		if (null == cIds || 0 == cIds.size()) {
			System.out.println(brands);
			return LianjiuResult.build(502, "产品类目下没有品牌信息");
		}
		PageHelper.startPage(pageNum, pageSize);
		// 去查产品
		List<ProductExcellent> productExcellents = productExcellentMapper.selectProductInfoByCategorys(cIds, orderBy);
		if (null == productExcellents || 0 == productExcellents.size()) {
			if (pageNum > 1) {
				return LianjiuResult.ok(new ArrayList<ProductExcellent>());
			}
			return LianjiuResult.build(503, "没有产品了");
		}
		for (ProductExcellent productExcellent : productExcellents) {
			String paramData = productExcellent.getExcellentParamData();
			productExcellent.setExcellentParamData(checkAdditionalFromParam(paramData));
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductExcellent> listProductInfo = (Page<ProductExcellent>) productExcellents;
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(productExcellents);
		result.setTotal(listProductInfo.getTotal());
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 根据品牌名称查询
	 */
	@Override
	public LianjiuResult getProductByBrand(String brand, int orderBy, int pageNum, int pageSize) {
		// 通过名称查分类id列表
		List<Long> cIds = categoryMapper.selectCidByCategoryName(brand);
		System.out.println(cIds);
		if (null == cIds || 0 == cIds.size()) {
			return LianjiuResult.build(502, "产品类目下没有品牌信息");
		}
		// 去查产品
		PageHelper.startPage(pageNum, pageSize);
		List<ProductExcellent> productExcellents = productExcellentMapper.selectProductInfoByCategorys(cIds, orderBy);
		if (null == productExcellents || 0 == productExcellents.size()) {
			if (pageNum > 1) {
				return LianjiuResult.ok(new ArrayList<ProductExcellent>());
			}
			return LianjiuResult.build(503, "没有产品了");
		}
		for (ProductExcellent productExcellent : productExcellents) {
			String paramData = productExcellent.getExcellentParamData();
			productExcellent.setExcellentParamData(checkAdditionalFromParam(paramData));
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductExcellent> listProductInfo = (Page<ProductExcellent>) productExcellents;
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(productExcellents);
		result.setTotal(listProductInfo.getTotal());
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	private String checkAdditionalFromParam(String orItemsParam) {
		String retrieveTypeData = null;
		StringBuffer additional = new StringBuffer();
		try {
			// 筛选出具体的参数
			JSONArray paramDataArray1 = JSONArray.fromObject(orItemsParam);// json串转为json数组
			for (Object object1 : paramDataArray1) {
				JSONArray paramDataArray2 = JSONArray.fromObject(object1);// json串转为json数组
				for (Object object2 : paramDataArray2) {
					JSONArray paramDataArray3 = JSONArray.fromObject(object2);// json串转为json数组
					for (Object children1 : paramDataArray3) {
						JSONObject majorDataJsonObject1 = JSONObject.fromObject(children1);// json串转为json对象
						JSONArray children2 = (JSONArray) majorDataJsonObject1.get("children"); // children为数组
						for (int i = 0; i < 3; i++) {
							JSONObject majorDataJsonObject = JSONObject.fromObject(children2.get(i));// json串转为json对象
							retrieveTypeData = (String) majorDataJsonObject.get("retrieveType");
							if (Util.notEmpty(retrieveTypeData)) {
								additional.append(retrieveTypeData + ",");
							}
						}
					}
				}
			}
		} catch (NullPointerException e) {
			return "精品json参数有问题";
		} catch (ClassCastException e) {
			return "精品json参数有问题";
		}
		return additional.substring(0, additional.length() - 1);
	}
}
