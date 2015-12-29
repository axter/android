package com.axter.example.pid;

import java.util.Comparator;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;

public class ProcessComparable implements Comparator<ActivityManager.RunningAppProcessInfo> {

	@Override
	public int compare(ActivityManager.RunningAppProcessInfo lhs, ActivityManager.RunningAppProcessInfo rhs) {
		return compare(lhs.pid, rhs.pid);
	}
	public static int compare(int lhs, int rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }
}