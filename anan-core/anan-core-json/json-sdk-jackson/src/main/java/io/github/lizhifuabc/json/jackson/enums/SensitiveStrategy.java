package io.github.lizhifuabc.json.jackson.enums;

/**
 * 脱敏策略枚举
 *
 * @author lizhifu
 * @since 2025/4/22
 */
public enum SensitiveStrategy {
    /**
     * 手机号码
     * 前三后四，例如：138****1234
     */
    PHONE,
    
    /**
     * 身份证号
     * 前三后四，例如：110***********1234
     */
    ID_CARD,
    
    /**
     * 银行卡号
     * 前四后四，例如：6222***********1234
     */
    BANK_CARD,
    
    /**
     * 姓名
     * 只显示第一个字符，例如：张**
     */
    NAME,
    
    /**
     * 邮箱
     * 邮箱前缀仅显示第一个字符，例如：t****@163.com
     */
    EMAIL,
    
    /**
     * 地址
     * 只显示到地区，不显示详细地址，例如：北京市海淀区****
     */
    ADDRESS,
    
    /**
     * 自定义
     * 使用 prefixKeep 和 suffixKeep 参数自定义保留长度
     */
    CUSTOM
}