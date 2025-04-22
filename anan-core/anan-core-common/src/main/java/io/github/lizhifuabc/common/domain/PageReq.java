package io.github.lizhifuabc.common.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分页请求参数
 *
 * @author lizhifu
 * @since 2025/4/22
 */
@Data
public class PageReq {
    @Schema(description = "页码(不能为空)", example = "1")
    @NotNull(message = "分页参数不能为空")
    private Long pageNum;

    @Schema(description = "每页数量(不能为空)", example = "10")
    @NotNull(message = "每页数量不能为空")
    @Max(value = 500, message = "每页最大为500")
    private Long pageSize;

    @Schema(description = "是否查询总条数")
    protected Boolean searchCount;

    @Schema(description = "排序字段集合")
    @Size(max = 10, message = "排序字段最多10")
    @Valid
    private List<SortItem> sortItemList;

    /**
     * 排序
     */
    @Data
    public static class SortItem {

        @Schema(description = "true正序|false倒序")
        @NotNull(message = "排序规则不能为空")
        private Boolean ascFlag;

        @Schema(description = "排序字段")
        @NotBlank(message = "排序字段不能为空")
        @Size(max = 30, message = "排序字段最多30")
        private String column;
    }
}
