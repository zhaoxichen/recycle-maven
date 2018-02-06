package com.lianjiu.service.RefoundConfig;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

/**
 * 创建时间：2016年11月10日 下午7:09:08
 * 
 * alipay支付
 * 
 * @author andy
 * @version 2.2
 */

public class AlipayUtil {

	public static final String ALIPAY_APPID = "2017101609336078"; // appid
	//应用密钥
	public static String APP_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMmkqG0S2dWWdOPhASAIYV3cg29dm7QNouDl7D/E8aa+aVwbjwbcXSdYbgj1cTSTQwo5CZt6cRwuB2rbeoSac8wBjbeMu7ioKaZjf3j8FzU35J39Kovve54CvV/SDABkkvTDH737Nq7f0p0j15GOC6RG+ZMcz94n42mydSSczjk9AgMBAAECgYAwKTDFJHx85Du09dfT88x1o6aM4dm/joY5mxDrRjnFbrMxea+Wnuls7VUStLS+LvITLf5acUs0xNWmsxc3NsCzKxAMCdvsn1rLMbN99GCZLTYyX4GOa6IBXMBGcrEVdOevaGfnhJqo7u3hp2vEr/1eOhOKbm/xmo5xJ/92jj3CLQJBAPt+u8A7oOnVoqrxvx+ImkDkodEuBjdOIEqKv8DS0HeUF4U8TyP2xP8YK+OGzioDg+JujQU/KM2FomD/PWu8Cu8CQQDNQVJZchoDICUTjmb2NWGlDowp1CkAaM9Em9TtGoDdhY5tT9l3EJc2KxFPResWaVXVJh+W4WKr/el+CIe78y6TAkEAhjf8u3sXRbRePB0IgyvEJx3dvmVxq9JmKWIjNCVOXIMMim7FTcxFFl8wDR3EqWd220FkwYgTnpvTLTEK9pREwQJBAMzoU1+8Sz+njZwSY7OT4yE2frMbfLtN5u2lbFIwTUnZFTEHsB/vTiXjpOow8Np02h1qa8riwvDVEY6i0W7O/UUCQHZA7nyCa8Q0J35u5lClp4tA6j2aTVK15JQgkmP2nt5iaLcpMOhOxnYRBoKKxFVeRhnSUZKg0sF3UCwAnCl4DJw="; // app支付私钥
	//应用公钥
	public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB"; // 支付宝公钥
	//支付宝公钥                                                      
	public static String ALIPAY_ZFB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	
	/*	static {
		try {
			Resource resource = new ClassPathResource("rsa_private_key_pkcs8.pem");
			APP_PRIVATE_KEY = FileUtil.readInputStream2String(resource.getInputStream());
			resource = new ClassPathResource("rsa_public_key.pem");
			ALIPAY_PUBLIC_KEY = FileUtil.readInputStream2String(resource.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	// 统一收单交易创建接口
	private static AlipayClient alipayClient = null;

	public static AlipayClient getAlipayClient() {
		if (alipayClient == null) {
			synchronized (AlipayUtil.class) {
				if (null == alipayClient) {
					alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALIPAY_APPID,
							APP_PRIVATE_KEY, "JSON", "UTF-8",ALIPAY_ZFB_KEY,"RSA");
				}
			}
		}
		return alipayClient;
	}
}
