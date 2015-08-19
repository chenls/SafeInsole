package com.cqupt.SafeInsole;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class SuccinctHelpActitvty extends Activity {

	private WebView webview;
	private ImageButton bt_image;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_succinchelp);
		webview = (WebView) findViewById(R.id.webview);
		bt_image = (ImageButton) findViewById(R.id.image);
		bt_image.setOnClickListener(new OnClickListenerImpl());
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		// webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		// webSettings.setBuiltInZoomControls(true);
		webview.loadUrl("http://www.zhichiwangluo.com/zhichi/index.php?r=Invitation/showNewInvitation&id=23964");
		// 设置Web视图
		webview.setWebViewClient(new webViewClient());

	}

	// 按钮点击事件
	private class OnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			finish();
		}
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
