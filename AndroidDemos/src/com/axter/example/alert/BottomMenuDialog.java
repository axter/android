package com.axter.example.alert;

import com.axter.example.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * <pre>
 * 文件名：	BottomMenuDialog.java
 * 作　者：	lijianqiang
 * 描　述：	自定义分享对话框
 * 
 * </pre>
 */
public class BottomMenuDialog extends Dialog {
	/**
	 * 使用方法
	 * 
	 * bottomMenuDialog = new BottomMenuDialog(this, btnAvatarHandler, new
	 * String[] { "拍照", "相册", "取消" }, new int[] { R.drawable.btn_photo_white,
	 * R.drawable.btn_picture_white, R.drawable.btn_cancle_white }); window =
	 * bottomMenuDialog.getWindow(); window.setGravity(Gravity.BOTTOM); //
	 * 此处可以设置dialog显示的位置 window.setWindowAnimations(R.style.mystyle); // 添加动画
	 * bottomMenuDialog.show();
	 * 
	 */

	/**
	 * menu类型：按钮
	 */
	public static final int MENU_TYPE_BUTTONS = 0;
	/**
	 * 上下文环境
	 */
	private Context mContext;
	/**
	 * 消息回调对象
	 */
	private Handler msgHandler = null;
	/**
	 * 项目列表
	 */
	private String[] itemList;
	/**
	 * 图标列表
	 */
	private int[] iconList;

	/**
	 * MENU_TYPE_BUTTONS类型，按钮点击监听
	 */
	private android.view.View.OnClickListener buttons_menu_onclickListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
			// 按钮点击事件处理，回调当前操作对象序列号
			int tag = (Integer) v.getTag();
			if (msgHandler != null) {
				msgHandler.sendEmptyMessage(tag);
			}
		}
	};

	/**
	 * MENU_TYPE_BUTTONS类型菜单，构造函数
	 * 
	 * @param context
	 * @param handler
	 * @param itemList
	 *            按钮名称列表
	 * @param iconList
	 *            按钮icon列表
	 */
	public BottomMenuDialog(Context context, Handler handler,
			String[] itemList, int[] iconList) {
		super(context, R.style.custom_dialog);
		mContext = context;
		msgHandler = handler;
		this.itemList = itemList;
		this.iconList = iconList;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initButtonsMenu();
	}

	/**
	 * 初始化底部菜单MENU_TYPE_BUTTONS
	 */
	@SuppressLint("NewApi") private void initButtonsMenu() {
		LinearLayout menuContainer = new LinearLayout(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		menuContainer.setLayoutParams(params);
		menuContainer.setOrientation(LinearLayout.VERTICAL);
		
		
		
		//setContentView(R.layout.frame_layout_bottom_menu_buttons);
		//LinearLayout menuContainer = (LinearLayout) findViewById(R.id.linear_bottom_container);
		int screenW = mContext.getResources().getDisplayMetrics().widthPixels;
		LayoutParams lpItem = new LayoutParams(screenW,
				LayoutParams.WRAP_CONTENT, 0, 0, 0);
		for (int k = 0; k < itemList.length; k++) {
//			View view_item = LayoutInflater.from(mContext).inflate(
//					R.layout.frame_layout_bottom_menu_buttons_item, null);
//			Button button = (Button) view_item
//					.findViewById(R.id.linear_bottom_container_item);
			
			Button button = new Button(mContext);
			ViewGroup.LayoutParams ll = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			button.setPadding(0, 10, 0, 10);
			button.setTop(15);
			button.setRight(15);
			button.setLeft(15);
			button.setLayoutParams(ll);
			
			
			String item_str = itemList[k];
			int item_res = iconList[k];
			button.setText(item_str);
			button.setTag(k);
			button.setOnClickListener(buttons_menu_onclickListener);
			button.setBackgroundResource(item_res);
			button.setTextColor(Color.RED);
			if (k == itemList.length - 1) {
				button.setTextColor(Color.WHITE);
			}
			//view_item.setLayoutParams(lpItem);
			menuContainer.addView(button);
		}
		
		setContentView(menuContainer);
	}
}
