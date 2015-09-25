package com.axter.example.testpopub;

import com.axter.example.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShowPopubActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout popupView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.testpopub, null);
		
		setContentView(popupView);
		
		{
			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			popupView.measure(w, h);
			int popupWidth = popupView.getMeasuredWidth();
			p(popupWidth);
		}
	}
	
	public void p(String str){
		Log.i("TA", "w = "+str);
	}
	public void p(long str){
		Log.i("TA", "w = "+String.valueOf(str));
	}
}
