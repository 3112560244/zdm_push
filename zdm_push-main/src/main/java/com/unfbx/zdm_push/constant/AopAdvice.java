package com.unfbx.zdm_push.constant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author: ZedQ
 * @Date: 2022/12/9 09:31
 * @Description:
 */
@Aspect
@Component
public class AopAdvice {

    //切入点：待增强的方法
    @Pointcut("execution(public * com.unfbx.zdm_push.processor.*(..))")
    //切入点签名
    public void log(){
        System.out.println("pointCut签名。。。");
    }

    @Before("log()")
    public void beforeAdvice() {
        System.out.println("beforeAdvice...");
    }

    @After("log()")
    public void afterAdvice() {
        System.out.println("afterAdvice...");
    }

    @Around("log()")
    public void aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("before");
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("after");
    }

}