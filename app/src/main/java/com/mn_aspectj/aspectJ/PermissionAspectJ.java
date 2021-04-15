package com.mn_aspectj.aspectJ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.mn_aspectj.activity.ApplyPermissionActivity;
import com.mn_aspectj.annotation.CheckPermission;
import com.mn_aspectj.annotation.PermissionDenied;
import com.mn_aspectj.annotation.PermissionFailed;
import com.mn_aspectj.interfaceImpl.PermissionRequestCallback;
import com.mn_aspectj.util.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import androidx.fragment.app.Fragment;

/**
 * Create by Jone on 4/5/21
 * 权限织入类
 */
@Aspect
public class PermissionAspectJ {

    //声明切入点,提供通配符语法：execution（得到切入点的方法）  call (只是调用切入点的方法)
    //@表示这是一个注解，不加表示它是一个类。@CheckPermission只是标示了一个注解，切点需要的是注解标示的方法
    //* *(..) 表示任何方法，任何参数，两个* 左右都有间隔
    // @Pointcut("execution(* initPermissions())") ,
    // && @annotation(checkPermission)" 表示把注解也传递过来，需要通过注解获取传递的参数，
    // 这里是权限名，请求码，括号内的变量名要与下面参数的变量名一致
    @Pointcut("execution(@com.mn_aspectj.annotation.CheckPermission * * (..)) && @annotation(permissions)")
    public void checkPermission(CheckPermission permissions) {
    }

    //这里是需要传递参数的变量名，而不用CheckPermission checkPermission{
    @Around("checkPermission(checkPermission)")
    public void invokeCheckPermission(final ProceedingJoinPoint joinPoint, CheckPermission checkPermission) {
        System.out.println("==============执行======invokeCheckPermission");
        //在方法执行之前进行权限申请，成功继续执行，失败就不执行。
        //权限申请的代码是否可以在这里执行？不行，一是拿不到context,最主要是onRequestPermissionsResult回调只有Activity中有
        //解决思路：创建一个透明的Activity ，先获取上下文
        Context context = null;
        //获取切入点的对象，但是部分注解可能是写在工具类中,获取到的当前类可能不是Activity
        Object aThis = joinPoint.getThis();
        if (aThis instanceof Activity) {
            context = (Context) aThis;
        } else if (aThis instanceof Fragment) {
            context = ((Fragment) aThis).getActivity();
        }
        if (context == null || checkPermission == null || checkPermission.value().length == 0)
            return;
        //获取权限
        String[] permissions = checkPermission.value();
        //跳转到透明Activity
        final Context finalContext = context;
        System.out.println("==============执行======ApplyPermissionActivity");
        ApplyPermissionActivity.launchActivity(context, permissions, checkPermission.requestCode(),
                new PermissionRequestCallback() {
                    @Override
                    public void permissionSuccess() {
                        System.out.println("============执行=====permissionSuccess");
                        try {
                            joinPoint.proceed();//执行原有逻辑
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override //申请失败，用户拒绝
                    public void permissionCanceled() {//把结果回调到context(Activity)对应的添加了该注解的方法中
                        System.out.println("=========执行=====permissionCanceled");
                        PermissionUtil.invokeAnnotation(finalContext, PermissionFailed.class,checkPermission.requestCode());
                    }

                    @Override //把结果回调到context(Activity)对应的添加了该注解的方法中
                    public void permissionDenied() {//申请失败，用户点击了不在询问,永久拒绝
                        System.out.println("=========执行=====permissionDenied");
                        PermissionUtil.invokeAnnotation(finalContext, PermissionDenied.class,checkPermission.requestCode());
                    }
                });

    }

}
