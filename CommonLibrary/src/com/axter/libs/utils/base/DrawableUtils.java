package com.axter.libs.utils.base;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by zhaobo on 2015/10/27.
 */
public class DrawableUtils {
	public static Drawable getDrawable(Context context, int resid) {
		Drawable drawable = context.getResources().getDrawable(resid);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		return drawable;
	}
}
