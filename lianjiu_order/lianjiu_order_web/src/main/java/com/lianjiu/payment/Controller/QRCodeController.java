package com.lianjiu.payment.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lianjiu.payment.config.AlipayConfig;
import com.lianjiu.payment.config.AlipayNotify;
import com.lianjiu.payment.config.AlipaySubmit;

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
        try {
            //把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap<>();
            sParaTemp.put("service", AlipayConfig.service);
            sParaTemp.put("partner", AlipayConfig.partner);
            sParaTemp.put("seller_id", AlipayConfig.seller_id);
            sParaTemp.put("_input_charset", AlipayConfig.input_charset);
            sParaTemp.put("payment_type", AlipayConfig.payment_type);
            sParaTemp.put("notify_url", AlipayConfig.notify_url);
            sParaTemp.put("return_url", AlipayConfig.return_url);
            sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
            sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
            sParaTemp.put("out_trade_no", out_trade_no);
            sParaTemp.put("subject", subject);
            sParaTemp.put("total_fee", total_fee);
            sParaTemp.put("body", body);
            sParaTemp.put("qr_pay_mode", "2");
            //建立请求
            String form = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
            response.setContentType("text/html;charset=" + AlipayConfig.input_charset);
            //直接将完整的表单html输出到页面
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            logger.error("生成订单失败(AliPay)：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 扫码支付回调接口
     *
     * @param request
     */
    @RequestMapping(value = "QRCodereturn")
    public void QRCode(HttpServletRequest request) {
        System.out.println("-------------------QCPAY-------------------");
        try {
            //获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String valueStr = "";
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);
            }
            //商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            //支付宝交易号
            String trade_no = request.getParameter("trade_no");
            //交易状态
            String trade_status = request.getParameter("trade_status");
            if (StringUtils.isNotBlank(out_trade_no) && StringUtils.isNotBlank(trade_no) && StringUtils.isNotBlank(trade_status)) {
                boolean verify_result = AlipayNotify.verify(params);
                if (verify_result) {
                    //验证成功
                    logger.info("支付成功！订单号：" + out_trade_no + "，支付宝交易号：" + trade_no);
                    if (trade_status.equals("TRADE_FINISHED")) {
                        //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                        //注意：
                        //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                        //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                    } else if (trade_status.equals("TRADE_SUCCESS")) {
                        //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                        //注意：
                        //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

}
