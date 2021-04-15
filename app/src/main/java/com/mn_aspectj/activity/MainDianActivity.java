package com.mn_aspectj.activity;

import android.os.Bundle;
import android.view.View;

import com.mn_aspectj.R;
import com.mn_aspectj.annotation.maiDian.MaiDianData;
import com.mn_aspectj.annotation.maiDian.ParamsAnnotation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Create by Jone on 4/11/21
 */
public class MainDianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 给服务器上传数据
     *
     * @param view
     */
    public void sendData(View view) {
        setUserToUi("Jone","qwe123");
    }

    /**
     * 收集用户当前Activity的行为统计
     *
     * @param name
     */
    @MaiDianData
    public void setUserToUi(@ParamsAnnotation("name") String name,@ParamsAnnotation("password") String password) {

    }

    //上传异常统计
    public void uploadError(String name, String time) {


    }
}
