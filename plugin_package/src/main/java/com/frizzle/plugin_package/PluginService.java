package com.frizzle.plugin_package;

import android.content.Intent;
import android.util.Log;

/**
 * author: LWJ
 * date: 2020/9/8$
 * description
 */
public class PluginService extends BaseService{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启子线程,执行耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        Log.e("Frizzle","插件服务运行中...");
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
