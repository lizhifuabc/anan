package io.github.lizhifuabc.db.dynamic.datasource;

import lombok.Data;

/**
 * 连接池配置
 *
 * @author lizhifu
 */
@Data
public class PoolConfig {

    /**
     * 最大连接池大小
     */
    private Integer maximumPoolSize;

    /**
     * 最小空闲连接数
     */
    private Integer minimumIdle;

    /**
     * 连接超时时间（毫秒）
     */
    private Long connectionTimeout;

    /**
     * 空闲连接超时时间（毫秒）
     */
    private Long idleTimeout;

    /**
     * 创建一个新的连接池配置构建器
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 连接池配置构建器
     */
    public static class Builder {
        private final PoolConfig config = new PoolConfig();

        /**
         * 设置最大连接池大小
         *
         * @param maximumPoolSize 最大连接池大小
         * @return 构建器
         */
        public Builder maximumPoolSize(int maximumPoolSize) {
            config.setMaximumPoolSize(maximumPoolSize);
            return this;
        }

        /**
         * 设置最小空闲连接数
         *
         * @param minimumIdle 最小空闲连接数
         * @return 构建器
         */
        public Builder minimumIdle(int minimumIdle) {
            config.setMinimumIdle(minimumIdle);
            return this;
        }

        /**
         * 设置连接超时时间（毫秒）
         *
         * @param connectionTimeout 连接超时时间（毫秒）
         * @return 构建器
         */
        public Builder connectionTimeout(long connectionTimeout) {
            config.setConnectionTimeout(connectionTimeout);
            return this;
        }

        /**
         * 设置空闲连接超时时间（毫秒）
         *
         * @param idleTimeout 空闲连接超时时间（毫秒）
         * @return 构建器
         */
        public Builder idleTimeout(long idleTimeout) {
            config.setIdleTimeout(idleTimeout);
            return this;
        }

        /**
         * 构建连接池配置
         *
         * @return 连接池配置
         */
        public PoolConfig build() {
            return config;
        }
    }
}