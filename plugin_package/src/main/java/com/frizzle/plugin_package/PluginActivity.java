package com.frizzle.plugin_package;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PluginActivity extends BaseActivity {

    private Button btnJump;
    private Button btnLaunchService;
    private Button btnRegisterReceiver;
    private final String ACTION = "com.frizzle.plugin_package.ACTION";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity_main);
        String appName = savedInstanceState.getString("appName");
        Log.e("Frizzle",appName);
        //插件包中的上下文必须使用宿主的上下文,因为插件包没有安装 所以是没有上下文环境的
        Toast.makeText(appActivity,"这是插件Activity",Toast.LENGTH_SHORT).show();
        btnJump = (Button) findViewById(R.id.jump);
        btnLaunchService = (Button) findViewById(R.id.btn_launch_service);
        btnRegisterReceiver = (Button) findViewById(R.id.btn_register_receiver);
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动插件中的Activity
                startActivity(new Intent(appActivity,SecondActivity.class));
            }
        });
        btnLaunchService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动插件中的Service
                startService(new Intent(appActivity,PluginService.class));
            }
        });
        btnRegisterReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //插件中注册动态广播
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION);
                registerReceiver(new PluginReceiver(),intentFilter);
            }
        });

        findViewById(R.id.btn_send_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送广播
                Intent intent = new Intent();
                intent.setAction(ACTION);
                sendBroadcast(intent);
            }
        });
    }
}
