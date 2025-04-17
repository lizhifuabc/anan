package io.github.lizhifuabc.db.dynamic.datasource.loader;

import io.github.lizhifuabc.db.dynamic.datasource.DataSourceProperties;
import io.github.lizhifuabc.db.dynamic.datasource.DynamicRoutingDataSource;
import io.github.lizhifuabc.db.dynamic.datasource.PoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库数据源加载器
 * 从外部数据源加载器获取配置信息，并动态添加到DynamicRoutingDataSource中
 *
 * @author lizhifu
 */
@Slf4j
@Component
public class DatabaseDataSourceLoader {

    /**
     * 动态路由数据源
     */
    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    /**
     * 数据源加载器
     * 必须由外部提供实现
     */
    private DataSourceLoader dataSourceLoader;

    /**
     * 已加载的数据源配置缓存
     * key: 数据源名称, value: 数据源配置的哈希值，用于检测配置是否变更
     */
    private final Map<String, Integer> loadedDataSourceConfigHashMap = new HashMap<>();

    /**
     * 构造方法
     *
     * @param dynamicRoutingDataSource 动态路由数据源
     */
    @Autowired
    public DatabaseDataSourceLoader(DynamicRoutingDataSource dynamicRoutingDataSource) {
        this.dynamicRoutingDataSource = dynamicRoutingDataSource;
    }
    
    /**
     * 设置数据源加载器
     * 必须在使用前设置，否则无法加载数据源配置
     *
     * @param dataSourceLoader 数据源加载器
     */
    public void setDataSourceLoader(DataSourceLoader dataSourceLoader) {
        if (dataSourceLoader != null) {
            this.dataSourceLoader = dataSourceLoader;
            log.info("已设置数据源加载器: {}", dataSourceLoader.getClass().getName());
        }
    }



    /**
     * 初始化加载数据源配置
     * 可以在应用启动时调用此方法，加载所有数据源配置
     */
    public void initDataSources() {
        log.info("开始加载数据源配置");
        loadDataSourcesFromDatabase();
    }

    /**
     * 定时刷新数据源配置
     * 默认每5分钟执行一次，可通过Spring的@Scheduled注解配置
     */
    @Scheduled(fixedDelayString = "${spring.datasource.dynamic.refresh-interval:300000}")
    public void refreshDataSources() {
        log.info("开始刷新数据源配置");
        loadDataSourcesFromDatabase();
    }

    /**
     * 加载数据源配置
     */
    private void loadDataSourcesFromDatabase() {
        try {
            // 检查是否已设置数据源加载器
            if (dataSourceLoader == null) {
                log.error("未设置数据源加载器，无法加载数据源配置");
                return;
            }
            
            // 使用配置的数据源加载器加载数据源配置
            List<DataSourceConfig> configs = dataSourceLoader.loadDataSourceConfigs();
            
            if (configs == null || configs.isEmpty()) {
                log.warn("未找到有效的数据源配置");
                return;
            }
            
            log.info("加载到{}个数据源配置", configs.size());
            
            // 处理数据源配置
            for (DataSourceConfig config : configs) {
                processDataSourceConfig(config);
            }
            
            // 检查需要移除的数据源
            checkAndRemoveDataSources(configs);
            
        } catch (Exception e) {
            log.error("加载数据源配置失败", e);
        }
    }

    /**
     * 处理单个数据源配置
     *
     * @param config 数据源配置
     */
    private void processDataSourceConfig(DataSourceConfig config) {
        String name = config.getName();
        
        // 计算配置的哈希值，用于检测配置是否变更
        int configHash = config.hashCode();
        
        // 检查数据源是否已存在且配置未变更
        if (loadedDataSourceConfigHashMap.containsKey(name) && 
            loadedDataSourceConfigHashMap.get(name) == configHash &&
            dynamicRoutingDataSource.getDataSourceManager().hasDataSource(name)) {
            log.debug("数据源[{}]配置未变更，无需更新", name);
            return;
        }
        
        try {
            // 创建数据源配置
            DataSourceProperties properties = createDataSourceProperties(config);
            
            // 添加或更新数据源
            dynamicRoutingDataSource.addDataSource(name, properties);
            
            // 更新配置哈希值缓存
            loadedDataSourceConfigHashMap.put(name, configHash);
            
            log.info("成功{}数据源[{}]", 
                    dynamicRoutingDataSource.getDataSourceManager().hasDataSource(name) ? "更新" : "添加", 
                    name);
            
        } catch (Exception e) {
            log.error("处理数据源[{}]配置失败", name, e);
        }
    }

