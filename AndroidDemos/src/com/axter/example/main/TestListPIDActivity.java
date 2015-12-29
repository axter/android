package com.axter.example.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axter.example.pid.ProcessComparable;
import com.axter.example.pid.ProcessComparable2;

public class TestListPIDActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new EfficientAdapter(this, getData()));

		getListView().setTextFilterEnabled(true);
	}

	private String getAppName(int pID) {
		String processName = "";
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+ info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	public List<PackageItem> getData() {
		List<PackageItem> items = new ArrayList<PackageItem>();

		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
		
		Collections.sort(appProcessList,new ProcessComparable2());
		
		for (ActivityManager.RunningAppProcessInfo info : appProcessList) {
			PackageItem item = new PackageItem();
			item.pid = info.pid;
			item.process = info.processName;
			items.add(item);
		}

		return items;
	}

	static class PackageItem {
		String process;
		int pid;
	}

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<PackageItem> mPaklist;

		public EfficientAdapter(Context context, List<PackageItem> paklist) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			mPaklist = paklist;
		}

		/**
		 * The number of items in the list is determined by the number of speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return mPaklist.size();
		}

		/**
		 * Since the data comes from an array, just returning the index is sufficent to get at the data. If we were using a more complex data structure, we would return whatever object represents one row in the list.
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
		 * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
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
			holder.text.setText(item.pid + "\t" + item.process);
			// holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);

			return convertView;
		}

		static class ViewHolder {
			TextView text;
		}
	}
}
