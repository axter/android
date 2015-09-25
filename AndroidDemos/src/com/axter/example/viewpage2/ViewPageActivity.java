package com.axter.example.viewpage2;

import java.util.ArrayList;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axter.example.R;

public class ViewPageActivity extends FragmentActivity {
	private ViewPager mPager;
	private ArrayList<Fragment> mFragmentList;
	private ArrayList<TextView> mTextViewList;
	private int mCurrIndex;// 当前页卡编号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_viewpage_2);

		initTextView();
		initImage();
		initViewPager();
		initViewPager2();
	}

	/*
	 * 初始化标签名
	 */
	public void initTextView() {
		mTextViewList = new ArrayList<TextView>();
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.layout_head);
		
		TextView view = (TextView) getLayoutInflater().inflate(R.layout.activity_test_viewpage_2_item, null);
		view.setText("标题栏1");
		layout.addView(view);
		
		mTextViewList.add(view);
		
		view.setOnClickListener(new txListener(0));
	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化图片的位移像素
	 */
	public void initImage() {
		
	}

	/*
	 * 初始化ViewPager
	 */
	public void initViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		mFragmentList = new ArrayList<Fragment>();
		Fragment btFragment = new ButtonFragment();
		mFragmentList.add(btFragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	public void initViewPager2(){
		Fragment secondFragment = TestFragment.newInstance("this is second fragment");
		Fragment thirdFragment = TestFragment.newInstance("this is third fragment");
		Fragment fourthFragment = TestFragment.newInstance("this is fourth fragment");
		mFragmentList.add(secondFragment);
		mFragmentList.add(thirdFragment);
		mFragmentList.add(fourthFragment);
		mPager.getAdapter().notifyDataSetChanged();
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
		}
	}

}