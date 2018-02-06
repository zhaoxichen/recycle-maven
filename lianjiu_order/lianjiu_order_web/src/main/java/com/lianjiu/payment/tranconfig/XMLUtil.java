package com.lianjiu.payment.tranconfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 解析xml的一种方法，此类针对微信企业付款全一级节点，没有考虑子节点，如有需要自己写子节点方法。
 * 该类 使用的jar包是  xml-apis-1.0.b2.jar（）
 *
 */
public class XMLUtil {
	
	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。 
	 * 微信
	 * @param strxml
	 * @return
	 * @throws Exception 
	 * @throws JDOMException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Map doXMLParse(String strxml){
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream in = null;
		try {
			strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
			if (null == strxml || "".equals(strxml)) {
				return null;
			}
			in  = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(in);
			Element root = doc.getDocumentElement();
			NodeList collegeNodes = root.getChildNodes();  
			for (int i = 0; i < collegeNodes.getLength(); i++) {
				Node college = collegeNodes.item(i);
				if (college != null && college.getNodeType() == Node.ELEMENT_NODE) {
					String k = college.getNodeName();
					String v = college.getTextContent();
					map.put(k, v);
				}
			}
		}catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}finally{ 
			//关闭流
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	
	/**
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters  请求参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getRequestXml(TreeMap<String, String> parameters) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
			}else {
				sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	

}
