package com.lianjiu.payment.tranconfig;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
/**
 * 读取证书
 * @author zzb
 *
 */
@SuppressWarnings("deprecation")
public class ReadSSl {
	private static ReadSSl readSSL = null;
	
	private ReadSSl(){
		
	}
	
	public static ReadSSl getInstance(){
		if(readSSL == null){
			readSSL = new ReadSSl();
		}
		return readSSL;
	}
	/**
	 *  读取 apiclient_cert.p12 证书
	 *  WeChatConfig.MCHID 为证书密码
	 * @return
	 * @throws Exception
	 */
	public  SSLConnectionSocketFactory readCustomSSL() throws Exception{
	    KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(WeChatConfig.CA_LICENSE));
        try {
            keyStore.load(instream, WeChatConfig.MCHID.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WeChatConfig.MCHID.toCharArray()).build();
        
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        return sslsf;
	}

}
