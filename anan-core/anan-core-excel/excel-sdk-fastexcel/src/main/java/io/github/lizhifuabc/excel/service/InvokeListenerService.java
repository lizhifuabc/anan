package io.github.lizhifuabc.excel.service;

import cn.idev.excel.context.AnalysisContext;

/**
 * 每解析到一行数据都会触发
 *
 * @author lizhifu
 * @since 2025/2/21
 */
public interface InvokeListenerService<T> {

    default void before(T row, AnalysisContext analysisContext){};

    default void after(T row, AnalysisContext analysisContext){};
}
