package com.lianjiu.rest.controller.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.service.content.HotTopicService;

/**
 * 热门话题
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/Topic/")
public class HotTopicController {

	@Autowired
	private HotTopicService adService;
	
	
	/**
	 * 按热门话题id查询
	 * @param topId :热门话题id
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectHotTopicById/{topId}", method = RequestMethod.GET)
	public LianjiuResult selectHotTopicById(@PathVariable String topId) {
		if(Util.isEmpty(topId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectHotTopicById(topId);
	}
	
	/**
	 * 根据页码查询热门话题list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectHotTopicList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectHotTopicList(@PathVariable int pageNum,@PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectHotTopicList(pageNum,pageSize);
	}
}
