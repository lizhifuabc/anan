package io.github.lizhifuabc.db.dynamic.datasource.annotation;

import java.lang.annotation.*;

/**
 * 数据源切换注解
 * 可以标注在类或方法上，用于指定使用的数据源
 *
 * @author lizhifu
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

    /**
     * 数据源名称
     * 如果不指定，将使用默认数据源
     *
     * @return 数据源名称
     */
    String value() default "";
}