package com.axter.example.alert;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.axter.example.R;

public class TestCustomDialogActivity extends Activity{
	private Context mContext;
	
	private String[] titles = new String[] { "本地照片", "拍 照", "取 消" };
	private int[] iconList = new int[] { R.drawable.btn_box_red,
			R.drawable.btn_box_red, R.drawable.btn_red };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_dialog);
		mContext = this;
		findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomDialog dialog = new CustomDialog(mContext);
				dialog.show();
				
//				BottomMenuDialog dialog = new BottomMenuDialog(mContext, handler, titles, iconList);
//				Window window = dialog.getWindow();
//				window.setGravity(Gravity.BOTTOM);
//				window.setWindowAnimations(R.style.DataSheetAnimation);
//				dialog.show();
			}
		});
	}
	
	private Handler handler = new Handler(){
		
	};
	
}
