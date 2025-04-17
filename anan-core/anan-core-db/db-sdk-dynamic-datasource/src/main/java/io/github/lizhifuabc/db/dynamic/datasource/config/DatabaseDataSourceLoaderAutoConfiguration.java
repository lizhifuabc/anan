package io.github.lizhifuabc.db.dynamic.datasource.config;

import io.github.lizhifuabc.db.dynamic.datasource.DynamicRoutingDataSource;
import io.github.lizhifuabc.db.dynamic.datasource.loader.DatabaseDataSourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 数据库数据源加载器自动配置
 * 用于从数据库加载数据源配置
 *
 * @author lizhifu
 */
@Slf4j
@Configuration
@EnableScheduling
@AutoConfigureAfter(DynamicDataSourceAutoConfiguration.class)
@ConditionalOnBean(DynamicRoutingDataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource.dynamic.database-loader", name = "enabled", havingValue = "true", matchIfMissing = false)
public class DatabaseDataSourceLoaderAutoConfiguration {

    /**
     * 数据库数据源加载器配置属性
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.database-loader")
    public DatabaseDataSourceLoaderProperties databaseDataSourceLoaderProperties() {
        return new DatabaseDataSourceLoaderProperties();
    }

    /**
     * 数据库数据源加载器
     *
     * @param dynamicRoutingDataSource 动态路由数据源
     * @param properties 配置属性
     * @return 数据库数据源加载器
     */
    @Bean
    public DatabaseDataSourceLoader databaseDataSourceLoader(
            DynamicRoutingDataSource dynamicRoutingDataSource,
            DatabaseDataSourceLoaderProperties properties) {
        
        log.info("初始化数据库数据源加载器");
        DatabaseDataSourceLoader loader = new DatabaseDataSourceLoader(dynamicRoutingDataSource);
        
        log.info("注意：必须设置DataSourceLoader实现才能加载数据源配置");
        log.info("请通过@Bean方式提供DataSourceLoader实现，并注入到DatabaseDataSourceLoader中");
        
        // 如果配置了自动初始化，则在启动时加载数据源配置
        if (properties.isInitOnStartup()) {
            log.info("启动时自动初始化数据源配置");
            loader.initDataSources();
        }
        
        return loader;
    }
}