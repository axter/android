package com.axter.example.customlistview;

import java.util.ArrayList;
import java.util.List;

import com.axter.example.R;
import com.axter.example.customlistview.MyListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi") public class MainActivity extends Activity {

	private List<String> data;
	private BaseAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		data = new ArrayList<String>();
		for(int i=0;i<100;i++){
			data.add("a"+i);
		}
		final MyListView listView = (MyListView) findViewById(R.id.listView);
		listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText(data.get(position));
				return tv;
			}

			public long getItemId(int position) {
				return 0;
			}

			public Object getItem(int position) {
				return null;
			}

			public int getCount() {
				return data.size();
			}
		};
		listView.setAdapter(adapter);

		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						data.add("aaabbbb");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}

				}.execute();
			}
		});
	}
}