package com.lianjiu.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersCompany;
import com.lianjiu.order.service.OrderCompanyService;

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
	 * huangfu 2017年9月12日 descrption:添加企业回收订单
	 * 
	 * @param Company
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addCompany", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addCompany(OrdersCompany company) {
		if (null == company) {
			return LianjiuResult.build(401, "传入的对象为空");
		}
		if (Util.isEmpty(company.getOrCompanyName())) {
			return LianjiuResult.build(402, "名称不能为空");
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
		if (null == company) {
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
}
