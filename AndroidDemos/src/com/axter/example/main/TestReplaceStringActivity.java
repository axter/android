package com.axter.example.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.axter.example.R;

public class TestReplaceStringActivity extends Activity{

	EditText et_str_1;
	EditText et_num_1;
	Button btn_do_1;
	TextView tv_time_1;
	
	EditText et_str_2;
	EditText et_num_2;
	Button btn_do_2;
	TextView tv_time_2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_replacestring);
		
		et_str_1 = (EditText)findViewById(R.id.et_str_1);
		et_num_1 = (EditText)findViewById(R.id.et_num_1);
		btn_do_1 = (Button)findViewById(R.id.btn_do_1);
		tv_time_1 = (TextView)findViewById(R.id.tv_time_1);
		btn_do_1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				do1();
			}
		});
		
		
		et_str_2 = (EditText)findViewById(R.id.et_str_2);
		et_num_2 = (EditText)findViewById(R.id.et_num_2);
		btn_do_2 = (Button)findViewById(R.id.btn_do_2);
		tv_time_2 = (TextView)findViewById(R.id.tv_time_2);
		btn_do_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				do2();
			}
		});
	}
	
	public void do1(){
		String str = et_str_1.getText().toString();
		String numStr =et_num_1.getText().toString();
		int num = Integer.parseInt(numStr);
		long time_begin = System.currentTimeMillis();
		for(int i=0;i<num;i++){
			str.replace("%d", numStr);
		}
		long time_end = System.currentTimeMillis();
		tv_time_1.setText(time_end - time_begin + "");
	}
	
	public void do2(){
		String str = et_str_2.getText().toString();
		int num = Integer.parseInt(et_num_2.getText().toString());
		long time_begin = System.currentTimeMillis();
		for(int i=0;i<num;i++){
			String.format(str, num);
		}
		long time_end = System.currentTimeMillis();
		tv_time_2.setText(time_end - time_begin + "");
	}
}
