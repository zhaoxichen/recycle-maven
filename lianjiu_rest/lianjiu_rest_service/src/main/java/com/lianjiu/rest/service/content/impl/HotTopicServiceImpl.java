package com.lianjiu.rest.service.content.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.AdHotTopic;
import com.lianjiu.rest.mapper.content.AdHotTopicMapper;
import com.lianjiu.rest.service.content.HotTopicService;

/**
 * 评论，，服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class HotTopicServiceImpl implements HotTopicService {
	@Autowired
	private AdHotTopicMapper  adHotTopicMapper;

	/*
	 * 查询指定热门产品id的内容
	 * 
	 */
	@Override
	public LianjiuResult selectHotTopicById(String topId) {
		AdHotTopic  record = adHotTopicMapper.selectByPrimaryKey(topId);
		LianjiuResult result = new LianjiuResult();
		result.setLianjiuData(record);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}
	/*
	 * 分页查询热门产品
	 * (non-Javadoc)
	 * @see com.lianjiu.service.content.ActivityService#selectActivityList(int, int)
	 */
	public LianjiuResult selectHotTopicList(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<AdHotTopic> list = adHotTopicMapper.selectElectronicList();
		Page<AdHotTopic> topiclist = (Page<AdHotTopic>) list;
		LianjiuResult result = new LianjiuResult();
		result.setTotal(topiclist.getTotal());
		result.setLianjiuData(list);
		result.setStatus(200);
		result.setMsg("ok");
		return result;
	}
}
