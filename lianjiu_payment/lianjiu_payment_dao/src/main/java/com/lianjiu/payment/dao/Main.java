package com.lianjiu.payment.dao;

public class Main {
	public static void main(String[] args) throws Exception {
	/*	String xx = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxpayGZHreturn";
		System.out.println("xx:"+xx);
		String xxx = URLEncoder.encode(xx);
		System.out.println("xxx:"+xxx);
		String xxxx = URLEncoder.encode(xx, "UTF-8");
		System.out.println("xxxx:"+xxxx);*/
		/*String money = "100";
		float sessionmoney = Float.parseFloat(money);
		System.out.println("sessionmoney:"+sessionmoney);
		String finalmoney = String.format("%.2f", sessionmoney);
		System.out.println("finalmoney:"+finalmoney);
		finalmoney = finalmoney.replace(".", "");
		System.out.println("finalmoney:"+finalmoney);
		money = String.valueOf((int) (Double.valueOf(money) * 100));
		System.out.println("money:"+money);
		int total_fee = Integer.parseInt(money);
		System.out.println("total_fee:"+total_fee);*/
		/*KeyStore keyStore = KeyStore.getInstance("PKCS12");
		System.out.println("keyStore:"+keyStore.toString());
		String keyStoreurl = "H:/cert/apiclient_cert.p12";
		FileInputStream instream = new FileInputStream(new File(keyStoreurl));
		try {
			// 指定PKCS12的密码(商户ID)
			keyStore.load(instream, "1489873322".toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1489873322".toCharArray()).build();
		System.out.println("sslcontext:"+sslcontext.toString());
		// 指定TLS版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
				null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		// 设置httpclient的SSLSocketFactory
		System.out.println("sslsf:"+sslsf.toString());
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		System.out.println("httpclient:"+httpclient.toString());

		String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

		HttpPost httpPost = new HttpPost(url);// 退款接口
		 */		
		System.out.println(System.getProperty("user.dir"));
//		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = sra.getRequest();
//		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//		System.out.println("request.getRequestURL():"+request.getRequestURL());
	}
	
}
