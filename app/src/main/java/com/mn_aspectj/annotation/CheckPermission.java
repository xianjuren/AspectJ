package com.mn_aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by Jone on 4/5/21
 */
@Target(ElementType.METHOD) //声明作用域
@Retention(RetentionPolicy.RUNTIME) //生命周期：在代码织入或者运行的时候都可能用到
public @interface CheckPermission {
    /**
     * 1.权限有哪些
     * 2.请求码
     */
    String[] value();

    int requestCode();
}
