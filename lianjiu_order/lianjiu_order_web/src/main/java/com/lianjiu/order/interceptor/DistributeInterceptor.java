package com.lianjiu.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截订单付款
 * 
 * @author zhaoxi
 *
 */
public class DistributeInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// handler执行之前
		String contextPath = request.getContextPath();
		System.out.println("拦截到url:" + contextPath);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后，返回ModelAndView之前
		System.out.println("handler执行之后");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 返回ModelAndView之后。
		// 响应用户之后。
		System.out.println("响应用户之后");
	}

}
