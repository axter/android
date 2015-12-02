package com.axter.libs.utils.base;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by zhaobo on 2015/11/24.
 */
public class SensorUtils {
	public SensorUtils reg(Context context) {
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		int sensorType = android.hardware.Sensor.TYPE_ACCELEROMETER;
		sm.registerListener(mySensorEventListener, sm.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_FASTEST);
		return this;
	}

	public void unreg(Context context) {
		SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sm.unregisterListener(mySensorEventListener);
	}

	public final SensorEventListener mySensorEventListener = new SensorEventListener() {
		long curTime, lastTime, duration, initTime;
		float shake, totalShake, last_x, last_y, last_z;

		@Override
		public void onAccuracyChanged(android.hardware.Sensor sensor,
		                              int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER) {

				// 获取加速度传感器的三个参数

				float x = event.values[SensorManager.DATA_X];

				float y = event.values[SensorManager.DATA_Y];

				float z = event.values[SensorManager.DATA_Z];

				// 获取当前时刻的毫秒数

				curTime = System.currentTimeMillis();

				// 100毫秒检测一次

				float aa = curTime - lastTime;

				if ((curTime - lastTime) > 100) {

					duration = (curTime - lastTime);

					// 看是不是刚开始晃动

					if (last_x == 0.0f && last_y == 0.0f && last_z == 0.0f) {

						// last_x、last_y、last_z同时为0时，表示刚刚开始记录

						initTime = System.currentTimeMillis();

					} else {

						// 单次晃动幅度

						shake = (Math.abs(x - last_x) + Math.abs(y - last_y) + Math
								.abs(z - last_z));
						// / duration * 1000;

					}

					// 把每次的晃动幅度相加，得到总体晃动幅度

					totalShake += shake;

					// 判断是否为摇动
					L.i("" + shake);
//					if (shake > 20) {
//						BitmapCache.show();
//					}
					last_x = x;

					last_y = y;

					last_z = z;

					lastTime = curTime;

				}

			}
		}
	};
}
