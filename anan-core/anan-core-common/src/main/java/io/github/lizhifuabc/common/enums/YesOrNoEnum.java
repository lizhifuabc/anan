package io.github.lizhifuabc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否
 *
 * @author lizhifu
 * @since 2025/4/16
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum implements BaseEnum<Integer>{
    NO(0, "否"),
    YES(1, "是"),;

    private final Integer value;
    private final String label;
}
