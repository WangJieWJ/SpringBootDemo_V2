package com.java8.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-09-18 15:19
 */
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    /**
     * 锁：保护账户余额
     */
    private final byte[] balLock = new byte[0];
    /**
     * 账户余额
     */
    private Integer balance;
    /**
     * 锁：保护账户密码
     */
    private final byte[] pwdLock = new byte[0];
    /**
     * 账户密码
     */
    private String password;

    /**
     * 账户余额 累加
     *
     * @param addNum 数额
     * @return 累加之后数额
     */
    public Integer addBalance(Integer addNum) {
        synchronized (balLock) {
            this.balance += addNum;
            reduceBalance(0);
            return this.balance;
        }
    }

    /**
     * 账户余额 减少
     *
     * @param reduceNum 数额
     * @return 是否成功
     */
    public boolean reduceBalance(Integer reduceNum) {
        synchronized (balLock) {
            if (this.balance >= reduceNum) {
                this.balance -= reduceNum;
                return true;
            }
            return false;
        }
    }

    /**
     * 获取账户余额
     *
     * @return 账户余额
     */
    public Integer getBalance() {
        synchronized (balLock) {
            return this.balance;
        }
    }

    /**
     * 修改账户密码
     *
     * @param updatePassword 修改后的密码
     * @return 修改之后的密码
     */
    public String updatePwd(String updatePassword) {
        synchronized (pwdLock) {
            this.password = updatePassword;
            return this.password;
        }
    }

    /**
     * 获取账户密码
     *
     * @return 账户密码
     */
    public String getPassword() {
        synchronized (pwdLock) {
            return this.password;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AccountService accountService = new AccountService();
        accountService.balance = 10;
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Integer balance = accountService.addBalance(i);
                LOGGER.info("线程t1已经执行完，账户余额的累加，累加之后金额为:{}", balance);
                boolean reduceResult = accountService.reduceBalance(i);
                LOGGER.info("线程t1已经执行完，账户余额的扣减，扣减是否成功:{}", reduceResult);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Integer balance = accountService.addBalance(i);
                LOGGER.info("线程t2已经执行完，账户余额的累加，累加之后金额为:{}", balance);
                boolean reduceResult = accountService.reduceBalance(i);
                LOGGER.info("线程t2已经执行完，账户余额的扣减，扣减是否成功:{}", reduceResult);
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        LOGGER.info("账户余额为:{}", accountService.getBalance());
    }
}
