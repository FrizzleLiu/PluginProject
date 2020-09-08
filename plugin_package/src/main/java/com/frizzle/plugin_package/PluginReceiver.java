package com.frizzle.plugin_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.frizzle.stander.ReceiverInterface;

/**
 * author: LWJ
 * date: 2020/9/8$
 * description
 */
public class PluginReceiver extends BroadcastReceiver implements ReceiverInterface {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Frizzle","插件中动态注册的广播  收到广播....");
    }
}
