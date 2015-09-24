package com.axter.libs.choosepics;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.axter.libs.choosepics.R;
import com.axter.libs.utils.BitmapCache;

public class MainActivity extends Activity {

	public static final int CHOSE_PIC = 1;// 选择图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn = (Button) findViewById(R.id.btn);
		// 初始化 imageloader 放在Application 中
		BitmapCache.init(this, null);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jump();
			}
		});

	}

	private void jump() {
		Intent it = new Intent(this, ImgDirsActivity.class);
		it.putExtra("maxcount", 6);// -1把最后一个减去
		startActivityForResult(it, CHOSE_PIC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CHOSE_PIC && resultCode == RESULT_OK) {
			ArrayList<String> files = data.getStringArrayListExtra("imgs");
			if (files == null) {
				Toast.makeText(this, "sm = null", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "sm = " + files.size(), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
