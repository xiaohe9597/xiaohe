package com.example.demo.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author MG02004
 * @createTime 2022/9/2 16:05
 * @description
 */
@Component
public class CrossFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CrossFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("CrossFilter init method...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("CrossFilter doFilter method enter...");

        logger.info("CrossFilter doFilter method out...");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
