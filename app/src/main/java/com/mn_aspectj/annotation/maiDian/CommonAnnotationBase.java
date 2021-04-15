package com.mn_aspectj.annotation.maiDian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by Jone on 4/11/21
 * 顶层注解，作用域注解上，区分操作类型的注解的基类，用于标示不同行注解的作用
 *
 * 如果要收集10中类型的数据，需要多少个注解？需要10个，不同的注解标示不同的行为,同时创建10个AspectJ类
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonAnnotationBase {
    String type();//类型
    String actionId();//类型定义的id
}
