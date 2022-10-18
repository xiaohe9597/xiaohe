package com.example.demo.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * link: https://blog.csdn.net/weixin_33005117/article/details/125918869
 * @author MG02004
 * @createTime 2022/10/14 11:37
 * @description
 */
//@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
//        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
//        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class SqlExecuteTimeCountInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(SqlExecuteTimeCountInterceptor.class);

    /**
     * 打印的参数字符串的最大长度
     */
    private final static int MAX_PARAM_LENGTH = 50;

    /**
     * 记录的最大SQL长度
     */
    private final static int MAX_SQL_LENGTH = 500;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();
        String methodName = invocation.getMethod().getName();
        Object[] args = invocation.getArgs();
        boolean execFlag = true;
        try {
            return invocation.proceed();
        } catch (InvocationTargetException e1) {
            execFlag = false;
            logger.error(e1.getTargetException().getMessage());
            throw e1;
        } catch (IllegalAccessException e2) {
            execFlag = false;
            logger.error(e2.getMessage());
            throw e2;
        } finally {
            long endTime = System.currentTimeMillis();
            long sqlCost = endTime - startTime;

            BoundSql boundSql = null;

            for (Object arg : args) {
                if (arg instanceof BoundSql) {
                    boundSql = (BoundSql) arg;
                }
            }

            if (boundSql == null) {
                MappedStatement ms = (MappedStatement) args[0];
                Object paramObj = args[1];//参数对象
                boundSql = ms.getBoundSql(paramObj);
            }

            String sql = boundSql.getSql();
            Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
            int sec = (int) (sqlCost / 1000);
            // 格式化Sql语句，去除换行符，替换参数
            sql = formatSql(sql, parameterObject, parameterMappingList);
            logger.info("执行SQL：[ " + sql + " ] 执行时间：[" + sqlCost + "ms]|统计:SQL统计|" + (execFlag ? "成功" : "失败") + "|"
                    + TimeScale.getString(sec)
            );
            // logger.info("执行SQL的参数：[ " +messageParameters(parameterMappingList));
        }
    }

    /**
     * 格式化sql
     *
     * @param sql
     * @param parameterObject
     * @param parameterMappingList
     * @return
     */
    @SuppressWarnings("unchecked")
    private String formatSql(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
        // 输入sql字符串空判断
        if (sql == null || sql.length() == 0) {
            return "";
        }

        // 美化sql
        sql = beautifySql(sql);

        // 不传参数的场景，直接把Sql美化一下返回出去
        if (parameterObject == null || parameterMappingList == null || parameterMappingList.size() == 0) {
            return sql;
        }

        // 定义一个没有替换过占位符的sql，用于出异常时返回
        String sqlWithoutReplacePlaceholder = sql;
        //TODO
        try {
            if (parameterMappingList != null) {
                Class<?> parameterObjectClass = parameterObject.getClass();

                // 如果参数是StrictMap且Value类型为Collection，获取key="list"的属性，这里主要是为了处理<foreach>循环时传入List这种参数的占位符替换
                // 例如select * from xxx where id in <foreach collection="list">...</foreach>
                if (isStrictMap(parameterObjectClass)) {
                    DefaultSqlSession.StrictMap<Collection<?>> strictMap = (DefaultSqlSession.StrictMap<Collection<?>>) parameterObject;

                    if (isList(strictMap.get("list").getClass())) {
                        sql = handleListParameter(sql, strictMap.get("list"));
                    }
                } else if (isMap(parameterObjectClass)) {
                    // 如果参数是Map则直接强转，通过map.get(key)方法获取真正的属性值
                    // 这里主要是为了处理<insert>、<delete>、<update>、<select>时传入parameterType为map的场景
                    Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
                    sql = handleMapParameter(sql, paramMap, parameterMappingList);
                } else {
                    // 通用场景，比如传的是一个自定义的对象或者八种基本数据类型之一或者String
                    sql = handleCommonParameter(sql, parameterMappingList, parameterObjectClass, parameterObject);
                }
            }
        } catch (Exception e) {
            // 占位符替换过程中出现异常，则返回没有替换过占位符但是格式美化过的sql，这样至少保证sql语句比BoundSql中的sql更好看
            return sqlWithoutReplacePlaceholder;
        }

        sql = beautifySql(sql);
        return sql;
    }

    /**
     * 返回限制长度之后的SQL语句
     *
     * @param sql                  原始SQL语句
     * @param parameterObject
     * @param parameterMappingList
     */
    private String limitSQLLength(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
        if (sql == null || sql.length() == 0) {
            return "";
        }
        Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
        StringBuilder paramsBuilder = new StringBuilder("\n参数列表：");
        parameterMap.forEach((key, value) -> {
            parameterMappingList.forEach(parameterMapping -> {
                if (parameterMapping.getProperty().equals(key)) {
                    String detail = "[" + key + ":" + value + "]；";
                    paramsBuilder.append(detail);
                }
            });
        });
        sql += paramsBuilder.toString();
        if (sql.length() > MAX_SQL_LENGTH) {
            return sql.substring(0, MAX_SQL_LENGTH);
        } else {
            return sql;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 美化sql
     *
     * @param sql sql语句
     */
    private String beautifySql(String sql) {
        sql = sql.replace("\n", " ").replace("\t", " ").replace("  ", " ").replace("( ", "(").replace(" )", ")").replace(" ,", ",");
        return sql;
    }

    /**
     * 处理参数为List的场景
     */
    private String handleListParameter(String sql, Collection<?> col) {
        String message = "";
        try {
            if (col != null && col.size() != 0) {
                for (Object obj : col) {
                    String value = null;
                    Class<?> objClass = obj.getClass();

                    // 只处理基本数据类型、基本数据类型的包装类、String这三种
                    // 如果是复合类型也是可以的，不过复杂点且这种场景较少，写代码的时候要判断一下要拿到的是复合类型中的哪个属性
                    if (isPrimitiveOrPrimitiveWrapper(objClass)) {
                        value = obj.toString();
                    } else if (objClass.isAssignableFrom(String.class)) {
                        value = "\"" + obj.toString() + "\"";
                    }
                    message += (value + ";");
                    sql = sql.replaceFirst("\\?", value);
                }
            }
        } finally {
            logger.info("sql:" + sql + "|参数:" + message);
        }
        return sql;
    }

    /**
     * 处理参数为Map的场景
     */
    private String handleMapParameter(String sql, Map<?, ?> paramMap, List<ParameterMapping> parameterMappingList) {
        String message = "";
        try {
            for (ParameterMapping parameterMapping : parameterMappingList) {
                Object propertyName = parameterMapping.getProperty();
                Object propertyValue = paramMap.get(propertyName);
                if (propertyValue != null) {
                    if (propertyValue.getClass().isAssignableFrom(String.class)) {
                        propertyValue = "\"" + propertyValue + "\"";
                    }
                    sql = sql.replaceFirst("\\?", propertyValue.toString());
                }
                message += (propertyName + "=" + propertyValue + ";");
            }
        } finally {
            logger.info("sql:" + sql + "|参数:" + message);
        }
        return sql;
    }

    /**
     * 处理通用的场景
     */
    private String handleCommonParameter(String sql, List<ParameterMapping> parameterMappingList, Class<?> parameterObjectClass,
                                         Object parameterObject) throws Exception {
        String message = "";
        Map<String, Object> map = (Map<String, Object>) parameterObject;
        try {
            for (ParameterMapping parameterMapping : parameterMappingList) {
                String propertyValue = null;
                String propertyName = null;
                // 基本数据类型或者基本数据类型的包装类，直接toString即可获取其真正的参数值，其余直接取paramterMapping中的property属性即可
                if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
                    propertyValue = parameterObject.toString();
                } else {
                    propertyName = parameterMapping.getProperty();
//                    Field field = parameterObjectClass.getDeclaredField(propertyName);
//                    // 要获取Field中的属性值，这里必须将私有属性的accessible设置为true
//                    field.setAccessible(true);
//                    propertyValue = String.valueOf(field.get(parameterObject));
                    if (parameterMapping.getJavaType().isAssignableFrom(String.class)) {
                        propertyValue = "\"" + map.get(propertyName) + "\"";
                    }
                    message += (propertyName + "=" + propertyValue + ";");
                }
                sql = sql.replaceFirst("\\?", propertyValue);
            }
        } catch (Exception e) {
                logger.info("sql 解析出错：" + e);
        } finally {
            logger.info("sql:" + sql + "|参数:" + message);
        }
        return sql;
    }

    /**
     * 是否基本数据类型或者基本数据类型的包装类
     */
    private boolean isPrimitiveOrPrimitiveWrapper(Class<?> parameterObjectClass) {
        return parameterObjectClass.isPrimitive() ||
                (parameterObjectClass.isAssignableFrom(Byte.class) || parameterObjectClass.isAssignableFrom(Short.class) ||
                        parameterObjectClass.isAssignableFrom(Integer.class) || parameterObjectClass.isAssignableFrom(Long.class) ||
                        parameterObjectClass.isAssignableFrom(Double.class) || parameterObjectClass.isAssignableFrom(Float.class) ||
                        parameterObjectClass.isAssignableFrom(Character.class) || parameterObjectClass.isAssignableFrom(Boolean.class));
    }

    /**
     * 是否DefaultSqlSession的内部类StrictMap
     */
    private boolean isStrictMap(Class<?> parameterObjectClass) {
        return parameterObjectClass.isAssignableFrom(DefaultSqlSession.StrictMap.class);
    }

    /**
     * 是否List的实现类
     */
    private boolean isList(Class<?> clazz) {
        Class<?>[] interfaceClasses = clazz.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(List.class)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否Map的实现类
     */
    private boolean isMap(Class<?> parameterObjectClass) {
        Class<?>[] interfaceClasses = parameterObjectClass.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(Map.class)) {
                return true;
            }
        }

        return false;
    }

