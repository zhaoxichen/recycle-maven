package  com.lianjiu.payment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lianjiu.payment.dao.config.AlipayConfig;
import com.lianjiu.payment.service.AliTansService;


@Controller
@RequestMapping(value="/alipay")
public class AliTansController{

    @Autowired
    private AliTansService alitansService;


    /**
     * 支付宝服务器异步通知页面
     * @param req
     * @param res
     */
    @RequestMapping(value="/alipayNotify")
    public void alipayNotify(HttpServletRequest request,HttpServletResponse res){
    	alitansService.alipayNotify(request, res);
       
    }

    /**
     * 向支付宝发送请求
     * @param req
     * @param res
     * @param alipayConfig
     */
    @RequestMapping(value="/alipayApi")
    public void alipayApi(HttpServletRequest request,HttpServletResponse response,AlipayConfig alipayConfig){
    	alitansService.alipayApi(request, response, alipayConfig);
    }



}