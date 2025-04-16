package io.github.lizhifuabc.excel.service;

import cn.idev.excel.context.AnalysisContext;

import java.util.Map;


/**
 * 读取到excel头信息时触发，会将表头数据转为Map集合
 *
 * @author lizhifu
 * @since 2025/2/21
 */
public interface InvokeHeadMapListenerService {

    default void before(Map<Integer, String> headMap, AnalysisContext analysisContext){};

    default void after(Map<Integer, String> headMap, AnalysisContext analysisContext){};
}
