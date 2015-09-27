package com.cqupt.SafeInsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        try {
            sharedPreferences = this.getSharedPreferences("mydata", Context.MODE_PRIVATE);
        } catch (Exception e) {
        }

        setContentView(R.layout.welcome);
        new Handler() {
            public void handleMessage(Message msg) {
                //是否进入滑动欢迎页
                if (!sharedPreferences.getBoolean("isFirst", false)) {
                    Intent intent = new Intent(WelcomeActivity.this, PageViewActivity.class);
                    WelcomeActivity.this.startActivity(intent);
                    WelcomeActivity.this.finish();
                    return;
                }
                //进入主页
                startActivity(new Intent(WelcomeActivity.this,
                        Palygif1Activity.class));
                finish();
            }

        }.sendEmptyMessageDelayed(0, 1500);

    }

    // 退出提示
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    WelcomeActivity.this)
                    .setTitle("退出程序")
                    .setMessage("是否退出程序")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    android.os.Process
                                            .killProcess(android.os.Process
                                                    .myPid());
                                }

                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    return;
                                }
                            }).create(); // 创建对话框

            alertDialog.show(); // 显示对话框

            return false;
        }

        return false;
    }
}
