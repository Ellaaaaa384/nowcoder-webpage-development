package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    // 定义切点
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))") // 任意方法返回值，service包下所有的业务组件下所有的方法里所有的参数
    public void pointcut() {

    }
    // 定义通知（五种）
    // 连接点开头记日志
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }
    // 连接点之后
    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }
    // 返回值后
    @AfterReturning("pointcut()")
    public void afterReturning() {
        System.out.println("afterReturning");
    }
    // 抛异常时
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }
    // 前后都织入
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed(); // 目标组件的返回值
        System.out.println("around after");
        return obj;
    }

}
