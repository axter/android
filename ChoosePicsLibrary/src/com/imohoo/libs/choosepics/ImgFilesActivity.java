package com.imohoo.libs.choosepics;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.imohoo.libs.R;
import com.imohoo.libs.choosepics.ImgFilesAdapter.OnItemClickClass;

/**
 * <p>选择图片页面 </p>
 * <li>maxcount 选择图片最大数(默认1)</li> 
 * <li>imgs 图片列表地址 (必传)</li>
 * <li>回传数据 imgs 选择的图片地址</li>
 * @author zhaobo
 * 
 */
public class ImgFilesActivity extends Activity implements View.OnClickListener {
	private TextView photo_count;
	private int maxcount;
	private ArrayList<String> list_imgs;
	private GridView photo_gridview;
	private ImgFilesAdapter photo_adapter;
	/** 选择的文件地址列表 */
	private ArrayList<String> selected_imgs = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_listimgs);
		findViewById(R.id.photo_back).setOnClickListener(this);
		findViewById(R.id.photo_send).setOnClickListener(this);
		photo_count = (TextView) findViewById(R.id.photo_count);
		photo_gridview = (GridView) findViewById(R.id.photo_gridview);

		// 外部数据
		maxcount = getIntent().getIntExtra("maxcount", 1);
		list_imgs = getIntent().getExtras().getStringArrayList("imgs");
		// 设置界面
		photo_count.setText(getString(R.string.photo_select_imgs_text, 0, maxcount));

		photo_adapter = new ImgFilesAdapter(this, list_imgs, onitemclick);
		photo_gridview.setAdapter(photo_adapter);
	}

	ImgFilesAdapter.OnItemClickClass onitemclick = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int position, CheckBox checkBox) {
			String filapath = list_imgs.get(position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				selected_imgs.remove(filapath);
				photo_count.setText(getString(R.string.photo_select_imgs_text, selected_imgs.size(), maxcount));
			} else {
				if (selected_imgs.size() == maxcount) {
					Toast.makeText(ImgFilesActivity.this, R.string.photo_imgs_overtop, Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					checkBox.setChecked(true);
					Log.i("img", "img choise position->" + position);
					selected_imgs.add(filapath);
					photo_count.setText(getString(R.string.photo_select_imgs_text, selected_imgs.size(), maxcount));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.photo_back){
			finish();
		}else if(id == R.id.photo_send){
			Intent intent = new Intent();
			intent.putStringArrayListExtra("imgs", selected_imgs);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
