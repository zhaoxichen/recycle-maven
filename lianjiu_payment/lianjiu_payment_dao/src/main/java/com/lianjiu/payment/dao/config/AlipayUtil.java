package com.lianjiu.payment.dao.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
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
	public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJpKhtEtnVlnTj4QEgCGFd3INvXZu0DaLg5ew/xPGmvmlcG48G3F0nWG4I9XE0k0MKOQmbenEcLgdq23qEmnPMAY23jLu4qCmmY394/Bc1N+Sd/SqL73ueAr1f0gwAZJL0wx+9+zau39KdI9eRjgukRvmTHM/eJ+NpsnUknM45PQIDAQAB"; // 支付宝公钥
	//支付宝公钥
	public static String ALIPAY_ZFB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	//public static String APP_PRIVATE_KEY2 =  "MIIEpAIBAAKCAQEAnCpkX3XHIWFFbW2eO31/NSwQsSZNYwcO3OT+gA3Wkhnyx4B/IDHrwo6oY9CibrTUvQSWUR2izIyElOwWt3vzj8vfKFEHQdiz+TQ+KjLPkxefjQjmHvuraR2tHn3qsCHmw25bAnvU4kFMK9fu0+e/aFIXYRPLon+3yf1lqPcoka6VvfS4J8ipM/Wroaedh+kF6xwVn9fodM7pm9maKuAw4ntJU0YuHuZjYBjKLayikpCwJDqto7Q3Sz9px4zcjnEPJqoA3emtotd6YpK44FTn2AXslV1QRTrqjWcc/vgvPhoJ5+IMxzrbXqVvjOq7dP6uHv2qdbFaqcI8T0L1my2zcwIDAQABAoIBAGUDptl4vc4BtX8LIQcqLlaeok10kSo1LpAEL/t0+A6ZaklR/Ok+YVoAHTwzgF5L6jA5/2GI4f2eB0StUiRtsSaaoUba4u7c1c2k1XFGXK6w4SvBTdlMwZvE7m6C1IQJ6dMQVx5MK3AO/M5/ZqT7ivxX3XeV7mq2YpPtrQyWk09ntBiuFe+gJPcW882D/Am4yMvmnxFu2GHXnknvZT1CR+DH4OPvzEqnSLC+JQBuW7kjvnCeEUT29BQLx25gX7pH2Lwspeq0m96pozAEEq+S+DUCNt1NEQ9xlyH5u4jHKNS1FL36S+Go0auPZj2RZhgAvhSgH0pU6fx7Vae3gsy750ECgYEAyJkzvlLUA2iD6XHpl8RoPnroW3h/qpMeziXzAIYbxl0DVtNg6z/Ulg1+plgUQbIWGTVtbLkvWhN4fGFKiQPdMUev+FZL57sqx5pB6ybquN+3eVAVihI/h00OVmHjLBV+4cULUSRdOTtpJT+AvRx1yPDSFBwJOhA4Mw9aRrM9NNUCgYEAx0ut5sNJEUqTRNc84LkWSVF60rIChux2FcAe1tNXilb+rTLV2T+rgei2ICvbv0eT6DbCCLZLIE3CadXoMH/zpjFEITh3tmz6zlurLVuoQ7IMMUfdjFf44lYIf3wRxAqFXBmqX0o8KGpwgOmCUfa2RVXLz645UvrOMKcqqS3/iycCgYEAk4LdYwrDr5Hu1v8WeK1Mvw1gysrsjsHLOGr9rBfyWsdRSkr8jWIN8R4RUT+Z/cl4qKb6RaZUJNgGlOhyiBkYUkHWLDdBcDHZvpg3+zVODA8ve30hqbvLjbJjZbBr/qH8AQgGT2//QbPnmSV+hRJlpxM4WONZAaKlwR58E1uBGdUCgYBbqF2xfO0U68Rxa8BO5jDuxW+EiWQKI6RnenV0fbyMHliA1Zlukg7R73Ibt7AX99Z5fP2ePiCtGbYpMLzZ2W74Zz4bPToph1OOJrDCap4njDJ5U8D789W+Wq0L3Mba76/H3PHzY1PpjpJxZ3ONOLc5iizzNgCdPkYoMCm5pzMZDwKBgQC3PfEHVjujfaAslQkRWfdRaEn/7040TeZA3h85J0vH9F5bpYriInPKqywsk+UXoQOS7Lcku9lC5mbo849gqrl5/0AgXic50QlmjIjCk3W5vdg3N8HqFYvHRN94eIVwUxMvwn/o2ZkP/S4nY7o7Sq4jsBp4Sb3v+3tHXhJecMWymQ==";	
	
	//public static String ALIPAY_PUBLIC_KEY2 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnCpkX3XHIWFFbW2eO31/NSwQsSZNYwcO3OT+gA3Wkhnyx4B/IDHrwo6oY9CibrTUvQSWUR2izIyElOwWt3vzj8vfKFEHQdiz+TQ+KjLPkxefjQjmHvuraR2tHn3qsCHmw25bAnvU4kFMK9fu0+e/aFIXYRPLon+3yf1lqPcoka6VvfS4J8ipM/Wroaedh+kF6xwVn9fodM7pm9maKuAw4ntJU0YuHuZjYBjKLayikpCwJDqto7Q3Sz9px4zcjnEPJqoA3emtotd6YpK44FTn2AXslV1QRTrqjWcc/vgvPhoJ5+IMxzrbXqVvjOq7dP6uHv2qdbFaqcI8T0L1my2zcwIDAQAB";
	public static final String AlipayAndroidJMS = "7";//支付宝Android加盟商
	public static final String AlipayAndroidJP = "8";//支付宝Android链旧精品
	public static final String AlipayIOSJMS = "9";//支付宝ios加盟商
	public static final String AlipayIOSJP = "10";//支付宝ios链旧精品
	public static final String AlipayH5 = "11";//支付宝H5
	
	/*static {
		try {
			Resource resource = new ClassPathResource("payConfig/alipay_private_key_pkcs8.pem");
			APP_PRIVATE_KEY = FileUtil.readInputStream2String(resource.getInputStream());
			resource = new ClassPathResource("alipay_public_key.pem");
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
							APP_PRIVATE_KEY, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8,
							ALIPAY_PUBLIC_KEY);
				}
			}
		}
		return alipayClient;
	}
}
