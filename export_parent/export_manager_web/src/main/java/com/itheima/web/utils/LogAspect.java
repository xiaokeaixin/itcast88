package com.itheima.web.utils;

import com.itheima.commons.utils.UtilFuns;
import com.itheima.domain.system.SysLog;
import com.itheima.domain.system.User;
import com.itheima.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 用于记录系统日志的信息
 * 需求：
 * 在用户点击了页面链接到达控制器之前，先来记录系统日志
 * 要求：
 * 使用spring基于注解的aop配置
 * 使用环绕通知
 * 增强方法写在控制器方法执行之前
 */
@Aspect//设置为切面类
@Component//让spring接管切面类的创建
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    /**
     * 环绕通知
     *
     * @param pjp 他是用于调用切入点方法（核心方法）的对象
     * @return private String id;          日志的唯一标识，此处不用处理，在service中处理
     * private String userName;    用户的名称，此处要处理        在用户对象里，用户对象在session域中，需要一个session
     * private String ip;          来访者ip，此处要处理          在request对象里                   需要一个request
     * private Date time;          来访时间，此处要处理          获取当前系统时间
     * private String method;      操作的方法，此处要处理        在环绕通知的参数中（pjp）
     * private String action;      操作的名称，此处要处理        在环绕通知的参数中，同时要求控制器方法的RequestMapping注解的name属性必须有值
     * private String companyId;   来访者的企业id                在用户对象里
     * private String companyName; 来访者的企业名称              在用户对象里
     */
    @Around("execution(* com.itheima.web.controller.*.*.*(..))")//配置环绕通知，同时制定切入点表达式
    public Object aroundStsLog(ProceedingJoinPoint pjp) {
        try {
            //获取pjp的签名
            Signature signature = pjp.getSignature();
            //判断当前的签名是不是方法签名
            if (signature instanceof MethodSignature) {
                //转成方法签名
                MethodSignature ms = (MethodSignature) signature;
                //从方法签名中获取当前方法
                Method method = ms.getMethod();
                //判断当前方法是不是被RequestMapping注解了
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    //创建返回值对象
                    SysLog sysLog = new SysLog();
                    //取出用户信息
                    User user = (User) session.getAttribute("user");
                    //给log中关于用户信息的数据赋值
                    if (user == null || UtilFuns.isEmpty(user.getUserName())) {
                        sysLog.setUserName("匿名");
                    } else {
                        sysLog.setUserName(user.getUserName());
                        sysLog.setCompanyId(user.getCompanyId());
                        sysLog.setCompanyName(user.getCompanyName());
                    }
                    //设置日志创建时间
                    sysLog.setTime(new Date());
                    //获取来访者id
                    String remoteId = request.getRemoteAddr();
                    sysLog.setIp(remoteId);
//                    System.out.println(remoteId);
                    //取出RequestMapping注解
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    //取出注解中name属性的值
                    String action = mapping.name();
                    //给log的action属性复制
                    sysLog.setAction(action);
                    //给方法名称赋值
                    sysLog.setMethod(method.getName());
                    sysLogService.saveLog(sysLog);
                }
            }
            //System.out.println("记录日志的方法执行了！");
            //获取当前执行方法所需的参数
            Object[] args = pjp.getArgs();
            //执行方法并返回
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
    }
    //开启spring对注解的支持
}
