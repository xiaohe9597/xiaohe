package com.example.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author MG02004
 * @createTime 2023/7/13 17:18
 * @description
 */
@Configuration
public class AppConfig implements EnvironmentAware {

    private String appName;

    private Environment environment;

    public static String ZOOKEEPER_ADDRESS = "zookeeper.address";

    public static final String CONFIG_PRE = "hnct.app.";

    public String getAppName() {
        return appName;
    }

    public String getStr(String str) {
        return environment.getProperty(CONFIG_PRE + str);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        appName = environment.getProperty("spring.application.name");
        if (StringUtils.isBlank(appName)) {
            throw new IllegalArgumentException(String.format("please set name of application first"));
        }
    }
}
