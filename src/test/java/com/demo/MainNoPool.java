package com.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainNoPool {
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 10000;
        UserDao userDao = new UserDao();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        System.out.println("====== 开始执行 "+threadCount+ " 个并发插入 (原生 DriverManager) ======");
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= threadCount; i++) {
            final int index = i;
            executorService.submit(() ->{
                try{
                    /*TRUNCATE TABLE users;
                    * 快速清空数据库当前数据*/
                    String username = "no_pool_user" + index;
                    String password = "pwd_" + index;

                    Long id = userDao.addUser(username, password);
                    System.out.println(Thread.currentThread().getName() + " 插入成功，ID: " + id);
                } catch (Exception e) {
                    System.err.println(Thread.currentThread().getName() + " 执行失败: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long endTime = System.currentTimeMillis();
        System.out.println("====== 所有任务执行完毕 ======");
        System.out.println("🔥 总耗时: " + (endTime - startTime) + " 毫秒");

        // 4. 关闭线程池
        executorService.shutdown();
    }
}
