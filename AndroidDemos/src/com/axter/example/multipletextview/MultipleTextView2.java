package com.axter.example.multipletextview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.axter.example.R;
import com.axter.example.multipletextview.MultipleTextView1.OnMultipleTVItemClickListener;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MultipleTextView2 extends LinearLayout {

	private Context context;
	private float textSize;
	private int textColor;
	private int wordMargin;
	private int lineMargin;
	private int textPaddingLeft;
	private int textPaddingRight;
	private int textPaddingTop;
	private int textPaddingBottom;
	private int textBackground;

	private int layout_width;
	private int marginLeft, marginRight;
	private OnMultipleTVItemClickListener listener;

	public MultipleTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		
		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MyView);
		textColor = array.getColor(R.styleable.MyView_textColor, 0XFF00FF00); // 提供默认值，放置未指定
		textSize = array.getDimension(R.styleable.MyView_textSize, 24);
		textSize = px2sp(context,textSize);
		wordMargin = array.getDimensionPixelSize(R.styleable.MyView_textWordMargin, 0);
		lineMargin = array.getDimensionPixelSize(R.styleable.MyView_textLineMargin, 0);
		textBackground = array.getResourceId(R.styleable.MyView_textBackground,-1);
		textPaddingLeft = array.getDimensionPixelSize(R.styleable.MyView_textPaddingLeft, 0);
		textPaddingRight = array.getDimensionPixelSize(R.styleable.MyView_textPaddingRight, 0);
		textPaddingTop = array.getDimensionPixelSize(R.styleable.MyView_textPaddingTop, 0);
		textPaddingBottom = array.getDimensionPixelSize(R.styleable.MyView_textPaddingBottom, 0);
		array.recycle();
		
		
		
		int[] attrsA = new int[] { 
				android.R.attr.layout_width,
				android.R.attr.layout_marginLeft,
				android.R.attr.layout_marginRight
			};
		TypedArray ta = context.obtainStyledAttributes(attrs, attrsA);
		try {
			layout_width = ta.getDimensionPixelSize(0, LayoutParams.MATCH_PARENT);
		} catch (Exception e) {
			DisplayMetrics dis = getResources().getDisplayMetrics();
			layout_width = dis.widthPixels;
		}
		marginLeft = ta.getDimensionPixelSize(1, 0);
		marginRight = ta.getDimensionPixelSize(2, 0);
		ta.recycle();
		

		// 实际内容宽度
		layout_width = layout_width - marginLeft - marginRight;
	}

	public OnMultipleTVItemClickListener getOnMultipleTVItemClickListener() {
		return listener;
	}

	public void setOnMultipleTVItemClickListener(OnMultipleTVItemClickListener listener) {
		this.listener = listener;
	}

	public int getMeasuredWidth(View tv) {
		// 计算TextView宽度
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		tv.measure(w, h);
		int tvh = tv.getMeasuredHeight();
		int tvw = tv.getMeasuredWidth();
		return tvw;
	}

	@SuppressLint("NewApi") 
	public TextView getTextView(String content) {
		TextView tv = new TextView(context);
		tv.setText(content);
		tv.setTextSize(16);
		
//		//aa = -1;
//		if(aa==-1){
//			textBackground = new ColorDrawable(Color.WHITE);
//		}else{
//			textBackground = getContext().getDrawable(aa);
//		}
		
		if(textBackground!=-1){
			//tv.setBackground(textBackground);
			//tv.setBackgroundColor(Color.WHITE);
			//tv.setBackground(new ColorDrawable(Color.WHITE));
			try{
				Drawable drawable = getResources().getDrawable(textBackground);
				tv.setBackground(drawable);
			}catch(Exception ex){
				Drawable drawable = new ColorDrawable(textBackground);
				tv.setBackground(drawable);
			}
		}
		tv.setTextColor(Color.BLACK);
		// tv.setPadding(10, 20, 10,20);
		// tv.setTag(i);// 标记position
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onMultipleTVItemClick(v, (Integer) v.getTag());
				}
			}
		});
		return tv;
	}
	public LinearLayout getLinearLayout(){
		LinearLayout layout = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(params);
		return layout;
	}
	public LinearLayout.LayoutParams getLeftParams(){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 20, 10);
		return lp;
	}
	public LinearLayout.LayoutParams getRightParams(){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 10, 0, 10);
		return lp;
	}
	public LinearLayout.LayoutParams getCenterParams(){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 10, 20, 10);
		return lp;
	}
	public void setTextViews(List<String> dataList) {
		if (dataList == null || dataList.size() == 0) {
			return;
		}

		int countW = 0;
		int lineNum = 0;
		SparseArray<ArrayList<TextView>> lines_tv = new SparseArray<ArrayList<TextView>>();
		lines_tv.put(lineNum, new ArrayList<TextView>());
		// 创建一行
//		LinearLayout lineLayout = new LinearLayout(context);
//		LinearLayout.LayoutParams linep = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		lineLayout.setLayoutParams(linep);
//		addView(lineLayout);

		ArrayList<TextView> list = lines_tv.get(lineNum);
		
		int dataSize = dataList.size();
		for (int i = 0; i < dataSize; i++) {
			// 创建TextView
			TextView tv = getTextView(dataList.get(i));
			
			// 计算TextView宽度
			int tvw = getMeasuredWidth(tv);
			
			// 判断是否需要换行
			if (layout_width < countW + tvw + list.size() * 20 * 2) {
				// 设置第一个间隔
				tv.setLayoutParams(getLeftParams());
				// 设置最后一个间隔
				list.get(list.size() - 1).setLayoutParams(getRightParams());
				// 平均padding
				int last = layout_width - countW - (list.size() - 1) * 20 * 2;
				last = last / list.size() / 2;
				for (TextView t : list) {
					t.setPadding(last, 10, last, 10);
				}
				// 创建新的行
				lineNum++;
				lines_tv.put(lineNum, new ArrayList<TextView>());
				list = lines_tv.get(lineNum);
				countW = tvw;
			} else {
				if (countW == 0) {
					tv.setLayoutParams(getLeftParams());
				} else {
					tv.setLayoutParams(getCenterParams());
				}
				countW += tvw;
			}
			list.add(tv);
		}
		if (list.size() == 0){
			return;
		}else if(list.size()==1){
			
		}else{
			list.get(list.size() - 1).setLayoutParams(getRightParams());
		}
		// 平均padding
		int last = layout_width - countW - (list.size() - 1) * 20 * 2;
		last = last / list.size() / 2;
		for (TextView t : list) {
			t.setPadding(last, 10, last, 10);
		}
		
		int lineSize = lines_tv.size();
		for(int i=0;i<lineSize;i++){
			ArrayList<TextView> l = lines_tv.get(i);
			LinearLayout layout = getLinearLayout();
			addView(layout);
			for(TextView tv:l){
				layout.addView(tv);
			}
		}
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

//	public interface OnMultipleTVItemClickListener {
//		public void onMultipleTVItemClick(View view, int position);
//	}
}
