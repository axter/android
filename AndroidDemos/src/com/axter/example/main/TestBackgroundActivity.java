package com.axter.example.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axter.example.R;
/**
 * <pre>
 * 展示了同一背景图片被应用于多个控件，布局计算刷新时，出现背景不统一的情况
 * bug初次发现于MultipleTextView控件，
 * 开源地址：<a href='https://github.com/MasonLiuChn/MultipleTextView'>https://github.com/MasonLiuChn/MultipleTextView</a>
 * </pre>
 * @author zhaobo
 *
 */
public class TestBackgroundActivity extends Activity {
	LinearLayout layout;
	Button btn_reflush;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_background);

		Drawable drawable = new ColorDrawable(Color.WHITE);
		
		layout = (LinearLayout) findViewById(R.id.layout);
		TextView tv_1 = new TextView(this);
		tv_1.setText("你的的还");
		tv_1.setBackground(drawable);
		
		TextView tv_2 = new TextView(this);
		tv_2.setText("你端的的还");
		tv_2.setBackground(drawable);
		
		TextView tv_3 = new TextView(this);
		tv_3.setText("你端");
		tv_3.setBackground(drawable);
		
		layout.addView(tv_1);
		layout.addView(tv_2);
		layout.addView(tv_3);
	}
}
