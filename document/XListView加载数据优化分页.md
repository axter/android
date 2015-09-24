## XListView加载数据优化分页

### 定义常量
```java
private int page = 0;
private int perpage = 10;
private int action = 1000;
private final int refresh = 1000;
private final int loadmore = 1001;
```

### 初始化XListView
```java
private void initList() {
	listview = (XListView) findViewById(R.id.xlist);

	listview.setPullRefreshEnable(true);//下拉刷新是否有效
	listview.setPullLoadEnable(false);//上拉加载更多是否有效
	listview.stopLoadMore();
	listview.stopRefresh();
	listview.setXListViewListener(this);//下拉，加载监听
	listview.setOnItemClickListener(this);//点击监听
	adapter = new ShanPaoGroupMemberAdapter(this, list,this);
	listview.setAdapter(adapter);
}
```

### 监听下拉刷新，下拉加载更多
```java
@Override
public void onRefresh() {
	action = refresh;
	page = 0;
	getMyGoup();
}

@Override
public void onLoadMore() {
	action = loadmore;
	page++;
	getMyGoup();
}
```

### 设置数据
```java
private void setMyGroup(ShanPaoGroupMyGroupResponse bean) {
	//数据错误，没数据
	if (bean == null || bean.getList() == null || bean.getCount() == 0) {
		list.clear();
		adapter.notifyDataSetChanged();
		listview.setPullLoadEnable(false);
		return;
	}
	List<Mylist> tempList = bean.getList();
	int aCount = bean.getCount();
	int lCount = tempList.size();
	if (lCount + page * perpage == aCount) {// 数据全部加载完
		listview.setPullLoadEnable(false);
	}else{// 数据没加载完
		listview.setPullLoadEnable(true);
	}
	//=======只需根据情况修改一下代码========
	if (action == refresh) {
		list.clear();
		list.addAll(tempList);
		adapter.notifyDataSetChanged();
	} else if (action == loadmore) {
		list.addAll(tempList);
		adapter.notifyDataSetChanged();
	}
	//=======================================
}
```

### 点击事件
```java
@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	int position = arg2 - 2;
	if(list.size()==0 || position<0 || position>=list.size()){
		return;
	}
	//写代码
}

arg0.getAdapter();
```