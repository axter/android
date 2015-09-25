package com.axter.example.tools;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.axter.example.R;

public class DoPic extends Activity implements OnTouchListener {
	private ImageView imgview;

	private Matrix matrix = new Matrix();
	/** 记录起点状态 */
	private Matrix savedMatrix = new Matrix();

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;

	/** 记录起点 */
	private PointF start = new PointF();
	/** 记录 缩放中心点 */
	private PointF mid = new PointF();
	private double oldDist = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_doimg);
		imgview = (ImageView) this.findViewById(R.id.imag1);

		imgview.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.aa));
		imgview.setOnTouchListener(this);
		imgview.setLongClickable(true);

	}

	/**计算缩放参数*/
	private double spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return Math.sqrt(x * x + y * y);
	}

	/** 计算缩放中心 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// http://my.oschina.net/banxi/blog/56421

		Log.d("Infor", "size:" + event.getSize());
		
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: // 开始触摸
				matrix.set(imgview.getImageMatrix());
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN: // 多点触摸
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:// 非主指 抬起
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:// 在移动
				if (mode == DRAG) { // 此实现图片的拖动功能...
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
				} else if (mode == ZOOM) {// 此实现图片的缩放功能...
					double newDist = spacing(event);
					if (newDist > 10) {
						matrix.set(savedMatrix);
						double scale = newDist / oldDist;
						matrix.postScale((float)scale, (float)scale, mid.x, mid.y);
					}
				}
				break;
		}
		imgview.setImageMatrix(matrix);
		return false;
	}
}
