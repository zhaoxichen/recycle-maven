package lianjiu_rest_service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class revert {
	/**
	 * unicode转中文
	 * 
	 * @param str
	 * @return
	 * @author yutao
	 * @date 2017年1月24日上午10:33:25
	 */
	public static String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

		Matcher matcher = pattern.matcher(str);

		char ch;

		while (matcher.find()) {

			ch = (char) Integer.parseInt(matcher.group(2), 16);

			str = str.replace(matcher.group(1), ch + "");

		}

		return str;

	}

	public static void main(String[] args) {
		String addres = "\u5E7F\u897F\u58EE\u65CF\u81EA\u6CBB\u533A\u6842\u6797\u5E02\u5E73\u4E50\u53BF";
		String string = unicodeToString(addres);
		System.out.println("-----" + string);
	}
}
