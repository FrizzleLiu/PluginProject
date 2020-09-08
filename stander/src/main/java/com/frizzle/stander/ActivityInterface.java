package com.frizzle.stander;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * author: LWJ
 * date: 2020/9/7$
 * description
 */
public interface ActivityInterface {

    //插件没有组件环境(没有上下文),需要在标准中提供接口 插入宿主的上下文供插件使用
    void insertAppContext(Activity appActivity);

    //几个声明周期方法,实际开发中需要写全
    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onDestroy();
}
