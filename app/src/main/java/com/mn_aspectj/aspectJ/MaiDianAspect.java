package com.mn_aspectj.aspectJ;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.mn_aspectj.annotation.maiDian.CommonAnnotationBase;
import com.mn_aspectj.annotation.maiDian.ParamsAnnotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Create by Jone on 4/11/21
 */
@Aspect
public class MaiDianAspect {
    //定义一个切点 然后进行匹配。任意方法 ，任意参数类型
    @Pointcut("execution(@com.mn_aspectj.annotation.maiDian.MaiDianData * * (..))")
    public void uploadMaiDian() {
    }


    //定义一个连接点
    @Around("uploadMaiDian()")
    public void executeSetting(ProceedingJoinPoint joinPoint) {
        //Log.e("MN---->","executeSetting");
        //通过反射得到方法.ProceedingJoinPoint不仅可以切到方法，还可以切到类和成员变量
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法上的所有注解
        Annotation[] annotations = method.getAnnotations();
        //获取这个方法内接收的属性注解；public void setUserToUi(@ParamsAnnotation("name") String name
        Annotation[] paramsAnnotation = getMethodParamsAnnotation(method);
        //创建一个CommonAnnotationBase
        CommonAnnotationBase commonAnnotationBase = null;
        //遍历方法的注解
        for (Annotation annotation : annotations) {
            //得到它的注解类型
            Class<?> aClass = annotation.annotationType();//这里是MaiDianData注解
            commonAnnotationBase = aClass.getAnnotation(CommonAnnotationBase.class);//MaiDianData上面的注解@CommonAnnotationBase
            if (commonAnnotationBase == null) {
                break;
            }
        }

        //return搬到for循环外边，是为了继续执行原有的业务逻辑，然后在return
        if (commonAnnotationBase == null) {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
        //获取注解携带的type以及Id
        String type = commonAnnotationBase.type();
        String actionId = commonAnnotationBase.actionId();
        //public void setUserToUi(@ParamsAnnotation("name") String name,@ParamsAnnotation("password") String password)
        Object[] args = joinPoint.getArgs();//获取该方法接收的所有参数:name ,password
        JSONObject object = getData(paramsAnnotation, args);
        //把收集起来的数据上传到服务器
        String msg = "上传埋点：" + "type：" + type + " actionId:" + actionId + " data: " + object.toString();
        Log.e("MN---->", msg);
        //可以通过上次类似的权限申请的注解回调，把埋点信息传入到上传的代码中

        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    /**
     * 将采集到的数据 key value
     *
     * @param paramsAnnotation
     * @param args
     * @return
     */
    private JSONObject getData(Annotation[] paramsAnnotation, Object[] args) {
        if (paramsAnnotation == null || paramsAnnotation.length <= 0) return null;
        JSONObject object = new JSONObject();
        for (int i = 0; i < paramsAnnotation.length; i++) {
            Annotation annotation = paramsAnnotation[i];
            if (annotation instanceof ParamsAnnotation) {
                String paramName = ((ParamsAnnotation) annotation).value();
                object.put(paramName, args[i].toString());
            }
        }
        return object;
    }

    /**
     * 获取到方法参数的注解
     *
     * @param method
     * @return
     */
    public Annotation[] getMethodParamsAnnotation(Method method) {
        //Parameter[] parameters = method.getParameters();//只支持最低API >= 26，不推荐使用
        //二维数组获取所有属性的注解,注意这里不是注解上还有注解，而是一个属性可能有多个注解。
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) return null;
        //方法接收的参数，可能并不是全部是注解参数，所以这里的数组长度需要是注解参数的数量，而不是方法参数的数量
        //public void setUserToUi(@ParamsAnnotation("name") String name,int age)
        Annotation[] annotations = new Annotation[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                annotations[i++] = annotation;
                //Log.e("MN------>", annotation + "");
            }
        }
        return annotations;

    }
}
