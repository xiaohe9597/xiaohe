package com.example.demo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author MG02004
 * @createTime 2022/9/2 14:52
 * @description
 */
@Component
public class ServerletFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ServerletFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("ServerletFilter init method...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("ServerletFilter doFilter method enter...");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestUri = httpServletRequest.getRequestURI();
        System.out.println(requestUri);
        logger.info("ServerletFilter doFilter method out...");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
