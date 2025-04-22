package io.github.lizhifuabc.json.jackson.util;

/**
 * 脱敏工具类
 *
 * @author lizhifu
 * @since 2025/4/22
 */
public class SensitiveUtil {
    
    /**
     * 手机号脱敏
     * 前三后四，例如：138****1234
     */
    public static String phone(String phone, char replaceChar) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return custom(phone, 3, 4, replaceChar);
    }
    
    /**
     * 身份证号脱敏
     * 前三后四，例如：110***********1234
     */
    public static String idCard(String idCard, char replaceChar) {
        return phone(idCard, replaceChar);
    }
    
    /**
     * 银行卡号脱敏
     * 前四后四，例如：6222***********1234
     */
    public static String bankCard(String bankCard, char replaceChar) {
        if (bankCard == null || bankCard.length() < 8) {
            return bankCard;
        }
        return custom(bankCard, 4, 4, replaceChar);
    }
    
    /**
     * 姓名脱敏
     * 只显示第一个字符，例如：张**
     */
    public static String name(String name, char replaceChar) {
        if (name == null || name.length() < 2) {
            return name;
        }
        return custom(name, 1, 0, replaceChar);
    }
    
    /**
     * 邮箱脱敏
     * 邮箱前缀仅显示第一个字符，例如：t****@163.com
     */
    public static String email(String email, char replaceChar) {
        if (email == null || email.length() < 2) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }
        
        String prefix = email.substring(0, 1);
        String middle = repeat(replaceChar, atIndex - 1);
        String suffix = email.substring(atIndex);
        
        return prefix + middle + suffix;
    }
    
    /**
     * 地址脱敏
     * 只显示到地区，不显示详细地址，例如：北京市海淀区****
     */
    public static String address(String address, char replaceChar) {
        if (address == null || address.length() < 6) {
            return address;
        }
        
        int length = address.length();
        int index = 6;
        if (length < 10) {
            index = length / 2;
        }
        
        return address.substring(0, index) + repeat(replaceChar, 4);
    }
    
    /**
     * 自定义脱敏
     * 
     * @param str 原始字符串
     * @param prefixKeep 前置保留长度
     * @param suffixKeep 后置保留长度
     * @param replaceChar 替换字符
     * @return 脱敏后的字符串
     */
    public static String custom(String str, int prefixKeep, int suffixKeep, char replaceChar) {
        if (str == null) {
            return null;
        }
        
        int length = str.length();
        
        // 如果长度不足，直接返回原始字符串
        if (length <= prefixKeep + suffixKeep) {
            return str;
        }
        
        String prefix = str.substring(0, prefixKeep);
        String suffix = str.substring(length - suffixKeep);
        String middle = repeat(replaceChar, length - prefixKeep - suffixKeep);
        
        return prefix + middle + suffix;
    }
    
    /**
     * 重复字符
     */
    private static String repeat(char ch, int repeat) {
        return String.valueOf(ch).repeat(Math.max(0, repeat));
    }
}