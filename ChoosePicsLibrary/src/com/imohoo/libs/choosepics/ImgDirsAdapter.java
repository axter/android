package com.imohoo.libs.choosepics;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imohoo.libs.R;
import com.imohoo.libs.utils.BitmapCache;

public class ImgDirsAdapter extends BaseAdapter {
	private static final String filecount = "filecount";
	private static final String filename = "filename";
	private static final String fileimg = "fileimg";
	// 图片文件夹数据集合
	private List<HashMap<String, String>> list_dirs;
	private LayoutInflater inflater;

	public ImgDirsAdapter(Context context, List<HashMap<String, String>> listdata) {
		this.inflater = LayoutInflater.from(context);
		this.list_dirs = listdata;
	}

	@Override
	public int getCount() {
		return list_dirs.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list_dirs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.photo_listdir_item, null);
			holder.photo_fileimg = (ImageView) view.findViewById(R.id.photo_fileimg);
			holder.photo_filecount = (TextView) view.findViewById(R.id.photo_filecount);
			holder.photo_filename = (TextView) view.findViewById(R.id.photo_filename);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		// 文件夹名
		holder.photo_filename.setText(list_dirs.get(arg0).get(filename));
		// 图片数量
		holder.photo_filecount.setText("(" + list_dirs.get(arg0).get(filecount) + ")");
		// 显示相册封面图片
		String url = "file://" + list_dirs.get(arg0).get(fileimg);
		BitmapCache.display(url, holder.photo_fileimg, R.drawable.photo_imgbg);

		return view;
	}

	class Holder {
		public ImageView photo_fileimg;
		public TextView photo_filecount;
		public TextView photo_filename;
	}

}
