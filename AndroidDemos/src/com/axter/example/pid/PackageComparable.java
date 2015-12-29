package com.axter.example.pid;

import java.util.Comparator;

import android.content.pm.PackageInfo;

public class PackageComparable implements Comparator<PackageInfo> {

	@Override
	public int compare(PackageInfo lhs, PackageInfo rhs) {
		return lhs.packageName.compareTo(rhs.packageName);
	}
	
}