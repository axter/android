package com.axter.example.alert;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.axter.example.R;

public class CustomDialog extends Dialog {
	private Context mContext;

	public CustomDialog(Context context) {
		super(context, R.style.Dialog_Fullscreen);
		mContext = context;
	}

	private String[] itemList = new String[] { "本地照片", "拍 照", "取 消" };
	private int[] iconList = new int[] { R.drawable.btn_box_red, R.drawable.btn_box_red, R.drawable.btn_red };

	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setGravity(Gravity.BOTTOM);
		if (1 == 1) {
//			initButtonsMenu();
			setContentView(R.layout.activity_test_dialog);
			// http://www.aichengxu.com/view/34982
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			EditText et = (EditText)findViewById(R.id.editText1);
			et.requestFocus();
//			LayoutParams lay = getWindow().getAttributes();
//			setParams(lay);
		} else {
			LinearLayout menuContainer = new LinearLayout(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			menuContainer.setLayoutParams(params);
			menuContainer.setOrientation(LinearLayout.VERTICAL);

			for (int k = 0; k < itemList.length; k++) {
				Button button = new Button(mContext);
				ViewGroup.LayoutParams ll = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				button.setPadding(0, 10, 0, 10);
				button.setTop(15);
				button.setRight(15);
				button.setLeft(15);
				button.setLayoutParams(ll);

				String item_str = itemList[k];
				int item_res = iconList[k];
				button.setText(item_str);
				button.setTag(k);
				button.setBackgroundResource(item_res);
				button.setTextColor(Color.RED);
				if (k == itemList.length - 1) {
					button.setTextColor(Color.WHITE);
				}
				menuContainer.addView(button);
			}

			setContentView(menuContainer);
		}
	}

	private void initButtonsMenu() {
		setContentView(R.layout.activity_test_dialog);
		LinearLayout menuContainer = (LinearLayout) findViewById(R.id.linear_bottom_container);
		int screenW = mContext.getResources().getDisplayMetrics().widthPixels;
		LayoutParams lpItem = new LayoutParams(screenW, LayoutParams.WRAP_CONTENT, 0, 0, 0);

		View view = new View(mContext);
		view.setLayoutParams(lpItem);
		menuContainer.addView(view);
		// view.setVisibility(View.GONE);

		// for (int k = 0; k < itemList.length; k++) {
		// View view_item = LayoutInflater.from(mContext).inflate(
		// R.layout.frame_layout_bottom_menu_buttons_item, null);
		// Button button = (Button) view_item
		// .findViewById(R.id.linear_bottom_container_item);
		// String item_str = itemList[k];
		// int item_res = iconList[k];
		// button.setText(item_str);
		// button.setTag(k);
		// button.setBackgroundResource(item_res);
		// button.setTextColor(Color.RED);
		// if (k == itemList.length - 1) {
		// button.setTextColor(Color.WHITE);
		// }
		// view_item.setLayoutParams(lpItem);
		// menuContainer.addView(view_item);
		// }
	}

	private void setParams(LayoutParams lay) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(dm);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.height = 300 - rect.top;
		lay.width = dm.widthPixels;
	}
}
