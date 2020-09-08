package com.frizzle.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

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
    public void loadPlugin(){
        try {
//            //加载class
//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
//            if (!file.exists()){
//                Log.e("Frizzle","插件包不存在");
//                return;
//            }
//            //dex路径
//            String pluginPath = file.getAbsolutePath();
//            //DexClassLoader需要一个缓存路径
//            File fileDir = mContext.getDir("pluginDie", Context.MODE_PRIVATE);
//            //加载
//            dexClassLoader = new DexClassLoader(pluginPath,fileDir.getAbsolutePath(),null,mContext.getClassLoader());
//
//            //加载插件里的layout 通过AssetManger
//            //这种方式可以拿到
////            AssetManager assets = mContext.getAssets();
//            //反射方式拿到AssetManager
//            AssetManager assetManager = AssetManager.class.newInstance();
//            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath",String.class);
//            addAssetPathMethod.invoke(assetManager,pluginPath);
//            Resources r = mContext.getResources();
//            //后面的参数是资源的配置信息
//            //用该Resources对象加载资源文件中的Resources,这里的用法类似于动态换肤中的使用
//            resources = new Resources(assetManager,r.getDisplayMetrics(),r.getConfiguration());
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
            if (!file.exists()) {
                Log.e("Frizzle","插件包不存在");
                return;
            }
            String pluginPaht = file.getAbsolutePath();

            /**
             *   下面是加载插件里面的 class
             */

            // dexClassLoader需要一个缓存目录   /data/data/当前应用的包名/pDir
            File fileDir = mContext.getDir("pluginDir", Context.MODE_PRIVATE);

            // Activity class
            dexClassLoader = new DexClassLoader(pluginPaht, fileDir.getAbsolutePath(), null, mContext.getClassLoader());


            /**
             *   下面是加载插件里面的layout
             */
            // 加载资源
            AssetManager assetManager = AssetManager.class.newInstance();

            // 我们要执行此方法，为了把插件包的路径 添加进去
            // public final int addAssetPath(String path)
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class); // 他是类类型 Class
            addAssetPathMethod.invoke(assetManager, pluginPaht); // 插件包的路径   pluginPaht

            Resources r = mContext.getResources(); // 宿主的资源配置信息

            // 特殊的 Resources，加载插件里面的资源的 Resources
            resources = new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration()); // 参数2 3  资源配置信息

        }catch (Exception e){

        }
    }
}
