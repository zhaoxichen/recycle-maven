package com.lianjiu.rest.service.content.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AdCarousel;
import com.lianjiu.model.AdElectronic;
import com.lianjiu.model.AdEssenceHot;
import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.AdSecond;
import com.lianjiu.model.AdTheme;
import com.lianjiu.model.AdThird;
import com.lianjiu.rest.mapper.content.AdCarouselMapper;
import com.lianjiu.rest.mapper.content.AdElectronicMapper;
import com.lianjiu.rest.mapper.content.AdEssenceHotMapper;
import com.lianjiu.rest.mapper.content.AdHotProductMapper;
import com.lianjiu.rest.mapper.content.AdSecondMapper;
import com.lianjiu.rest.mapper.content.AdThemeMapper;
import com.lianjiu.rest.mapper.content.AdThirdMapper;
import com.lianjiu.rest.service.content.AdService;

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

	/*
	 * 查询指定轮播图 id的内容 (non-Javadoc)
	 */
	@Override
	public LianjiuResult selectByPrimaryKey(String caId) {
		AdCarousel carousel = adCarouselMapper.selectByPrimaryKey(caId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(carousel);
		result.setStatus(200);
		result.setMsg("ok");
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
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/*
	 * 首页轮播图
	 * 
	 */
	public LianjiuResult selectCararouseFour() {
		List<AdCarousel> adCarousel = adCarouselMapper.selectCararouseFour();
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(adCarousel);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
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
		result.setStatus(200);
		result.setMsg("ok");
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
		result.setStatus(200);
		result.setMsg("ok");
		return result;
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
		result.setStatus(200);
		result.setMsg("ok");
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
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	@Override
	public LianjiuResult selectHotProductFour() {
		List<AdHotProduct> adHotProduct = adHotProductMapper.selectHotProductFour();
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(adHotProduct);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
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
		result.setStatus(200);
		result.setMsg("ok");
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
		result.setStatus(200);
		result.setMsg("ok");
		return result;
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
		result.setStatus(200);
		result.setMsg("ok");
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
		result.setStatus(200);
		result.setMsg("ok");
		return result;
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
		result.setStatus(200);
		result.setMsg("ok");
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
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	/**
	 * 精品，热卖
	 */
	@Override
	public LianjiuResult selectAdEssenceHotFour() {
		List<AdEssenceHot> adEssenceHot = adEssenceHotMapper.selectEssenceHotFour();
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(adEssenceHot);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

	@Override
	public LianjiuResult selectCararouseFourType(int type) {
		// 首页轮播图
		List<AdCarousel> adCarousel = adCarouselMapper.selectCararouseFourType(type);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(adCarousel);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}

}
