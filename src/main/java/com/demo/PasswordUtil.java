package com.demo;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // 生成哈希密码
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // 校验密码
    public static boolean check(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