    /**
     * 检查并移除不再需要的数据源
     *
     * @param configs 当前有效的数据源配置列表
     */
    private void checkAndRemoveDataSources(List<DataSourceConfig> configs) {
        // 获取当前配置中的所有数据源名称
        Map<String, Boolean> currentDataSourceNames = new HashMap<>();
        for (DataSourceConfig config : configs) {
            currentDataSourceNames.put(config.getName(), true);
        }
        
        // 获取主数据源名称，主数据源不能被移除
        String primaryName = dynamicRoutingDataSource.getDataSourceManager().getPrimaryDataSourceName();
        
        // 检查已加载的数据源，移除不在当前配置中的数据源
        for (String name : loadedDataSourceConfigHashMap.keySet().toArray(new String[0])) {
            // 跳过主数据源
            if (name.equals(primaryName)) {
                continue;
            }
            
            // 如果数据源不在当前配置中，则移除
            if (!currentDataSourceNames.containsKey(name)) {
                try {
                    dynamicRoutingDataSource.removeDataSource(name);
                    loadedDataSourceConfigHashMap.remove(name);
                    log.info("移除不再需要的数据源[{}]", name);
                } catch (Exception e) {
                    log.error("移除数据源[{}]失败", name, e);
                }
            }
        }
    }

    /**
     * 创建数据源配置
     *
     * @param config 数据库中的数据源配置
     * @return 数据源配置
     */
    private DataSourceProperties createDataSourceProperties(DataSourceConfig config) {
        // 创建连接池配置
        PoolConfig poolConfig = PoolConfig.builder()
                .maximumPoolSize(config.getMaxPoolSize())
                .minimumIdle(config.getMinIdle())
                .connectionTimeout(config.getConnectionTimeout())
                .idleTimeout(config.getIdleTimeout())
                .build();
        
        // 创建数据源配置
        return DataSourceProperties.builder()
                .url(config.getUrl())
                .username(config.getUsername())
                .password(config.getPassword())
                .driverClassName(config.getDriverClassName())
                .poolConfig(poolConfig)
                .build();
    }

    /**
     * 数据源配置实体类
     * 对应数据库表中的记录
     */
    public static class DataSourceConfig {
        private String name;            // 数据源名称
        private String url;             // 数据库连接URL
        private String username;        // 数据库用户名
        private String password;        // 数据库密码
        private String driverClassName; // 数据库驱动类名
        private Integer maxPoolSize;    // 最大连接数
        private Integer minIdle;        // 最小空闲连接数
        private Long connectionTimeout; // 连接超时时间
        private Long idleTimeout;       // 空闲连接超时时间
        private Integer status;         // 状态：1-启用，0-禁用

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getDriverClassName() { return driverClassName; }
        public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
        
        public Integer getMaxPoolSize() { return maxPoolSize; }
        public void setMaxPoolSize(Integer maxPoolSize) { this.maxPoolSize = maxPoolSize; }
        
        public Integer getMinIdle() { return minIdle; }
        public void setMinIdle(Integer minIdle) { this.minIdle = minIdle; }
        
        public Long getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(Long connectionTimeout) { this.connectionTimeout = connectionTimeout; }
        
        public Long getIdleTimeout() { return idleTimeout; }
        public void setIdleTimeout(Long idleTimeout) { this.idleTimeout = idleTimeout; }
        
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (url != null ? url.hashCode() : 0);
            result = 31 * result + (username != null ? username.hashCode() : 0);
            result = 31 * result + (password != null ? password.hashCode() : 0);
            result = 31 * result + (driverClassName != null ? driverClassName.hashCode() : 0);
            result = 31 * result + (maxPoolSize != null ? maxPoolSize.hashCode() : 0);
            result = 31 * result + (minIdle != null ? minIdle.hashCode() : 0);
            result = 31 * result + (connectionTimeout != null ? connectionTimeout.hashCode() : 0);
            result = 31 * result + (idleTimeout != null ? idleTimeout.hashCode() : 0);
            return result;
        }
    }


}