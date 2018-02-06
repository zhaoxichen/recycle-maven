package com.lianjiu.payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.omg.CORBA.ServerRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Main {
	public static void main(String[] args) throws Exception {
		String money = "0.01";
//		System.out.println("money:"+money);
//		BigDecimal bg = new BigDecimal(money);
//		System.out.println("bg:"+bg);
//		System.out.println("money:"+money);
//		System.out.println("Double.valueOf(money) / 100:"+Double.valueOf(money) / 100);
//		System.out.println("Double.valueOf(money) :"+Double.valueOf(money));
//		money = Double.toString(Double.valueOf(money) / 100);
//		System.out.println("money:"+money);
//		Double cash = Double.valueOf(money);
//		//money = String.format("%.2f",cash);
//		//wall.setWalletMoney(money);
//		System.out.println("money:"+money);
		System.out.println("money:"+money);
//		String xx = String.format("%.2f", Float.parseFloat(money));
//
//		System.out.println("xxxxx:"+money);
//		//money = String.valueOf((int) (Double.valueOf(money) * 100));
//		//BigDecimal bg = new BigDecimal(money);
//		//BigDecimal bg1 = new BigDecimal(100);
//		//bg=bg.multiply(bg1);
//		//System.out.println("bg:"+bg);
//		System.out.println("xxxxx:"+xx);
//		System.out.println("xxxxx:"+money);
		
		money = Double.toString(Double.valueOf(money) / 100);
		System.out.println("money:"+money);
		Double cash = Double.valueOf(money);
		System.out.println("cash:"+cash);
		money = String.format("%.2f",cash);
		money = String.format("%.4f",cash);
		
//		money = Double.toString(Double.valueOf(money) / 100);
//		Double cash = Double.valueOf(money);
//		money = String.format("%.2f",cash);
		
		//money = String.valueOf((int) (Double.valueOf(money) * 100));
		System.out.println("money："+money);
	}
	
}
