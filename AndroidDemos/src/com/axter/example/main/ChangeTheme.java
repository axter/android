package com.axter.example.main;

import com.axter.example.R;

import android.app.Activity;
import android.os.Bundle;

public class ChangeTheme extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Black);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
}
