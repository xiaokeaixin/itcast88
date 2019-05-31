package com.itheima.test.proxy;

import com.itheima.dao.system.UserDao;
import com.itheima.service.system.impl.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        Proxy.newProxyInstance(UserDao.class.getClassLoader(),
                new Class[]{UserDao.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //增强代码
                        return null;
                    }
                });
        Proxy.newProxyInstance(UserServiceImpl.class.getClassLoader(),
                UserServiceImpl.class.getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //增强代码
                        return null;
                    }
                });
    }
}
