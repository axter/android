package com.axter.example.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.axter.libs.utils.base.DensityUtils;
import com.axter.libs.utils.base.ScreenUtils;

import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class InfoActivity extends ListActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setListAdapter(new SimpleAdapter(this, getData(), android.R.layout.simple_list_item_1,
        new String[] {"name"}, new int[] {R.id.text1}));

    getListView().setTextFilterEnabled(true);
  }

  public List<Map<String, String>> getData() {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    list.add(getValue("[1dp=px] " + DensityUtils.dp2px(this, 1)));
    list.add(getValue("[分辨率] " + ScreenUtils.getScreenWidth(this) + "x"
        + ScreenUtils.getScreenHeight(this)));
    list.add(getValue("[状态栏高度] " + ScreenUtils.getStatusHeight(this)));
    list.add(getValue("[density] " + getResources().getDisplayMetrics().density));
    list.add(getValue("[densityDpi] " + getResources().getDisplayMetrics().densityDpi));
    list.add(getValue("[xdpi] " + getResources().getDisplayMetrics().xdpi));
    list.add(getValue("[ydpi] " + getResources().getDisplayMetrics().ydpi));
    return list;
  }

  public HashMap<String, String> getValue(String value) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", value);
    return map;
  }
}
