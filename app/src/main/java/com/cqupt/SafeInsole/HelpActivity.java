package com.cqupt.SafeInsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HelpActivity extends Activity {
	private ImageButton btimage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		btimage = (ImageButton) findViewById(R.id.bt_image);
		btimage.setOnClickListener(new OnClickListenerImpl());
	}

	// 按钮点击事件
	private class OnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(HelpActivity.this, SuccinctHelpActitvty.class));

		}
	}

	// 退出提示
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			AlertDialog alertDialog = new AlertDialog.Builder(HelpActivity.this)
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
