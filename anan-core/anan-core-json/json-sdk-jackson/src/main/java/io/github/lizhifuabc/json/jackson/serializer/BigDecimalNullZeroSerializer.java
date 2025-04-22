package io.github.lizhifuabc.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 将 null 的 BigDecimal 值序列化为 0
 *
 * @author lizhifu
 * @since 2025/4/22
 */
public class BigDecimalNullZeroSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNumber(BigDecimal.ZERO);
            return;
        }
        jsonGenerator.writeNumber(value);
    }
}
