package com.itheima.web.listeners;

import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/**
 * 监听ServletContext域中属性发生变化的监听器
 */
public class ServletContextAttributeListenerImpl implements ServletContextAttributeListener{

    /**
     * 存入应用域
     * @param servletContextAttributeEvent
     */
    @Override
    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent) {
        //取出ServletContext对象
        ServletContext context = servletContextAttributeEvent.getServletContext();
        //取出spring的ioc容器
        WebApplicationContext webApplicationContext = (WebApplicationContext) context.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        //取出所有的bean名称
        String[] names = webApplicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent) {

    }
}
