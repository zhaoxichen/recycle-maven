package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.service.order.OrderFaceFaceDetailsService;
/**
 * 上门回收订单详情
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/orderFacefaceDetails")
public class OrderFaceFaceDetailsController {


	@Autowired
	private OrderFaceFaceDetailsService orderFaceFaceDetailsService;
	
	/**
	 * 
	 * huangfu 2017年9月12日 descrption:查询所有回收订单详情
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDetails/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getDetailsAll() {
		return orderFaceFaceDetailsService.getDetailsAll();
	}
	
	/**
	 * 
	 * huangfu 2017年9月12日 descrption:分页查询所有回收订单详情
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDetails/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getDetailsAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderFaceFaceDetailsService.getDetailsAll(pageNum, pageSize);
	}
	
	/**
	 * 
	 * huangfu 2017年9月7日 descrption:通过上门回收订单详情获取详情
	 * 
	 * @param ExpressId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDetails/{facefaceId}/ByEid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getDetailsByFacefaceId(@PathVariable String facefaceId) {
		if(Util.isEmpty(facefaceId)){
			return LianjiuResult.build(401, "请传入正确的facefaceId");
		}
		return orderFaceFaceDetailsService.getDetailsByFacefaceId(facefaceId);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param detailsId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getDetails/{detailsId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getDetailsById(@PathVariable String detailsId) {
		if(Util.isEmpty(detailsId)){
			return LianjiuResult.build(401, "请传入正确的detailsId");
		}
		return orderFaceFaceDetailsService.getDetailsById(detailsId);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:添加上门回收订单详情详情
	 * 
	 * @param details
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addDetails", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addDetails(OrdersFacefaceDetails details) {
		if(null == details){
			return LianjiuResult.build(401, "details对象绑定错误");
		}
		Util.printModelDetails(details);
		return orderFaceFaceDetailsService.addDetails(details);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:更新上门回收订单详情详情
	 * 
	 * @param details
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyDetails", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateDetails(OrdersFacefaceDetails details) {
		if(null == details){
			return LianjiuResult.build(401, "details对象绑定错误");
		}
		return orderFaceFaceDetailsService.updateDetails(details);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:删除上门回收订单详情详情,跟随上门回收订单详情
	 * 
	 * @param detailsId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeDetails/{detailsId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteDetails(@PathVariable String detailsId) {
		if(Util.isEmpty(detailsId)){
			return LianjiuResult.build(401, "请传入正确的detailsId");
		}
		return orderFaceFaceDetailsService.deleteDetails(detailsId);
	}
	
}
