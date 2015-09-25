package com.axter.example.multipletextview;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.axter.example.R;

public class TestMultipleTextView2 extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_test_multitextview2);
		
		MultipleTextView2 mtv = (MultipleTextView2)findViewById(R.id.mtv);
		
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0;i<100;i++){
			list.add("你好"+i);
		}
		long begin = System.currentTimeMillis();
		mtv.setTextViews(list);
		long end = System.currentTimeMillis();
		
		Log.i("花费时间2",end-begin +"");
	}
	
}
