package io.github.lizhifuabc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * 自定义记录请求的参数、请求体、请求头和客户端信息。
 * <br>需要配置日志级别为 DEBUG，就可以详细记录请求信息
 * <br>logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG
 * @author lizhifu
 * @since 2025/2/21
 */
@Configuration
public class RequestLoggingConfig {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        // 请求的查询字符串
        filter.setIncludeQueryString(true);
        // 请求的有效负载
        filter.setIncludePayload(true);
        // 请求的头部信息
        filter.setIncludeHeaders(true);
        // 客户端信息
        filter.setIncludeClientInfo(true);
        // 日志前缀
        filter.setAfterMessagePrefix("REQUEST DATA-");
        return filter;
    }
}
