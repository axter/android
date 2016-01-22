package com.axter.example.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axter.example.R;

/**
 * 网页浏览
 */
public class MyWebViewActivity extends Activity implements OnClickListener {
	// Web view
	private WebSettings settings;
	private WebView webView;

	// 网址
	private String url = "http://192.168.0.118/index.php";
	private String title = "";//标题头
	private String share_url;//分享地址
	private boolean is_share = true;//是否分享
	private int share_type=0;//分享类型
	private String share_content;//分享内容
	private String share_title;//分享标题头
	private String share_img_url;//图片地址

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview2);
		initApp();
		initView();
	}

//	@Override
//	protected void onPause() {
//		webView.reload();
//
//		super.onPause();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		webView.removeAllViews();
//		webView.clearCache(true);
//		webView.destroy();
//	}

	/**
	 * 初始化参数
	 */
	private void initApp() {
		Bundle bd = getIntent().getExtras();
		if (bd == null)
			return;
		if (bd.containsKey("url")) {
			url = bd.getString("url");
			if (TextUtils.isEmpty(url))
				Log.i("tag", url);
		}
		if (bd.containsKey("is_share")) {
			is_share = bd.getBoolean("is_share");
		}
		if (bd.containsKey("share_title")) {
			share_title = bd.getString("share_title");
		}
		if (bd.containsKey("share_type")) {
			share_type = bd.getInt("share_type");
		}
		if (bd.containsKey("share_url")) {
			share_url = bd.getString("share_url");
		}
		if (TextUtils.isEmpty(share_url)) {
			share_url = url;
		}
		//咨询分享所需，请勿删除
		if (bd.containsKey("share_content")) {
			share_content = bd.getString("share_content");
		}
		if (bd.containsKey("share_imgurl")) {
			share_img_url = bd.getString("share_imgurl");
		}

		// 检查url
		if (!TextUtils.isEmpty(url)) {
			if (!url.startsWith("http://") && !url.startsWith("https://")) {
				url = "http://" + url;
			}
		}
	}

	protected void initView() {
		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new MyWebClient());

		settings = webView.getSettings();

		settings.setAllowFileAccess(true);
		settings.setPluginState(PluginState.ON);
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(false);
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setTextSize(WebSettings.TextSize.NORMAL);

		webView.setWebChromeClient(chrome);
		/**
		 善跑app内所有打开的url链接，添加参数 myrunnersfid=xxxxxx
		 其中xxxx = userid ^ 20150331     575 ^ 20150331 = 20150788
		 服务器需要统计时可以获取到uid = 20150788 ^ 20150331 = 575
		 20150331 为myrunners.com 网站开通时间
		 */
		webView.loadUrl(url);
	}

	WebChromeClient chrome = new WebChromeClient() {
		public void onReceivedTitle(WebView view, String title) {
			MyWebViewActivity.this.title = title;
			if (TextUtils.isEmpty(share_content)){
				share_content=title;
			}
		}

		//扩展浏览器上传文件
		//3.0++版本
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			openFileChooserImpl(uploadMsg);
		}

		//3.0--版本
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooserImpl(uploadMsg);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			//uploadMsg.onReceiveValue(Uri.fromFile(new File("/storage/emulated/0/what.txt")));
			openFileChooserImpl(uploadMsg);
		}

		// For Android > 5.0
		public boolean onShowFileChooser (WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
			openFileChooserImplForAndroid5(uploadMsg);
			return true;
		}
	};

	class MyWebClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
			} else if (url.startsWith("http:") || url.startsWith("https:")) {
				view.loadUrl(url);
			}
			return true;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");
		startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
	}

	private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
		mUploadMessageForAndroid5 = uploadMsg;
		Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
		contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
		contentSelectionIntent.setType("*/*");

		Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
		chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
		chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

		startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
	}

	public boolean haveFile = false;
	public Uri uri;
	public ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> mUploadMessageForAndroid5;
	public final static int FILECHOOSER_RESULTCODE = 1;
	public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;

		} else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5){
			if (null == mUploadMessageForAndroid5)
				return;
			Uri result = (intent == null || resultCode != RESULT_OK) ? null: intent.getData();
			if (result != null) {
				mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
			} else {
				mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
			}
			mUploadMessageForAndroid5 = null;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
