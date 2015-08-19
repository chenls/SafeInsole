package com.cqupt.SafeInsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.welcome);
		new Handler() {
			public void handleMessage(Message msg) {
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
