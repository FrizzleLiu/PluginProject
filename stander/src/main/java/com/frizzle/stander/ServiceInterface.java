package com.frizzle.stander;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * author: LWJ
 * date: 2020/9/8$
 * description
 */
public interface ServiceInterface {

    //注入宿主的上下文
    void insertAppContext(Service appService);

    IBinder onBind(Intent intent);

    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();
}
