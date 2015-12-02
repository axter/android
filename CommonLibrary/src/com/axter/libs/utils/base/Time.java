package com.axter.libs.utils.base;

/**
 * Created by zhaobo on 2015/11/25.
 */
public class Time {
	private static long time = System.currentTimeMillis();;

	public static void print(String str) {
		long temp = System.currentTimeMillis();

		long cha = temp - time;
		L.i("cha", str + "|" + cha);

		time = temp;
	}
}
