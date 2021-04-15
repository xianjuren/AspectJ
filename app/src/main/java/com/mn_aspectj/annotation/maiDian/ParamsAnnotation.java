package com.mn_aspectj.annotation.maiDian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by Jone on 4/12/21
 * 用来标示每个数据收集所对应的数据的key，它是参数注解
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})//参数注解可以声明在参数上，以及方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamsAnnotation {
    String value() default "";//@ParamsAnnotation String name
}
