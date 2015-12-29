package com.axter.example.bx;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.axter.example.R;

public class ZXView extends View {
	/**paint 折线*/
	private Paint paint_line = new Paint();
	/**paint 内圆点*/
	private Paint paint_in = new Paint();
	/**paint 外圆点*/
	private Paint paint_ou = new Paint();
	/**paint 内圆点被点击后*/
	private Paint paint_in_ed = new Paint();
	/**paint 星期字*/
	private Paint paint_week = new Paint();
	/**paint 虚线字*/
	private Paint paint_xu_text = new Paint();
	/**paint 虚线*/
	private Paint paint_xu_line = new Paint();
	
	/**被选择了第几个*/
	private int selected = -1;
	
	
	/**画布宽*/
	private int fullWidth = 0;
	/**画布高*/
	private int fullHeight = 0;
	/**星期字之间的间隔*/
	private int oneWeekSpaceX = 0;

	/**星期字的宽*/
	private float m_WeekWidth;
	/**星期字的高*/
	private float m_WeekHeight;
	
	/** 默认以10KM为最高点 */
	private float maxM = 10000;

	private ArrayList<Float> mils = new ArrayList<Float>();
	private ArrayList<PointF> points_draw = new ArrayList<PointF>();
	private ArrayList<String> weeks = new ArrayList<String>();
	
	
	public ZXView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context, attrs);
	}

	public ZXView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData(context, attrs);
	}

	private int xblColor;
	private int xInColor;
	private int xOuColor;

	private int xInSize;
	private int xOuSize;

	private int xPaddingLeft;
	private int xPaddingRight;
	private int xPaddingBottom;

	private int xWeekTextColor;
	private int xWeekTextSize;
	private int xWeekBottom;

	private void initData(Context context, AttributeSet attrs) {
		float density = context.getResources().getDisplayMetrics().density;
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ZXView);
		xblColor = array.getColor(R.styleable.ZXView_blColor, Color.parseColor("#FFE71C"));
		xInColor = array.getColor(R.styleable.ZXView_inColor, Color.parseColor("#FF9000"));
		xOuColor = array.getColor(R.styleable.ZXView_ouColor, Color.WHITE);

		xPaddingLeft = array.getDimensionPixelSize(R.styleable.ZXView_paddingLeft, (int) (30 * density + 0.5f));
		xPaddingRight = array.getDimensionPixelSize(R.styleable.ZXView_paddingRight, (int) (30 * density + 0.5f));
		xPaddingBottom = array.getDimensionPixelSize(R.styleable.ZXView_paddingBottom, (int) (20 * density + 0.5f));

		xInSize = array.getDimensionPixelSize(R.styleable.ZXView_inSize, (int) (7 * density + 0.5f));
		xOuSize = array.getDimensionPixelSize(R.styleable.ZXView_ouSize, (int) (10 * density + 0.5f));

		xWeekTextColor = array.getColor(R.styleable.ZXView_weekTextColor, Color.parseColor("#CD3C1A"));
		xWeekTextSize = array.getDimensionPixelSize(R.styleable.ZXView_weekTextSize, (int) (16 * scale + 0.5f));
		xWeekBottom = array.getDimensionPixelSize(R.styleable.ZXView_weekBottom, (int) (5 * density + 0.5f));

		array.recycle();

		paint_line.setColor(xblColor);
		paint_in.setColor(xInColor);
		paint_in_ed.setColor(Color.RED);
		paint_ou.setColor(xOuColor);
		paint_week.setColor(xWeekTextColor);
		paint_week.setTextSize(xWeekTextSize);
		
		paint_xu_line = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_xu_line.setStyle(Paint.Style.STROKE);
		paint_xu_line.setStrokeWidth(2);
		paint_xu_line.setColor(Color.parseColor("#66000000"));
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5}, 1);
		paint_xu_line.setPathEffect(effects);
		
		paint_xu_text.setColor(Color.parseColor("#66000000"));
		paint_xu_text.setTextSize(xWeekTextSize);
		//Typeface font = Typeface.createFromAsset(getContext().getAssets(),"fonts/HATTEN.TTF");
		//paint_xu_text.setTypeface(font);
		
		doWeeks();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		fullWidth = getWidth();
		fullHeight = getHeight();
		oneWeekSpaceX = (fullWidth - xPaddingLeft - xPaddingRight) / 6;
		
		if(isRun){
			isRun = false;
		}else{
			doPoints();
			if(onClickLis!=null){
				onClickLis.onReflash(fullWidth, fullHeight);
			}
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int count = points_draw.size();
		float temp = fullHeight - xPaddingBottom - m_WeekHeight;
		// ----------绘制折线-------------
		Path p = new Path();
		p.moveTo(-50, fullHeight);
		p.lineTo(0, temp);
		for (int i = 0; i < count; i++) {
			PointF pf = points_draw.get(i);
			p.lineTo(pf.x, pf.y);
		}
		p.lineTo(fullWidth, temp);
		p.lineTo(fullWidth + 50, fullHeight);

		canvas.drawPath(p, paint_line);
		// ----------绘制圆点-------------
		for (int i = 0; i < count; i++) {
			PointF pf = points_draw.get(i);
			canvas.drawCircle(pf.x, pf.y, xOuSize, paint_ou);
			if (selected == i)
				canvas.drawCircle(pf.x, pf.y, xInSize, paint_in_ed);
			else
				canvas.drawCircle(pf.x, pf.y, xInSize, paint_in);
		}
		// ------------绘制日期------------
		for (int i = 0; i < 7; i++) {
			PointF pf = points_draw.get(i);
			canvas.drawText(weeks.get(i), pf.x - m_WeekWidth / 2, fullHeight - xWeekBottom, paint_week);

			// 公里
			// String mStr = String.valueOf(mils[i]);
			// float w = paint_week.measureText(mStr, 0, mStr.length());
			// canvas.drawText(mStr, pf.x-w/2, pf.y+xOuSize*2+5, paint_week);
		}
		// -------------绘制虚线--------------
		{
			canvas.drawText("0 KM", 10, sY + sH, paint_xu_text);
			
			Path p1 = new Path();
			p1.moveTo(sX, sY);
			p1.lineTo(fullWidth, sY);
			canvas.drawPath(p1 , paint_xu_line);
			
			canvas.drawText(maxM/1000 +" KM", 10, eY + sH, paint_xu_text);
			
			Path p2 = new Path();
			p2.moveTo(sX, eY);
			p2.lineTo(fullWidth, eY);
			canvas.drawPath(p2, paint_xu_line);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float two = xOuSize << 1;
			for (int i = 0; i < 7; i++) {
				PointF pf = points_draw.get(i);
				if (x > pf.x - two && x < pf.x + two && y > pf.y - two && y < pf.y + two) {
					if (onClickLis != null) {
						selected = i;
						postInvalidate();

						onClickLis.onClick(i, pf.x, fullHeight - pf.y,fullWidth,fullHeight);
						return false;
					}
				}
			}
		}
		if (onClickLis != null) {
			onClickLis.onOthers(x, fullHeight - y);
			return false;
		}

		return super.onTouchEvent(event);
	}

	private OnClick onClickLis;

	public void setOnClick(OnClick onClick) {
		this.onClickLis = onClick;
	}

	public interface OnClick {
		public void onClick(int index, float x, float y,float w,float h);

		public void onOthers(float x, float y);
		
		public void onLoaded(float w,float h);
		
		public void onReflash(float w,float h);
	}

	public float getIndex(int index) {
		int count = mils.size();
		if(index>=count || index<0)
			return 0;
		return mils.get(index);
	}
	public PointF focusIndex(int index){
		int count = points_draw.size();
		if(index>=count || index<0){
			index = points_draw.size()-1;
		}
		selected = index;
		postInvalidate();
		PointF f = points_draw.get(index);
		return new PointF(f.x,fullHeight - f.y);
	}

	private ArrayList<PointF> points_ago = new ArrayList<PointF>();
	private ArrayList<PointF> points_new = new ArrayList<PointF>();

	public void setMils(ArrayList<Float> ms) {
		isRun = false;

		copyPoint(points_draw, points_ago);
		mils.clear();
		mils = ms;
		doPoints();
		copyPoint(points_draw, points_new);

		new Thread(new runnable()).start();
	}

	public void setSeven(ArrayList<SevenDaysResponse> ms) {
		isRun = false;
		
		copyPoint(points_draw, points_ago);
		mils.clear();
		weeks.clear();
		int count = 0;
		for (SevenDaysResponse bean : ms) {
			if(count==5){
				weeks.add("昨天");
			}else if(count==6){
				weeks.add("今天");
			}else{
				weeks.add(getWeek(bean.getDay_of_week() + 1));
			}
			mils.add((float) bean.getValid_distances());
			count++;
		}
		doPoints();
		copyPoint(points_draw, points_new);
		
		new Thread(new runnable()).start();
	}

	public void copyPoint(ArrayList<PointF> a1, ArrayList<PointF> to1) {
		to1.clear();
		for (PointF p : a1) {
			to1.add(new PointF(p.x, p.y));
		}
	}

	public void copyPoint(float index) {
		for (int i = 0; i < 7; i++) {
			PointF p = points_draw.get(i);
			PointF p1 = points_ago.get(i);
			PointF p2 = points_new.get(i);
			p.x = p1.x + (p2.x - p1.x) * index;
			p.y = p1.y + (p2.y - p1.y) * index;
		}
	}
	/**
	 * 防止 动画没结束 就又开始新动画
	 */
	private static boolean isRun = false;
	class runnable implements Runnable{
		float count = 0.05f;
		@Override
		public void run() {
			try {
				isRun = true;
				while (isRun && count < 1.05f) {
					copyPoint(count);
					postInvalidate();
					count += 0.05f;

					Thread.sleep(20);
				}
				doPoints();
				postInvalidate();
				if(null != onClickLis){
					((Activity) getContext()).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onClickLis.onLoaded(fullWidth,fullHeight);
						}
					});
					
				}
				isRun = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 折线各点位置计算
	 */
	public void doPoints() {
		// ----------计算字宽高-----------
		String text = "今天";
		Rect bounds = new Rect();
		paint_week.getTextBounds(text, 0, text.length(), bounds);
		m_WeekWidth = paint_week.measureText(text, 0, text.length());
		m_WeekHeight = bounds.height();
		// ----------计算峰值-------------
		for (float m : mils) {
			if (m > maxM) {
				maxM = m;
			}
		}
		// ----------计算点-------------
		float bl = (fullHeight - xOuSize - xPaddingBottom - m_WeekHeight) / maxM;

		for (int i = 0; i < 7; i++) {
			float x = i * oneWeekSpaceX + xPaddingLeft;
			float y = fullHeight - xPaddingBottom - m_WeekHeight - (mils.get(i) * bl);
			PointF pf = points_draw.get(i);
			pf.x = x;
			pf.y = y;
		}
		doXu(bl);
	}

	/**虚线字，高偏移量*/
	private int sH = 0;
	/**虚线右移偏移量*/
	private float sX=0;
	/**虚线最低Y坐标*/
	private float sY=0;
	/**虚线最高Y坐标*/
	private float eY=0;
	/**
	 * 初始化 虚线 虚线字 相关参数
	 * @param bl 高度比
	 */
	private void doXu(float bl){
		String txt_min = "0 KM";
		String txt_max = maxM/1000 +" KM";
		Rect bounds_min = new Rect();
		paint_xu_text.getTextBounds(txt_min, 0, txt_min.length(), bounds_min);
		Rect bounds_max = new Rect();
		paint_xu_text.getTextBounds(txt_max, 0, txt_max.length(), bounds_max);
		
		int maxW = bounds_min.width();
		if(bounds_max.width() > bounds_min.width()){
			maxW = bounds_max.width();
		}
		sH = bounds_max.height()/3;
		
		sX = 10 + maxW + 10;
		sY = (fullHeight - (0 * bl) - xPaddingBottom - m_WeekHeight);
		eY = (fullHeight - (maxM * bl) - xPaddingBottom - m_WeekHeight);
	}
	/**
	 * 初始化周横幅
	 */
	public void doWeeks() {
		weeks.clear();
		Calendar now = Calendar.getInstance();

		weeks.add(getWeek(now, -6));
		weeks.add(getWeek(now, 1));
		weeks.add(getWeek(now, 1));
		weeks.add(getWeek(now, 1));
		weeks.add(getWeek(now, 1));
		weeks.add("昨天");
		weeks.add("今天");

		mils.clear();

		mils.add(0f);
		mils.add(1000f);
		mils.add(0f);
		mils.add(500f);
		mils.add(0f);
		mils.add(700f);
		mils.add(0f);
		
		points_draw.clear();
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
	}

	private String getWeek(Calendar cal, int value) {
		cal.add(Calendar.DAY_OF_MONTH, value);
		int week = cal.get(Calendar.DAY_OF_WEEK);

		return getWeek(week);
	}

	/**
	 * 根据数据返回周几
	 * @param week 1 周日，依次类推
	 * @return
	 */
	private String getWeek(int week) {
		if (week == 1) {
			return "周日";
		} else if (week == 2) {
			return "周一";
		} else if (week == 3) {
			return "周二";
		} else if (week == 4) {
			return "周三";
		} else if (week == 5) {
			return "周四";
		} else if (week == 6) {
			return "周五";
		} else if (week == 7) {
			return "周六";
		}
		return "";
	}
}
