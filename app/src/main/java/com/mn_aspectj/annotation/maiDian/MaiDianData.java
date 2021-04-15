package com.mn_aspectj.annotation.maiDian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by Jone on 4/11/21
 * 专门用来标示 上传用户行为统计
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CommonAnnotationBase(type = "event", actionId = "1001") //这里的命名需要与后端协商
public @interface MaiDianData {
    String value() default "";
}
