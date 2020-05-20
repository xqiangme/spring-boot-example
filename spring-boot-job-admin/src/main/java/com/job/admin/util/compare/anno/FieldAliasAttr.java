package com.job.admin.util.compare.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性别名注解
 * 与工具类CompareObjectUtil 配合使用
 * @author mengq
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldAliasAttr {

    /**
     * 属性别名
     */
    String alias() default "";

}
