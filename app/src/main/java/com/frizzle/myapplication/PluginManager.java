package com.frizzle.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * author: LWJ
 * date: 2020/9/7$
 * description
 */
public class PluginManager {
    private static PluginManager pluginManager;
    private Context mContext;
    private DexClassLoader dexClassLoader;
    private Resources resources;

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }

    private PluginManager(Context context) {
        mContext = context;
    }

    public static PluginManager getInstance(Context context) {
        if (pluginManager == null) {
            synchronized (PluginManager.class) {
                if (pluginManager == null) {
                    pluginManager = new PluginManager(context);
                }
            }
        }
        return pluginManager;
    }

    //加载插件 主要是加载Activity 和 layout资源
    public void loadPlugin() {
        try {
            //加载class
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
            if (!file.exists()) {
                Log.e("Frizzle", "插件包不存在");
                return;
            }
            //dex路径
            String pluginPath = file.getAbsolutePath();
            //DexClassLoader需要一个缓存路径
            File fileDir = mContext.getDir("pluginDir", Context.MODE_PRIVATE);
            //加载
            dexClassLoader = new DexClassLoader(pluginPath, fileDir.getAbsolutePath(), null, mContext.getClassLoader());

            //加载插件里的layout 通过AssetManger
            //这种方式可以拿到
//            AssetManager assets = mContext.getAssets();
            //反射方式拿到AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginPath);
            Resources r = mContext.getResources();
            //后面的参数是资源的配置信息
            //用该Resources对象加载资源文件中的Resources,这里的用法类似于动态换肤中的使用
            resources = new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration());

        } catch (Exception e) {

        }
    }

    //加载并注册插件包中的静态广播
    //使用反射的方式调用系统api解析插件apk所有信息
    public void paserApkAction() {
        //插件包路径
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
        try {
            // 1.执行此方法 public Package parsePackage(File packageFile, int flags)，就是为了，拿到Package Package中包含apk所有组件信息
            Class mPackagePaserClass = Class.forName("android.content.pm.PackageParser");
            //实例化PackagePaser对象
            Object mPackagePaser = mPackagePaserClass.newInstance();
            Method paserPackageMethod = mPackagePaserClass.getMethod("parsePackage", File.class, int.class);
            //内部类,用Object接收
            Object mPackage = paserPackageMethod.invoke(mPackagePaser, file, PackageManager.GET_ACTIVITIES);
            //获取广播接收者的集合
            Field receiversField = mPackage.getClass().getDeclaredField("receivers");
            Object receivers = receiversField.get(mPackage);
            // 此Activity 不是组件的Activity，是PackageParser里面的内部类
            //强转ArrayList<Activity>
            ArrayList arrayList = (ArrayList) receivers;

            for (Object mActivity : arrayList) {
                // 获取 <intent-filter>    intents== 可能有很多 Intent-Filter
                // 通过反射拿到 intents
                Class mComponentClass = Class.forName("android.content.pm.PackageParser$Component");//内部类加$
                Field intentsField = mComponentClass.getDeclaredField("intents");
                //intents所属PackagePaser$Component 但是 内部类Activity(不是四大组件中的Activity,同名而已)是Component的子类
                ArrayList<IntentFilter> intents = (ArrayList) intentsField.get(mActivity);
                //需要拿到Receiver的全类名
                // activityInfo.name; == android:name=".StaticPluginReceiver"
                Class mPackageUserState = Class.forName("android.content.pm.PackageUserState");

                Class mUserHandle = Class.forName("android.os.UserHandle");
                int userId = (int) mUserHandle.getMethod("getCallingUserId").invoke(null);
                /**
                 * 执行此方法，获取 ActivityInfo
                 * public static final ActivityInfo generateActivityInfo(Activity a, int flags,
                 *             PackageUserState state, int userId)
                 */
                Method generateActivityInfo = mPackagePaserClass.getMethod("generateActivityInfo", mActivity.getClass(), int.class, mPackageUserState, int.class);
                //静态方法执行时候不需要传对象
                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfo.invoke(null,mActivity, 0, mPackageUserState.newInstance(), userId);
                String receiverClassName = activityInfo.name;
                Class mStaticReceiverClass = getDexClassLoader().loadClass(receiverClassName);

                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) mStaticReceiverClass.newInstance();
                for (IntentFilter intentFilter : intents) {
                    //注册广播
                    mContext.registerReceiver(broadcastReceiver, intentFilter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
