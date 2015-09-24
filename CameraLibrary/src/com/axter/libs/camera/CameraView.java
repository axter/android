package com.axter.libs.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.axter.libs.camera.CameraContainer.TakePictureListener;

/**
 * @ClassName: Item_CameraView
 * @Description: 和相机绑定的SurfaceView 封装了拍照方法
 * @author LinJ
 * @date 2014-12-31 上午9:44:56
 * @modify
 */
@SuppressWarnings("deprecation")
public class CameraView extends SurfaceView implements CameraOperation {
	public final static String TAG = "CameraView";
	/** 和该View绑定的Camera对象 */
	private Camera mCamera;

	/** 当前闪光灯类型，默认为关闭 */
	private FlashMode mFlashMode = FlashMode.ON;

	/** 当前屏幕旋转角度 */
	private int mOrientation = 0;
	/** 是否打开前置相机,true为前置,false为后置 */
	private boolean mIsFrontCamera = false;

	public CameraView(Context context) {
		super(context);
		// 初始化容器
		getHolder().addCallback(callback);
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化容器
		getHolder().addCallback(callback);
	}

	/**有些机器待机SurfaceView会消亡*/
	private boolean isCreated = false;
	private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				isCreated = true;
				open();
			} catch (Exception e) {
				Toast.makeText(getContext(), "打开相机失败", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			//updateCameraOrientation();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			isCreated = false;
			// 停止录像
			close();
		}
	};

	/**
	 * 是否使用的前置摄像头
	 * 
	 * @return
	 */
	public boolean isUseFrontCamera() {
		return mIsFrontCamera;
	}

	/**
	 * 转换前置和后置照相机
	 */
	@Override
	public void switchCamera() {
		if (getWidth() <= 0 && !isCreated)
			return;
		mIsFrontCamera = !mIsFrontCamera;
		open();
	}

	/**
	 * 手动聚焦
	 * 
	 * @param point
	 *            触屏坐标
	 */
	@SuppressLint("NewApi")
	protected void onFocus(Point point, AutoFocusCallback callback) {
		if (mCamera != null) {
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				// 不支持设置自定义聚焦，则使用自动聚焦，返回
				if (parameters.getMaxNumFocusAreas() <= 0) {
					mCamera.autoFocus(callback);
					return;
				}
				List<Area> areas = new ArrayList<Camera.Area>();
				int left = point.x - 300;
				int top = point.y - 300;
				int right = point.x + 300;
				int bottom = point.y + 300;
				left = left < -1000 ? -1000 : left;
				top = top < -1000 ? -1000 : top;
				right = right > 1000 ? 1000 : right;
				bottom = bottom > 1000 ? 1000 : bottom;
				areas.add(new Area(new Rect(left, top, right, bottom), 100));
				parameters.setFocusAreas(areas);
				// 本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
				// 目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
				mCamera.setParameters(parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mCamera.autoFocus(callback);
		}
	}

	/**
	 * 根据当前照相机状态(前置或后置)，打开对应相机
	 */
	private boolean openCamera() {
		close();

		if (mIsFrontCamera) {
			Camera.CameraInfo cameraInfo = new CameraInfo();
			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				Camera.getCameraInfo(i, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					try {
						mCamera = Camera.open(i);
					} catch (Exception e) {
						mCamera = null;
						return false;
					}

				}
			}
		} else {
			try {
				mCamera = Camera.open();
			} catch (Exception e) {
				mCamera = null;
				return false;
			}

		}
		return true;
	}

	@Override
	public void takePicture(PictureCallback callback, TakePictureListener listener) {
		if (mCamera != null) {
			try {
				mCamera.takePicture(null, null, callback);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置照相机参数
	 */
	private void setCameraParameters() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int screenWidht = wm.getDefaultDisplay().getWidth();
		int screenHeight = wm.getDefaultDisplay().getHeight();
		int screenArea = screenWidht * screenHeight;

		Camera.Parameters parameters = mCamera.getParameters();
		Size cameraSize = null;
		// 选择合适的预览尺寸
		List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
		if (sizeList.size() > 0) {
			for (Size size : sizeList) {
				if (cameraSize == null || (Math.abs(size.width * size.height - screenArea) < Math.abs(cameraSize.width * cameraSize.height - screenArea))) {
					cameraSize = size;
				}
			}
			// cameraSize=sizeList.get(0);
			// 预览图片大小
			parameters.setPreviewSize(cameraSize.width, cameraSize.height);
		}

		// 设置生成的图片大小
		Size fileSize = null;
		sizeList = parameters.getSupportedPictureSizes();
		if (sizeList.size() > 0) {
			for (Size size : sizeList) {
				if (fileSize == null || (Math.abs(size.width * size.height - screenArea) < Math.abs(fileSize.width * fileSize.height - screenArea))) {
					fileSize = size;
				}
			}
			parameters.setPictureSize(fileSize.width, fileSize.height);
		}
		// 设置图片格式
		parameters.setPictureFormat(ImageFormat.JPEG);
		parameters.setJpegQuality(100);
		parameters.setJpegThumbnailQuality(100);
		// 自动聚焦模式
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		try {
			mCamera.setParameters(parameters);
		} catch (Exception e) {
			Log.e(TAG, "set camera parameters is failed, picture size:" + fileSize.width + "x" + fileSize.height);
			for (int i = 0; i < sizeList.size(); i++) {
				Size tmpSize = sizeList.get(i);
				parameters.setPictureSize(tmpSize.width, tmpSize.height);
				try {
					mCamera.setParameters(parameters);
					Log.d(TAG, "set camera parameters is success, picture size:" + tmpSize.width + "x" + tmpSize.height + ",index is" + i);
					break;
				} catch (Exception e1) {
					Log.e(TAG, "set camera parameters is failed, picture size:" + tmpSize.width + "x" + tmpSize.height);
				}
			}
		}
	}

	private OrientationEventListener mOrEventListener;

	/**
	 * 启动屏幕朝向改变监听函数 用于在屏幕横竖屏切换时改变保存的图片的方向
	 */
	private void startOrientationChangeListener() {
		if (mOrEventListener == null) {
			mOrEventListener = new OrientationEventListener(getContext()) {
				@Override
				public void onOrientationChanged(int rotation) {
					Log.i("rota", rotation + "");
					if (((rotation >= 0) && (rotation <= 45)) || (rotation > 315)) {
						rotation = 0;
					} else if ((rotation > 45) && (rotation <= 135)) {
						rotation = 90;
					} else if ((rotation > 135) && (rotation <= 225)) {
						rotation = 180;
					} else if ((rotation > 225) && (rotation <= 315)) {
						rotation = 270;
					} else {
						rotation = 0;
					}
					if (rotation == mOrientation)
						return;
					mOrientation = rotation;
					if(orilis!=null)
						orilis.orientationChangeListener(mOrientation);
					//updateCameraOrientation();
				}
			};
		}
		mOrEventListener.enable();
	}

	/**
	 * 根据当前朝向修改保存图片的旋转角度
	 */
	private void updateCameraOrientation() {
		if (mCamera != null) {
			Camera.Parameters parameters = mCamera.getParameters();
			// rotation参数为 0、90、180、270。水平方向为0。
			int rotation = 90 + mOrientation == 360 ? 0 : 90 + mOrientation;
			// 前置摄像头需要对垂直方向做变换，否则照片是颠倒的
			if (mIsFrontCamera) {
				if (rotation == 90)
					rotation = 270;
				else if (rotation == 270)
					rotation = 90;
			}
			parameters.setRotation(rotation);// 生成的图片转90°
			// 预览图片旋转90°
			mCamera.setDisplayOrientation(90);// 预览转90°
			mCamera.setParameters(parameters);
		}
	}

	/**
	 * @Description: 闪光灯类型枚举 默认为关闭
	 */
	public enum FlashMode {
		/** ON:拍照时打开闪光灯 */
		ON,
		/** OFF：不打开闪光灯 */
		OFF,
		/** AUTO：系统决定是否打开闪光灯 */
		AUTO,
		/** TORCH：一直打开闪光灯 */
		TORCH
	}

	public int getOrientation() {
		return mOrientation;
	}

	public void onPause() {
		if (mOrEventListener != null) {
			mOrEventListener.disable();
		}
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	public void onResume() {
		// 开启屏幕朝向监听
		startOrientationChangeListener();
		if (getWidth() > 0 && isCreated) {
			open();
		}
	}

	private void open(){
		new Thread(runnable).start();
	}
	private void open1() {
		try {
			openCamera();
			if (mCamera != null) {
				setCameraParameters();
				mCamera.setPreviewDisplay(getHolder());
				mCamera.startPreview();
				//updateCameraOrientation();
				mCamera.setDisplayOrientation(90);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			open1();
		}
	};

	public void startPreview() {
		if (mCamera != null) {
			mCamera.startPreview();
		}
	}

	public void stopPreview() {
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}
	
	private MyOrientationEventListener orilis;
	public void setOrientationChangeListener(MyOrientationEventListener orilis) {
		this.orilis = orilis;
	}
	
	public static interface MyOrientationEventListener{
		public void orientationChangeListener(int rotation);
	}
}
