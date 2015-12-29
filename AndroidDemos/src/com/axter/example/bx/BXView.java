package com.axter.example.bx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.axter.example.BuildConfig;
import com.axter.example.R;

public class BXView extends View implements OnGestureListener {
	/** paint 折线 */
	private Paint paint_line = new Paint();
	/** paint 内圆点 */
	private Paint paint_in = new Paint();
	/** paint 外圆点 */
	private Paint paint_ou = new Paint();
	/** paint 内圆点被点击后 */
	private Paint paint_in_ed = new Paint();
	/** paint 星期字 */
	private Paint paint_week = new Paint();
	/** paint 虚线字 */
	private Paint paint_xu_text = new Paint();
	/** paint 虚线 */
	private Paint paint_xu_line = new Paint();

	private Paint paint_bo_1 = new Paint();
	private Paint paint_bo_2 = new Paint();
	private Paint paint_bo_3 = new Paint();
	/** 被选择了第几个 */
	private int selected = -1;

	/** 画布宽 */
	private int fullWidth = 0;
	/** 画布高 */
	private int fullHeight = 0;
	/** 星期字之间的间隔 */
	private int oneWeekSpaceX = 0;

	/** 星期字的宽 */
	private float m_WeekWidth;
	/** 星期字的高 */
	private float m_WeekHeight;

	private float m_BL;
	/** 默认以10KM为最高点 */
	private float maxM = 5000;

	private ArrayList<Float> mils = new ArrayList<Float>(7);
	private ArrayList<PointF> points_count = new ArrayList<PointF>(7);
	private ArrayList<PointF> points_draw = new ArrayList<PointF>(7);
	private ArrayList<PointF> points_draw2 = new ArrayList<PointF>(7);
	private ArrayList<String> weeks = new ArrayList<String>(7);
	private ArrayList<String> days = new ArrayList<String>(7);

