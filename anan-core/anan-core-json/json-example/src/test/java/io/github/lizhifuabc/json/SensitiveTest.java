package io.github.lizhifuabc.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lizhifuabc.json.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 脱敏测试
 *
 * @author lizhifu
 * @since 2025/4/22
 */
@SpringBootTest
public class SensitiveTest {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testSensitive() throws JsonProcessingException {
        User user = new User();
        user.setName("张三");
        user.setPhone("13800138000");
        user.setIdCard("110101199001011234");
        user.setEmail("zhangsan@example.com");
        user.setAddress("北京市海淀区中关村南大街5号");
        user.setBankCard("6222021234567890123");
        user.setCustom("1234567890");
        
        // 序列化为 JSON 字符串（会进行脱敏）
        String json = objectMapper.writeValueAsString(user);
        System.out.println("脱敏后的 JSON：");
        System.out.println(json);
        
        // 打印原始值（用于对比）
        System.out.println("\n原始值：");
        System.out.println("姓名：" + user.getName());
        System.out.println("手机号：" + user.getPhone());
        System.out.println("身份证号：" + user.getIdCard());
        System.out.println("邮箱：" + user.getEmail());
        System.out.println("地址：" + user.getAddress());
        System.out.println("银行卡号：" + user.getBankCard());
        System.out.println("自定义：" + user.getCustom());
    }
}