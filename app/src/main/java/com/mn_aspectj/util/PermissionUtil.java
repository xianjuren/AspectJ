package com.mn_aspectj.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.mn_aspectj.annotation.PermissionDenied;
import com.mn_aspectj.annotation.PermissionFailed;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Create by Jone on 4/5/21
 */
public class PermissionUtil {

    public static final String MANUFACTURER_HUAWEI = "huawei";//华为
    public static final String MANUFACTURER_MEIZU = "meizu";//魅族
    public static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    public static final String MANUFACTURER_SONY = "sony";//索尼
    public static final String MANUFACTURER_OPPO = "oppo";
    public static final String MANUFACTURER_LG = "lg";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    public static final String MANUFACTURER_LETV = "letv";//乐视
    public static final String MANUFACTURER_ZTE = "zte";//中兴
    public static final String MANUFACTURER_YULONG = "yulong";//酷派
    public static final String MANUFACTURER_LENOVO = "lenovo";//联想
    private static final String MANUFACTURER_DEFAULT = "Default";//默认
    private static HashMap<String, Class<? extends ISetting>> permissionMenu = new HashMap<>();

    static {
        permissionMenu.put(MANUFACTURER_DEFAULT, DefaultStartSettings.class);
        permissionMenu.put(MANUFACTURER_OPPO, OPPOStartSettings.class);
        permissionMenu.put(MANUFACTURER_VIVO, VIVOStartSettings.class);
    }

    /**
     * 判断所有权限是否都给了  如果有一个权限没给  就返回false
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissionRequest(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 最后判断下 是否全部真正的成功
     *
     * @param gantedResult
     * @return
     */
    public static boolean requestPermissionSuccess(int... gantedResult) {
        if (gantedResult == null || gantedResult.length <= 0) {
            return false;
        }
        for (int permissionValue : gantedResult) {
            if (permissionValue != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 用户是否拒绝了并且点击了不再提示
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            //如果用户点击了不再提示  就返回true
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 专门去 callback invoke ---》 执行  被注解的方法
     *
     * @param object          切点所在的对象
     * @param annotationClass 注解类
     * @param code
     */
    public static void invokeAnnotation(Object object, Class annotationClass, int code) {
        // 获取 object 的 Class对象
        Class<?> objectClass = object.getClass();
        // 遍历 所有的方法
        Method[] methods = objectClass.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true); // 让虚拟机，不要去检测 private
            // 判断方法 是否有被 annotationClass注解的方法
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);
            int mCurrentCode = -1;
            if (annotationPresent) {
                // 当前方法 代表包含了 annotationClass注解的
                Annotation annotation = method.getAnnotation(annotationClass);
                if (annotation instanceof PermissionFailed) {
                    mCurrentCode = ((PermissionFailed) annotation).requestCode();
                } else if (annotation instanceof PermissionDenied) {
                    mCurrentCode = ((PermissionDenied) annotation).requestCode();
                }
                try {
                    if (mCurrentCode == code)
                        method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO 专门去 跳转到 设置界面
    public static void startAndroidSettings(Context context) {
        // 拿到当前手机品牌制造商，来获取 具体细节
        Class aClass = permissionMenu.get(Build.MANUFACTURER.toLowerCase());

        if (aClass == null) {
            aClass = permissionMenu.get(MANUFACTURER_DEFAULT);
        }

        try {
            Object newInstance = aClass.newInstance(); // new OPPOStartSettings()

            ISetting iMenu = (ISetting) newInstance; // ISetting iMenu = (ISetting) oPPOStartSettings;
            // 高层 面向抽象，而不是具体细节
            Intent startActivityIntent = iMenu.getStartSettingsIntent(context);
            if (startActivityIntent != null) {
                context.startActivity(startActivityIntent);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
