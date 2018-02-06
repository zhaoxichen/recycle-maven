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
import com.lianjiu.model.AdActivity;
import com.lianjiu.service.content.ActivityService;

/**
 * 活动信息
 * 
 * @author xiangyang
 *
 */
@Controller
@RequestMapping("/activity/")
public class ActivityController {
	@Autowired
	private ActivityService activityService;
	/**
	 * 根据id删除活动广告
	 * @param actId :活动广告id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="activityDeleteById", method = RequestMethod.POST)
	public LianjiuResult deleteByPrimaryKey(String actId) {
		if(Util.isEmpty(actId)){
			return LianjiuResult.build(401, "指定要删除的活动id");
		}
		return activityService.deleteByPrimaryKey(actId);
	}
	/**
	 * 更新修改活动内容
	 * @param AdActivity :活动广告
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateactivity", method = RequestMethod.POST)
	public LianjiuResult updateByPrimaryKeySelective(AdActivity record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return activityService.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 添加
	 * @param AdActivity :活动广告
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="insertActivity", method = RequestMethod.POST)
	public LianjiuResult insertActivity(HttpServletRequest request,AdActivity record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return activityService.insertActivity(record);
	}
	/**
	 * 按活动id查询
	 * @param actId :活动广告id
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectActivityById/{actId}/actId", method = RequestMethod.GET)
	public LianjiuResult selectByPrimaryKey(@PathVariable String actId) {
		if(Util.isEmpty(actId)){
			return LianjiuResult.build(401, "请指定要查询的活动Id");
		}
		return activityService.selectByPrimaryKey(actId);
	}
	/**
	 * 查询活动list
	 * @param AdActivity :活动广告
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectActivityList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectActivityList(@PathVariable  int pageNum, @PathVariable  int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return activityService.selectActivityList(pageNum,pageSize);
	}
}
