package io.github.lizhifuabc.excel.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import io.github.lizhifuabc.excel.service.DoAfterAllAnalysedListenerService;
import io.github.lizhifuabc.excel.service.InvokeHeadMapListenerService;
import io.github.lizhifuabc.excel.service.InvokeListenerService;
import io.github.lizhifuabc.excel.util.ExcelUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Excel通用读取监听器
 *
 * @author lizhifu
 * @since 2025/2/21
 */
public class CommonListener<T> extends AnalysisEventListener<T> {
    /**
     * 最终数据
     */
    @Getter
    private final List<T> data;

    /**
     * 字段列表
     */
    private final Field[] fields;

    /**
     * Excel 实体类
     */
    private final Class<T> clazz;

    /**
     * 是否表头校验
     */
    @Setter
    private boolean validateSwitch = true;

    private final InvokeListenerService<T> invokeListenerService;

    private final DoAfterAllAnalysedListenerService doAfterAllAnalysedListenerService;

    private final InvokeHeadMapListenerService invokeHeadMapListenerService;

    public CommonListener(Class<T> clazz) {
        this(clazz, null, null, null);
    }


    public CommonListener(Class<T> clazz, InvokeListenerService<T> invokeListenerService, DoAfterAllAnalysedListenerService doAfterAllAnalysedListenerService, InvokeHeadMapListenerService invokeHeadMapListenerService) {
        fields = clazz.getDeclaredFields();
        this.clazz = clazz;
        this.data = new ArrayList<T>();

        this.invokeListenerService = invokeListenerService;
        this.doAfterAllAnalysedListenerService = doAfterAllAnalysedListenerService;
        this.invokeHeadMapListenerService = invokeHeadMapListenerService;
    }

    /**
     * 每解析到一行数据都会触发
     * @param row 数据
     * @param analysisContext 解析上下文
     */
    @Override
    public void invoke(T row, AnalysisContext analysisContext) {
        if (invokeListenerService != null) {
            invokeListenerService.before(row, analysisContext);
            data.add(row);
            invokeListenerService.after(row, analysisContext);
        } else {
            InvokeListenerService<T> service = new InvokeListenerService<>() {
            };
            service.before(row, analysisContext);
            data.add(row);
            service.after(row, analysisContext);
        }
    }

    /**
     * 读取到excel头信息时触发，会将表头数据转为Map集合
     * @param headMap 表头
     * @param analysisContext 解析上下文
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext analysisContext) {
        if (invokeHeadMapListenerService != null) {
            invokeHeadMapListenerService.before(headMap, analysisContext);
            if (validateSwitch) {
                ExcelUtil.validateExcelTemplate(headMap, clazz, fields);
            }
            invokeHeadMapListenerService.after(headMap, analysisContext);
        }else {
            InvokeHeadMapListenerService service = new InvokeHeadMapListenerService() {};
            service.before(headMap, analysisContext);
            if (validateSwitch) {
                ExcelUtil.validateExcelTemplate(headMap, clazz, fields);
            }
            service.after(headMap, analysisContext);
        }
    }

    /**
     * 所有数据解析完之后触发
     * @param analysisContext 解析上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        Objects.requireNonNullElseGet(doAfterAllAnalysedListenerService, () -> new DoAfterAllAnalysedListenerService() {
        }).after(analysisContext);
    }
}
