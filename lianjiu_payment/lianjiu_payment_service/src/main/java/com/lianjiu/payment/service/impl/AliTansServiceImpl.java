package com.lianjiu.payment.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.lianjiu.payment.dao.config.AlipayConfig;
import com.lianjiu.payment.dao.config.AlipayNotify;
import com.lianjiu.payment.dao.config.AlipaySubmit;
import com.lianjiu.payment.dao.config.UtilDate;
import com.lianjiu.payment.service.AliTansService;
@Service
public class AliTansServiceImpl implements AliTansService {
	//支付宝服务器异步通知页面
	@Override
	public void alipayNotify(HttpServletRequest request, HttpServletResponse res) {
		 //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //批量付款数据中转账成功的详细信息
        String success_details = "";
        //批量付款数据中转账失败的详细信息
        String fail_details = "";

        try {
            success_details = new String(request.getParameter("success_details").getBytes("ISO-8859-1"),"UTF-8");
            fail_details = new String(request.getParameter("fail_details").getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

        if(AlipayNotify.verify(params)){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            //判断是否在商户网站中已经做过了这次通知返回的处理
                //如果没有做过处理，那么执行商户的业务程序
                //如果有做过处理，那么不执行商户的业务程序

            out("success",res); //请不要修改或删除

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            //
        }else{//验证失败
            out("fail",res);
        }
		
	}
	// 向支付宝发送请求
	@Override
	public void alipayApi(HttpServletRequest request, HttpServletResponse response, AlipayConfig alipayConfig) {
		 // 服务器异步通知页面路径
        String notify_url = "http://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath()
                + "/alipay/alipayNotify";
        // 需http://格式的完整路径，不能加?id=123这类自定义参数

        //付款账号
        String email = "";
        //必填

        //付款账户名
        String account_name = "";
        //必填，个人支付宝账号是真实姓名---公司支付宝账号是公司名称

        //批次号
        String batch_no = "";
        //必填，格式：当天日期[8位]+序列号[3至16位]，如：201008010000001

        //付款总金额
        String batch_fee = "";
        //必填，即参数detail_data的值中所有金额的总和

        //付款笔数
        String batch_num = "";
        //必填，即参数detail_data的值中，“|”字符出现的数量加1，最大支持1000笔（即“|”字符出现的数量999个）

        //付款详细数据
        String detail_data = "";
        //必填，格式：流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1|流水号2^收款方帐号2^真实姓名^付款金额2^备注说明2....
        try {
            batch_no = new String(request.getParameter("WIDbatch_no").getBytes("ISO-8859-1"),"UTF-8");
            batch_fee = new String(request.getParameter("WIDbatch_fee").getBytes("ISO-8859-1"),"UTF-8");
            detail_data = new String(request.getParameter("WIDdetail_data").getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        //////////////////////////////////////////////////////////////////////////////////

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "batch_trans_notify");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", notify_url);
        sParaTemp.put("email", email);
        sParaTemp.put("account_name", account_name);
        //付款当天日期//必填，格式：年[4位]月[2位]日[2位]，如：20100801
        sParaTemp.put("pay_date", UtilDate.getDate());
        sParaTemp.put("batch_no", batch_no);
        sParaTemp.put("batch_fee", batch_fee);
        sParaTemp.put("batch_num", batch_num);
        sParaTemp.put("detail_data", detail_data);

        /**建立请求*/
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
        System.out.println(sHtmlText);
        out(sHtmlText,response);
	}

    public static String out(String msg,HttpServletResponse response) {

        HttpServletResponse res = response;

        try {
            PrintWriter out = res.getWriter();
            out.print(msg);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
