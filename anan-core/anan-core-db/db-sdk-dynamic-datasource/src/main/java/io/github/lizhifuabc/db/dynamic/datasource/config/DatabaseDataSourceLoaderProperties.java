package io.github.lizhifuabc.db.dynamic.datasource.config;

import lombok.Data;

/**
 * 数据库数据源加载器配置属性
 *
 * @author lizhifu
 */
@Data
public class DatabaseDataSourceLoaderProperties {

    /**
     * 是否启用数据库数据源加载器
     */
    private boolean enabled = false;

    /**
     * 是否在启动时初始化数据源配置
     */
    private boolean initOnStartup = true;

    /**
     * 刷新间隔（毫秒）
     * 默认5分钟刷新一次
     */
    private long refreshInterval = 300000;
}