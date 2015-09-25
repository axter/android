package com.axter.example.main;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 自定义textview frist
 * @author sy
 *
 */
public class CustomFontTextView extends TextView
{

	public CustomFontTextView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	public CustomFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		setText(android.os.Build.MODEL);
		if(!android.os.Build.MODEL.contains("sdk")){			
			AssetManager assetManager=context.getAssets();
			Typeface font=Typeface.createFromAsset(assetManager, "fonts/HATTEN.TTF");
			setTypeface(font);
		}
	}
	
}
