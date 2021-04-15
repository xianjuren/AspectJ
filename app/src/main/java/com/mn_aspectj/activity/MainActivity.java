package com.mn_aspectj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mn_aspectj.R;
import com.mn_aspectj.annotation.ExecuteTime;
import com.mn_aspectj.annotation.maiDian.MaiDianData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getTime();
        getAnnotationGetTime();
        getOtherTime();
    }

    public void getTime() {
        //oop
        // long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("执行时间：" + (endTime - startTime));
    }

    /**
     * 使用注解方式开启耗时监控
     */
    @ExecuteTime
    public void getAnnotationGetTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ExecuteTime
    public void getOtherTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
