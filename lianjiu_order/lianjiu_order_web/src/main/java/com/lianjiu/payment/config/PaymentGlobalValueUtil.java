package com.lianjiu.payment.config;

/**
 * 全局参数字段
 * 
 * @author zhaoxi
 *
 */
public class PaymentGlobalValueUtil {
	// 创建时间：2017年12月12日 支付宝支付

	/**
	 * 这里notify_url是 支付完成后支付宝发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
	 */
	public static final String ALIPAY_NOTIFY_URL = "https://orders.lianjiuhuishou.com/orders/alipayApp/notify";
	/**
	 * appid
	 */
	public static final String ALIPAY_APPID = "2017101609336078"; 
	/**
	 * 应用密钥
	 */
	public static final String APP_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMmkqG0S2dWWdOPhASAIYV3cg29dm7QNouDl7D/E8aa+aVwbjwbcXSdYbgj1cTSTQwo5CZt6cRwuB2rbeoSac8wBjbeMu7ioKaZjf3j8FzU35J39Kovve54CvV/SDABkkvTDH737Nq7f0p0j15GOC6RG+ZMcz94n42mydSSczjk9AgMBAAECgYAwKTDFJHx85Du09dfT88x1o6aM4dm/joY5mxDrRjnFbrMxea+Wnuls7VUStLS+LvITLf5acUs0xNWmsxc3NsCzKxAMCdvsn1rLMbN99GCZLTYyX4GOa6IBXMBGcrEVdOevaGfnhJqo7u3hp2vEr/1eOhOKbm/xmo5xJ/92jj3CLQJBAPt+u8A7oOnVoqrxvx+ImkDkodEuBjdOIEqKv8DS0HeUF4U8TyP2xP8YK+OGzioDg+JujQU/KM2FomD/PWu8Cu8CQQDNQVJZchoDICUTjmb2NWGlDowp1CkAaM9Em9TtGoDdhY5tT9l3EJc2KxFPResWaVXVJh+W4WKr/el+CIe78y6TAkEAhjf8u3sXRbRePB0IgyvEJx3dvmVxq9JmKWIjNCVOXIMMim7FTcxFFl8wDR3EqWd220FkwYgTnpvTLTEK9pREwQJBAMzoU1+8Sz+njZwSY7OT4yE2frMbfLtN5u2lbFIwTUnZFTEHsB/vTiXjpOow8Np02h1qa8riwvDVEY6i0W7O/UUCQHZA7nyCa8Q0J35u5lClp4tA6j2aTVK15JQgkmP2nt5iaLcpMOhOxnYRBoKKxFVeRhnSUZKg0sF3UCwAnCl4DJw="; // app支付私钥
	/**
	 * 应用公钥
	 */
	public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJpKhtEtnVlnTj4QEgCGFd3INvXZu0DaLg5ew/xPGmvmlcG48G3F0nWG4I9XE0k0MKOQmbenEcLgdq23qEmnPMAY23jLu4qCmmY394/Bc1N+Sd/SqL73ueAr1f0gwAZJL0wx+9+zau39KdI9eRjgukRvmTHM/eJ+NpsnUknM45PQIDAQAB"; // 支付宝公钥
	/**
	 * 支付宝公钥
	 */
	public static final String ALIPAY_ZFB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	
	// 创建时间：2017年12月12日 微信支付
	/**
	 * 统一支付接口url,向微信方创建，发起支付请求的url
	 */
	public static final String WECHAT_CREATE_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
	 */
	public static final String WECHAT_NOTIFY_URL = "https://payment.lianjiuhuishou.com/payment/WxPay/wxRollBack";
}
