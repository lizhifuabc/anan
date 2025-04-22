package io.github.lizhifuabc.json.jackson.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.lizhifuabc.json.jackson.serializer.BigDecimalNullZeroSerializer;
import io.github.lizhifuabc.json.jackson.serializer.LongToStringSerializer;
import io.github.lizhifuabc.json.jackson.serializer.SensitiveSerializer;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * Anan Jackson 模块
 * 注册所有的序列化器和反序列化器
 *
 * @author lizhifu
 * @since 2025/4/22
 */
public class JacksonExtendModule extends SimpleModule {
    @Serial
    private static final long serialVersionUID = 1L;

    public JacksonExtendModule() {
        super("AnanJacksonModule");
        // 注册序列化器
        addSerializer(BigDecimal.class, new BigDecimalNullZeroSerializer());
        addSerializer(Long.class, new LongToStringSerializer());
        addSerializer(String.class, new SensitiveSerializer());

        // 注册反序列化器
    }
}