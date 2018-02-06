package com.lianjiu.rest.web.test.user;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class PriceEvaluateTest {

	@SuppressWarnings("unused")
	private ApplicationContext context;

	@Before
	public void config() {
		context = new FileSystemXmlApplicationContext("classpath:spring/applicationContext-*.xml");// 没有classpath表示当前目录
	}

	public static void main(String[] args) {
		String min = "13244744918";
		// 验证手机号码格式是否正确
		String reg = "^1[3|4|5|7|8][0-9]{9}$";
		if (min.matches(reg)) {
			System.out.println(min);
		} else {
			System.err.println(min);
		}
	}
}
