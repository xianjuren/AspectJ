package com.mn_aspectj.aspectJ;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Create by Jone on 4/4/21
 * 声明一个织入类，由Aspectj编译器编译
 */

@Aspect
public class TimeAspectJ {
    //声明切入点,提供通配符语法：execution（得到切入点的方法）  call (只是调用切入点的方法)
    // *代表任意对象，任意字符,任意返回参数，与右侧有间隔 ; execution(包名+类名+方法名)  ;  ..两个点可以表示有参数和无参数
    //这是针对某个对象某个类的具体方法
    // @Pointcut("execution(* com.mn_aspectj.activity.MainActivity.getTime(..))")
    //针对项目中所有getTime（）的监控
    @Pointcut("execution(* getTime(..))")
    public void getTimes() {
    }

    //怎么把切点交给真正执行的方法，把切点的方法当做参数传入执行的注解方法。这里的注解必须依赖节点
    @Around("getTimes()") //ProceedingJoinPoint切入点 就是传递的参数getTimes()
    public void invokeGetTime(ProceedingJoinPoint joinPoint) {
        setTime(joinPoint);
    }

    /**
     * 使用注解方式开启所有标示的方法
     * 可以忽略方法名的不同
     */
    //@表示这是一个注解，不加表示它是一个类。@ExecuteTime只是标示了一个注解，切点需要的是注解标示的方法
    //* *(..) 第一个*表示方法的任意返回类型，第二个*表示任意的参数，两个* 左右都有间隔
    // @Pointcut("execution(@ExecuteTime void * (..))") //只匹配void返回类型
    //当注解类ExecuteTime，AspectJ文件TimeAspectJ，不在同一个目录下的时候，注解添加上路径
    @Pointcut("execution(@com.mn_aspectj.annotation.ExecuteTime * * (..))")
    public void annotationGetTime() {
    }

    //选择了Around注解，方法中一定要有joinPoint.proceed()，否则它获取的方法，即便在MainActivity调用了也不会执行。
    //多再次做登陆判断
    @Around("annotationGetTime()")
    public void invokeAnnotationGetTime(ProceedingJoinPoint joinPoint) {
        setTime(joinPoint);
    }

    //before,After注解是获取不到ProceedingJoinPoint对应的方法的，这时是无参的
    //在切入点之前去织入，可以做行为统计
    @Before("annotationGetTime()")
    public void beforeInvoke() {
        Log.e("MN------->", "Before");
    }

    //在切入点之后去织入
    @After("annotationGetTime()")
    public void afterInvoke() {
        Log.e("MN------->", "After");
    }

    private void setTime(ProceedingJoinPoint joinPoint) {
        //执行切入点的方法
        long startTime = System.currentTimeMillis();
        try {
            joinPoint.proceed();//执行获取的方法
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        Log.e("MN------->", "执行时间:" + (endTime - startTime));
    }

}
