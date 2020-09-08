package com.frizzle.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btnLoadPlugin;
    private Button btnLaunchPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPerms();

    }

    private void requestPerms() {
        //权限,简单处理下
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
            String[] perms= {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms,200);
            }else {
                initView();
            }
        } else {
            initView();
        }
    }


    private void initView() {
        btnLoadPlugin = (Button) findViewById(R.id.btn_load_plugin);
        btnLaunchPlugin = (Button) findViewById(R.id.btn_launch_plugin);
        //加载插件
        btnLoadPlugin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginManager.getInstance(MainActivity.this).loadPlugin();
            }
        });
        //启动插件中的Activity,启动代理ProxyActivity,有ProxyActivity做代理实现加载插件中的Activity
        btnLaunchPlugin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去插件包中的activitys
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
                String path = file.getAbsolutePath();
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                ActivityInfo activityInfo = packageInfo.activities[0];
                Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
                intent.putExtra("className",activityInfo.name);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_load_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginManager.getInstance(MainActivity.this).paserApkAction();
            }
        });
        findViewById(R.id.btn_send_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("plugin.static_receiver");
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}