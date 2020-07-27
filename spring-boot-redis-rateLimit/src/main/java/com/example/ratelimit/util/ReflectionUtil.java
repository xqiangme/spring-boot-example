package com.example.ratelimit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 反射工具
 *
 * @author 程序员小强
 */
public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    private static final String JAVA_PACKAGE_PREFIX = "java.";
    private static final String JAVAX_PACKAGE_PREFIX = "javax.";

    /**
     * 根据属性名获取属性元素，
     * 包括各种安全范围和所有父类
     *
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getFieldByClazz(String fieldName, Object object) {
        Field field = null;
        Class<?> clazz = object.getClass();
        try {
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    //子类中查询不到属性-继续向父类查
                    field = clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException ignored) {
                }
            }
            if (null == field) {
                return null;
            }
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            //通过反射获取 属性值失败
            logger.error("[ ReflectionUtil ] >> [getFieldByClazz] fieldName:{} ", fieldName, e);
        }
        return null;
    }

    /**
     * 判断类-是否为Jdk自带类
     *
     * @param clazz
     * @return
     */
    public static boolean isJdkClazz(Class clazz) {
        if (null == clazz) {
            return false;
        }

        //是否为基本类型
        if (clazz.isPrimitive()) {
            return true;
        }

        // 全类名以 java. 或者 javax.前缀
        boolean javaPackage = (clazz.getName().startsWith(JAVA_PACKAGE_PREFIX) || clazz.getName().startsWith(JAVAX_PACKAGE_PREFIX));

        return javaPackage && (null == clazz.getClassLoader());
    }

    /**
     * 判断对象属性是否是基本数据类型,包括是否包括string | BigDecimal
     *
     * @param clazz
     * @return
     */
    public static boolean isBaseType(Class clazz) {
        if (null == clazz) {
            return false;
        }
        //基本类型
        if (clazz.isPrimitive()) {
            return true;
        }
        //String
        if (clazz.equals(String.class)) {
            return true;
        }
        //Integer
        if (clazz.equals(Integer.class)) {
            return true;
        }
        //Boolean
        if (clazz.equals(Boolean.class)) {
            return true;
        }
        //BigDecimal
        if (clazz.equals(BigDecimal.class)) {
            return true;
        }
        //Byte
        if (clazz.equals(Byte.class)) {
            return true;
        }
        //Long
        if (clazz.equals(Long.class)) {
            return true;
        }
        //Double
        if (clazz.equals(Double.class)) {
            return true;
        }
        //Float
        if (clazz.equals(Float.class)) {
            return true;
        }
        //Character
        if (clazz.equals(Character.class)) {
            return true;
        }
        //Short
        return clazz.equals(Short.class);
    }
}