package com.example.demo.util;

import com.example.demo.annotation.DateFormat;
import com.example.demo.annotation.Desensitizate;
import com.example.demo.annotation.Dict;
import com.example.demo.bean.PubSystemCode;
import com.example.demo.constants.CommonConstants;
import com.example.demo.eunm.EnumColumnEnum;
import com.example.demo.eunm.StrategyTypeEnum;
import com.example.demo.init.ForkJoinConfig;
import com.example.demo.service.PubSystemCodeService;
import com.github.houbb.sensitive.api.IStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MG02004
 * @createTime 2022/10/9 10:46
 * @description
 */
@Component
@Slf4j
public class DataDictionaryUtil {

    @Autowired
    private ForkJoinConfig forkJoinConfig;

    @Autowired
    private PubSystemCodeService pubSystemCodeService;

    public Object dictionary(Object object) {
        long startTime = System.currentTimeMillis();
        log.info("字典转化开始时间：{}", startTime);
        if (null == object) {
            return null;
        }
        if (object instanceof List) {
            object = processList(object);
        } else {
            object = processObject(object);
        }
        log.info("字典转化总耗间：{} 秒", (System.currentTimeMillis() - startTime) / 1000);
        return object;
    }

    /**
     * 实体对象转换
     *
     * @param object
     * @return
     */
    private Object processObject(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        try {
            return processMethod(object);
        } catch (Exception e) {
            log.info("转化异常：对象转化异常");
        }
        return object;
    }

    /**
     * 实体转换对象
     *
     * @param object
     * @return
     */
    private Object processMethod(Object object) throws Exception {
        Class clzz = object.getClass();
        //如果存在父类，先处理父类的字段
        Class superClazz = clzz.getSuperclass();
        if (superClazz != null && !StringUtils.equalsAnyIgnoreCase(superClazz.getName(), Object.class.getName())) {
            processField(superClazz, object);
        }
        processField(clzz, object);
        return object;
    }

    private void processField(Class clazz, Object object) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                processField(clazz, object, field);
            } catch (Exception e) {
                log.info("转化异常：字段{}转化异常{}", field.getName(), e);
            }
        }
    }

    /**
     * 单个字段转换
     *
     * @param clazz
     * @param object
     * @param field
     * @throws Exception
     */
    public void processField(Class clazz, Object object, Field field) throws Exception {
        Class caz = field.getType();
        field.setAccessible(true);
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            if (parameterizedType.getRawType().equals(List.class)) {
                if (Optional.ofNullable(field.get(object)).isPresent()) {
                    field.set(object, processList(field.get(object)));
                }
            }
        } else if (caz != String.class && !caz.isPrimitive() && caz != Long.class && caz != Double.class && caz != Float.class && caz != Integer.class
                && caz != BigDecimal.class && caz != Number.class && caz != Boolean.class && caz != Date.class) {
            if (Optional.ofNullable(field.get(object)).isPresent()) {
                field.set(object, dictionary(field.get(object)));
            }
        } else if (caz == String.class) {
            if (Optional.ofNullable(field.get(object)).isPresent() && !Objects.equals(field.get(object), "")) {
                if (field.isAnnotationPresent(Dict.class)) {
                    processDict(clazz, object, field);
                } else if (field.isAnnotationPresent(DateFormat.class)) {
                    processDate(clazz, object, field);
                } else if (field.isAnnotationPresent(Desensitizate.class)) {
                    prcessVirtual(clazz, object, field);

                }
            }
        }
    }

    /**
     * 敏感信息脱敏
     *
     * @param clazz
     * @param object
     * @param field
     */
    private void prcessVirtual(Class clazz, Object object, Field field) throws Exception {
        Desensitizate desensitizate = field.getAnnotation(Desensitizate.class);
        IStrategy iStrategy = StrategyTypeEnum.getIStrategy(desensitizate.strategyType());
        if (Objects.nonNull(iStrategy)) {
            field.setAccessible(true);
            field.set(object, iStrategy.des(field.get(object), null));
        }
    }

    /**
     * 日期格式转换
     *
     * @param clazz
     * @param object
     * @param field
     */
    private void processDate(Class clazz, Object object, Field field) throws Exception {
        DateFormat dateFormat = field.getAnnotation(DateFormat.class);
        String date = (String) field.get(object);
        Date oldFormat = DateUtil.parse(date, dateFormat.oldFormat());
        String newFormat = DateUtil.format(oldFormat, dateFormat.newFormat());
        field.setAccessible(true);
        field.set(object, newFormat);
    }

    /**
     * 字段描述转换
     *
     * @param clazz
     * @param object
     * @param field
     */
    private void processDict(Class clazz, Object object, Field field) throws Exception {
        Dict dict = field.getAnnotation(Dict.class);
        if (dict.dictType().equals(CommonConstants.DictType.CODENO)) {
            Field fieldDesc = clazz.getDeclaredField(field.getName().concat(dict.suffix()));
            fieldDesc.setAccessible(true);
            fieldDesc.set(object, Optional.ofNullable(pubSystemCodeService.selectValueByCodeType(dict.dictNo(), (String) field.get(object))).map(
                    PubSystemCode::getCodeName).orElse(""));
        } else if (dict.dictType().equals(CommonConstants.DictType.ENUM)) {
            Field fieldDesc = clazz.getDeclaredField(field.getName().concat(dict.suffix()));
            fieldDesc.setAccessible(true);
            fieldDesc.set(object, EnumColumnEnum.getValue((String) field.get(object)));
        } else if (dict.dictType().equals(CommonConstants.DictType.TABLE)) {
            String[] tabs = dict.tableCode();
            Field fieldDesc = clazz.getDeclaredField(field.getName().concat(dict.suffix()));
            for (int i = 0; i <= tabs.length - 1; i = i + 2) {
                if (tabs[i].equals(field.get(object))) {
                    fieldDesc.setAccessible(true);
                    fieldDesc.set(object, tabs[i + 1]);
                }
            }
        }
    }

    /**
     * list对象转换
     *
     * @param object
     * @return
     */
    private Object processList(Object object) {
        List<Object> list = (List) object;
        List<Object> processList;
        try {
            processList = forkJoinConfig.getForkJoinPool().submit(() -> list.parallelStream()
                    .map(this::dictionary).collect(Collectors.toList())).join();
        } catch (Exception e) {
            log.info("转换异常：数组转换异常");
            return list;
        }
        return processList;
    }

}
