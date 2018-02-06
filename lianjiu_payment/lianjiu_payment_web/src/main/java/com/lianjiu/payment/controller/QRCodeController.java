package com.lianjiu.payment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.payment.service.QRCodeService;

/**
 * @author WCS.Wang
 * @version V1.0
 * @Package com.handsure.pay.controller
 * @Name ali-pay
 * @Description: 扫码支付
 * @date 2017/8/15
 */
@Controller
@RequestMapping("/alipaypc/")
public class QRCodeController {
	private Logger logger = LoggerFactory.getLogger(QRCodeController.class);
    @Autowired
    private QRCodeService qRCodeService;
    /**
     * 扫码支付
     *
     * @param out_trade_no 商户订单号，商户网站订单系统中唯一订单号，必填
     * @param total_fee    付款金额，必填
     * @param subject      订单名称，必填
     * @param body         商品描述，可空
     * @param response
     */
    @RequestMapping(value = "QRCode",method = RequestMethod.POST)
    public void pay(String out_trade_no, String total_fee, String subject, String body, HttpServletResponse response) {
    	logger.debug("out_trade_no:"+out_trade_no);
    	logger.debug("total_fee:"+total_fee);
    	logger.debug("subject:"+subject);
    	if(StringUtil.isEmpty(out_trade_no)){
			return;
		}else if(StringUtil.isEmpty(subject)){
			return;
		}else if(StringUtil.isEmpty(body)){
			return;
		}
    	qRCodeService.pay(out_trade_no, total_fee, subject, body, response);
    }

    /**
     * 扫码支付回调接口
     *
     * @param request
     */
    @RequestMapping(value = "QRCodereturn")
    public void QRCode(HttpServletRequest request) {
    	qRCodeService.QRCode(request);
    	System.out.println("-------------------QCPAY-------------------");
       
    }

}
