package com.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;

public class DBUtil {
    // 获取连接池对象
    @Getter
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        // 数据库连接信息
        config.setJdbcUrl("jdbc:mysql://localhost:3306/thread_test" +
                "?serverTimezone=Asia/Shanghai" +
                "&useSSL=false" +
                "&allowPublicKeyRetrieval=true" +
                "&rewriteBatchedStatements=true");
        config.setUsername("root");
        config.setPassword("123456");

        config.setMaximumPoolSize(20);

        config.setMaximumPoolSize(5);            // 池子只放 5 个，泄漏 5 次就满
        config.setConnectionTimeout(1000);       // 1 秒拿不到连接就抛异常（默认 30 秒太长）
        config.setLeakDetectionThreshold(2000);  // 连接借出超过 2 秒未归还 → 打印泄漏堆栈

        // 初始化连接池
        dataSource = new HikariDataSource(config);
    }
}
