package io.github.lizhifuabc.excel.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import io.github.lizhifuabc.excel.util.ExcelUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 分批处理监听器（适用于大数据场景）
 *
 * @author lizhifu
 * @since 2025/2/21
 */
public class BatchHandleListener<T, R> extends AnalysisEventListener<T> {
    /**
     * 每批的处理行数,默认为 1000
     */
    private static int BATCH_NUMBER = 1000;

    /**
     * 临时存储读取到的excel数据
     */
    private List<T> data;

    @Getter
    private int rows, batchNo;

    @Setter
    private boolean validateSwitch = true;

    /**
     * 每批数据的业务逻辑处理器 Function<List<T>, R> businessHandler
     */
    private final Consumer<List<T>> businessHandler;

    /**
     * 用于校验excel模板正确性的字段
     */
    private final Field[] fields;
    private final Class<T> clazz;

    public BatchHandleListener(Class<T> clazz, Consumer<List<T>> handle) {
        // 通过构造器为字段赋值，用于校验excel文件与模板是否匹配
        this(clazz, handle, BATCH_NUMBER);
    }

    public BatchHandleListener(Class<T> clazz, Consumer<List<T>> handle, int batchNumber) {
        // 通过构造器为字段赋值，用于校验excel文件与模板是否匹配
        this.clazz = clazz;
        this.fields = clazz.getDeclaredFields();
        // 初始化临时存储数据的集合，及外部传入的业务方法
        this.businessHandler = handle;
        BATCH_NUMBER = batchNumber;
        this.data = new ArrayList<>(BATCH_NUMBER);
    }

    /**
     * 读取到excel头信息时触发，会将表头数据转为Map集合（用于校验导入的excel文件与模板是否匹配）
     * 注意点1：当前校验逻辑不适用于多行头模板（如果是多行头的文件，请关闭表头验证）；
     * 注意点2：使用当前监听器的导入场景，模型类不允许出现既未忽略、又未使用ExcelProperty注解的字段；
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (validateSwitch) {
            ExcelUtil.validateExcelTemplate(headMap, clazz, fields);
        }
    }

    /**
     * 每解析到一行数据都会触发
     * @param row 数据
     * @param analysisContext 解析上下文
     */
    @Override
    public void invoke(T row, AnalysisContext analysisContext) {
        data.add(row);
        // 判断当前已解析的数据是否达到本批上限，是则执行对应的业务处理
        if (data.size() >= BATCH_NUMBER) {
            // 更新读取到的总行数、批次数
            rows += data.size();
            batchNo++;

            // 触发业务逻辑处理
            this.businessHandler.accept(data);
            // 处理完本批数据后，使用新List替换旧List，旧List失去引用后会很快被GC
            // 说明：不使用clear()是因为内部会去断开与每个元素的引用，浪费性能
            data = new ArrayList<>(BATCH_NUMBER);
        }
    }

    /**
     * 所有数据解析完之后触发
     * @param analysisContext 解析上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 因为最后一批可能达不到指定的上限，所以解析完之后要做一次收尾处理
        if (!data.isEmpty()) {
            this.businessHandler.accept(data);
            // 更新读取到的总行数、批次数，以及清理集合辅助GC
            rows += data.size();
            batchNo++;
            data.clear();
        }
    }
}
