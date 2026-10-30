package com.demo;

import com.zaxxer.hikari.HikariPoolMXBean;

import javax.sql.DataSource;
import java.sql.*;

public class UserDaoWithPool {

    // 依赖注入 DataSource
    private final DataSource dataSource;

    public UserDaoWithPool(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /*添加用户*/
    public Long addUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        Connection conn = dataSource.getConnection();      // 借出
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();


        return 1L;
        // 故意不写 conn.close() / ps.close() → 连接永久被占
    }

    /*批量添加用户*/
    public boolean addUserBatch(int totalCount) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)")) {
            conn.setAutoCommit(false);

            for (int i = 1; i <= totalCount; i++) {
                ps.setString(1, "batch_user_" + i);
                ps.setString(2, "pwd_" + i);

                // 1. 将参数加入批处理缓冲区（此时还没发给数据库）
                ps.addBatch();

                // 2. 防止内存溢出，建议每 1000 条执行一次发送
                if (i % 1000 == 0) {
                    ps.executeBatch(); // 将攒好的 1000 条发送给 MySQL
                    conn.commit();     // 提交事务
                    // 注意：如果继续插入，可以清空批处理，但通常 executeBatch 后会自动清空，或者手动 ps.clearBatch();
                }
            }

            ps.executeBatch();
            conn.commit(); // 提交事务
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("批量插入失败", e);
        }
    }
}