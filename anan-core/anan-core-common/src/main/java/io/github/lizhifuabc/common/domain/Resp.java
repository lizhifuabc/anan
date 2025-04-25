package io.github.lizhifuabc.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据返回
 *
 * @author lizhifu
 * @since 2025/4/25
 */
@Data
public class Resp<T> {
    @Schema(description = "返回码")
    private Integer code;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "返回描述")
    private String msg;
}
