package com.oyc.demo.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @ClassName CustomPasswordEncoder
 * @Description 密码加密器，因为我们数据库是明文存储的，所以明文返回即可
 * @Author oyc
 * @Date 2020/12/2 9:23
 * @Version
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(charSequence.toString());
    }
}