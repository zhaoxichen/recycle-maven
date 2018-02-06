package com.lianjiu.rest.service.content.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AdActivity;
import com.lianjiu.rest.mapper.content.AdActivityMapper;
import com.lianjiu.rest.service.content.ActivityService;


@Service("AdService")
@Transactional
public class ActivityServiceImpl implements ActivityService {
	@Autowired
	private AdActivityMapper adActivityMapper;
	
	/*
	 * 查询指定活动id的内容
	 * (non-Javadoc)
	 * @see com.lianjiu.service.content.ActivityService#selectByPrimaryKey(java.lang.String)
	 */
	@Override
	public LianjiuResult selectByPrimaryKey(String actId) {
		AdActivity  adActivity = adActivityMapper.selectByPrimaryKey(actId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(adActivity);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}
	/*
	 * 分页查询活动
	 * (non-Javadoc)
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int, int)
	 */
	@Override
	public LianjiuResult selectActivityList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdActivity>  adActivity = adActivityMapper.selectActivityList();
		Page<AdActivity> list = (Page<AdActivity>) adActivity;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(list.getTotal());
		result.setLianjiuData(adActivity);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}
	
	
	
}
