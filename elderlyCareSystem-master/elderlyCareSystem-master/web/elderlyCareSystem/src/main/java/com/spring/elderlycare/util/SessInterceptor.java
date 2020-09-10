package com.spring.elderlycare.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler)throws Exception{
		HttpSession sess = request.getSession();
		if(sess.getAttribute("uid") == null) {
			response.sendRedirect(request.getContextPath()+"/users/login");
			
			return false;
		}else return true;//컨트롤러 실행
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)throws Exception{
		super.postHandle(request, response, handler, modelAndView);
	}
}