///**
// * haifenbb.com
// * Copyright (C) 2019-2020 All Rights Reserved.
// */
//package com.example.datasource.aspect;
//
//import com.example.datasource.DynamicDataSourceContextHolder;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class DataSourceAop {
//    /**
//     * 只读：
//     * 不是Master注解的对象或方法  && select开头的方法  ||  get开头的方法
//     */
//    @Pointcut("!@annotation(com.example.datasource.annotation.TargetDataSource) " +
//            "&& (execution(* com.example.service.UserService..*.select*(..)) " +
//            "|| execution(* com.example.service.UserService..*.get*(..)))")
//    public void readPointcut() {
//
//    }
//
//    /**
//     * 写：
//     * Master注解的对象或方法 || insert开头的方法  ||  add开头的方法 || update开头的方法
//     * || edlt开头的方法 || delete开头的方法 || remove开头的方法
//     */
//    @Pointcut("@annotation(com.example.datasource.annotation.TargetDataSource) " +
//            "|| execution(* com.example.service.UserService..*.insert*(..)) " +
//            "|| execution(* com.example.service.UserService..*.add*(..)) " +
//            "|| execution(* com.example.service.UserService..*.update*(..)) " +
//            "|| execution(* com.example.service.UserService..*.edit*(..)) " +
//            "|| execution(* com.example.service.UserService..*.delete*(..)) " +
//            "|| execution(* com.example.service.UserService..*.remove*(..))")
//    public void writePointcut() {
//
//    }
//
//    @Before("readPointcut()")
//    public void read() {
//        System.out.println("********** read **********");
//        DynamicDataSourceContextHolder.slave();
//    }
//
//    @Before("writePointcut()")
//    public void write() {
//        System.out.println("********** write **********");
//        DynamicDataSourceContextHolder.master();
//    }
//
//}