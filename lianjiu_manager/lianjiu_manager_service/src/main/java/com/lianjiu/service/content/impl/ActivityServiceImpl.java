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
import com.lianjiu.model.AdActivity;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.content.AdActivityMapper;
import com.lianjiu.service.content.ActivityService;

@Service("AdService")
@Transactional
public class ActivityServiceImpl implements ActivityService {
	@Autowired
	private AdActivityMapper adActivityMapper;
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 删除活动更加id
	 */
	@Override
	public LianjiuResult deleteByPrimaryKey(String actId) {
		int i = adActivityMapper.deleteByPrimaryKey(actId);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_SECOND);
		// 返回的i=0 是错,i = 1 是对的
		return LianjiuResult.ok(i);
	}

	/*
	 * 添加上传 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.AdService#insertActivity(com.lianjiu.model.
	 * AdActivity)
	 */
	@Override
	public LianjiuResult insertActivity(AdActivity record) {
		record.setActId(IDUtils.genGUIDs());
		record.setAddTime(new Date());
		int i = adActivityMapper.insert(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_SECOND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 修改活动指定内容
	 * 
	 * @see
	 * com.lianjiu.service.content.ActivityService#updateByPrimaryKeySelective(
	 * com.lianjiu.model.AdActivity)
	 */
	@Override
	public LianjiuResult updateByPrimaryKeySelective(AdActivity record) {
		int i = adActivityMapper.updateByPrimaryKeySelective(record);
		// 清缓存
		jedisClient.del(GlobalValueJedis.INDEX_AD_SECOND);
		return LianjiuResult.ok(i);
	}

	/*
	 * 查询指定活动id的内容 (non-Javadoc)
	 * 
	 * @see
	 * com.lianjiu.service.content.ActivityService#selectByPrimaryKey(java.lang.
	 * String)
	 */
	@Override
	public LianjiuResult selectByPrimaryKey(String actId) {
		AdActivity adActivity = adActivityMapper.selectByPrimaryKey(actId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(adActivity);
		return result;
	}

	/*
	 * 分页查询活动 (non-Javadoc)
	 * 
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int,
	 * int)
	 */
	@Override
	public LianjiuResult selectActivityList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdActivity> adActivity = adActivityMapper.selectActivityList();
		Page<AdActivity> list = (Page<AdActivity>) adActivity;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list.getTotal());
		result.setLianjiuData(adActivity);
		return result;
	}

}
