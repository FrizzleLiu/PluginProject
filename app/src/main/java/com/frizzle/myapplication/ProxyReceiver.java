package com.frizzle.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.frizzle.stander.ReceiverInterface;

/**
 * author: LWJ
 * date: 2020/9/8$
 * description
 * 代理广播接受者,这里是可以接收广播的
 *
 */
public class ProxyReceiver extends BroadcastReceiver {

    //插件中receiver全类名
    private final String pluginReceiverName;

    public ProxyReceiver(String pluginReceiverName) {
       this.pluginReceiverName = pluginReceiverName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //加载插件中的Receiver
        try {
            Class pluginReceiverClass = PluginManager.getInstance(context).getDexClassLoader().loadClass(pluginReceiverName);
            ReceiverInterface receiverInterface = (ReceiverInterface) pluginReceiverClass.newInstance();
            //执行插件中的onReceive方法
            receiverInterface.onReceive(context,intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
