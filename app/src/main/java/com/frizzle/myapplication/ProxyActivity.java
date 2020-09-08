package com.frizzle.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.frizzle.stander.ActivityInterface;

import java.lang.reflect.Constructor;

/**
 * author: LWJ
 * date: 2020/9/7$
 * description
 * 代理(占位)Activity 用于加载插件中的Activity
 */
public class ProxyActivity extends Activity {
    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getDexClassLoader();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = getIntent().getStringExtra("className");
        try {
            Class pluginActivityClass = getClassLoader().loadClass(className);
            //获取插件包中Activity的构造
            Constructor constructor = pluginActivityClass.getConstructor(new Class[]{});
            //实例化插件包中的Activity
            Object pluginActivity = constructor.newInstance(new Object[]{});
            ActivityInterface activityInterface = (ActivityInterface) pluginActivity;
            //注入上下文
            activityInterface.insertAppContext(this);
            Bundle bundle = new Bundle();
            bundle.putString("appName","宿主app携带的参数");
            activityInterface.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent proxyIntent = new Intent(this, ProxyActivity.class);
        proxyIntent.putExtra("className",className);//插件中的Activity的全类名
        super.startActivity(proxyIntent);
    }

    @Override
    public ComponentName startService(Intent service) {
        String className = service.getStringExtra("className");
        Intent intent = new Intent(this, ProxyService.class);
        intent.putExtra("className",className);
        return super.startService(intent);
    }
}
