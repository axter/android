package com.imohoo.libs.camera;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.imohoo.libs.R;
import com.imohoo.libs.camera.CameraContainer.TakePictureListener;
import com.imohoo.libs.utils.BitmapTool;

public class CameraActivity extends Activity implements View.OnClickListener, TakePictureListener {
	/** 相片体 */
	private CameraContainer container;
	private ImageButton btn_shutter_camera;
	private ImageView btn_switch_camera;
	private ImageView btn_cancel;
	private ImageView iv_show;

	/** 是否已经拍照了 */
	private boolean isShoted = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认无标题,无边框
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.layout_item_camera);

		container = (CameraContainer) findViewById(R.id.container);
		btn_switch_camera = (ImageView) findViewById(R.id.btn_switch_camera);
		btn_shutter_camera = (ImageButton) findViewById(R.id.btn_shutter_camera);
		btn_cancel = (ImageView) findViewById(R.id.btn_cancel);
		iv_show = (ImageView) findViewById(R.id.iv_show);

		btn_shutter_camera.setOnClickListener(this);
		btn_switch_camera.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(MediaStore.EXTRA_OUTPUT)) {
			Uri uri = (Uri) getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);
			container.setRootPath(uri.getPath());
		} else {
			// 本业务文件主目录
			StringBuilder pathBuilder = new StringBuilder();
			// 添加应用存储路径
			pathBuilder.append(getFilesDir().getAbsolutePath());
			pathBuilder.append(File.separator);
			// 添加文件总目录
			pathBuilder.append("camera");
			pathBuilder.append(File.separator);
			pathBuilder.append(System.currentTimeMillis());
			pathBuilder.append(".png");
			container.setRootPath(pathBuilder.toString());
		}

		// 判断是否可以切换前置后置摄像头
		btn_switch_camera.setVisibility(View.GONE);
		int numCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numCameras; i++) {
			Camera.CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (Camera.CameraInfo.CAMERA_FACING_FRONT == info.facing) {
				btn_switch_camera.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btn_shutter_camera) {
			if (!isShoted) {
				btn_shutter_camera.setClickable(false);
				container.takePicture(this);
			} else {
				setResult(Activity.RESULT_OK);
				finish();
			}
		} else if (id == R.id.btn_switch_camera) {
			if (!isShoted) {
				container.switchCamera();
			}
		} else if (id == R.id.btn_cancel) {
			if (isShoted) {
				iv_show.setImageBitmap(null);
				iv_show.setVisibility(View.GONE);
				btn_shutter_camera.setBackgroundResource(R.drawable.camera_fun_takephoto);
				isShoted = false;
				return;
			}
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isShoted) {
				iv_show.setImageBitmap(null);
				iv_show.setVisibility(View.GONE);
				btn_shutter_camera.setBackgroundResource(R.drawable.camera_fun_takephoto);
				isShoted = false;
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		container.close();
	}

	@Override
	public void onTakePictureEnd(File file, Camera camera) {
		isShoted = true;

		btn_shutter_camera.setClickable(true);
		btn_shutter_camera.setBackgroundResource(R.drawable.camera_fun_ok);

		iv_show.setVisibility(View.VISIBLE);

		Bitmap bitmap = BitmapTool.decodeBitmap(0, file.getAbsolutePath());
		iv_show.setImageBitmap(bitmap);
	}

	@Override
	public void onAnimtionEnd(Bitmap bm, boolean isVideo) {
		// TODO Auto-generated method stub

	}

}