package com.axter.example.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axter.example.pid.PackageComparable;

public class TestListAppsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new EfficientAdapter(this, getData()));

		getListView().setTextFilterEnabled(true);
	}

	public List<PackageItem> getData() {
		List<PackageItem> items = new ArrayList<PackageItem>();
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = this.getPackageManager();
		/** 获取手机内所有应用 */
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			/** 判断是否为非系统预装的应用 (大于0为系统预装应用，小于等于0为非系统应用) */
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				apps.add(pak);
			}
		}
		Collections.sort(apps,new PackageComparable());
		for(PackageInfo app:apps){
			PackageItem item = new PackageItem();
			item.text = app.packageName;
			item.img = pManager.getApplicationIcon(app.applicationInfo);
			// 这一步必须要做,否则不会显示.
			item.img.setBounds(0, 0, 80, 80);
			items.add(item);
		}
		return items;
	}
	static class PackageItem {
        String text;
        Drawable img;
    }
	private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<PackageItem> mPaklist;
        public EfficientAdapter(Context context,List<PackageItem> paklist) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
            mPaklist = paklist;
        }

        /**
         * The number of items in the list is determined by the number of speeches
         * in our array.
         *
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return mPaklist.size();
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(android.R.id.text1);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }
            PackageItem item = mPaklist.get(position);
            // Bind the data efficiently with the holder.
            holder.text.setText(item.text);
            holder.text.setCompoundDrawables(null, null, item.img, null);
            //holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);

            return convertView;
        }

        static class ViewHolder {
            TextView text;
        }
    }
}
