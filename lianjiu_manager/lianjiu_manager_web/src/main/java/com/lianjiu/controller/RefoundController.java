package com.lianjiu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.service.RefoundService;

@Controller
@RequestMapping("/Refound")
public class RefoundController {
	@Autowired
	private RefoundService refoundService;

	@RequestMapping(value = "/payBack", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult payBack(HttpServletRequest request, HttpServletResponse response, String outTradeNo,
			String money) {
		if (Util.isEmpty(outTradeNo)) {
			return LianjiuResult.build(401, "订单id不能为空");
		}
		if (Util.isEmpty(money)) {
			return LianjiuResult.build(402, "请传入退款金额");
		}
		if(Util.isNumeric(money)){
			return LianjiuResult.build(403, "请传入正确金额");
		}
		return refoundService.payBack(outTradeNo, money, request, response);
	}
}