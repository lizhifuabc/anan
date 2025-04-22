package io.github.lizhifuabc.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Long 类型序列化为字符串
 * 解决前端 JS 中 Long 精度丢失问题
 *
 * @author lizhifu
 * @since 2025/4/22
 */
public class LongToStringSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
            return;
        }
        // js中最大安全整数16位 Number.MAX_SAFE_INTEGER
        String longStr = String.valueOf(value);
        if (longStr.length() > 16) {
            jsonGenerator.writeString(longStr);
        } else {
            jsonGenerator.writeNumber(value);
        }
    }
}