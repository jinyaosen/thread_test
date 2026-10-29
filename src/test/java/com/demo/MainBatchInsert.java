package com.demo;

public class MainBatchInsert {
    public static void main(String[] args) {
        UserDaoWithPool userDao = new UserDaoWithPool(DBUtil.getDataSource());

        int totalCount = 10000; // 插入一万条

        System.out.println("====== 开始执行 " + totalCount + " 条批量插入 (单线程 + 批处理) ======");

        long startTime = System.currentTimeMillis();

        // 单线程直接调用批量插入
        userDao.addUserBatch(totalCount);

        long endTime = System.currentTimeMillis();

        System.out.println("====== 插入完毕 ======");
        System.out.println("🔥 总耗时: " + (endTime - startTime) + " 毫秒");
    }
}