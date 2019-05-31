package com.itheima.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * CompanyService的服务启动类
 */
public class CargoProvider {
    public static void main(String[] args) throws IOException {
        //1.读取配置文件
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        //2.启动容器
        context.start();
        //3.任意键退出
        System.in.read();
    }
}
