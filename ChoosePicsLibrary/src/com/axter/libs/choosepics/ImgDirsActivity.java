package com.axter.libs.choosepics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.axter.libs.choosepics.R;

/**
 * <p>显示系统相册</p>
 * <li>maxcount 选择图片最大数(默认1)</li>
 * 
 * @author zhaobo
 * 
 */
public class ImgDirsActivity extends Activity implements OnItemClickListener, View.OnClickListener {
	private static final String filecount = "filecount";
	private static final String filename = "filename";
	private static final String fileimg = "fileimg";
	/** 选择图片 */
	private static final int CHOSE_PIC = 1;

	/** 可以选择的照片数量 */
	private int maxcount;
	private ListView photo_listview;
	private ImgDirsAdapter photo_adapter;
	/** key文件夹名,ArrayList&ltString&gt 图片地址 */
	private TreeMap<String, ArrayList<String>> all_pics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_listdir);
		// 可以多选的数量
		maxcount = this.getIntent().getIntExtra("maxcount", 1);

		findViewById(R.id.photo_cancel).setOnClickListener(this);
		photo_listview = (ListView) findViewById(R.id.photo_listview);

		// 获取本地图片文件集合
		all_pics = LocalImgFileList(this);

		// 将文件集合转换为需要的map数据
		List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
		for (Entry<String, ArrayList<String>> entry : all_pics.entrySet()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(filename, entry.getKey());
			map.put(filecount, entry.getValue().size() + getString(R.string.photo_piece));
			map.put(fileimg, entry.getValue().size() == 0 ? null : entry.getValue().get(0));
			listdata.add(map);
		}

		// 数据设置到列表上展示
		photo_adapter = new ImgDirsAdapter(this, listdata);
		photo_listview.setAdapter(photo_adapter);
		photo_listview.setOnItemClickListener(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		HashMap<String, String> value = (HashMap<String, String>) arg0.getAdapter().getItem(arg2);

		Intent intent = new Intent(this, ImgFilesActivity.class);
		intent.putExtra("maxcount", maxcount);
		intent.putExtra("imgs", all_pics.get(value.get("filename")));
		startActivityForResult(intent, CHOSE_PIC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CHOSE_PIC && resultCode == RESULT_OK) {
			ArrayList<String> files = data.getStringArrayListExtra("imgs");
			Intent intent = new Intent();
			intent.putStringArrayListExtra("imgs", files);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	public static TreeMap<String, ArrayList<String>> LocalImgFileList(Context context) {
		TreeMap<String, ArrayList<String>> maps = new TreeMap<String, ArrayList<String>>();
		// 获取所有图片文件路径集合
		List<String> allImageFile = listAlldir(context);
		if (allImageFile != null) {
			for (String imgFile : allImageFile) {
				// 获取文件夹名
				String parentFileName = getParentFileName(imgFile);
				// 先根据文件夹名filename创建一个ft对象
				if (maps.get(parentFileName) == null) {
					ArrayList<String> list = new ArrayList<String>();
					list.add(imgFile);
					maps.put(parentFileName, list);
				} else {
					List<String> list = maps.get(parentFileName);
					list.add(imgFile);
				}
			}
		}
		return maps;
	}

	public static ArrayList<String> listAlldir(Context context) {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Uri uri = intent.getData();
		ArrayList<String> list = new ArrayList<String>();
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);// managedQuery(uri, proj, null, null, null);
		while (cursor.moveToNext()) {
			String path = cursor.getString(0);
			list.add(new File(path).getAbsolutePath());
		}
		return list;
	}

	public static String getParentFileName(String data) {
		String filename[] = data.split("/");
		if (filename != null) {
			return filename[filename.length - 2];
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.photo_cancel){
			finish();
		}
	}
}
