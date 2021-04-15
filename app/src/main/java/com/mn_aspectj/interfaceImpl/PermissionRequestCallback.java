package com.mn_aspectj.interfaceImpl;

/**
 * Create by Jone on 4/5/21
 * 框架层把申请的结果返回给调用者
 */
public interface PermissionRequestCallback {

    void permissionSuccess();//申请成功

    void permissionCanceled();//申请失败，用户拒绝

    void permissionDenied();//申请失败，用户点击了不在询问
}
