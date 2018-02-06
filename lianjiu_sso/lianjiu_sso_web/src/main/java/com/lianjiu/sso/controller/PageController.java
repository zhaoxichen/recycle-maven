package com.lianjiu.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转Controller
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
	 * 
	 * zhaoxi 2017年8月18日 descrption:注册
	 * 
	 * @return String
	 */
	@RequestMapping("/register")
	public String showRegister() {
		return "register";
	}

	/**
	 * 
	 * zhaoxi 2017年8月18日 descrption:默认pc端登录界面
	 * 
	 * @param redirect
	 * @param model
	 * @return String
	 */
	@RequestMapping("/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}
	/**
	 * @param redirect
	 * @param model
	 * @return
	 */
	@RequestMapping("/adminLogin")
	public String adminLogin(String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "adminLogin";
	}

	/**
	 * 
	 * zhaoxi 2017年8月18日 descrption:切换到微信登录界面
	 * 
	 * @param redirect
	 * @param model
	 * @return String
	 */
	@RequestMapping("/loginWeixin")
	public String showLoginWeixin(String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "loginWeixin";
	}
}
