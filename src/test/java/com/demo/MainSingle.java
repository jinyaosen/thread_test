package com.demo;

public class MainSingle {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        String username = "alice";
        String plainPwd = "mySecret123";

        // 1. 加密密码
        String hashPwd = PasswordUtil.hash(plainPwd);
        System.out.println("生成的哈希值: " + hashPwd);

        // 2. 存入数据库
        Long id = userDao.addUser(username, hashPwd);
        System.out.println("新增用户成功，ID: " + id);

        // 3. 从数据库查出来
        User queriedUser = userDao.queryUser(username);
        System.out.println("查询到用户: " + queriedUser.getUsername());

        // 4. 模拟登录校验
        boolean isMatch = PasswordUtil.check(plainPwd, queriedUser.getPassword());
        System.out.println("密码校验结果: " + isMatch);
    }
}