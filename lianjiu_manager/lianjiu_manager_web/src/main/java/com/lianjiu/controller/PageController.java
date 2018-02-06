package com.lianjiu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转controller
 * <p>
 * Title: PageController
 * </p>
 * <p>
 * Description:
 * </p>
 */
@Controller
@RequestMapping("/page")
public class PageController {
	/**
	 * 展示其他页面
	 * <p>
	 * Title: showpage
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showpage(@PathVariable String page) {
		System.out.println("拦截到页面：" + page);
		return page;
	}
}
