package com.frizzle.plugin_package;

import android.app.Activity;
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
public class BaseService extends Service implements ServiceInterface {

    //宿主传进来的Activity
    public Service appService;

    //注入宿主的上下文
    public void insertAppContext(Service appService) {
        this.appService = appService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {
    }
}
