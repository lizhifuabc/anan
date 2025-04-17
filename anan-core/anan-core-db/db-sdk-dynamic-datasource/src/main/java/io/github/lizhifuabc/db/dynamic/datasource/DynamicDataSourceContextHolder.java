package io.github.lizhifuabc.db.dynamic.datasource;

import lombok.extern.slf4j.Slf4j;

/**
 * 动态数据源上下文持有器
 * 用于存储当前线程使用的数据源名称
 *
 * @author lizhifu
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * 使用ThreadLocal存储当前线程数据源名称
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源名称
     *
     * @param dataSourceName 数据源名称
     */
    public static void setDataSourceName(String dataSourceName) {
        log.debug("切换数据源到: {}", dataSourceName);
        CONTEXT_HOLDER.set(dataSourceName);
    }

    /**
     * 获取数据源名称
     *
     * @return 数据源名称
     */
    public static String getDataSourceName() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除数据源名称
     * 清除后，将使用默认数据源
     */
    public static void clearDataSourceName() {
        CONTEXT_HOLDER.remove();
        log.debug("清除数据源名称，将使用默认数据源");
    }
}