package com.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DBUtil {
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

        // 最小空闲连接数
        config.setMinimumIdle(5);

        // 连接超时时间（毫秒）：如果 20 个连接全被借走且还没还，第 21 个线程最多等 3 秒
        config.setConnectionTimeout(3000);

        // 初始化连接池
        dataSource = new HikariDataSource(config);
    }

    // 获取连接池对象
    public static DataSource getDataSource() {
        return dataSource;
    }
}
