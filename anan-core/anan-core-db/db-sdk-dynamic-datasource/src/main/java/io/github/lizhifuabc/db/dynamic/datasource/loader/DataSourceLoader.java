package io.github.lizhifuabc.db.dynamic.datasource.loader;

import java.util.List;

/**
 * 数据源加载器接口
 * 定义从外部加载数据源配置的方法
 *
 * @author lizhifu
 */
public interface DataSourceLoader {

    /**
     * 加载数据源配置
     *
     * @return 数据源配置列表
     */
    List<DatabaseDataSourceLoader.DataSourceConfig> loadDataSourceConfigs();
}