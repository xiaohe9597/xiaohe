package com.example.demo.config;

import com.example.demo.filter.CrossFilter;
import com.example.demo.filter.ServerletFilter;
import com.example.demo.interceptor.ServerletInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Arrays;

/**
 * @author MG02004
 * @createTime 2022/8/31 15:59
 * @description
 */
@Configuration
public class ServerletConfig extends WebMvcConfigurationSupport {

    /**
     *
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        ServerletInterceptor serverletInterceptor = new ServerletInterceptor();
        registry.addInterceptor(serverletInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean<CrossFilter> filterRegistrationBean(){
        FilterRegistrationBean<CrossFilter> filterRegistrationBean = new FilterRegistrationBean<CrossFilter>();
        CrossFilter crossFilter = new CrossFilter();
        filterRegistrationBean.setFilter(crossFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<ServerletFilter> filterRegistrationBean2(){
        FilterRegistrationBean<ServerletFilter> filterRegistrationBean = new FilterRegistrationBean<ServerletFilter>();
        ServerletFilter serverletFilter = new ServerletFilter();
        filterRegistrationBean.setFilter(serverletFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}
