package io.github.lizhifuabc.common.enums;

import java.io.Serializable;

/**
 * 基础枚举
 * @param <T> 枚举类value字段的类型
 * @author lizhifu
 * @since 2025/4/16
 */
public interface BaseEnum<T> extends Serializable {
    /**
     * 枚举选项的值,通常由字母或者数字组成,并且在同一个枚举中值唯一;对应数据库中的值通常也为此值
     *
     * @return 枚举的值
     */
    T getValue();

    /**
     * 枚举选项的文本，通常为中文
     *
     * @return 枚举的文本
     */
    String getLabel();
}
