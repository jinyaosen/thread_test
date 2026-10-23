package com.demo;


public class Main {
    public static void main(String[] args) {
        UserDao dao = new UserDao();

        // 1. 先单线程跑通：注册一个用户，然后查出来看看
        //System.out.println(dao.addUser("testUser", "hash_123456"));
        System.out.println(dao.queryUser("testUser"));

        // 2. 单线程跑通后，再在这里引入线程池、ExecutorService
        // 让 100 个线程同时去调用 dao.addUser(...) 或 dao.queryUser(...)
    }
}