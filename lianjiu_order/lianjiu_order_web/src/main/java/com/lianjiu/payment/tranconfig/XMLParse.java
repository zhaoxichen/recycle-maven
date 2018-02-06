package com.lianjiu.payment.tranconfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;


/**
 *  解析xml的另外两种方法，第二种有获取子节点。 
 *  该类没被使用到，使用的jar包是 jdom-1.0.jar，根据项目已有jar包使用，减少冗余。
 *
 */
public class XMLParse {
	/**
	 * 解析xml,第二种方法     返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Map doXMLParse(String strxml)  {
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream in = null;
		try {
			strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

			if(null == strxml || "".equals(strxml)) {
				return null;
			}
			
			in  = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			List list = root.getChildren();
			Iterator it = list.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String k = e.getName();
				String v = "";
				List children = e.getChildren();
				if(children.isEmpty()) {
					v = e.getTextNormalize();
				} else {
					v = XMLParse.getChildrenText(children);
				}
				map.put(k, v);
			}
		}catch (JDOMException je ) {
			je.printStackTrace();
	    } catch (IOException e1) {
			e1.printStackTrace();
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
	 * 获取子结点的xml
	 * @param children
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(XMLParse.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
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
//		return new String(sb.toString().getBytes(), "ISO8859-1");
		return sb.toString();
	}
	
	
	
	/**
	 * 解析xml 第三种方法
	 * @param xml
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}	
	
}
