package com.lianjiu.service.content.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.AdCarousel;
import com.lianjiu.model.AdElectronic;
import com.lianjiu.model.AdEssenceBrand;
import com.lianjiu.model.AdEssenceHot;
import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.AdSecond;
import com.lianjiu.model.AdTheme;
import com.lianjiu.model.AdThird;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.content.AdCarouselMapper;
import com.lianjiu.rest.mapper.content.AdElectronicMapper;
import com.lianjiu.rest.mapper.content.AdEssenceBrandMapper;
import com.lianjiu.rest.mapper.content.AdEssenceHotMapper;
import com.lianjiu.rest.mapper.content.AdHotProductMapper;
import com.lianjiu.rest.mapper.content.AdSecondMapper;
import com.lianjiu.rest.mapper.content.AdThemeMapper;
import com.lianjiu.rest.mapper.content.AdThirdMapper;
import com.lianjiu.service.content.AdService;

@Service
@Transactional
public class AdServiceImpl implements AdService {

	@Autowired
	private AdCarouselMapper adCarouselMapper;
	@Autowired
	private AdElectronicMapper adElectronicMapper;
	@Autowired
	private AdHotProductMapper adHotProductMapper;
	@Autowired
	private AdSecondMapper adSecondMapper;
	@Autowired
	private AdThemeMapper adThemeMapper;
	@Autowired
	private AdThirdMapper adThirdMapper;
	@Autowired
	private AdEssenceHotMapper adEssenceHotMapper;
	@Autowired
	private AdEssenceBrandMapper adEssenceBrandMapper;
	@Autowired
	private JedisClient jedisClient;

