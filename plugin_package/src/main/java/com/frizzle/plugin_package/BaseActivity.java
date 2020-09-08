package com.frizzle.plugin_package;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.frizzle.stander.ActivityInterface;

/**
 * author: LWJ
 * date: 2020/9/7$
 * description
 */
public class BaseActivity extends Activity implements ActivityInterface {
    //宿主传进来的Activity
    public Activity appActivity;

    //注入宿主的上下文
    @Override
    public void insertAppContext(Activity appActivity) {
        this.appActivity = appActivity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    public void setContentView(int resId){
        appActivity.setContentView(resId);
    }

    public View findViewById(int layoutId){
     return appActivity.findViewById(layoutId);
    }

    public void startActivity(Intent intent){
        Intent intentNew = new Intent();
        intentNew.putExtra("className",intent.getComponent().getClassName());//PluginActivity的全类名
        appActivity.startActivity(intentNew);
    }

    @Override
    public ComponentName startService(Intent intent){
        Intent intentNew = new Intent();
        intentNew.putExtra("className",intent.getComponent().getClassName());//PluginService的全类名
        return appActivity.startService(intentNew);
    }

}
