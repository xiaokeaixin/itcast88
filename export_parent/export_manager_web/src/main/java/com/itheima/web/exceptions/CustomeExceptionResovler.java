package com.itheima.web.exceptions;

import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常处理器类
 */
@Component
public class CustomeExceptionResovler implements HandlerExceptionResolver{
    /**
     * 此方法是用于前往错误页面
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //1.定义返回值对象
        ModelAndView mv = new ModelAndView();
        //2.设置响应页面
        mv.setViewName("error");
        //3.设置错误消息
        //判断e是不是自定义异常类型
        if (e instanceof CustomeException) {
            //自定义异常类型
            mv.addObject("errorMsg", e.getMessage());
        } else if (e instanceof UnauthenticatedException) {
            mv.setViewName("forward:/unauthorized.jsp");
        } else {
            e.printStackTrace();//打印到控制台
            mv.addObject("errorMsg", "服务器忙！");
        }
        return mv;
    }
}
