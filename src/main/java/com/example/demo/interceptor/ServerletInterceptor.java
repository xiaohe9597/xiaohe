package com.example.demo.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author MG02004
 * @createTime 2022/9/2 15:20
 * @description
 */
@Component
public class ServerletInterceptor implements HandlerInterceptor {

    public static final Logger logger = LoggerFactory.getLogger(ServerletInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("ServerletInterceptor preHandle enter...");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        logger.info("ServerletInterceptor postHandle enter...");
        String requestUri = request.getRequestURI();
        System.out.println(requestUri);
        logger.info("ServerletInterceptor postHandle out...");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
