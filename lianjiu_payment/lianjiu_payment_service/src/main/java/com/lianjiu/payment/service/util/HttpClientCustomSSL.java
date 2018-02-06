package com.lianjiu.payment.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.KeyStore;
import java.util.List;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 微信转账，http带签名请求
 * @author ligp
 * @date 2015-12-28
 */
@SuppressWarnings("deprecation")
public class HttpClientCustomSSL {
	/**
	 * httpClient 带证书请求，该方法亲测使用，下面两种，一个是不同请求方式，另一个是不同参数封装方法，编码都有问题，没空测。
	 * @param parms
	 * @return  处理过的xml格式字符
	 * @throws Exception
	 */
	public static String httpClientResult(TreeMap<String, String> parms) throws Exception{
		StringBuffer reultBuffer = new StringBuffer();
	
		String requestXML = XMLUtil.getRequestXml(parms);// 封装好的请求XML
		SSLConnectionSocketFactory sslsf = ReadSSl.getInstance().readCustomSSL();//获取证书
		
		HttpPost httpPost = new HttpPost(WeChatConfig.POST_URL);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        StringEntity myEntity = new org.apache.http.entity.StringEntity(requestXML, WeChatConfig.CHARSET);
        myEntity.setContentType("text/xml;charset=UTF-8");
        myEntity.setContentEncoding(WeChatConfig.CHARSET);
        httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");
        httpPost.setEntity(myEntity);
        
        CloseableHttpResponse response      = null;
        InputStream inputStream		        = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader       = null;
        try {
        	response = httpclient.execute(httpPost);
        	HttpEntity entity = response.getEntity();
			if (entity!=null){
				inputStream = entity.getContent();
				inputStreamReader = new InputStreamReader(inputStream, WeChatConfig.CHARSET);
				bufferedReader = new BufferedReader(inputStreamReader);
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					reultBuffer.append(str);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			// 释放资源
			httpclient.close();
			response.close();
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
		}
        System.out.println("httpclient结果:"+reultBuffer.toString());
        return reultBuffer.toString();
	}
	
	
	/**
	 * 第二种
	 */
	public static String httpsUrlRequest(String requestUrl, String outputStr) {
		try {
	        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File("D:/wechat/cert/apiclient_cert.p12"));
	        try {
	            keyStore.load(instream, WeChatConfig.MCHID.toCharArray());
	        } finally {
	            instream.close();
	        }
	        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WeChatConfig.MCHID.toCharArray()).build();
	        
//	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslcontext, new String[] { "TLSv1" }, null,
//	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        
			SSLSocketFactory ssf = sslcontext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod("post");
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes(WeChatConfig.CHARSET));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, WeChatConfig.CHARSET);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 *  第三种
	 * @throws Exception
	 */
	public void requestThree() throws Exception{
		    TreeMap<String, String> parms = new TreeMap<String, String>(); 
		    KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File("D:/wechat/cert/apiclient_cert.p12"));
	        try {
	            keyStore.load(instream, "1296637601".toCharArray());
	        } finally {
	            instream.close();
	        }

	        // Trust own CA and all self-signed certs
	        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1296637601".toCharArray()).build();
	        // Allow TLSv1 protocol only
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( sslcontext, new String[] { "TLSv1" }, null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			
            HttpPost httpPost = new HttpPost(WeChatConfig.POST_URL);

            httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");
			List<NameValuePair> nvps = WeChatUtil.generatListNameValuePair(parms);
			UrlEncodedFormEntity uene = new UrlEncodedFormEntity(nvps,"utf-8");
			uene.setContentType("text/xml;charset=UTF-8");
			try {
				httpPost.setEntity(uene);  
				CloseableHttpResponse response = httpclient.execute(httpPost);
				
				HttpEntity entity = response.getEntity();
				if (entity!=null){
					InputStream inputStream = entity.getContent();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					String str = null;
					StringBuffer buffer = new StringBuffer();
					while ((str = bufferedReader.readLine()) != null) {
						buffer.append(str);
					}
					// 释放资源
					bufferedReader.close();
					inputStreamReader.close();
					inputStream.close();
					inputStream = null;
					buffer.toString();
					System.out.println("httpclient结果:"+buffer.toString());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
