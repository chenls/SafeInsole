package com.cqupt.SafeInsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class FallActivity extends Activity {
	private String sButton = "1";
	private RadioGroup sButtonGroup;
	private RadioButton openButton;
	private RadioButton closeButton;
	private EditText numberText;
	private EditText contentText;
	private SharedPreferencesUtil util;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_fall);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.titlefall);
		sButtonGroup = (RadioGroup) findViewById(R.id.sbuttongroup);
		openButton = (RadioButton) findViewById(R.id.openbutton);
		closeButton = (RadioButton) findViewById(R.id.closebutton);
		numberText = (EditText) this.findViewById(R.id.phone_number);
		contentText = (EditText) this.findViewById(R.id.sent_content);
		Button button = (Button) this.findViewById(R.id.sent_button);
		RadioGroupListener listener = new RadioGroupListener();
		sButtonGroup.setOnCheckedChangeListener(listener);
		button.setOnClickListener(new ButtonClickListener());
		util = new SharedPreferencesUtil(this);

		String mykeyvalue = util.read("fallkey");
		if (mykeyvalue != null) {
			String[] stringArr = mykeyvalue.split(",");
			if (stringArr[0].equals("1"))
				openButton.setChecked(true);
			if (stringArr[0].equals("0"))
				closeButton.setChecked(true);
			numberText.setText(stringArr[1]);
			contentText.setText(stringArr[2]);

		}

	}


	class RadioGroupListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == openButton.getId()) {
				sButton = "1";
			} else if (checkedId == closeButton.getId()) {
				sButton = "0";
			}
		}
	}

	private final class ButtonClickListener implements View.OnClickListener {

		public void onClick(View v) {

			String estring = "请输入内容！";
			ForegroundColorSpan fg = new ForegroundColorSpan(Color.BLACK);
			SpannableStringBuilder ss = new SpannableStringBuilder(estring);
			ss.setSpan(fg, 0, estring.length(), 0);

			if(numberText.getText().toString().equals("")){
				numberText.setError(ss);
				Toast.makeText(FallActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
				return;
			}
			if(contentText.getText().toString().equals("")){
				contentText.setError(ss);
				Toast.makeText(FallActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
				return;
			}
			String number = numberText.getText().toString();
			String content = contentText.getText().toString();

			boolean b = util.save("fallkey", sButton + "," + number + ","
					+ content);
			String msg;
			if (b) {
				msg = "保存成功";
			} else {
				msg = "保存失败";
			}
			Toast.makeText(FallActivity.this, msg, Toast.LENGTH_SHORT).show();

			/*
			 * SmsManager manager = SmsManager.getDefault(); ArrayList<String>
			 * texts = manager.divideMessage(content); for(String text : texts){
			 * manager.sendTextMessage(number, null, text, null, null); }
			 * Toast.makeText(FallActivity.this, R.string.success,
			 * Toast.LENGTH_LONG).show();
			 */
		}

	}

	// 退出提示
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			AlertDialog alertDialog = new AlertDialog.Builder(FallActivity.this)
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