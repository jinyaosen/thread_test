package com.demo;

import java.sql.*;

public class UserDao {

    private final String url = "jdbc:mysql://localhost:3306/thread_test";
    private final String user = "root";
    private final String pwd = "123456";

    /*添加用户*/
    public Long addUser(String username, String password) {
        String sql = "INSERT INTO users (username,password) VALUES (?,?)";
        try(Connection conn = DriverManager.getConnection(url, user, pwd);
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            try(ResultSet keys = ps.getGeneratedKeys()) {
                if(keys.next()) {
                    return keys.getLong(1);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("新增用户失败", e);
        }
    }

    /*查找用户*/
    public User queryUser(String username) {
        String sql = "SELECT id, username, password, created_at FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pwd);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setCreatedAt(rs.getObject("created_at", java.time.LocalDateTime.class));
                    return u;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("查询用户失败", e);
        }
    }
}