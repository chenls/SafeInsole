package com.cqupt.SafeInsole;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import java.util.Calendar;

public class SetActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private String sex = "100";
	private Button birthday_button;
	private Button set_button;
	private EditText Height;
	private EditText Weight;
	private SharedPreferencesUtil util;
	private Dialog mdialog;
	private Calendar calendar = null;
	private DatePicker dp;
	private RadioGroup sexGroupId;
	private RadioButton femaleButton;
	private RadioButton maleButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);
		initUi();
		birthday_button.setOnClickListener(this);
	}

	public void initUi() {
		util = new SharedPreferencesUtil(this);
		set_button = (Button) findViewById(R.id.set_button);
		birthday_button = (Button) findViewById(R.id.birthday_button);
		Height = (EditText) findViewById(R.id.height_edit);
		Weight = (EditText) findViewById(R.id.weight_edit);
		sexGroupId = (RadioGroup) findViewById(R.id.sexGroupId);
		femaleButton = (RadioButton) findViewById(R.id.femaleButtonId);
		maleButton = (RadioButton) findViewById(R.id.maleButtonId);
		RadioGroupListener listener = new RadioGroupListener();
		sexGroupId.setOnCheckedChangeListener(listener);
		setListener();
		String value = util.read("mykey");
		if (value != null) {
			String[] stringArr = value.split(",");
			int w0 = Integer.parseInt(stringArr[0], 10);
			int w1 = Integer.parseInt(stringArr[1], 10);
			int w2 = Integer.parseInt(stringArr[2], 10);
			if (w0 == 85)
				femaleButton.setChecked(true);
			if (w0 == 100)
				maleButton.setChecked(true);
			Height.setText(w1 + "");
			Weight.setText(w2 + "");
		}
	}

	class RadioGroupListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == femaleButton.getId()) {
				sex = "85";
			} else if (checkedId == maleButton.getId()) {
				sex = "100";
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showDialog(0);// 日期弹出框
		int SDKVersion = SetActivity.this.getSDKVersionNumber();// 获取系统版本
		System.out.println("SDKVersion = " + SDKVersion);
		dp = findDatePicker((ViewGroup) mdialog.getWindow().getDecorView());// 设置弹出年月日
		// dp.updateDate(2015, 0, 1);
		if (dp != null) {

			if (SDKVersion < 11) {
				((ViewGroup) dp.getChildAt(0)).getChildAt(0).setVisibility(
						View.GONE);
			}

			else if (SDKVersion > 14) {
				((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(1))
						.getChildAt(0).setVisibility(View.GONE);
			}
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) { // 对应上面的showDialog(0);//日期弹出框
		mdialog = null;
		switch (id) {
			case 0:
				calendar = Calendar.getInstance();
				mdialog = new DatePickerDialog(this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
												  int monthOfYear, int dayOfMonth) {
								birthday_button.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
							}
						}, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
				break;
		}
		return mdialog;
	}

	/**
	 * 从当前Dialog中查找DatePicker子控件
	 *
	 * @param group
	 * @return
	 */
	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	/**
	 * 获取系统SDK版本
	 *
	 * @return
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	public void setListener() {

		set_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String estring = "请输入内容！";
				ForegroundColorSpan fg = new ForegroundColorSpan(Color.BLACK);
				SpannableStringBuilder ss = new SpannableStringBuilder(estring);
				ss.setSpan(fg, 0, estring.length(), 0);

				if(Height.getText().toString().equals("")){
					Height.setError(ss);
					Toast.makeText(SetActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
					return;
				}
				if(Weight.getText().toString().equals("")){
					Weight.setError(ss);
					Toast.makeText(SetActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
					return;
				}
				boolean b = util.save("mykey", sex + ","
						+ Height.getText().toString() + ","
						+ Weight.getText().toString() + ","
						+ birthday_button.getText().toString());
				String msg;
				if (b) {
					msg = "保存成功";
				} else {
					msg = "保存失败";
				}
				Toast.makeText(SetActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	// 退出提示
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			AlertDialog alertDialog = new AlertDialog.Builder(SetActivity.this)
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