package io.github.lizhifuabc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 男女
 *
 * @author lizhifu
 * @since 2025/4/16
 */
@AllArgsConstructor
@Getter
public enum GenderEnum implements BaseEnum<Integer> {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private final Integer value;
    private final String label;
}
