package io.github.lizhifuabc.db.dynamic.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源管理器
 * 负责数据源的创建、获取、移除等操作
 *
 * @author lizhifu
 */
@Slf4j
public class DynamicDataSourceManager {

    /**
     * 数据源缓存
     */
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * 默认数据源名称
     */
    private String primaryDataSourceName = "master";

    /**
     * 添加数据源
     *
     * @param name 数据源名称
     * @param dataSource 数据源
     */
    public void addDataSource(String name, DataSource dataSource) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("数据源名称不能为空");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("数据源不能为空");
        }
        if (dataSourceMap.containsKey(name)) {
            log.warn("数据源[{}]已存在，将被覆盖", name);
        }
        dataSourceMap.put(name, dataSource);
        log.info("添加数据源[{}]成功", name);
    }

    /**
     * 添加数据源
     *
     * @param name 数据源名称
     * @param config 数据源配置
     */
    public void addDataSource(String name, DataSourceProperties config) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("数据源名称不能为空");
        }
        if (config == null) {
            throw new IllegalArgumentException("数据源配置不能为空");
        }
        
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setDriverClassName(config.getDriverClassName());
        
        // 设置连接池配置
        if (config.getPoolConfig() != null) {
            PoolConfig poolConfig = config.getPoolConfig();
            if (poolConfig.getMaximumPoolSize() != null) {
                dataSource.setMaximumPoolSize(poolConfig.getMaximumPoolSize());
            }
            if (poolConfig.getMinimumIdle() != null) {
                dataSource.setMinimumIdle(poolConfig.getMinimumIdle());
            }
            if (poolConfig.getConnectionTimeout() != null) {
                dataSource.setConnectionTimeout(poolConfig.getConnectionTimeout());
            }
            if (poolConfig.getIdleTimeout() != null) {
                dataSource.setIdleTimeout(poolConfig.getIdleTimeout());
            }
        }
        
        addDataSource(name, dataSource);
    }

    /**
     * 移除数据源
     *
     * @param name 数据源名称
     * @return 被移除的数据源，如果不存在则返回null
     */
    public DataSource removeDataSource(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("数据源名称不能为空");
        }
        if (name.equals(primaryDataSourceName)) {
            throw new IllegalArgumentException("不能移除主数据源");
        }
        DataSource removed = dataSourceMap.remove(name);
        if (removed != null) {
            log.info("移除数据源[{}]成功", name);
            // 如果是HikariDataSource，需要关闭连接池
            if (removed instanceof HikariDataSource) {
                ((HikariDataSource) removed).close();
                log.info("关闭数据源[{}]连接池", name);
            }
        } else {
            log.warn("数据源[{}]不存在，无法移除", name);
        }
        return removed;
    }

    /**
     * 获取数据源
     *
     * @param name 数据源名称
     * @return 数据源
     */
    public DataSource getDataSource(String name) {
        if (!StringUtils.hasText(name)) {
            return getPrimaryDataSource();
        }
        DataSource dataSource = dataSourceMap.get(name);
        if (dataSource == null) {
            log.warn("数据源[{}]不存在，将使用主数据源", name);
            return getPrimaryDataSource();
        }
        return dataSource;
    }

    /**
     * 获取主数据源
     *
     * @return 主数据源
     */
    public DataSource getPrimaryDataSource() {
        DataSource dataSource = dataSourceMap.get(primaryDataSourceName);
        if (dataSource == null) {
            throw new IllegalStateException("主数据源[" + primaryDataSourceName + "]不存在");
        }
        return dataSource;
    }

    /**
     * 设置主数据源名称
     *
     * @param primaryDataSourceName 主数据源名称
     */
    public void setPrimaryDataSourceName(String primaryDataSourceName) {
        if (!StringUtils.hasText(primaryDataSourceName)) {
            throw new IllegalArgumentException("主数据源名称不能为空");
        }
        if (!dataSourceMap.containsKey(primaryDataSourceName)) {
            throw new IllegalArgumentException("数据源[" + primaryDataSourceName + "]不存在，无法设置为主数据源");
        }
        this.primaryDataSourceName = primaryDataSourceName;
        log.info("设置主数据源为[{}]", primaryDataSourceName);
    }

    /**
     * 获取主数据源名称
     *
     * @return 主数据源名称
     */
    public String getPrimaryDataSourceName() {
        return primaryDataSourceName;
    }

    /**
     * 获取所有数据源
     *
     * @return 所有数据源
     */
    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    /**
     * 判断数据源是否存在
     *
     * @param name 数据源名称
     * @return 是否存在
     */
    public boolean hasDataSource(String name) {
        return dataSourceMap.containsKey(name);
    }
}