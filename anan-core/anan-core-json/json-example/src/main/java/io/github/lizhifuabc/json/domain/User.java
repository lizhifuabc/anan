package io.github.lizhifuabc.json.domain;

import io.github.lizhifuabc.json.jackson.annotation.Sensitive;
import io.github.lizhifuabc.json.jackson.enums.SensitiveStrategy;
import lombok.Data;

/**
 * user
 *
 * @author lizhifu
 * @since 2025/4/22
 */
@Data
public class User {
    @Sensitive(strategy = SensitiveStrategy.NAME)
    private String name;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phone;

    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    private String idCard;

    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    private String email;

    @Sensitive(strategy = SensitiveStrategy.ADDRESS)
    private String address;

    @Sensitive(strategy = SensitiveStrategy.BANK_CARD)
    private String bankCard;

    @Sensitive(strategy = SensitiveStrategy.CUSTOM, prefixKeep = 2, suffixKeep = 2)
    private String custom;
}
