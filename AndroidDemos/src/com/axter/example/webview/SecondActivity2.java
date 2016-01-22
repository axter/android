package com.axter.example.webview;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebStorage.QuotaUpdater;

import com.axter.example.R;

/**
 * Created by moyun on 2016/1/21.
 */
public class SecondActivity2 extends Activity {

	private WebView mWebView;
	private String loadUrl = "http://192.168.0.118/index.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview2);

		initViews();
	}

	private void initViews() {
		mLanguageIso3 = getLanguageIso3();
		
		mWebView = (WebView) findViewById(R.id.webview);

		mWebView.setSaveEnabled(true);

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setAllowFileAccess(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		mWebView.setWebChromeClient(client);
		mWebView.loadUrl(loadUrl);
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
		if (requestCode == mRequestCodeFilePicker) {
			if (resultCode == Activity.RESULT_OK) {
				if (intent != null) {
					if (mFileUploadCallbackFirst != null) {
						mFileUploadCallbackFirst.onReceiveValue(intent.getData());
						mFileUploadCallbackFirst = null;
					} else if (mFileUploadCallbackSecond != null) {
						Uri[] dataUris;
						try {
							dataUris = new Uri[] { Uri.parse(intent.getDataString()) };
						} catch (Exception e) {
							dataUris = null;
						}

						mFileUploadCallbackSecond.onReceiveValue(dataUris);
						mFileUploadCallbackSecond = null;
					}
				}
			} else {
				if (mFileUploadCallbackFirst != null) {
					mFileUploadCallbackFirst.onReceiveValue(null);
					mFileUploadCallbackFirst = null;
				} else if (mFileUploadCallbackSecond != null) {
					mFileUploadCallbackSecond.onReceiveValue(null);
					mFileUploadCallbackSecond = null;
				}
			}
		}
	}

	private WebChromeClient client = new WebChromeClient() {

		// file upload callback (Android 2.2 (API level 8) -- Android 2.3 (API level 10)) (hidden method)
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooser(uploadMsg, null);
		}

		// file upload callback (Android 3.0 (API level 11) -- Android 4.0 (API level 15)) (hidden method)
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			openFileChooser(uploadMsg, acceptType, null);
		}

		// file upload callback (Android 4.1 (API level 16) -- Android 4.3 (API level 18)) (hidden method)
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileInput(uploadMsg, null);
		}

		// file upload callback (Android 5.0 (API level 21) -- current) (public method)
		@SuppressWarnings("all")
		public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
			openFileInput(null, filePathCallback);
			return true;
		}
	};

	protected String mUploadableFileTypes = "*/*";
	@SuppressLint("NewApi")
	protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond) {
		if (mFileUploadCallbackFirst != null) {
			mFileUploadCallbackFirst.onReceiveValue(null);
		}
		mFileUploadCallbackFirst = fileUploadCallbackFirst;

		if (mFileUploadCallbackSecond != null) {
			mFileUploadCallbackSecond.onReceiveValue(null);
		}
		mFileUploadCallbackSecond = fileUploadCallbackSecond;

		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType(mUploadableFileTypes);

		startActivityForResult(Intent.createChooser(i, getFileUploadPromptLabel()), mRequestCodeFilePicker);
	}
	
	
	protected static final String LANGUAGE_DEFAULT_ISO3 = "eng";
	protected String mLanguageIso3;
	protected static String getLanguageIso3() {
		try {
			return Locale.getDefault().getISO3Language().toLowerCase(Locale.US);
		}
		catch (MissingResourceException e) {
			return LANGUAGE_DEFAULT_ISO3;
		}
	}
	/** Provides localizations for the 25 most widely spoken languages that have a ISO 639-2/T code */
	protected String getFileUploadPromptLabel() {
		try {
			if (mLanguageIso3.equals("zho")) return decodeBase64("6YCJ5oup5LiA5Liq5paH5Lu2");
			else if (mLanguageIso3.equals("spa")) return decodeBase64("RWxpamEgdW4gYXJjaGl2bw==");
			else if (mLanguageIso3.equals("hin")) return decodeBase64("4KSP4KSVIOCkq+CkvOCkvuCkh+CksiDgpJrgpYHgpKjgpYfgpII=");
			else if (mLanguageIso3.equals("ben")) return decodeBase64("4KaP4KaV4Kaf4Ka/IOCmq+CmvuCmh+CmsiDgpqjgpr/gprDgp43gpqzgpr7gpprgpqg=");
			else if (mLanguageIso3.equals("ara")) return decodeBase64("2KfYrtiq2YrYp9ixINmF2YTZgSDZiNin2K3Yrw==");
			else if (mLanguageIso3.equals("por")) return decodeBase64("RXNjb2xoYSB1bSBhcnF1aXZv");
			else if (mLanguageIso3.equals("rus")) return decodeBase64("0JLRi9Cx0LXRgNC40YLQtSDQvtC00LjQvSDRhNCw0LnQuw==");
			else if (mLanguageIso3.equals("jpn")) return decodeBase64("MeODleOCoeOCpOODq+OCkumBuOaKnuOBl+OBpuOBj+OBoOOBleOBhA==");
			else if (mLanguageIso3.equals("pan")) return decodeBase64("4KiH4Kmx4KiVIOCoq+CovuCoh+CosiDgqJrgqYHgqKPgqYs=");
			else if (mLanguageIso3.equals("deu")) return decodeBase64("V8OkaGxlIGVpbmUgRGF0ZWk=");
			else if (mLanguageIso3.equals("jav")) return decodeBase64("UGlsaWggc2lqaSBiZXJrYXM=");
			else if (mLanguageIso3.equals("msa")) return decodeBase64("UGlsaWggc2F0dSBmYWls");
			else if (mLanguageIso3.equals("tel")) return decodeBase64("4LCS4LCVIOCwq+CxhuCxluCwsuCxjeCwqOCxgSDgsI7gsILgsJrgsYHgsJXgsYvgsILgsKHgsL8=");
			else if (mLanguageIso3.equals("vie")) return decodeBase64("Q2jhu41uIG3hu5l0IHThuq1wIHRpbg==");
			else if (mLanguageIso3.equals("kor")) return decodeBase64("7ZWY64KY7J2YIO2MjOydvOydhCDshKDtg50=");
			else if (mLanguageIso3.equals("fra")) return decodeBase64("Q2hvaXNpc3NleiB1biBmaWNoaWVy");
			else if (mLanguageIso3.equals("mar")) return decodeBase64("4KSr4KS+4KSH4KSyIOCkqOCkv+CkteCkoeCkvg==");
			else if (mLanguageIso3.equals("tam")) return decodeBase64("4K6S4K6w4K+BIOCuleCvh+CuvuCuquCvjeCuquCviCDgrqTgr4fgrrDgr43grrXgr4E=");
			else if (mLanguageIso3.equals("urd")) return decodeBase64("2KfbjNqpINmB2KfYptmEINmF24zauiDYs9uSINin2YbYqtiu2KfYqCDaqdix24zaug==");
			else if (mLanguageIso3.equals("fas")) return decodeBase64("2LHYpyDYp9mG2KrYrtin2Kgg2qnZhtuM2K8g24zaqSDZgdin24zZhA==");
			else if (mLanguageIso3.equals("tur")) return decodeBase64("QmlyIGRvc3lhIHNlw6dpbg==");
			else if (mLanguageIso3.equals("ita")) return decodeBase64("U2NlZ2xpIHVuIGZpbGU=");
			else if (mLanguageIso3.equals("tha")) return decodeBase64("4LmA4Lil4Li34Lit4LiB4LmE4Lif4Lil4LmM4Lir4LiZ4Li24LmI4LiH");
			else if (mLanguageIso3.equals("guj")) return decodeBase64("4KqP4KqVIOCqq+CqvuCqh+CqsuCqqOCrhyDgqqrgqrjgqoLgqqY=");
		}
		catch (Exception e) { }

		// return English translation by default
		return "Choose a file";
	}
	protected static final String CHARSET_DEFAULT = "UTF-8";
	protected static String decodeBase64(final String base64) throws IllegalArgumentException, UnsupportedEncodingException {
		final byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
		return new String(bytes, CHARSET_DEFAULT);
	}
}
