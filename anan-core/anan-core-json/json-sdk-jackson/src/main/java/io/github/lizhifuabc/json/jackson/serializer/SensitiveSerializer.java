package io.github.lizhifuabc.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.github.lizhifuabc.json.jackson.annotation.Sensitive;
import io.github.lizhifuabc.json.jackson.enums.SensitiveStrategy;
import io.github.lizhifuabc.json.jackson.util.SensitiveUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * 敏感信息序列化器
 *
 * @author lizhifu
 * @since 2025/4/22
 */
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
    
    private final SensitiveStrategy strategy;
    private int prefixKeep;
    private int suffixKeep;
    private char replaceChar;
    
    public SensitiveSerializer() {
        this.strategy = null;
    }
    
    public SensitiveSerializer(SensitiveStrategy strategy, int prefixKeep, int suffixKeep, char replaceChar) {
        this.strategy = strategy;
        this.prefixKeep = prefixKeep;
        this.suffixKeep = suffixKeep;
        this.replaceChar = replaceChar;
    }
    
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null || value.isEmpty()) {
            jsonGenerator.writeString(value);
            return;
        }
        
        String result = value;
        
        switch (strategy) {
            case PHONE:
                result = SensitiveUtil.phone(value, replaceChar);
                break;
            case ID_CARD:
                result = SensitiveUtil.idCard(value, replaceChar);
                break;
            case BANK_CARD:
                result = SensitiveUtil.bankCard(value, replaceChar);
                break;
            case NAME:
                result = SensitiveUtil.name(value, replaceChar);
                break;
            case EMAIL:
                result = SensitiveUtil.email(value, replaceChar);
                break;
            case ADDRESS:
                result = SensitiveUtil.address(value, replaceChar);
                break;
            case CUSTOM:
                result = SensitiveUtil.custom(value, prefixKeep, suffixKeep, replaceChar);
                break;
            default:
                break;
        }
        
        jsonGenerator.writeString(result);
    }
    
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        
        if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
            if (sensitive == null) {
                sensitive = beanProperty.getContextAnnotation(Sensitive.class);
            }
            
            if (sensitive != null) {
                return new SensitiveSerializer(
                        sensitive.strategy(),
                        sensitive.prefixKeep(),
                        sensitive.suffixKeep(),
                        sensitive.replaceChar()
                );
            }
        }
        
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}