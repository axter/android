package com.imohoo.libs.camera;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.imohoo.libs.R;
import com.imohoo.libs.camera.CameraContainer.TakePictureListener;
import com.imohoo.libs.camera.CameraView.MyOrientationEventListener;
import com.imohoo.libs.rotate.RotateImageView;
import com.imohoo.libs.utils.BitmapTool;

public class CameraActivity extends Activity implements View.OnClickListener, TakePictureListener, MyOrientationEventListener {
	/** 相片体 */
	private CameraContainer container;
	private RotateImageView btn_shutter_camera;
	private RotateImageView btn_switch_camera;
	private RotateImageView btn_cancel;
	private RotateImageView iv_show;

	/** 是否已经拍照了 */
	private boolean isShoted = false;
	/** 最大屏幕边 */
	private int max;
	private Bitmap bitmapPreview;
	private RotateAnimation operatingAnim;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认无标题,无边框
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.camera_activity);

		int w = getResources().getDisplayMetrics().widthPixels;
		int h = getResources().getDisplayMetrics().heightPixels;
		max = w > h ? w : h;

		container = (CameraContainer) findViewById(R.id.container);
		container.setOrientationChangeListener(this);
		btn_switch_camera = (RotateImageView) findViewById(R.id.btn_switch_camera);
		btn_shutter_camera = (RotateImageView) findViewById(R.id.btn_shutter_camera);
		btn_cancel = (RotateImageView) findViewById(R.id.btn_cancel);
		iv_show = (RotateImageView) findViewById(R.id.iv_show);

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
			pathBuilder.append(".jpg");
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
			// 预览状态不允许切换摄像头
			if (!isShoted) {
				container.switchCamera();
			}
		} else if (id == R.id.btn_cancel) {
			if(isPreview(false)){
				return;
			}
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(isPreview(false)){
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		recycle(bitmapPreview);
	}

	/**
	 * 
	 * @param flag true 进入预览,false 退出预览
	 * @return 是否退出预览成功
	 */
	private boolean isPreview(boolean flag){
		if(flag){
			// 预览图状态
			isShoted = true;
			container.stopPreview();
			btn_shutter_camera.setClickable(true);
			setRid(btn_shutter_camera, R.drawable.camera_fun_ok);
			btn_switch_camera.setVisibility(View.GONE);
		}else{
			if (isShoted) {
				isShoted = false;
				container.startPreview();
				iv_show.setVisibility(View.GONE);
				setRid(btn_shutter_camera, R.drawable.camera_fun_takephoto);
				btn_switch_camera.setVisibility(View.VISIBLE);
				return true;
			}
		}
		return false;
	}
	/**照片旋转角度*/
	private int savedRotate = 0;

	@Override
	public void onTakePictureEnd(File file, Camera camera) {
		isPreview(true);
		
		bitmapPreview = BitmapTool.decodeBitmap(max, file.getAbsolutePath());
		
		savedRotate = BitmapTool.readPictureDegree(file.getAbsolutePath());
		iv_show.setImageBitmap(bitmapPreview);
		iv_show.setOrientation(mRotation + (360 - savedRotate) % 360, false);
		iv_show.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		container.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		container.onPause();
	}

	@Override
	public void onAnimtionEnd(Bitmap bm, boolean isVideo) {
		// TODO Auto-generated method stub

	}

	private void recycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	private int mRotation = 0;

	@Override
	public void orientationChangeListener(int rotation) {
		if (iv_show.getVisibility() == View.VISIBLE) {
			iv_show.setOrientation(rotation + (360 - savedRotate) % 360, true);
		}
		btn_switch_camera.setOrientation(rotation, true);
		btn_shutter_camera.setOrientation(rotation, true);
		btn_cancel.setOrientation(rotation, true);
		mRotation = rotation;
	}

	private void setRid(ImageView iv, int rid) {
		Drawable d = getResources().getDrawable(rid);
		if (d != null) {
			iv.setImageDrawable(d);
		}
	}
}