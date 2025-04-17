package io.github.lizhifuabc.db.dynamic.datasource.config;

import io.github.lizhifuabc.db.dynamic.datasource.DataSourceProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 动态数据源配置属性
 *
 * @author lizhifu
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceProperties {

    /**
     * 是否启用动态数据源
     */
    private boolean enabled = false;

    /**
     * 主数据源名称
     */
    private String primary = "master";

    /**
     * 严格模式，当数据源不存在时是否抛出异常
     */
    private boolean strict = false;

    /**
     * 数据源配置
     */
    private Map<String, DataSourceProperties> datasource = new LinkedHashMap<>();

    /**
     * 获取数据源配置
     *
     * @param name 数据源名称
     * @return 数据源配置
     */
    public DataSourceProperties getDatasource(String name) {
        return datasource.get(name);
    }

    /**
     * 添加数据源配置
     *
     * @param name 数据源名称
     * @param properties 数据源配置
     */
    public void addDatasource(String name, DataSourceProperties properties) {
        datasource.put(name, properties);
    }

    /**
     * 移除数据源配置
     *
     * @param name 数据源名称
     * @return 被移除的数据源配置
     */
    public DataSourceProperties removeDatasource(String name) {
        return datasource.remove(name);
    }
}