package com.mn_aspectj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mn_aspectj.interfaceImpl.PermissionRequestCallback;
import com.mn_aspectj.util.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Create by Jone on 4/5/21
 */
public class ApplyPermissionActivity extends AppCompatActivity {

    private static final String REQUEST_PERMISSION = "request_permission";
    private static final String REQUEST_CODE = "request_code";
    private static PermissionRequestCallback sRequestCallback;

    public static void launchActivity(Context context, String[] permissions,
                                      int requestCode, PermissionRequestCallback callback) {
        sRequestCallback = callback;
        Bundle bundle = new Bundle();
        bundle.putStringArray(REQUEST_PERMISSION, permissions);
        bundle.putInt(REQUEST_CODE, requestCode);
        Intent intent = new Intent(context, ApplyPermissionActivity.class);//透明Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            String[] permissions = extras.getStringArray(REQUEST_PERMISSION);
            int requestCode = extras.getInt(REQUEST_CODE, -1);
            if (permissions == null || requestCode == -1 || sRequestCallback == null) {
                finish();
                return;
            }
            System.out.println("==============执行======Apply");
            //开始申请权限
            if (PermissionUtil.hasPermissionRequest(this, permissions)) {
                sRequestCallback.permissionSuccess();
                finish();
            }
            //申请权限，已有权限的在这里不会触发，无需我们去排除
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        } else finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //在请求结果返回的时候，这里不能直接执行不同的业务逻辑，需要一个回调，把真实的业务需求在需要执行的地方去执行，这里作为一个框架的调用类
        //判断所有权限是否申请成功
        System.out.println("==============执行======onRequestPermissionsResult");
        if (PermissionUtil.requestPermissionSuccess(grantResults))
        // if (PermissionUtil.hasPermissionRequest(this, permissions)) //两种判断方法都可以
        {
            sRequestCallback.permissionSuccess();
            finish();
            return;
        }
        //权限拒绝，用户勾选了不再提示
        if (PermissionUtil.shouldShowRequestPermissionRationale(this, permissions)) {
            sRequestCallback.permissionDenied();
            finish();
            return;
        }
        //用户点击了决绝，但不是永久拒绝
        sRequestCallback.permissionCanceled();
        finish();
    }

    @Override
    public void finish() {
        super.finish();//销毁时，没有感知
        overridePendingTransition(0, 0);
    }
}
