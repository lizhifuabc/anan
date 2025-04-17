package io.github.lizhifuabc.db.dynamic.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态路由数据源
 * 继承Spring的AbstractRoutingDataSource，实现数据源的动态切换
 *
 * @author lizhifu
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 数据源管理器
     */
    private final DynamicDataSourceManager dataSourceManager;

    /**
     * 构造方法
     *
     * @param dataSourceManager 数据源管理器
     */
    public DynamicRoutingDataSource(DynamicDataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
        // 设置默认目标数据源
        setDefaultTargetDataSource(dataSourceManager.getPrimaryDataSource());
        refresh();
    }

    /**
     * 获取当前数据源的key
     * 这个方法决定使用哪个数据源
     *
     * @return 数据源key
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceName();
    }

    /**
     * 刷新数据源
     * 当数据源发生变化时，需要调用此方法刷新
     * 使用synchronized关键字确保线程安全，防止并发刷新导致的数据源状态不一致
     */
    public synchronized void refresh() {
        Map<Object, Object> targetDataSources = new HashMap<>(dataSourceManager.getDataSourceMap());
        setTargetDataSources(targetDataSources);
        afterPropertiesSet();
        log.info("刷新动态数据源完成");
    }

    /**
     * 添加数据源
     * 使用synchronized关键字确保线程安全，防止并发添加数据源导致的数据源状态不一致
     *
     * @param name 数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String name, DataSource dataSource) {
        dataSourceManager.addDataSource(name, dataSource);
        refresh();
    }

    /**
     * 添加数据源
     * 使用synchronized关键字确保线程安全，防止并发添加数据源导致的数据源状态不一致
     *
     * @param name 数据源名称
     * @param properties 数据源配置
     */
    public synchronized void addDataSource(String name, DataSourceProperties properties) {
        dataSourceManager.addDataSource(name, properties);
        refresh();
    }

    /**
     * 移除数据源
     * 使用synchronized关键字确保线程安全，防止并发移除数据源导致的数据源状态不一致
     *
     * @param name 数据源名称
     * @return 被移除的数据源
     */
    public synchronized DataSource removeDataSource(String name) {
        DataSource removed = dataSourceManager.removeDataSource(name);
        refresh();
        return removed;
    }

    /**
     * 获取数据源管理器
     *
     * @return 数据源管理器
     */
    public DynamicDataSourceManager getDataSourceManager() {
        return dataSourceManager;
    }
}