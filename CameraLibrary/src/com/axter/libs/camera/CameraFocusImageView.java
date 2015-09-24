package com.axter.libs.camera;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.axter.libs.camera.R;

/** 
 * @ClassName: Item_FocusImageView 
 * @Description: 
 * @author LinJ
 * @date 2015-1-4 
 *  
 */
public class CameraFocusImageView extends ImageView {
	public final static String TAG="Item_FocusImageView";
	private static final int NO_ID=-1;
	private int mFocusImg=NO_ID;
	private int mFocusSucceedImg=NO_ID;
	private int mFocusFailedImg=NO_ID;
	private Animation mAnimation;
	private Handler mHandler;
	public CameraFocusImageView(Context context) {
		super(context);
		mAnimation=AnimationUtils.loadAnimation(getContext(), R.anim.camera_focus_show);
		setVisibility(View.GONE);
		mHandler=new Handler();
	}

	public CameraFocusImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mAnimation=AnimationUtils.loadAnimation(context, R.anim.camera_focus_show);
		mHandler=new Handler();

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CameraFocus);
		mFocusImg = a.getResourceId(R.styleable.CameraFocus_camerafocus_focusing_id, NO_ID);
		mFocusSucceedImg=a.getResourceId(R.styleable.CameraFocus_camerafocus_success_id, NO_ID);
		mFocusFailedImg=a.getResourceId(R.styleable.CameraFocus_camerafocus_fail_id, NO_ID);
		a.recycle();

		//�۽�ͼƬ����Ϊ��
		if (mFocusImg==NO_ID||mFocusSucceedImg==NO_ID||mFocusFailedImg==NO_ID) 
			throw new RuntimeException("Animation is null");
	}

	/**  
	 *  ��ʾ�۽�ͼ��
	 *  @param x ������x����
	 *  @param y ������y����
	 */
	public void startFocus(Point point){
		if (mFocusImg==NO_ID||mFocusSucceedImg==NO_ID||mFocusFailedImg==NO_ID) 
			throw new RuntimeException("focus image is null");
		//���ݴ������������þ۽�ͼ����λ��
		RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) getLayoutParams();
		params.topMargin= point.y-getHeight()/2;
		params.leftMargin=point.x-getWidth()/2;
		setLayoutParams(params);	
		//���ÿؼ��ɼ�������ʼ����
		setVisibility(View.VISIBLE);
		setImageResource(mFocusImg);
		startAnimation(mAnimation);	
		//3�������View���ڴ˴����������ڿ��ܾ۽��¼����ܲ�������
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
			}
		},3500);
	}
	
	/**  
	*   �۽��ɹ��ص�
	*/
	public void onFocusSuccess(){
		setImageResource(mFocusSucceedImg);
		//�Ƴ���startFocus�����õ�callback��1������ظÿؼ�
		mHandler.removeCallbacks(null, null);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
			}
		},1000);
		
	}
	
	/**  
	*   �۽�ʧ�ܻص�
	*/
	public void onFocusFailed(){
		setImageResource(mFocusFailedImg);
		//�Ƴ���startFocus�����õ�callback��1������ظÿؼ�
		mHandler.removeCallbacks(null, null);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
			}
		},1000);
	}

	/**  
	 * ���ÿ�ʼ�۽�ʱ��ͼƬ
	 *  @param focus   
	 */
	public void setFocusImg(int focus) {
		this.mFocusImg = focus;
	}

	/**  
	 *  ���þ۽��ɹ���ʾ��ͼƬ
	 *  @param focusSucceed   
	 */
	public void setFocusSucceedImg(int focusSucceed) {
		this.mFocusSucceedImg = focusSucceed;
	}
}
