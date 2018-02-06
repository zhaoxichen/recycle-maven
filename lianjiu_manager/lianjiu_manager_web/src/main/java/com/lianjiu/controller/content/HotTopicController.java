package com.lianjiu.controller.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AdHotTopic;
import com.lianjiu.service.content.HotTopicService;

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
	 * 热门话题添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertHotTopic", method = RequestMethod.POST)
	public LianjiuResult insertHotTopic(HttpServletRequest request, AdHotTopic record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertHotTopic(record);
	}
	/**
	 *  热门话题删除
	 * @param hotId 热门话题id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteHotTopic", method = RequestMethod.POST)
	public LianjiuResult deleteAdHotTopic(String topId) {
		if(Util.isEmpty(topId)){
			return LianjiuResult.build(501, "请指定要删除的热门产品Id");
		}
		return adService.deleteHotTopic(topId);
	}
	
	/**
	 * 更新热门话题内容
	 * @param AdActivity :活动广告
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateHotTopic", method = RequestMethod.POST)
	public LianjiuResult updateHotTopic(AdHotTopic record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateHotTopic(record);
	}
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
			return LianjiuResult.build(501, "请指定要查询的电器广告Id");
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
	
	/**
	 * 
	 * huangfu 2017年1月9日 descrption:热门话题模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(AdHotTopic adHotTopic, int pageNum,int pageSize,String cratedStart,String cratedOver) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("adHotTopic getTopTitle："+adHotTopic.getTopTitle());
		System.out.println("cratedStart："+cratedStart);
		System.out.println("cratedOver："+cratedOver);
		System.out.println("pageNum："+pageNum);
		System.out.println("pageSize："+pageSize);
		
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return adService.vagueQuery(adHotTopic, pageNum,pageSize,cratedStart,cratedOver);
	}
}
