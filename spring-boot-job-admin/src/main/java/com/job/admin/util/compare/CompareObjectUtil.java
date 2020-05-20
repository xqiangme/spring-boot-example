package com.job.admin.util.compare;


import com.job.admin.util.compare.anno.FieldAliasAttr;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 获取对象-属性值前后-映射工具类
 * <p>
 * 注：
 * 1).支持自定义属性别名详见注解@FieldAliasAttr
 * 2).支持嵌套类
 * 3).支持简单的泛型，示例: List<xxx>
 *
 * @author mengq
 * @see FieldAliasAttr
 */
@Slf4j
public class CompareObjectUtil {

    private static final int ZERO = 0;
    private static final String JAVA_PACK_PREFIX = "java.";
    private static final String EMPTY_STR = "";
    private static final String NULL_STR = "null";
    private static final String GENERIC_START = "<";
    private static final String GENERIC_END = ">";

    /**
     * 获取存在变化的属性-新老内容集
     *
     * @param oldObj 旧对象(不可为空)
     * @param oldObj 新对象(不可为空)
     * @param clazz  对象类(不可为空)
     * @return key:属性名， value :[老值，新值]比较后的Map, 示例 {"username":["lisi","zhangsan"]}
     */
    public static Map<String, Object> getChangeFieldValues(Object oldObj, Object newObj, Class<?> clazz) {
        return getAllFieldValues(oldObj, newObj, clazz, true);
    }

    /**
     * 获取所有属性-新老内容集
     *
     * @param oldObj 旧对象(不可为空)
     * @param oldObj 新对象(不可为空)
     * @param clazz  对象类(不可为空)
     * @return key:属性名， value :[老值，新值]比较后的Map, 示例 {"username":["lisi","zhangsan"]}
     * @author mengqiang
     * @date 2019-09-21
     */
    public static Map<String, Object> getAllFieldValues(Object oldObj, Object newObj, Class<?> clazz) {
        return getAllFieldValues(oldObj, newObj, clazz, false);
    }

    /**
     * 获取同一个类中前后属性值
     *
     * @param oldObj        旧对象(不可为空)
     * @param oldObj        新对象(不可为空)
     * @param clazz         对象类(不可为空)
     * @param diffValueFlag 是否仅输出不同内容的属性
     * @return key:属性名， value :[老值，新值]比较后的Map, 示例 {"username":["lisi","zhangsan"]}
     */
    public static Map<String, Object> getAllFieldValues(Object oldObj, Object newObj, Class<?> clazz, boolean diffValueFlag) {
        //参数非空校验
        if (null == oldObj || null == newObj || null == clazz) {
            return new HashMap<>(ZERO);
        }

        //获取对象中-所有属性(包括父类属性)
        List<Field> allFieldList = getAllFields(clazz);
        if (null == allFieldList || allFieldList.size() == ZERO) {
            return new HashMap<>(ZERO);
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        try {
            for (Field field : allFieldList) {
                //过滤静态变量
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                //属性参数对应的类
                Class filedClass = getClass(field);
                //非自定义类参数
                if (null == filedClass) {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    Method readMethod = pd.getReadMethod();
                    //旧值
                    String oldValue = String.valueOf(readMethod.invoke(oldObj)).replace(NULL_STR, EMPTY_STR);
                    //新值
                    String newValue = String.valueOf(readMethod.invoke(newObj)).replace(NULL_STR, EMPTY_STR);
                    //是否存在变化
                    if (diffValueFlag && oldValue.equals(newValue)) {
                        continue;
                    }
                    String[] object = {oldValue, newValue};
                    resultMap.put(getFileAliasName(field), object);
                } else {
                    //自定义类参数
                    //如果是当前类则过滤
                    if (filedClass.getTypeName().equals(clazz.getTypeName())) {
                        continue;
                    }
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    Method readMethod = pd.getReadMethod();
                    //旧值属性对象
                    Object oldObjFiled = readMethod.invoke(oldObj);
                    //新值属性对象
                    Object newObjFiled = readMethod.invoke(newObj);
                    //子集参数比较
                    Map<String, Object> compareFileValue = getAllFieldValues(oldObjFiled, newObjFiled, filedClass, diffValueFlag);
                    //返回为空则过滤
                    if (null == compareFileValue || compareFileValue.size() == ZERO) {
                        continue;
                    }
                    resultMap.put(getFileAliasName(field), compareFileValue);
                }
            }
        } catch (Exception ex) {
            log.error("[ CompareObjectUtil ] >> compare obj  exception ", ex);
            throw new RuntimeException("比较新老对象值发生异常了", ex);
        }
        return resultMap;
    }

    /**
     * 获取类class
     * 注：非java 包下的属性，泛型则只返回泛型内的对象（只支持简单的泛型，例如 List<>）
     */
    private static Class getClass(Field field) {
        String filedTypeName = field.getGenericType().getTypeName();
        //泛型处理
        if (filedTypeName.contains(GENERIC_START)) {
            filedTypeName = filedTypeName
                    .substring(filedTypeName.indexOf(GENERIC_START))
                    .replace(GENERIC_START, EMPTY_STR)
                    .replace(GENERIC_END, EMPTY_STR);
        }

        //是否为java包下的参数
        if (filedTypeName.startsWith(JAVA_PACK_PREFIX)) {
            return null;
        }

        Class<?> aClass = null;
        try {
            aClass = Class.forName(filedTypeName);
        } catch (Exception e) {
            return null;
        }
        return aClass;
    }

    /**
     * 属性名称
     * 默认返回属性原名称，使用别名后用别名替换
     *
     * @param field
     */
    private static String getFileAliasName(Field field) {
        // 是否使用了FormAttribute注解
        if (field.isAnnotationPresent(FieldAliasAttr.class)) {
            FieldAliasAttr fieldAlias = field.getAnnotation(FieldAliasAttr.class);
            if ("".equals(fieldAlias.alias().trim())) {
                return field.getName();
            }
            return fieldAlias.alias().trim();
        }
        return field.getName();
    }

    /**
     * 获取类的所有属性（包括父类）
     */
    private static List<Field> getAllFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

}