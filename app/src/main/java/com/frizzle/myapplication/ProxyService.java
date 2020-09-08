package com.frizzle.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.frizzle.stander.ServiceInterface;

/**
 * author: LWJ
 * date: 2020/9/8$
 * description
 */
public class ProxyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String className = intent.getStringExtra("className");
        //插件中的Service全类名
        try {
            Class pluginServiceClass = PluginManager.getInstance(this).getDexClassLoader().loadClass(className);
            ServiceInterface serviceInterface = (ServiceInterface) pluginServiceClass.newInstance();
            //注入组件环境
            serviceInterface.insertAppContext(this);
            serviceInterface.onStartCommand(intent,flags,startId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
