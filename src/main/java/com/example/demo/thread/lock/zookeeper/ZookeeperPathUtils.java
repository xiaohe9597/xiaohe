package com.example.demo.thread.lock.zookeeper;

/**
 * @author MG02004
 * @createTime 2023/7/13 17:50
 * @description
 */
public class ZookeeperPathUtils {

    public static String getRootPath(String appName) {
        return "/" + appName;
    }

    public static String getSubPath(String appName, String serviceName, String path) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/").append(appName);
        stringBuffer.append("/").append(serviceName);
        stringBuffer.append("/").append(path);
        return stringBuffer.toString();
    }
}
