package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersCompany;
import com.lianjiu.service.order.OrderCompanyService;

/**
 * 企业回收订单
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/orderCompany")
public class OrderCompanyController {

	@Autowired
	private OrderCompanyService orderCompanyService;

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:查询所有企业回收订单
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCompany/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getCompanyAll() {
		return orderCompanyService.getCompanyAll();
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:分页查询所有企业回收订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCompany/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getCompanyAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderCompanyService.getCompanyAll(pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:获取当前分类的所有企业回收订单
	 * 
	 * @param Cid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCompany/{Cid}/ByCid/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getCompanyByCid(@PathVariable Long Cid) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid对象绑定错误");
		}
		return orderCompanyService.getCompanyByCid(Cid);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:获取当前分类的所有企业回收订单，分页
	 * 
	 * @param Cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCompany/{Cid}/{pageNum}/{pageSize}/ByCid", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getCompanyByCid(@PathVariable Long Cid, @PathVariable int pageNum, @PathVariable int pageSize) {
		if(null == Cid){
			return LianjiuResult.build(401, "Cid对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderCompanyService.getCompanyByCid(Cid, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月7日 descrption:根据主键id查询
	 * 
	 * @param CompanyId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCompany/{companyId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getCompanyByCid(@PathVariable String companyId) {
		if(Util.isEmpty(companyId)){
			return LianjiuResult.build(401, "请传入正确的companyId");
		}
		return orderCompanyService.getCompanyById(companyId);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:添加企业回收订单
	 * 
	 * @param Company
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addCompany", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addCompany(OrdersCompany company) {
		if(null == company){
			return LianjiuResult.build(401, "company对象绑定错误");
		}
		Util.printModelDetails(company);
		return orderCompanyService.addCompany(company);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:更新企业回收订单
	 * 
	 * @param Company
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyCompany", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateCompany(OrdersCompany company) {
		if(null == company){
			return LianjiuResult.build(401, "company对象绑定错误");
		}
		return orderCompanyService.updateCompany(company);
	}

	/**
	 * 
	 * huangfu 2017年9月12日 descrption:删除企业回收订单
	 * 
	 * @param CompanyId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeCompany/{companyId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteCompany(@PathVariable String companyId) {
		if(Util.isEmpty(companyId)){
			return LianjiuResult.build(401, "请传入正确的companyId");
		}
		return orderCompanyService.deleteCompany(companyId);
	}
	
	/**
	 * 
	 * huangfu 2017年1月9日 descrption:维修订单模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(OrdersCompany ordersCompany, int pageNum,int pageSize,String cratedStart,String cratedOver) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("ordersCompany getOrExpressUserId："+ordersCompany.getOrCompanyPhone());
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
		return orderCompanyService.vagueQuery(ordersCompany, pageNum,pageSize,cratedStart,cratedOver);
	}
}
