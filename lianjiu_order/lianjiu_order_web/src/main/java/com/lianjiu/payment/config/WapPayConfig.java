package com.lianjiu.payment.config;

/**
 * @author WCS.Wang
 * @version V1.0
 * @Package com.handsure.pay.config
 * @Name ali-pay
 * @Description: 支付宝手机支付相关配置
 * @date 2017/8/18
 */
public class WapPayConfig {

    // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String partner = "2088821302442990";

    //商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    public static String app_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMmkqG0S2dWWdOPhASAIYV3cg29dm7QNouDl7D/E8aa+aVwbjwbcXSdYbgj1cTSTQwo5CZt6cRwuB2rbeoSac8wBjbeMu7ioKaZjf3j8FzU35J39Kovve54CvV/SDABkkvTDH737Nq7f0p0j15GOC6RG+ZMcz94n42mydSSczjk9AgMBAAECgYAwKTDFJHx85Du09dfT88x1o6aM4dm/joY5mxDrRjnFbrMxea+Wnuls7VUStLS+LvITLf5acUs0xNWmsxc3NsCzKxAMCdvsn1rLMbN99GCZLTYyX4GOa6IBXMBGcrEVdOevaGfnhJqo7u3hp2vEr/1eOhOKbm/xmo5xJ/92jj3CLQJBAPt+u8A7oOnVoqrxvx+ImkDkodEuBjdOIEqKv8DS0HeUF4U8TyP2xP8YK+OGzioDg+JujQU/KM2FomD/PWu8Cu8CQQDNQVJZchoDICUTjmb2NWGlDowp1CkAaM9Em9TtGoDdhY5tT9l3EJc2KxFPResWaVXVJh+W4WKr/el+CIe78y6TAkEAhjf8u3sXRbRePB0IgyvEJx3dvmVxq9JmKWIjNCVOXIMMim7FTcxFFl8wDR3EqWd220FkwYgTnpvTLTEK9pREwQJBAMzoU1+8Sz+njZwSY7OT4yE2frMbfLtN5u2lbFIwTUnZFTEHsB/vTiXjpOow8Np02h1qa8riwvDVEY6i0W7O/UUCQHZA7nyCa8Q0J35u5lClp4tA6j2aTVK15JQgkmP2nt5iaLcpMOhOxnYRBoKKxFVeRhnSUZKg0sF3UCwAnCl4DJw=";

    // 沙箱测试商户的私钥
    public static String app_private_key_dev = "************************************************************************************************************************************";

    // 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJpKhtEtnVlnTj4QEgCGFd3INvXZu0DaLg5ew/xPGmvmlcG48G3F0nWG4I9XE0k0MKOQmbenEcLgdq23qEmnPMAY23jLu4qCmmY394/Bc1N+Sd/SqL73ueAr1f0gwAZJL0wx+9+zau39KdI9eRjgukRvmTHM/eJ+NpsnUknM45PQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //public static String notify_url = "https://orders.lianjiuhuishou.com/orders/alipayweb/wapPayreturn";
    public static String notify_url =  "https://orders.lianjiuhuishou.com/orders/alipayApp/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://m.lianjiuhuishou.com/orderform";

    // 签名方式
    public static String sign_type = "RSA";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset = "utf-8";

    // 应用ID
    public static String app_id = "2017101609336078";

    // 沙箱测试应用ID
    public static String app_id_dev = "***********************";

    // 上线正式地址
    public static String order_url = "https://openapi.alipay.com/gateway.do";

    // 沙箱测试地址
    public static String order_dev_url = "https://openapi.alipaydev.com/gateway.do";

    // 格式
    public static String data_format = "JSON";

}