//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object target = invocation.getTarget();
//        long startTime = System.currentTimeMillis();
//        StatementHandler statementHandler = (StatementHandler) target;
//        try {
//            return invocation.proceed();
//        } finally {
//            long endTime = System.currentTimeMillis();
//            long timeCount = endTime - startTime;
//
//            BoundSql boundSql = statementHandler.getBoundSql();
//            String sql = boundSql.getSql();
//            Object parameterObject = boundSql.getParameterObject();
//            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
//
//            // 格式化Sql语句，去除换行符，替换参数
//            sql = formatSQL(sql, parameterObject, parameterMappingList);
//
//            logger.info("执行 SQL：[{}]执行耗时[ {} ms])", sql, timeCount);
//        }
//    }
//
//    /**
//     * 格式化/美化 SQL语句
//     *
//     * @param sql                  sql 语句
//     * @param parameterObject      参数的Map
//     * @param parameterMappingList 参数的List
//     * @return 格式化之后的SQL
//     */
//    private String formatSQL(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
//        // 输入sql字符串空判断
//        if (sql == null || sql.length() == 0) {
//            return "";
//        }
//        // 美化sql
//        sql = beautifySql(sql);
//        // 不传参数的场景，直接把sql美化一下返回出去
//        if (parameterObject == null || parameterMappingList == null || parameterMappingList.size() == 0) {
//            return sql;
//        }
//        return limitSQLLength(sql, parameterObject, parameterMappingList);
//    }
//
//    /**
//     * 返回限制长度之后的SQL语句
//     *
//     * @param sql                  原始SQL语句
//     * @param parameterObject
//     * @param parameterMappingList
//     */
//    private String limitSQLLength(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
//        if (sql == null || sql.length() == 0) {
//            return "";
//        }
//        Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
//        StringBuilder paramsBuilder = new StringBuilder("\n参数列表：");
//        parameterMap.forEach((key, value) -> {
//            parameterMappingList.forEach(parameterMapping -> {
//                if (parameterMapping.getProperty().equals(key)) {
//                    String detail = "[" + key + ":" + value + "]；";
//                    paramsBuilder.append(detail);
//                }
//            });
//        });
//        sql += paramsBuilder.toString();
//        if (sql.length() > MAX_SQL_LENGTH) {
//            return sql.substring(0, MAX_SQL_LENGTH);
//        } else {
//            return sql;
//        }
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//
//    /**
//     * 美化sql
//     *
//     * @param sql sql语句
//     */
//    private String beautifySql(String sql) {
//        sql = sql.replaceAll("[\\s\n ]+", "  ");
//        return sql;
//    }

}
