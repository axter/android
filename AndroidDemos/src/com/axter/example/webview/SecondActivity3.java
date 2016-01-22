package com.axter.example.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.axter.example.R;

/**
 * Created by moyun on 2016/1/21.
 */
public class SecondActivity3 extends Activity {

	private WebView mWebView;
	private String loadUrl = "http://192.168.0.118/index.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview2);

		initViews();
	}

	private void initViews() {
		mWebView = (WebView) findViewById(R.id.webview);

		mWebView.setWebViewClient(new MyWebClient());
		WebSettings settings = mWebView.getSettings();

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

		mWebView.setWebChromeClient(client);
		mWebView.loadUrl(loadUrl);
	}

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

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
		// ...
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		mWebView.onPause();
		// ...
		super.onPause();
	}

	/** File upload callback for platform versions prior to Android 5.0 */
	protected ValueCallback<Uri> mFileUploadCallbackFirst;
	/** File upload callback for Android 5.0+ */
	protected ValueCallback<Uri[]> mFileUploadCallbackSecond;
	protected static final int REQUEST_CODE_FILE_PICKER = 51426;
	protected int mRequestCodeFilePicker = REQUEST_CODE_FILE_PICKER;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;

		} else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
			if (null == mUploadMessageForAndroid5)
				return;
			Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
			if (result != null) {
				mUploadMessageForAndroid5.onReceiveValue(new Uri[] { result });
			} else {
				mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
			}
			mUploadMessageForAndroid5 = null;
		}
	}

	WebChromeClient client = new WebChromeClient() {
		// 扩展浏览器上传文件
		// 3.0++版本
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			openFileChooserImpl(uploadMsg);
		}

		// 3.0--版本
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooserImpl(uploadMsg);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileChooserImpl(uploadMsg);
		}

		// For Android > 5.0
		public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
			openFileChooserImplForAndroid5(uploadMsg);
			return true;
		}
	};

	public ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> mUploadMessageForAndroid5;
	public final static int FILECHOOSER_RESULTCODE = 1;
	public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

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
}
