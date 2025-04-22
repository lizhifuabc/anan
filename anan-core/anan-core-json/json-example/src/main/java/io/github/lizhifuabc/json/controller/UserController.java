package io.github.lizhifuabc.json.controller;

import io.github.lizhifuabc.json.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * 展示脱敏功能
 *
 * @author lizhifu
 * @since 2025/4/22
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    /**
     * 获取用户信息（脱敏后）
     */
    @GetMapping("/info")
    public User getUserInfo() {
        User user = new User();
        user.setName("张三");
        user.setPhone("13800138000");
        user.setIdCard("110101199001011234");
        user.setEmail("zhangsan@example.com");
        user.setAddress("北京市海淀区中关村南大街5号");
        user.setBankCard("6222021234567890123");
        user.setCustom("1234567890");
        return user;
    }
    
    /**
     * 获取原始用户信息（不脱敏，仅用于对比）
     */
    @GetMapping("/raw")
    public String getRawUserInfo() {
        User user = new User();
        user.setName("张三");
        user.setPhone("13800138000");
        user.setIdCard("110101199001011234");
        user.setEmail("zhangsan@example.com");
        user.setAddress("北京市海淀区中关村南大街5号");
        user.setBankCard("6222021234567890123");
        user.setCustom("1234567890");
        
        StringBuilder sb = new StringBuilder();
        sb.append("姓名：").append(user.getName()).append("\n");
        sb.append("手机号：").append(user.getPhone()).append("\n");
        sb.append("身份证号：").append(user.getIdCard()).append("\n");
        sb.append("邮箱：").append(user.getEmail()).append("\n");
        sb.append("地址：").append(user.getAddress()).append("\n");
        sb.append("银行卡号：").append(user.getBankCard()).append("\n");
        sb.append("自定义：").append(user.getCustom()).append("\n");
        
        return sb.toString();
    }
}