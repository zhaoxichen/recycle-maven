package com.lianjiu.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.model.AdCarousel;
import com.lianjiu.model.AdElectronic;
import com.lianjiu.model.AdEssenceBrand;
import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.AdHotTopic;
import com.lianjiu.model.AdSecond;
import com.lianjiu.model.AdTheme;
import com.lianjiu.model.AdThird;
import com.lianjiu.model.Category;
import com.lianjiu.model.OrdersBulkItem;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.Product;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.rest.mapper.content.AdCarouselMapper;
import com.lianjiu.rest.mapper.content.AdElectronicMapper;
import com.lianjiu.rest.mapper.content.AdEssenceBrandMapper;
import com.lianjiu.rest.mapper.content.AdHotProductMapper;
import com.lianjiu.rest.mapper.content.AdHotTopicMapper;
import com.lianjiu.rest.mapper.content.AdSecondMapper;
import com.lianjiu.rest.mapper.content.AdThemeMapper;
import com.lianjiu.rest.mapper.content.AdThirdMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductBulkMapper;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.rest.service.IndexService;
import com.lianjiu.rest.tool.JedisTool;

@Service
public class IndexServiceImpl implements IndexService {
	@Autowired
	private AdCarouselMapper adCarouselMapper;
	@Autowired
	private AdHotTopicMapper adHotTopicMapper;
	@Autowired
	private AdHotProductMapper adHotProductMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	ProductMapper productMapper;
	@Autowired
	ProductBulkMapper productBulkMapper;
	@Autowired
	private AdElectronicMapper adElectronicMapper;
	@Autowired
	private AdSecondMapper adSecondMapper;
	@Autowired
	private AdThirdMapper adThirdMapper;
	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	private AdThemeMapper adThemeMapper;
	@Autowired
	private AdEssenceBrandMapper adEssenceBrandMapper;
	@Autowired
	private JedisClient jedisClient;

	private List<Long> categoryList = new ArrayList<Long>();

