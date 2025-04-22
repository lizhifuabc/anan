package io.github.lizhifuabc.json.jackson.annotation;

import io.github.lizhifuabc.json.jackson.enums.SensitiveStrategy;

import java.lang.annotation.*;

/**
 * 敏感信息注解
 * 用于标记需要脱敏的字段
 *
 * @author lizhifu
 * @since 2025/4/22
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {
    /**
     * 脱敏策略
     */
    SensitiveStrategy strategy();
    
    /**
     * 前置保留长度
     */
    int prefixKeep() default 0;
    
    /**
     * 后置保留长度
     */
    int suffixKeep() default 0;
    
    /**
     * 替换字符
     */
    char replaceChar() default '*';
}