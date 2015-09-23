package com.imohoo.libs.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.imohoo.libs.R;

/**
 * @ClassName: Item_CameraContainer
 * @Description: 相机界面的容器 包含相机绑定的surfaceview、拍照后的临时图片View和聚焦View
 * @author LinJ
 * @date 2014-12-31 上午9:38:52
 * 
 */
public class CameraContainer extends RelativeLayout implements CameraOperation {
	public final static String TAG = "Item_CameraContainer";

	/** 相机绑定的SurfaceView */
	private CameraView mCameraView;

	/** 触摸屏幕时显示的聚焦图案 */
	private CameraFocusImageView mFocusImageView;

	/** 存放照片的根目录 */
	private String mSavePath;

	/** 照片字节流处理类 */
	private DataHandler mDataHandler;

	/** 拍照监听接口，用以在拍照开始和结束后执行相应操作 */
	private TakePictureListener mListener;

	public CameraContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		setOnTouchListener(new TouchListener());
	}

	/**
	 * 初始化子控件
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		inflate(context, R.layout.camera_container, this);
		mCameraView = (CameraView) findViewById(R.id.cameraView);

		mFocusImageView = (CameraFocusImageView) findViewById(R.id.focusImageView);
	}

	/**
	 * 前置、后置摄像头转换
	 */
	@Override
	public void switchCamera() {
		mCameraView.switchCamera();
	}

	/**
	 * 设置文件保存路径
	 * 
	 * @param rootPath
	 */
	public void setRootPath(String rootPath) {
		this.mSavePath = rootPath;
	}

	/**
	 * 拍照方法
	 * 
	 * @param callback
	 */
	public void takePicture() {
		takePicture(pictureCallback, mListener);
	}

	/**
	 * @Description: 拍照方法
	 * @param @param listener 拍照监听接口
	 * @return void
	 * @throws
	 */
	public void takePicture(TakePictureListener listener) {
		this.mListener = listener;
		takePicture(pictureCallback, mListener);
	}

	@Override
	public void takePicture(PictureCallback callback, TakePictureListener listener) {
		mCameraView.takePicture(callback, listener);
	}

	private final PictureCallback pictureCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (mSavePath == null)
				throw new RuntimeException("mSavePath is null");
			if (mDataHandler == null)
				mDataHandler = new DataHandler();
			mDataHandler.setMaxSize(100);
			File file = mDataHandler.save(data);
			// 重新打开预览图，进行下一次的拍照准备
			// camera.startPreview();
			if (mListener != null)
				mListener.onTakePictureEnd(file, camera);
		}
	};

	/**
	 * 拍照返回的byte数据处理类
	 * 
	 * @author linj
	 * 
	 */
	private final class DataHandler {
		/** 压缩后的图片最大值 单位KB */
		private int maxSize = 100;
		public DataHandler() {
			File folder = new File(mSavePath).getParentFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}

		private Bitmap rotateBitmap(Bitmap bitmap, int degress, boolean isHorizontalTurn) {
			if (bitmap != null) {
				Matrix m = new Matrix();
				if (isHorizontalTurn) {
					m.postScale(-1, 1); // 镜像水平翻转
				}
				m.postRotate(degress);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			}
			return bitmap;
		}

		/**
		 * 保存图片
		 * 
		 * @param 相机返回的文件流
		 * @return 解析流生成的缩略图
		 */
		public File save(byte[] data) {
			try {
				File file = new File(mSavePath);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data);
				fos.flush();
				fos.close();
				
//				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));  
//				Bitmap fileBm = BitmapFactory.decodeByteArray(data, 0, data.length);
//				fileBm.compress(CompressFormat.JPEG, 100, bos);
//		        bos.flush();
//		        bos.close();
		        
				ExifInterface exifInterface = new ExifInterface(mSavePath);
				int orientation = mCameraView.getOrientation();
				switch(orientation+90) {
	                case 90:
	                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,""+ExifInterface.ORIENTATION_ROTATE_90);
	                    break;
	                case 180:
	                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,""+ExifInterface.ORIENTATION_ROTATE_180);
	                    break;
	                case 270:
	                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,""+ExifInterface.ORIENTATION_ROTATE_270);
	                    break;
	                default:
	                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,""+ExifInterface.ORIENTATION_NORMAL);
	                    break;
				}
				exifInterface.saveAttributes();
				return file;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			if (data == null) {
				// 解析生成相机返回的图片
				Bitmap fileBm = BitmapFactory.decodeByteArray(data, 0, data.length);
				if (!mCameraView.isUseFrontCamera()) {
					if (fileBm.getWidth() > fileBm.getHeight()) {
						Bitmap oldBm = fileBm;
						fileBm = rotateBitmap(oldBm, 90, false);
						oldBm.recycle();
						oldBm = null;
					}
				} else {
					int degress = 0;
					if (fileBm.getWidth() > fileBm.getHeight()) {
						degress = 90;
					}
					Bitmap oldBm = fileBm;
					fileBm = rotateBitmap(oldBm, degress, true);
					oldBm.recycle();
					oldBm = null;
				}
				// bitmap裁剪
				int width = getWidth();
				int height = getHeight();
				float scaleW = (float) width / fileBm.getWidth();
				float scaleH = (float) height / fileBm.getHeight();
				float scale = (scaleW > scaleH) ? scaleW : scaleH;

				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale); // 长和宽放大缩小的比例
				Bitmap bm = null;
				int picY = 0;
				int picX = 0;
				if (width < fileBm.getWidth() && height < fileBm.getHeight()) {
					// picX = ((int)(fileBm.getWidth()*scale)-width>0) ? ((int)(fileBm.getWidth()*scale)-width) : 0;
					picY = ((int) (fileBm.getHeight() * scale) - height > 0) ? ((int) (fileBm.getHeight() * scale) - height) : 0;
					bm = Bitmap.createBitmap(fileBm, picX, picY, width, height, matrix, true);
					fileBm.recycle();
					fileBm = null;
				} else {
					float rateFile = (float) fileBm.getWidth() / fileBm.getHeight();
					float rateGoal = (float) width / height;
					if (rateFile < rateGoal) {
						int newHeight = fileBm.getWidth() * height / width;
						picY = fileBm.getHeight() - newHeight;
						bm = Bitmap.createBitmap(fileBm, 0, picY, fileBm.getWidth(), newHeight, matrix, true);
					} else if (rateFile > rateGoal) {
						int newWidth = width * fileBm.getHeight() / height;
						picX = (fileBm.getWidth() - newWidth) / 2;
						bm = Bitmap.createBitmap(fileBm, picX, 0, newWidth, fileBm.getHeight(), matrix, true);
					} else {
						bm = Bitmap.createBitmap(fileBm, 0, 0, fileBm.getWidth(), fileBm.getHeight(), matrix, true);
					}
				}

				// fileBm.recycle();
				// fileBm = null;
				// 获取加水印的图片
				// 生成缩略图
				Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 213, 213);

				File file = new File(mSavePath);
				try {
					// 存图片大图
					FileOutputStream fos = new FileOutputStream(file);
					ByteArrayOutputStream bos = compress(bm);
					fos.write(bos.toByteArray());
					fos.flush();
					fos.close();
					// 存图片小图
//					BufferedOutputStream bufferos = new BufferedOutputStream(new FileOutputStream(thumFile));
//					thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bufferos);
//					bufferos.flush();
//					bufferos.close();
					return file;
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					Toast.makeText(getContext(), "解析相机返回流失败", Toast.LENGTH_SHORT).show();

				} finally {
					if (bm != null) {
						bm.recycle();
						bm = null;
					}
					if (thumbnail != null) {
						thumbnail.recycle();
						thumbnail = null;
					}
					if (fileBm != null) {
						fileBm.recycle();
						fileBm = null;
					}
				}
			} else {
				Toast.makeText(getContext(), "拍照失败，请重试", Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		/**
		 * 图片压缩方法
		 * 
		 * @param bitmap
		 *            图片文件
		 * @param max
		 *            文件大小最大值
		 * @return 压缩后的字节流
		 * @throws Exception
		 */
		public ByteArrayOutputStream compress(Bitmap bitmap) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 49;
			while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				options -= 3;// 每次都减少10
				// 压缩比小于0，不再压缩
				if (options < 0) {
					break;
				}
				Log.i(TAG, baos.toByteArray().length / 1024 + "");
				baos.reset();// 重置baos即清空baos
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			}
			return baos;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}
	}

	/**
	 * @ClassName: TakePictureListener
	 * @Description: 拍照监听接口，用以在拍照开始和结束后执行相应操作
	 * @author LinJ
	 * @date 2014-12-31 上午9:50:33
	 * 
	 */
	public static interface TakePictureListener {
		/**
		 * 拍照结束执行的动作，该方法会在onPictureTaken函数执行后触发
		 * 
		 * @param bm
		 *            拍照生成的图片
		 */
		public void onTakePictureEnd(File file, Camera camera);

		/**
		 * 临时图片动画结束后触发
		 * 
		 * @param bm
		 *            拍照生成的图片
		 * @param isVideo
		 *            true：当前为录像缩略图 false:为拍照缩略图
		 * */
		public void onAnimtionEnd(Bitmap bm, boolean isVideo);
	}

	private final class TouchListener implements OnTouchListener {

		/** 记录是拖拉照片模式还是放大缩小照片模式 */

		private static final int MODE_INIT = 0;
		/** 放大缩小照片模式 */
		private static final int MODE_ZOOM = 1;
		private int mode = MODE_INIT;// 初始状态

		/** 用于记录拖拉图片移动的坐标位置 */

		private float startDis;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 手指压下屏幕
				case MotionEvent.ACTION_DOWN:
					mode = MODE_INIT;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:

					mode = MODE_ZOOM;
					/** 计算两个手指间的距离 */
					startDis = distance(event);
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				// 手指离开屏幕
				case MotionEvent.ACTION_UP:
					if (mode != MODE_ZOOM) {
						// 设置聚焦
						Point point = new Point((int) event.getX(), (int) event.getY());
						mCameraView.onFocus(point, autoFocusCallback);
						mFocusImageView.startFocus(point);
					}
					break;
			}
			return true;
		}

		private final AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				// 聚焦之后根据结果修改图片
				if (success) {
					mFocusImageView.onFocusSuccess();
				} else {
					mFocusImageView.onFocusFailed();
				}
			}
		};

		/** 计算两个手指间的距离 */
		private float distance(MotionEvent event) {
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			/** 使用勾股定理返回两点之间的距离 */
			return (float) Math.sqrt(dx * dx + dy * dy);
		}

	}

	public void close() {
		mCameraView.close();
	}
}