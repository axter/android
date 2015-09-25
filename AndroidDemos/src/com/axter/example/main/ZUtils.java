package com.axter.example.main;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ZUtils {
	public static void init1(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static void init2(Activity activity) {
		Point point = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(point);
	}

	/**
	 * 判断储存设备是否挂载
	 * 
	 * @return
	 */
	public static boolean isMount() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public void aa(Context context){
		// sd卡目录
		Environment.getExternalStorageDirectory();
		
		context.getExternalFilesDir("");
		
		
	}
	
	/**
	 * 安装apk
	 * 
	 * @param context
	 * @param apkfile
	 */
	public static void installApk(Context context, File apkfile) {

		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(i);
		// android.os.Process.killProcess(android.os.Process.myPid());
	}
}
