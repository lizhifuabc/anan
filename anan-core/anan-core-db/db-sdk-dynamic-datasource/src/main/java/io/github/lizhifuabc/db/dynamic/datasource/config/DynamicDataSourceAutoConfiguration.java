package io.github.lizhifuabc.db.dynamic.datasource.config;

import io.github.lizhifuabc.db.dynamic.datasource.DynamicDataSourceManager;
import io.github.lizhifuabc.db.dynamic.datasource.DynamicRoutingDataSource;
import io.github.lizhifuabc.db.dynamic.datasource.aop.DynamicDataSourceAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 动态数据源自动配置类
 *
 * @author lizhifu
 */
@Slf4j
@AutoConfiguration(before = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@ConditionalOnProperty(prefix = "spring.datasource.dynamic", name = "enabled", havingValue = "true", matchIfMissing = false)
public class DynamicDataSourceAutoConfiguration {

    /**
     * 数据源管理器
     *
     * @return 数据源管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceManager dynamicDataSourceManager() {
        log.info("初始化动态数据源管理器");
        return new DynamicDataSourceManager();
    }

    /**
     * 动态路由数据源
     *
     * @param dataSourceManager 数据源管理器
     * @return 动态路由数据源
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicRoutingDataSource dynamicRoutingDataSource(DynamicDataSourceManager dataSourceManager) {
        log.info("初始化动态路由数据源");
        return new DynamicRoutingDataSource(dataSourceManager);
    }

    /**
     * 动态数据源切面
     *
     * @return 动态数据源切面
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        log.info("初始化动态数据源切面");
        return new DynamicDataSourceAspect();
    }
}