	private void getDescendants(Long categoryId) {
		List<Long> categoryIds = categoryMapper.selectCidsByPid(categoryId);
		// 边界条件
		if (null == categoryIds || categoryIds.size() == 0) {
			return;
		}
		categoryList.addAll(categoryIds);
		// 遍历
		for (Long cId : categoryIds) {
			// 递归
			getDescendants(cId);
		}
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:链旧首页,加入缓存
	 * 
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult getIndexLianjiu(int type) {
		Map<String, Object> results = new HashMap<>();
		// 首页广告图
		List<AdTheme> adTheme = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.INDEX_AD_THEME,
				AdTheme.class);
		if (null == adTheme) {
			adTheme = adThemeMapper.selectAdSecondList();
		}
		if (null == adTheme) {
			adTheme = new ArrayList<AdTheme>(0);
		} else {
			jedisClient.set(GlobalValueJedis.INDEX_AD_THEME, JsonUtils.objectToJson(adTheme));
		}
		results.put("adThemeList", adTheme);
		// 首页轮播图
		List<AdCarousel> adCarousel = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.INDEX_AD_CAROUSEL,
				AdCarousel.class);
		if (null == adCarousel) {
			adCarousel = adCarouselMapper.selectCararouseFourType(type);
		}
		if (null == adCarousel) {
			adCarousel = new ArrayList<AdCarousel>(0);
		} else {
			jedisClient.set(GlobalValueJedis.INDEX_AD_CAROUSEL, JsonUtils.objectToJson(adCarousel));
		}
		results.put("adCarousel", adCarousel);
		// 热卖产品
		List<AdHotProduct> adHotProduct = JedisTool.checkListFormRedis(jedisClient,
				GlobalValueJedis.INDEX_AD_HOT_PRODUCT, AdHotProduct.class);
		if (null == adHotProduct) {
			adHotProduct = adHotProductMapper.selectHotProductFour();
			if (null != adHotProduct) {
				jedisClient.set(GlobalValueJedis.INDEX_AD_HOT_PRODUCT, JsonUtils.objectToJson(adHotProduct));
			}
		}
		List<AdHotProduct> adHotProduct1 = new ArrayList<AdHotProduct>();
		List<AdHotProduct> adHotProduct2 = new ArrayList<AdHotProduct>();
		for (int i = 0; i <= 5; i++) {
			if (i <= 1) {
				adHotProduct1.add(adHotProduct.get(i));
			} else {
				adHotProduct2.add(adHotProduct.get(i));
			}
		}
		results.put("adHotProduct1", adHotProduct1);
		results.put("adHotProduct2", adHotProduct2);
		// 卖出商品轮播轴
		List<OrdersItem> ordersItem = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.INDEX_AD_EXPRESS_ITEM,
				OrdersItem.class);
		if (null == ordersItem) {
			ordersItem = ordersItemMapper.selectFrontPageMatter();
		}
		if (null == ordersItem) {
			ordersItem = new ArrayList<OrdersItem>(0);
		} else {
			jedisClient.set(GlobalValueJedis.INDEX_AD_EXPRESS_ITEM, JsonUtils.objectToJson(ordersItem));
		}
		results.put("ordersItem", ordersItem);
		// 热门话题
		List<AdHotTopic> adHotTopics = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.INDEX_AD_HOT_TOPICS,
				AdHotTopic.class);
		if (null == adHotTopics) {
			PageHelper.startPage(1, 10);
			adHotTopics = adHotTopicMapper.selectElectronicList();
		}
		if (null == adHotTopics) {
			adHotTopics = new ArrayList<AdHotTopic>(0);
		} else {
			jedisClient.set(GlobalValueJedis.INDEX_AD_HOT_TOPICS, JsonUtils.objectToJson(adHotTopics));
		}
		results.put("adHotTopics", adHotTopics);
		// 二层广告
		List<AdSecond> adSecond = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.INDEX_AD_SECOND,
				AdSecond.class);
		if (null == adSecond) {
			PageHelper.startPage(1, 2);
			adSecond = adSecondMapper.selectAdSecondList();
		}
		if (null == adSecond) {
			adSecond = new ArrayList<AdSecond>(0);
		} else {
			jedisClient.set(GlobalValueJedis.INDEX_AD_SECOND, JsonUtils.objectToJson(adSecond));
		}
		results.put("adSecond", adSecond);
		// 三层广告
		List<AdThird> adThird = JedisTool.checkListFormRedis(jedisClient, GlobalValueJedis.INDEX_AD_THIRD,
				AdThird.class);
		if (null == adThird) {
			PageHelper.startPage(1, 3);
			adThird = adThirdMapper.selectAdThirdList();
		}
		if (null == adThird) {
			adThird = new ArrayList<AdThird>(0);
		} else {
			jedisClient.set(GlobalValueJedis.INDEX_AD_THIRD, JsonUtils.objectToJson(adThird));
		}
		results.put("adThird", adThird);
		return LianjiuResult.ok(results);
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:买精品首页
	 * 
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult getIndexExcellent(int type) {
		Map<String, Object> results = new HashMap<>();
		// 首页轮播图
		List<AdCarousel> adCarousel = adCarouselMapper.selectCararouseFourType(type);
		results.put("adCarousel", adCarousel);
		// 热卖墙
		List<AdHotProduct> adHotProduct = adHotProductMapper.selectHotProductFour();
		results.put("adHotProduct", adHotProduct);
		// 精品的分类id 品牌墙
		Long idExcellent = categoryMapper.selectByCategoryName("优品信息");
		if (null == idExcellent) {
			return LianjiuResult.build(501, "没有查到优品信息");
		}
		List<Category> productBrands = categoryMapper.selectByParentId(idExcellent);
		results.put("brands", productBrands);
		return LianjiuResult.ok(results);
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:卖家电首页
	 * 
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult getIndexHousehold() {
		Map<String, Object> results = new HashMap<>();
		// 首页轮播图
		/*
		 * List<AdCarousel> adCarousel = adCarouselMapper.selectCararouseFour();
		 * results.put("adCarousel", adCarousel);
		 */
		// 首页广告图
		AdElectronic adElectronic = adElectronicMapper.getImageByOrderId("6");
		results.put("adElectronic", adElectronic);
		// 家电导航栏
		Long idHousehold = categoryMapper.selectByCategoryName("家电信息");
		if (null == idHousehold) {
			return LianjiuResult.build(501, "没有查到家电信息");
		}
		List<Category> childrenHouseholds = categoryMapper.selectByParentId(idHousehold);
		results.put("households", childrenHouseholds);
		// 家电品牌导航栏
		if (0 < childrenHouseholds.size()) {
			List<Category> householdBrands = categoryMapper.selectByParentId(childrenHouseholds.get(0).getCategoryId());
			results.put("brands", householdBrands);
		}
		return LianjiuResult.ok(results);
	}

	/**
	 * 
	 * xyz 2017年9月21日 descrption:电子产品信息首页
	 * 
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult getIndexElectronic() {
		Map<String, Object> results = new HashMap<>();
		// 首页轮播图
		// List<AdCarousel> adCarousel = adCarouselMapper.selectCararouseFour();
		// results.put("adCarousel", adCarousel);
		// 电子产品信息导航栏
		// 首页广告图 order:1
		AdElectronic adElectronic = adElectronicMapper.getImageByOrderId("1");
		results.put("adElectronic", adElectronic);
		Long idElectronic = categoryMapper.selectByCategoryName("电子产品信息");
		if (null == idElectronic) {
			return LianjiuResult.build(501, "没有查到电子产品信息");
		}
		List<Category> childrenElectronics = categoryMapper.selectByParentId(idElectronic);
		results.put("electronics", childrenElectronics);
		List<Category> electronicBrands = null;
		// 电子产品品牌导航栏
		if (0 < childrenElectronics.size()) {
			electronicBrands = categoryMapper.selectByParentId(childrenElectronics.get(0).getCategoryId());
			results.put("brands", electronicBrands);
		}
		// 手机热卖推荐,选出手机下的所有分类id
		// 先清空容器
		categoryList.clear();
		// 递归查询
		this.getDescendants((long) 40);
		if (null != electronicBrands && 0 < electronicBrands.size()) {
			PageHelper.startPage(1, 10);
			List<Product> product = productMapper.selectFrontPageHotProduct(categoryList);
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<Product> listTemplate = (Page<Product>) product;
			System.out.println("总页数：" + listTemplate.getPages());
			results.put("product", product);
		}
		return LianjiuResult.ok(results);
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:卖废品首页
	 * 
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult getIndexWaste() {
		Map<String, Object> results = new HashMap<>();
		// 废品信息导航栏
		Long idWaste = categoryMapper.selectByCategoryName("废品信息");
		if (null == idWaste) {
			return LianjiuResult.build(501, "没有查到废品信息");
		}
		List<Category> childrenWastes = categoryMapper.selectByParentId(idWaste);
		results.put("wastes", childrenWastes);
		return LianjiuResult.ok(results);
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:手机维修首页
	 * 
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult getIndexRepair() {
		// 维修方案信息导航栏
		Long idRepair = categoryMapper.selectByCategoryName("维修方案信息");
		if (null == idRepair) {
			return LianjiuResult.build(501, "没有查到维修品信息");
		}
		List<Category> childrenRepairs = categoryMapper.selectByParentId(idRepair);
		return LianjiuResult.ok(childrenRepairs);
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
		List<Product> products = productMapper.selectProductInfoByCategorys(cIds, orderBy);
		if (null == products || 0 == products.size()) {
			return LianjiuResult.build(503, "产品类目下没有品牌信息");
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Product> listProductInfo = (Page<Product>) products;
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(products);
		result.setTotal(listProductInfo.getTotal());
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 家具信息
	 */
	@Override
	public LianjiuResult getIndexFurniture() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 大宗交易
	 */
	@Override
	public LianjiuResult getIndexBulk() {
		Map<String, Object> results = new HashMap<>();
		// 大宗交易仓库列表
		Long idBulk = categoryMapper.selectByCategoryName("大宗交易信息");
		if (null == idBulk) {
			return LianjiuResult.build(501, "没有查到大宗交易信息");
		}
		List<Category> childrenBulks = categoryMapper.selectByParentId(idBulk);
		if (null == childrenBulks) {
			childrenBulks = new ArrayList<Category>();
		}
		results.put("warehouseList", childrenBulks);// 仓库列表
		// 生成一个空的清单车
		String TAKEN = IDUtils.genToken();
		jedisClient.set(GlobalValueJedis.BULK_CART + TAKEN, JsonUtils.objectToJson(new ArrayList<OrdersBulkItem>(0)));
		jedisClient.expire(GlobalValueJedis.BULK_CART + TAKEN, 1800);
		results.put("TAKEN", TAKEN);
		return LianjiuResult.ok(results);
	}

	/**
	 * 大宗交易
	 */
	@Override
	public LianjiuResult getIndexBulkCategory(String TAKEN, Long categoryId) {
		if (null == categoryId) {
			categoryId = JedisTool.checkObjectFormRedis(jedisClient, GlobalValueJedis.BULK_WAREHOUSE + TAKEN,
					Long.class);
			System.out.println(categoryId);
		}
		if (null == categoryId) {
			return LianjiuResult.build(501, "请传入仓库编号");
		}
		Map<String, Object> results = new HashMap<>();
		// 大宗交易导航栏
		List<Category> childrenBulks = categoryMapper.selectByParentId(categoryId);
		if (null == childrenBulks) {
			childrenBulks = new ArrayList<Category>();
		}
		results.put("categoryList", childrenBulks);// 分类列表
		// 记住当前仓库
		jedisClient.set(GlobalValueJedis.BULK_WAREHOUSE + TAKEN, JsonUtils.objectToJson(categoryId));
		jedisClient.expire(GlobalValueJedis.BULK_WAREHOUSE + TAKEN, 1800);
		results.put("TAKEN", TAKEN);
		return LianjiuResult.ok(results);
	}
}
