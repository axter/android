package com.axter.libs.utils;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.SoftReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * <p>使用注意</p>
 * <p>服务器double可能会返回""字串，所以处理要小心</p>
 *
 * @author zhaobo
 */
public class GsonTool {
	private static SoftReference<Gson> instance;

	public static Gson getInstance() {
		if (instance == null || instance.get() == null) {
			Gson mGson;
			if (Build.VERSION.SDK_INT >= 23) {
				GsonBuilder gsonBuilder = new GsonBuilder()
						.excludeFieldsWithModifiers(
								Modifier.FINAL,
								Modifier.TRANSIENT,
								Modifier.STATIC);
				mGson = gsonBuilder.create();
			} else {
				mGson = new Gson();
			}
			instance = new SoftReference(mGson);
		}
		return instance.get();
	}

	/**
	 * json -> 对象
	 *
	 * @param json json字符串
	 * @param cls  目标数据对象
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> cls) {
		try {
			return getInstance().fromJson(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object toObject(String result, Type mType) {
		try {
			return getInstance().fromJson(result, mType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toString(Object obj) {
		try {
			return getInstance().toJson(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}