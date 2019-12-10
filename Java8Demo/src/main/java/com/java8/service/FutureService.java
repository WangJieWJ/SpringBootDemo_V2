package com.java8.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.*;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-24 22:44
 */
public class FutureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FutureService.class);

    /**
     * 线程池的Future测试
     */
    private static void threadPoolFutureTest() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 方式一
        Future runnableResult = executorService.submit(() -> {
            try {
                Thread.sleep(1000L);
                LOGGER.info("Runnable线程执行完毕");
            } catch (InterruptedException e) {
                LOGGER.error("线程被打断", e);
                Thread.currentThread().interrupt();
            }
        });
        runnableResult.get();
        LOGGER.info("获取Runnable线程执行结果");

        // 方式二
        Future<String> callableResult = executorService.submit(() -> {
            try {
                Thread.sleep(1000L);
                LOGGER.info("Callable线程执行完毕");
            } catch (InterruptedException e) {
                LOGGER.error("线程被打断", e);
                Thread.currentThread().interrupt();
            }
            return "callableResult";
        });
        LOGGER.info("获取Callable线程的执行结果:{}", callableResult.get());

        // 方式三
        ThreadRunInfo threadRunInfo = new ThreadRunInfo();
        threadRunInfo.setStartDate(new Date());
        Future<ThreadRunInfo> runnableWithParResult = executorService.submit(new DataLoadTask(threadRunInfo), threadRunInfo);
        LOGGER.info("带参数的Runnable线程执行结果为:{}", JSON.toJSONString(runnableWithParResult.get()));
    }

    /**
     * 线程运行结果
     */
    public static class ThreadRunInfo {
        /**
         * 开始时间
         */
        private Date startDate;
        /**
         * 结束时间
         */
        private Date endDate;
        /**
         * 执行结果
         */
        private String result;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    /**
     * 数据加载任务
     */
    public static class DataLoadTask implements Runnable {

        private ThreadRunInfo threadRunInfo;

        public DataLoadTask(ThreadRunInfo info) {
            this.threadRunInfo = info;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                LOGGER.error("线程被打断!", e);
                Thread.currentThread().interrupt();
            }
            threadRunInfo.setEndDate(new Date());
            threadRunInfo.setResult("线程执行结束");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        threadPoolFutureTest();
    }
}
