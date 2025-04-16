package io.github.lizhifuabc.excel.service;

import cn.idev.excel.context.AnalysisContext;

/**
 * 所有数据解析完之后触发
 *
 * @author lizhifu
 * @since 2025/2/21
 */
public interface DoAfterAllAnalysedListenerService {

    default void after(AnalysisContext analysisContext){};
}
