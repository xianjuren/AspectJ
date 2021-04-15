package com.mn_aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by Jone on 4/5/21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface ExecuteTime {
}
