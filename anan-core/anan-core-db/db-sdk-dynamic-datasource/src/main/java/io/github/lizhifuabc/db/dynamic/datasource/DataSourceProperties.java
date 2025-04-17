package io.github.lizhifuabc.db.dynamic.datasource;

import lombok.Data;

/**
 * 数据源配置属性
 *
 * @author lizhifu
 */
@Data
public class DataSourceProperties {

    /**
     * 数据库连接URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库驱动类名
     */
    private String driverClassName;

    /**
     * 连接池配置
     */
    private PoolConfig poolConfig;

    /**
     * 创建一个新的数据源配置构建器
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 数据源配置构建器
     */
    public static class Builder {
        private final DataSourceProperties properties = new DataSourceProperties();

        /**
         * 设置数据库连接URL
         *
         * @param url 数据库连接URL
         * @return 构建器
         */
        public Builder url(String url) {
            properties.setUrl(url);
            return this;
        }

        /**
         * 设置数据库用户名
         *
         * @param username 数据库用户名
         * @return 构建器
         */
        public Builder username(String username) {
            properties.setUsername(username);
            return this;
        }

        /**
         * 设置数据库密码
         *
         * @param password 数据库密码
         * @return 构建器
         */
        public Builder password(String password) {
            properties.setPassword(password);
            return this;
        }

        /**
         * 设置数据库驱动类名
         *
         * @param driverClassName 数据库驱动类名
         * @return 构建器
         */
        public Builder driverClassName(String driverClassName) {
            properties.setDriverClassName(driverClassName);
            return this;
        }

        /**
         * 设置连接池配置
         *
         * @param poolConfig 连接池配置
         * @return 构建器
         */
        public Builder poolConfig(PoolConfig poolConfig) {
            properties.setPoolConfig(poolConfig);
            return this;
        }

        /**
         * 构建数据源配置
         *
         * @return 数据源配置
         */
        public DataSourceProperties build() {
            return properties;
        }
    }
}