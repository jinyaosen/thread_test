package com.demo;

import com.zaxxer.hikari.HikariPoolMXBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWithPool {
    public static void main(String[] args) throws InterruptedException {
        int threadCount = 100;
        int userCount = 100;

        // 从 DBUtil 获取连接池，传给 DAO
        UserDaoWithPool userDao = new UserDaoWithPool(DBUtil.getDataSource());

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(userCount);

        System.out.println("====== 开始执行 "+userCount+" 个并发插入 (HikariCP 连接池) ======");
        long startTime = System.currentTimeMillis();
        HikariPoolMXBean mx = DBUtil.getDataSource().getHikariPoolMXBean();
        System.out.printf("开始前: total=%d active=%d idle=%d waiting=%d%n",
                mx.getTotalConnections(), mx.getActiveConnections(),
                mx.getIdleConnections(), mx.getThreadsAwaitingConnection());

        for (int i = 1; i <= userCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    String username = "pool_user_" + index;
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
        System.out.printf("结束后: total=%d active=%d idle=%d waiting=%d%n",
                mx.getTotalConnections(), mx.getActiveConnections(),
                mx.getIdleConnections(), mx.getThreadsAwaitingConnection());
        long endTime = System.currentTimeMillis();
        System.out.println("====== 所有任务执行完毕 ======");
        System.out.println("🚀 总耗时: " + (endTime - startTime) + " 毫秒");

        executorService.shutdown();

        // 实际项目中连接池通常不需要手动关闭，除非应用停机
        // 这里为了结束 JVM 进程，我们就不写 dataSource.close() 了，JVM 退出会自动释放。
    }
}