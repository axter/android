package com.axter.libs.choosepics;

import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.axter.libs.choosepics.R;
import com.axter.libs.utils.BitmapCache;

public class ImgFilesAdapter extends BaseAdapter {
	/** 图片文件绝对路径 */
	private List<String> list_imgs;
	private OnItemClickClass onitemclick;
	private LayoutInflater inflater;

	public ImgFilesAdapter(Context context, List<String> data, OnItemClickClass onItemClickClass) {
		this.inflater = LayoutInflater.from(context);
		this.list_imgs = data;
		// 倒叙
		Collections.reverse(this.list_imgs);
		this.onitemclick = onItemClickClass;
	}

	@Override
	public int getCount() {
		return list_imgs.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list_imgs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.photo_listimgs_item, null);
			holder = new ViewHolder();
			holder.photo_imageview = (ImageView) convertView.findViewById(R.id.photo_imageview);
			holder.photo_checkbox = (CheckBox) convertView.findViewById(R.id.photo_checkbox);
			convertView.setOnClickListener(photoclick);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 当前图片地址pos
		holder.position = position;
		// UIL框架加载本地图片
		String url = "file://" + list_imgs.get(position);
		BitmapCache.display(url, holder.photo_imageview, R.drawable.photo_imgbg);

		return convertView;
	}

	class ViewHolder {
		ImageView photo_imageview;
		CheckBox photo_checkbox;
		int position;
	}

	public interface OnItemClickClass {
		public void OnItemClick(View v, int position, CheckBox checkBox);
	}

	private OnPhotoClick photoclick = new OnPhotoClick();

	class OnPhotoClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			ViewHolder holder = (ViewHolder) v.getTag();
			if (list_imgs != null && onitemclick != null) {
				onitemclick.OnItemClick(v, holder.position, holder.photo_checkbox);
			}
		}
	}

}