	public BXView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
		initData();
	}

	public BXView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
		initData();
	}

	/** 线颜色 */
	private int xblColor;
	/** 内圆圈颜色 */
	private int xInColor;
	/** 外圆圈颜色 */
	private int xOuColor;
	/** 内圆圈半径 */
	private int xInSize;
	/** 外圆圈半径 */
	private int xOuSize;

	/** 距离左侧距离 */
	private int xPaddingLeft;
	/** 距离右侧距离 */
	private int xPaddingRight;
	/** 距离底部字的距离 */
	private int xPaddingBottom;
	/** 距离顶部距离 */
	private int xPaddingTop;

	/** 下方字颜色 */
	private int xWeekTextColor;
	/** 下方字大小 */
	private int xWeekTextSize;
	/** 下方字距离 */
	private int xWeekBottom;
	/** 字间最小间距 */
	private int xWeekPadding;

	@SuppressLint("ClickableViewAccessibility")
	private void initView(Context context, AttributeSet attrs) {
		float density = context.getResources().getDisplayMetrics().density;
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		// 防止宽变化太多导致只绘制一半的问题
		fullWidth = context.getResources().getDisplayMetrics().widthPixels;

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BXView);

		xblColor = array.getColor(R.styleable.BXView_b_blColor, 0xFFFFE71C);
		xInColor = array.getColor(R.styleable.BXView_b_inColor, 0xFFFF9000);
		xOuColor = array.getColor(R.styleable.BXView_b_ouColor, Color.WHITE);

		xPaddingLeft = array.getDimensionPixelSize(R.styleable.BXView_b_paddingLeft, (int) (30 * density + 0.5f));
		xPaddingRight = array.getDimensionPixelSize(R.styleable.BXView_b_paddingRight, (int) (30 * density + 0.5f));
		xPaddingBottom = array.getDimensionPixelSize(R.styleable.BXView_b_paddingBottom, (int) (20 * density + 0.5f));
		xPaddingTop = (int) (30 * density + 0.5f);

		xInSize = array.getDimensionPixelSize(R.styleable.BXView_b_inSize, (int) (7 * density + 0.5f));
		xOuSize = array.getDimensionPixelSize(R.styleable.BXView_b_ouSize, (int) (10 * density + 0.5f));

		xWeekTextColor = array.getColor(R.styleable.BXView_b_weekTextColor, 0xFFCD3C1A);
		xWeekTextSize = array.getDimensionPixelSize(R.styleable.BXView_b_weekTextSize, (int) (16 * scale + 0.5f));
		xWeekBottom = array.getDimensionPixelSize(R.styleable.BXView_b_weekBottom, (int) (5 * density + 0.5f));
		xWeekPadding = array.getDimensionPixelSize(R.styleable.BXView_b_weekPadding, (int) (10 * density + 0.5f));

		array.recycle();

		paint_line.setColor(xblColor);
		paint_in.setColor(xInColor);
		paint_in_ed.setColor(0xFFFFFFFF);
		paint_ou.setColor(xOuColor);
		paint_in.setAntiAlias(true);
		paint_in_ed.setAntiAlias(true);
		paint_ou.setAntiAlias(true);

		paint_week.setColor(xWeekTextColor);
		paint_week.setTextSize(xWeekTextSize);
		paint_week.setTextAlign(Paint.Align.CENTER);

		paint_xu_line = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_xu_line.setStyle(Paint.Style.STROKE);
		paint_xu_line.setStrokeWidth(4);
		paint_xu_line.setColor(0xFFC16A5F);
		// 虚线
		// PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5}, 1);
		// paint_xu_line.setPathEffect(effects);

		paint_xu_text.setColor(0x7FFFFFFF);
		paint_xu_text.setTextSize(xWeekTextSize);
		if (!BuildConfig.DEBUG) {
			Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/HATTEN.TTF");
			paint_xu_text.setTypeface(font);
		}

		{
			paint_bo_1.setAntiAlias(true);// 抗锯齿
			paint_bo_1.setDither(true);// 图像抖动
			paint_bo_1.setColor(0x66f9f3f1);
			// paint_bo_1.setShadowLayer(20,5,2,Color.RED);// 设置阴影层0x44b0c2d4
			paint_bo_1.setStyle(Paint.Style.FILL);
			paint_bo_1.setStrokeJoin(Paint.Join.ROUND);
			paint_bo_1.setStrokeCap(Paint.Cap.ROUND);
			paint_bo_1.setStrokeWidth(5);
			// -----------------------------------------
			paint_bo_2.setAntiAlias(true);// 抗锯齿
			paint_bo_2.setDither(true);// 图像抖动
			paint_bo_2.setColor(0xFF962227);
			paint_bo_2.setStyle(Paint.Style.STROKE);
			paint_bo_2.setStrokeJoin(Paint.Join.ROUND);
			paint_bo_2.setStrokeCap(Paint.Cap.ROUND);
			paint_bo_2.setStrokeWidth(5);
			// -----------------------------------------
			paint_bo_3.setAntiAlias(true);// 抗锯齿
			paint_bo_3.setDither(true);// 图像抖动
			paint_bo_3.setColor(0xFFFFFFFF);
			paint_bo_3.setStyle(Paint.Style.STROKE);
			paint_bo_3.setStrokeJoin(Paint.Join.ROUND);
			paint_bo_3.setStrokeCap(Paint.Cap.ROUND);
			paint_bo_3.setStrokeWidth(5);
		}

		mGesture = new GestureDetector(context, this);
	}

	private void initData() {
		initWeeks();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		fullHeight = getHeight();

		// 如果有线程动画，就停止
		if (run != null && run.isRun) {
			run.isRun = false;
		}

		doAll();
		if (onClickLis != null) {
			PointF pf = getSelectedPointF();
			onClickLis.onReflash(pf.x + mTranslateX, pf.y, fullWidth, fullHeight);
		}
	}

	/** 小屏幕偏移量 */
	private float mTranslateX = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		try {
			// canvas.save();
			drawXu(canvas);
			canvas.translate(mTranslateX, 0);
			drawBoLangs(canvas);
			drawWeek(canvas);
			drawSelect(canvas);
			// drawPoint(canvas);
			// canvas.restore();
			// drawXu(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private GestureDetector mGesture;

	@SuppressLint({ "ClickableViewAccessibility" })
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGesture.onTouchEvent(event);
	}

	private OnClick onClickLis;

	public void setOnClick(OnClick onClick) {
		this.onClickLis = onClick;
	}

	public interface OnClick {
		public void onClick(int index, float x, float y, float w, float h);

		public void onOthers(float x, float y);

		public void onLoaded(float x, float y, float w, float h);

		public void onReflash(float x, float y, float w, float h);
	}

	public float getSelectedValue() {
		int count = mils.size();
		if (selected >= count || selected < 0)
			selected = 0;
		return mils.get(selected);
	}

	public PointF getSelectedPointF() {
		int count = points_draw.size();
		if (selected >= count || selected < 0) {
			selected = points_draw.size() - 1;
		}
		postInvalidate();
		PointF f = points_draw.get(selected);
		return new PointF(f.x, fullHeight - f.y);
	}

	public String getSelectedText() {
		int count = days.size();
		if (selected >= count || selected < 0)
			return "";
		return days.get(selected);
	}

	private ArrayList<PointF> points_ago = new ArrayList<PointF>();
	private ArrayList<PointF> points_new = new ArrayList<PointF>();

	public void setMils(ArrayList<Float> ms) {
		if (run != null && run.isRun) {
			run.isRun = false;
		}

		int len = ms.size();
		for (int i = 0; i < len; i++) {
			mils.set(i, ms.get(i));
		}

		copyPoint(points_draw, points_ago);
		doAll();
		copyPoint(points_count, points_new);

		run = new runnable();
		run.start();
	}

	public static SimpleDateFormat sdf = new SimpleDateFormat("MM.dd", Locale.CHINA);

	@SuppressLint("NewApi")
	public void setSeven(ArrayList<SevenDaysResponse> ms) {
		if (ms == null || ms.size() == 0)
			return;

		// 动画在执行中，又有新数据传入，造成数据不符的问题
		if (run != null && run.isRun) {
			run.isRun = false;
		}

		// 设置数据
		Calendar cal = Calendar.getInstance();
		int len = ms.size();
		for (int i = 0; i < len; i++) {
			SevenDaysResponse bean = ms.get(i);
			cal.setTimeInMillis(bean.getTime());
			weeks.set(i, sdf.format(cal.getTime()));
			mils.set(i, (float) bean.getValid_distances());
			days.set(i, getWeek(cal.get(Calendar.DAY_OF_WEEK)));
		}

		// 计算动画相关数据，播放动画
		copyPoint(points_draw, points_ago);
		doAll();
		mTranslateX = -maxTransX;
		copyPoint(points_count, points_new);

		run = new runnable();
		run.start();
	}

	public void copyPoint(ArrayList<PointF> a1, ArrayList<PointF> to1) {
		int len = a1.size();
		for (int i = 0; i < len; i++) {
			PointF pf = to1.get(i);
			PointF p = a1.get(i);
			pf.x = p.x;
			pf.y = p.y;
		}
	}

	public void doOverPoint(float index) {
		int len = points_draw.size();

		for (int i = 0; i < len; i++) {
			PointF p = points_draw.get(i);
			PointF p1 = points_ago.get(i);
			PointF p2 = points_new.get(i);
			p.x = p1.x + (p2.x - p1.x) * index;
			p.y = p1.y + (p2.y - p1.y) * index;
		}
	}

	private runnable run;

	/**
	 * 防止 动画没结束 就又开始新动画
	 */
	class runnable extends Thread {
		public boolean isRun = true;
		float count = 0.05f;

		@Override
		public void run() {
			try {
				while (isRun && count < 1.05f) {
					doOverPoint(count);
					postInvalidate();
					count += 0.05f;

					Thread.sleep(20);
				}
				if (null != onClickLis) {
					((Activity) getContext()).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							PointF pf = getSelectedPointF();
							onClickLis.onLoaded(pf.x + mTranslateX, pf.y, fullWidth, fullHeight);
						}
					});

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// =======================================================
	/**
	 * 折线各点位置计算
	 */
	public void doAll() {
		try {
			doWeeks();

			doPoints();
			copyPoint(points_count, points_draw);

			doXu(m_BL);

			doShade();

			LinearGradient lg = new LinearGradient(0, sY, 0, eY, 0xFFEDFA1B, Color.WHITE, Shader.TileMode.CLAMP);
			paint_bo_3.setShader(lg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String tempStr = "TEMP";
	private float aw = 0;
	private float maxTransX = 0;

	/**
	 * 计算字宽高
	 */
	private void doWeeks() {
		if (weeks == null || weeks.size() == 0)
			return;
		for (String text : weeks) {
			Rect bounds = new Rect();
			paint_week.getTextBounds(text, 0, text.length(), bounds);
			float w = paint_week.measureText(text, 0, text.length());
			float h = bounds.height();
			if (w > m_WeekWidth)
				m_WeekWidth = w;
			if (h > m_WeekHeight)
				m_WeekHeight = h;
		}
		int aSize = weeks.size();
		int bSize = aSize - 1;
		oneWeekSpaceX = (fullWidth - xPaddingLeft - xPaddingRight) / bSize;

		aw = aSize * m_WeekWidth + xPaddingLeft + xPaddingRight + bSize * xWeekPadding;

		if (aw < fullWidth) {
			aw = fullWidth;
			xWeekPadding = (int) ((aw - (aSize * m_WeekWidth + xPaddingLeft + xPaddingRight)) / bSize);
		}

		oneWeekSpaceX = (int) (xWeekPadding + m_WeekWidth);

		maxTransX = aw - fullWidth;

	}

	private void doPoints() {
		if (mils == null || mils.size() == 0)
			return;
		// ----------计算峰值-------------
		maxM = 10000;
		for (float m : mils) {
			if (m > maxM) {
				maxM = m;
			}
		}
		// ----------计算点-------------
		int len = mils.size();
		// 像素／单位
		m_BL = (fullHeight - xOuSize - xPaddingBottom - m_WeekHeight - xPaddingTop) / maxM;

		for (int i = 0; i < len; i++) {
			float x = i * oneWeekSpaceX + xPaddingLeft + m_WeekWidth / 2;
			float y = fullHeight - xPaddingBottom - m_WeekHeight - (mils.get(i) * m_BL);
			PointF pf = points_count.get(i);
			pf.x = x;
			pf.y = y;
		}
	}

	/** 虚线字，高偏移量 */
	private int sH = 0;
	/** 虚线右移偏移量 */
	private float sX = 0;
	/** 虚线最低Y坐标 */
	private float sY = 0;
	/** 虚线最高Y坐标 */
	private float eY = 0;

	/**
	 * 初始化 虚线 虚线字 相关参数
	 * 
	 * @param bl
	 *            高度比
	 */
	private void doXu(float bl) {
		String txt_min = "0 KM";
		String txt_max = maxM / 1000 + " KM";
		Rect bounds_min = new Rect();
		paint_xu_text.getTextBounds(txt_min, 0, txt_min.length(), bounds_min);
		Rect bounds_max = new Rect();
		paint_xu_text.getTextBounds(txt_max, 0, txt_max.length(), bounds_max);

		int maxW = bounds_min.width();
		if (bounds_max.width() > bounds_min.width()) {
			maxW = bounds_max.width();
		}
		sH = bounds_max.height() / 3;

		sX = 10 + maxW + 10;
		sY = (fullHeight - (0 * bl) - xPaddingBottom - m_WeekHeight);
		eY = (fullHeight - (maxM * bl) - xPaddingBottom - m_WeekHeight);
	}

	/**
	 * 波浪图，阴影计算
	 */
	private void doShade() {
		copyPoint(points_count, points_draw2);
		for (PointF p : points_draw2) {
			p.y += 20;
		}
	}

	// =======================================================
	/**
	 * 初始化周横幅
	 */
	public void initWeeks() {
		weeks.clear();
		days.clear();
		Calendar now = Calendar.getInstance();

		weeks.add(getMonthDay(now, -6));
		days.add(getWeek(now));
		weeks.add(getMonthDay(now, 1));
		days.add(getWeek(now));
		weeks.add(getMonthDay(now, 1));
		days.add(getWeek(now));
		weeks.add(getMonthDay(now, 1));
		days.add(getWeek(now));
		weeks.add(getMonthDay(now, 1));
		days.add(getWeek(now));
		weeks.add(getMonthDay(now, 1));
		days.add(getWeek(now));
		weeks.add(getMonthDay(now, 1));
		days.add(getWeek(now));

		mils.clear();
		if (BuildConfig.DEBUG) {
			mils.add(10000.0f);
			mils.add(0.0f);
			mils.add(5000.0f);
			mils.add(20000.0f);
			mils.add(15000.0f);
			mils.add(10000.0f);
			mils.add(10000.0f);
		} else {
			mils.add(0.0f);
			mils.add(0.0f);
			mils.add(0.0f);
			mils.add(0.0f);
			mils.add(0.0f);
			mils.add(0.0f);
			mils.add(0.0f);
		}
		selected = 6;

		points_count.clear();
		points_count.add(new PointF(0, 0));
		points_count.add(new PointF(0, 0));
		points_count.add(new PointF(0, 0));
		points_count.add(new PointF(0, 0));
		points_count.add(new PointF(0, 0));
		points_count.add(new PointF(0, 0));
		points_count.add(new PointF(0, 0));

		points_draw.clear();
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));
		points_draw.add(new PointF(0, 0));

		points_ago.clear();
		points_ago.add(new PointF(0, 0));
		points_ago.add(new PointF(0, 0));
		points_ago.add(new PointF(0, 0));
		points_ago.add(new PointF(0, 0));
		points_ago.add(new PointF(0, 0));
		points_ago.add(new PointF(0, 0));
		points_ago.add(new PointF(0, 0));

		points_new.clear();
		points_new.add(new PointF(0, 0));
		points_new.add(new PointF(0, 0));
		points_new.add(new PointF(0, 0));
		points_new.add(new PointF(0, 0));
		points_new.add(new PointF(0, 0));
		points_new.add(new PointF(0, 0));
		points_new.add(new PointF(0, 0));

		points_draw2.clear();
		points_draw2.add(new PointF(0, 0));
		points_draw2.add(new PointF(0, 0));
		points_draw2.add(new PointF(0, 0));
		points_draw2.add(new PointF(0, 0));
		points_draw2.add(new PointF(0, 0));
		points_draw2.add(new PointF(0, 0));
		points_draw2.add(new PointF(0, 0));
	}

	private static String getMonthDay(Calendar cal, int value) {
		cal.add(Calendar.DAY_OF_MONTH, value);
		return sdf.format(cal.getTime());
	}

	public static String getWeek(Calendar cal) {
		return getWeek(cal.get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * 根据数据返回周几
	 * 
	 * @param week
	 *            1 周日，依次类推
	 * @return
	 */
	public static String getWeek(int week) {
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

	/**
	 * 绘制折线
	 */
	private void drawZeXian(ArrayList<PointF> points, Canvas canvas) {
		// ----------绘制折线-------------
		int count = points_draw.size();
		float temp = fullHeight - xPaddingBottom - m_WeekHeight;
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
	}

	/**
	 * 计算贝塞尔曲线 二次
	 * 
	 * @param mX
	 * @param mY
	 * @param x
	 * @param y
	 * @param path
	 */
	private void doAB2(float mX, float mY, float x, float y, Path path) {
		float wt = (mX + x) / 2;
		PointF p1 = new PointF();
		PointF p2 = new PointF();
		p1.x = wt;
		p1.y = mY;
		p2.x = wt;
		p2.y = y;

		path.cubicTo(p1.x, p1.y, p2.x, p2.y, x, y);
	}

	private Path mPath = new Path();

	/**
	 * 绘制波浪线
	 * 
	 * @param points
	 * @param canvas
	 * @param mPaint
	 */
	private void drawBoLang(ArrayList<PointF> points, Canvas canvas, Paint mPaint) {
		if (points == null || points.size() == 0)
			return;
		mPath.reset();

		int len = points.size() - 1;
		for (int i = 0; i <= len; i++) {
			PointF p1 = points.get(i);
			if (i == 0) {
				mPath.moveTo(-50, fullHeight);
				mPath.lineTo(-50, p1.y);// fullHeight - xPaddingBottom - m_WeekHeight

				mPath.lineTo(p1.x, p1.y);

				PointF p = points.get(i + 1);
				doAB2(p1.x, p1.y, p.x, p.y, mPath);
			} else if (i == len) {
				mPath.lineTo(p1.x, p1.y);

				mPath.lineTo(aw + 50, p1.y);
				mPath.lineTo(aw + 50, fullHeight);
			} else {
				PointF p = points.get(i + 1);
				doAB2(p1.x, p1.y, p.x, p.y, mPath);
			}
		}
		canvas.drawPath(mPath, mPaint);
	}

	/**
	 * 绘制波浪线
	 * 
	 * @param canvas
	 */
	private void drawBoLangs(Canvas canvas) {
		if (points_draw == null || points_draw.size() == 0)
			return;
		// drawBoLang(points_draw, canvas,paint_bo_1);
		drawBoLang(points_draw2, canvas, paint_bo_2);
		drawBoLang(points_draw, canvas, paint_bo_3);

	}

	/**
	 * 绘制日期
	 * 
	 * @param canvas
	 */
	private void drawWeek(Canvas canvas) {
		int len = weeks.size();
		int len2 = points_draw.size();
		if (len != len2)
			return;
		for (int i = 0; i < len; i++) {
			PointF pf = points_draw.get(i);
			canvas.drawText(weeks.get(i), pf.x, fullHeight - xWeekBottom, paint_week);
		}
	}

	/**
	 * 绘制圆点
	 * 
	 * @param canvas
	 */
	private void drawPoint(Canvas canvas) {
		if (points_draw == null || points_draw.size() == 0)
			return;

		int count = points_draw.size();
		for (int i = 0; i < count; i++) {
			PointF pf = points_draw.get(i);
			canvas.drawCircle(pf.x, pf.y, xOuSize, paint_ou);
			if (selected == i)
				canvas.drawCircle(pf.x, pf.y, xInSize, paint_in_ed);
			else
				canvas.drawCircle(pf.x, pf.y, xInSize, paint_in);
		}
	}

	private void drawXu(Canvas canvas) {
		// 0KM
		canvas.drawText("0 KM", 10, sY + sH, paint_xu_text);

		Path p1 = new Path();
		p1.moveTo(sX, sY);
		p1.lineTo(fullWidth, sY);
		canvas.drawPath(p1, paint_xu_line);
		// maxKM
		canvas.drawText((int) maxM / 1000 + " KM", 10, eY + sH, paint_xu_text);

		Path p2 = new Path();
		p2.moveTo(sX, eY);
		p2.lineTo(fullWidth, eY);
		canvas.drawPath(p2, paint_xu_line);
		// centerKM
		float center = (sY + eY) / 2;
		canvas.drawText((int) maxM / 2000 + " KM", 10, center + sH, paint_xu_text);

		Path p3 = new Path();
		p3.moveTo(sX, center);
		p3.lineTo(fullWidth, center);
		canvas.drawPath(p3, paint_xu_line);
	}

	/**
	 * 绘制选择处
	 * 
	 * @param canvas
	 */
	private void drawSelect(Canvas canvas) {
		if (selected < 0 || selected > points_draw.size() - 1) {
			return;
		}
		// =============================
		Paint paint = new Paint();
		paint.setColor(xWeekTextColor);
		paint.setTextSize(xWeekTextSize);
		paint.setTextAlign(Paint.Align.CENTER);

		Paint pl = new Paint(Paint.ANTI_ALIAS_FLAG);
		pl.setStyle(Paint.Style.STROKE);
		pl.setStrokeWidth(5);
		pl.setColor(0xFFC16A5F);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		pl.setAntiAlias(true);
		pl.setPathEffect(effects);
		Path path = new Path();
		// =============================
		PointF pf = points_draw.get(selected);
		String value = getSelectedValue() + " KM";

		Rect bounds = new Rect();
		paint.getTextBounds(value, 0, value.length(), bounds);
		float w = paint.measureText(value, 0, value.length());
		float h = bounds.height();
		float off = h * 3;
		if (pf.y - off < h) {
			off = -h + pf.y;
		}
		path.moveTo(pf.x, fullHeight - xWeekBottom - m_WeekHeight - 5);
		path.lineTo(pf.x, pf.y - off + 10);
		canvas.drawPath(path, pl);
		canvas.drawText(value, pf.x, pf.y - off, paint);

		canvas.drawCircle(pf.x, pf.y, xOuSize, paint_ou);
		canvas.drawCircle(pf.x, pf.y, xInSize, paint_in_ed);
	}

	@Override
	public boolean onDown(MotionEvent event) {
		float x = event.getX() - mTranslateX;
		float y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int count = points_draw.size();
			float two = xOuSize << 1;
			for (int i = 0; i < count; i++) {
				PointF pf = points_draw.get(i);
				if (x > pf.x - two && x < pf.x + two && y > pf.y - two && y < pf.y + two) {
					if (onClickLis != null) {
						selected = i;
						postInvalidate();

						onClickLis.onClick(i, pf.x + mTranslateX, fullHeight - pf.y, fullWidth, fullHeight);
						return true;
					}
				}
			}
		}
		if (onClickLis != null) {
			onClickLis.onOthers(x, fullHeight - y);
			return true;
		}
		Log.i("TOUCH", "onDown");
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.i("TOUCH", "onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.i("TOUCH", "onSingleTapUp");
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		Log.i("TOUCH", "onScroll-" + mTranslateX);
		float temp = mTranslateX;
		temp += -distanceX;
		if (temp > 0) {
			temp = 0;
		} else if (temp < -maxTransX) {
			temp = -maxTransX;
		}
		if (temp == mTranslateX) {
			return true;
		}
		mTranslateX = temp;

		invalidate();
		if (onClickLis != null) {
			PointF pf = getSelectedPointF();
			onClickLis.onReflash(pf.x + mTranslateX, pf.y, fullWidth, fullHeight);
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.i("TOUCH", "onLongPress");
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		Log.i("TOUCH", "onFling");
		return true;
	}
}
