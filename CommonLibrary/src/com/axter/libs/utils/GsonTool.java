package com.axter.libs.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;

/**
 * <p>
 * 使用注意
 * <p>
 * <p>
 * 服务器double可能会返回""字串，所以处理要小心
 * </p>
 * 
 * @author zhaobo
 * 
 */
public class GsonTool {
	/**
	 * json -> 对象
	 * 
	 * @param json
	 *            json字符串
	 * @param cls
	 *            目标数据对象
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> cls) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toString(Object obj) {
		try {
			Gson gson = new Gson();
			return gson.toJson(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Object toObject(String result, Type mType) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(result, mType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}