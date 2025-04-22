package io.github.lizhifuabc.json.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lizhifuabc.json.jackson.module.JacksonExtendModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 自动配置类
 *
 * @author lizhifu
 * @since 2025/4/22
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JacksonExtendAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JacksonExtendModule ananJacksonModule() {
        return new JacksonExtendModule();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper(JacksonExtendModule jacksonExtendModule) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(jacksonExtendModule);
        return objectMapper;
    }
}