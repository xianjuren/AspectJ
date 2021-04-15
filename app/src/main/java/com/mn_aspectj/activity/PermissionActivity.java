package com.mn_aspectj.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.mn_aspectj.R;
import com.mn_aspectj.annotation.CheckPermission;
import com.mn_aspectj.annotation.PermissionDenied;
import com.mn_aspectj.annotation.PermissionFailed;
import com.mn_aspectj.util.PermissionUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Create by Jone on 4/5/21
 */
public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        //initPermissions();
    }

    //加载注解,作为一个切入点
    @CheckPermission(value = Manifest.permission.READ_EXTERNAL_STORAGE, requestCode = 1)
    private void getSd() {
        //具体的读写逻辑
        Toast.makeText(this, "读取成功", Toast.LENGTH_SHORT).show();
    }


    /**
     * 点击事件
     *
     * @param view
     */
    public void getSd(View view) {
        //判断权限逻辑
        getSd();
    }

    @PermissionFailed(requestCode = 1)
    private void requestPermissionFailed() {
        Toast.makeText(this, "用户拒绝了权限", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(requestCode = 1)
    private void requestPermissionDenied() {
        Toast.makeText(this, "权限申请失败,不再询问", Toast.LENGTH_SHORT).show();
        //开发者可以根据自己的需求看是否需要跳转到设置页面去
        PermissionUtil.startAndroidSettings(this);
    }

    public void goSetting(View view) {
        Uri packageURI = Uri.parse("package:" + "com.mn_aspectj");
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivity(intent);
    }

    @CheckPermission(value = Manifest.permission.CAMERA, requestCode = 2)
    public void getCamera(View view) {

    }

    @PermissionDenied(requestCode = 2)
    private void requestPermissionCmeraDenied() {
        Toast.makeText(this, "相机权限申请失败,不再询问", Toast.LENGTH_SHORT).show();
    }


    @PermissionFailed(requestCode = 2)
    private void requestPermissionCameraFailed() {
        Toast.makeText(this, "用户拒绝了相机权限", Toast.LENGTH_SHORT).show();
    }


//    //调用此方法判断是否拥有权限
//    private void initPermissions() {
//        //判断是否已经拥有权限
//        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //判断是否永久拒绝了权限
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    //权限申请被永久拒绝
//                    Uri packageURI = Uri.parse("package:" + "com.mn_aspectj");
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                    startActivity(intent);
//                } else {
//                    //请求权限，第二参数权限String数据，第三个参数是请求码便于在onRequestPermissionsResult 方法中根据code进行判断
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            1);
//                }
//            } else {
//
//            }
//        } else {
//            //拥有权限执行操作
//            Toast.makeText(PermissionActivity.this, "已经拥有权限", Toast.LENGTH_LONG).show();
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {//响应Code
//            case 1:
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(this, "未拥有相应权限", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    }
//                    //拥有权限执行操作
//                } else {
//                    Toast.makeText(this, "未拥有相应权限", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }
}
