package com.axter.libs.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * <p>右上角菜单PopupWindow封装类</p>
 * 
 * <p>布局文件写法可参考以前的</p>
 * <strong>
 * <p>重要布局LinearLayout RelativeLayout</p>
 * 
 * <p>指向图标ImageView#img_point</p>
 * </strong>
 * 
 * <li>构造{@link AutoPopupMenu#AutoPopupMenu(Context, int)}</li>
 * <li>指向图标{@link AutoPopupMenu#setPoint(int)}</li>
 * <li>设置背景色{@link AutoPopupMenu#setBackgroundColor(int)}</li>
 * <li>显示{@link AutoPopupMenu#showAsDropDown(View)}</li>
 * <li>关闭{@link AutoPopupMenu#dismiss()}</li>
 * <li>获取View对象，操作布局元素{@link AutoPopupMenu#getView()}</li>
 * 
 * @author zhaobo
 *
 */
public class AutoPopupMenu {
	private Context mContext;
	
	private PopupWindow popupWindow;
	private View popupView;
	private ImageView img_point;

	private int popupWidth;
	private int pointW;
	private boolean mIsFullW = false;
	/**
	 * 构造
	 * @param context
	 * @param rsid 资源id
	 */
	public AutoPopupMenu(Context context,int rsid) {
		initPopupMenu(context, rsid, false);
	}
	
	/**
	 * 构造
	 * @param context
	 * @param rsid
	 * @param isFullW 是否铺满宽
	 */
	public AutoPopupMenu(Context context,int rsid,boolean isFullW) {
		initPopupMenu(context, rsid, isFullW);
	}
	
	/**
	 * 初始化
	 * @param context
	 * @param rsid 资源id
	 * @param isFullW 是否铺满宽
	 */
	private void initPopupMenu(Context context,int rsid,boolean isFullW) {
		mContext = context;
		mIsFullW = isFullW;
		
		popupView = LayoutInflater.from(mContext).inflate(rsid, null);

		popupView.measure(0, 0);
		popupWidth = popupView.getMeasuredWidth();
		
		popupWindow = new PopupWindow(popupView);
		if(isFullW){
			popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		}else{
			popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
	}

	/**
	 * 设置指向点
	 * @param rsid
	 */
	public void setPoint(int rsid){
		img_point = (ImageView) popupView.findViewById(rsid);
		if(img_point==null)
			return;
		img_point.measure(0, 0);
		pointW = img_point.getMeasuredWidth();
	}
	
	/**
	 * 设置背景色
	 * @param color
	 */
	public void setBackgroundColor(int color){
		popupWindow.setBackgroundDrawable(new ColorDrawable(color));
	}
	
	public void showAsDropDown(View anchor) {
		int wW = anchor.getWidth();
		if(mIsFullW){
			int[] xy = new int[2];
			anchor.getLocationOnScreen(xy);
			setTranslationX(img_point, xy[0] + (wW>>1));
			popupWindow.showAsDropDown(anchor, 0, 0);
			return;
		}
		int mX = 0;
		if (popupWidth > wW + 20) {
			mX = -(popupWidth - wW) - 20;
		}
		setTranslationX(img_point, popupWidth + 20 - ((wW >> 1) + (pointW >> 1)));
		popupWindow.showAsDropDown(anchor, mX, 0);
	}

	private void setTranslationX(View view,float x){
		if(view==null)
			return;
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
		if(params == null){
			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		}
		params.setMargins((int) x, 0, 0, 0);
	}
	
	public void dismiss() {
		if (popupWindow.isShowing())
			popupWindow.dismiss();
	}

	public View getView(){
		return popupView;
	}
}