	/*
	 * 添加轮播图 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertCarousel(com.lianjiu.model.
	 * AdCarousel)
	 */
	@Override
	public LianjiuResult insertCarousel(AdCarousel record) {
		record.setCaId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adCarouselMapper.insertCarousel(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_CAROUSEL);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除轮播图
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#deleteCararouse(java.lang.String)
	 */
	@Override
	public LianjiuResult deleteCararouse(String caId) {
		int i = adCarouselMapper.deleteByPrimaryKey(caId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_CAROUSEL );
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改轮播图 指定内容
	 */
	@Override
	public LianjiuResult updateByPrimaryKeySelective(AdCarousel record) {
		int i = adCarouselMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_CAROUSEL );
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定轮播图 id的内容 (non-Javadoc)
	 */
	@Override
	public LianjiuResult selectByPrimaryKey(String caId) {
		AdCarousel carousel = adCarouselMapper.selectByPrimaryKey(caId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(carousel);
		return result;
	}

	/*
	 * 分页查询轮播图 (non-Javadoc)
	 * 
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int,
	 * int)
	 */
	@Override
	public LianjiuResult selectCararouseList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdCarousel> adCarousel = adCarouselMapper.selectActivityList();
		Page<AdCarousel> list = (Page<AdCarousel>) adCarousel;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list.getTotal());
		result.setLianjiuData(adCarousel);
		return result;
	}

	/**
	 * 添加电器广告
	 */
	@Override
	public LianjiuResult insertElectronic(AdElectronic record) {
		record.setEleId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adElectronicMapper.insertElectronic(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_ELECTRONIC);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除电器广告 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#deleteCararouse(java.lang.String)
	 */
	@Override
	public LianjiuResult deleteElectronic(String eleId) {
		int i = adElectronicMapper.deleteByPrimaryKey(eleId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_ELECTRONIC);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改活动指定内容
	 */
	@Override
	public LianjiuResult updateElectronic(AdElectronic record) {
		int i = adElectronicMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_ELECTRONIC);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定活动id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectElectronicById(String eleId) {
		AdElectronic record = adElectronicMapper.selectByPrimaryKey(eleId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		return result;
	}

	/*
	 * 分页查询轮播图 (non-Javadoc)
	 * 
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int,
	 * int)
	 */
	public LianjiuResult selectElectronicList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdElectronic> adElectronic = adElectronicMapper.selectElectronicList();
		Page<AdElectronic> list = (Page<AdElectronic>) adElectronic;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list.getTotal());
		result.setLianjiuData(adElectronic);
		return result;
	}

	/*
	 * 热门产品添加 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertHotProduct(com.lianjiu.model.
	 * AdHotProduct)
	 */
	@Override
	public LianjiuResult insertHotProduct(AdHotProduct record) {
		record.setHotId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adHotProductMapper.insertHotProduct(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_PRODUCT);
		return LianjiuResult.ok(i);
	}

	/**
	 * 删除指定的热门产品
	 */
	@Override
	public LianjiuResult deleteHotProduct(String hotId) {
		int i = adHotProductMapper.deleteByPrimaryKey(hotId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_PRODUCT);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改指定热门产品内容
	 */
	@Override
	public LianjiuResult updateHotProduct(AdHotProduct record) {
		int i = adHotProductMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_PRODUCT);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定热门产品id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectHotProductById(String hotId) {
		AdHotProduct record = adHotProductMapper.selectByPrimaryKey(hotId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		return result;
	}

	/*
	 * 分页查询热门产品 (non-Javadoc)
	 * 
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int,
	 * int)
	 */
	public LianjiuResult selectHotProductList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdHotProduct> adHotProduct = adHotProductMapper.selectElectronicList();
		Page<AdHotProduct> list = (Page<AdHotProduct>) adHotProduct;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list.getTotal());
		result.setLianjiuData(adHotProduct);
		return result;
	}

	/*
	 * 二层广告添加 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertHotProduct(com.lianjiu.model.
	 * AdHotProduct)
	 */
	@Override
	public LianjiuResult insertAdSecond(AdSecond record) {
		record.setSecId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adSecondMapper.insertAdSecond(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_SECOND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除指定的二层广告
	 */
	@Override
	public LianjiuResult deleteAdSecond(String secId) {
		int i = adSecondMapper.deleteByPrimaryKey(secId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_SECOND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改指定二层广告内容
	 */
	@Override
	public LianjiuResult updateAdSecond(AdSecond record) {
		int i = adSecondMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_SECOND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定二层广告id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectAdSecondById(String secId) {
		AdSecond record = adSecondMapper.selectByPrimaryKey(secId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		return result;
	}

	/*
	 * 分页查询二层广告 (non-Javadoc)
	 * 
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int,
	 * int)
	 */
	public LianjiuResult selectAdSecondList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdSecond> list = adSecondMapper.selectAdSecondList();
		Page<AdSecond> list1 = (Page<AdSecond>) list;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list1.getTotal());
		result.setLianjiuData(list);
		return result;
	}

	/*
	 * 广告主题添加 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertHotProduct(com.lianjiu.model.
	 * AdHotProduct)
	 */
	@Override
	public LianjiuResult insertAdTheme(AdTheme record) {
		record.setTheId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adThemeMapper.insertAdTheme(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_THEME);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除指定的广告主题
	 */
	@Override
	public LianjiuResult deleteAdTheme(String theId) {
		int i = adThemeMapper.deleteByPrimaryKey(theId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_THEME);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改指定广告主题内容
	 */
	@Override
	public LianjiuResult updateAdTheme(AdTheme record) {
		int i = adThemeMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_THEME);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定二层广告id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectAdThemeById(String theId) {
		AdTheme record = adThemeMapper.selectByPrimaryKey(theId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		return result;
	}

	/*
	 * 分页查询广告主题 (non-Javadoc)
	 */
	public LianjiuResult selectAdThemeList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdTheme> list = adThemeMapper.selectAdSecondList();
		Page<AdTheme> list1 = (Page<AdTheme>) list;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list1.getTotal());
		result.setLianjiuData(list);
		return result;
	}

	/*
	 * 三层广告添加 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertHotProduct(com.lianjiu.model.
	 * AdHotProduct)
	 */
	@Override
	public LianjiuResult insertAdThird(AdThird record) {
		record.setThiId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adThirdMapper.insertAdTheme(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_THIRD);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除指定的三层广告
	 */
	@Override
	public LianjiuResult deleteAdThird(String thiId) {
		int i = adThirdMapper.deleteByPrimaryKey(thiId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_THIRD);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改指定三层广告内容
	 */
	@Override
	public LianjiuResult updateAdThird(AdThird record) {
		int i = adThirdMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_THIRD);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定三层广告id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectAdThirdById(String thiId) {
		AdThird record = adThirdMapper.selectByPrimaryKey(thiId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		return result;
	}

	/*
	 * 分页查询三层广告 (non-Javadoc)
	 */
	public LianjiuResult selectAdThirdList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdThird> list = adThirdMapper.selectAdThirdList();
		Page<AdThird> list1 = (Page<AdThird>) list;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list1.getTotal());
		result.setLianjiuData(list);
		return result;
	}

	/*
	 * 精品热卖添加 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertHotProduct(com.lianjiu.model.
	 * AdHotProduct)
	 */
	@Override
	public LianjiuResult insertAdEssenceHot(AdEssenceHot record) {
		record.setEssId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adEssenceHotMapper.insertAdEssenceHot(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_EXCELLENT);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除指定的精品热卖广告
	 */
	@Override
	public LianjiuResult deleteAdEssenceHot(String essId) {
		int i = adEssenceHotMapper.deleteByPrimaryKey(essId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_EXCELLENT);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改指定精品热卖广告内容
	 */
	@Override
	public LianjiuResult updateAdEssenceHot(AdEssenceHot record) {
		int i = adEssenceHotMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_EXCELLENT);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定精品热卖id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectAdEssenceHotById(String essId) {
		AdEssenceHot record = adEssenceHotMapper.selectByPrimaryKey(essId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		return result;
	}

	/*
	 * 分页查询精品热卖 (non-Javadoc)
	 */
	public LianjiuResult selectAdEssenceHotList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdEssenceHot> list = adEssenceHotMapper.selectAdEssenceHotList();
		Page<AdEssenceHot> list1 = (Page<AdEssenceHot>) list;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list1.getTotal());
		result.setLianjiuData(list);
		return result;
	}

	/*
	 * 二手精品 品牌墙添加 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertHotProduct(com.lianjiu.model.
	 * AdHotProduct)
	 */
	@Override
	public LianjiuResult insertAdEssenceBrand(AdEssenceBrand record) {
		record.setBrId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		record.setUpdateTime(new Date());
		int i = adEssenceBrandMapper.insertAdEssenceBrand(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_EXCELLENT_BRAND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 删除指定的二手精品 品牌墙
	 */
	@Override
	public LianjiuResult deleteAdEssenceBrand(String brId) {
		int i = adEssenceBrandMapper.deleteByPrimaryKey(brId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_EXCELLENT_BRAND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改指定二手精品 品牌墙
	 */
	@Override
	public LianjiuResult updateAdEssenceBrand(AdEssenceBrand record) {
		record.setUpdateTime(new Date());
		int i = adEssenceBrandMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_HOT_EXCELLENT_BRAND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定二手精品 品牌墙id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectAdEssenceBrandById(String brId) {
		AdEssenceBrand record = adEssenceBrandMapper.selectByPrimaryKey(brId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		return result;
	}

	/*
	 * 分页查询二手精品 品牌墙 (non-Javadoc)
	 */
	public LianjiuResult selectAdEssenceBrandList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdEssenceBrand> list = adEssenceBrandMapper.selectAdEssenceBrandList();
		Page<AdEssenceBrand> list1 = (Page<AdEssenceBrand>) list;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list1.getTotal());
		result.setLianjiuData(list);
		return result;
	}
}
