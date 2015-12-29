package com.axter.example.pid;

import java.util.Comparator;

import android.app.ActivityManager;

public class ProcessComparable2 implements Comparator<ActivityManager.RunningAppProcessInfo> {

	@Override
	public int compare(ActivityManager.RunningAppProcessInfo lhs, ActivityManager.RunningAppProcessInfo rhs) {
		return lhs.processName.compareTo(rhs.processName);
	}
	
}