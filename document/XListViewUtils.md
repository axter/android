### 说明
工具类是借用一下方法操作
* getList() 当前获取的数据
* getPerpage() 每页显示数
* getCount() 总数

### 声明
```java
private XListView listView;
// 适配器,继承自CommonXListAdapter<>
private ShanPaoZiXunListAdapter adapter = new ShanPaoZiXunListAdapter();
// XListView工具类
private XListViewUtils xListViewUtils = new XListViewUtils();
```

### 初始化
```java
private void initListView() {
	// 1,获取XListView对象
	listView = (XListView) layout_view.findViewById(R.id.listview);
	// 2,初始化adapter
	adapter.init(context);
	// 3,初始化XListView
	xListViewUtils.initList(listView, adapter, new XListViewUtils.CallBack() {
		@Override
		public void onRefresh(int page) {
			// 上拉刷新执行
			getShanPaoZiXunList(page);
		}

		@Override
		public void onLoadMore(int page) {
			// 下拉加载执行
			getShanPaoZiXunList(page);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ShanPaoZiXunItemBean bean = adapter.getItem(position);
			// 点击事件监听
		}

		@Override
		public void onRefreshNoData() {
			// 上拉刷新,无数据,可以显示空数据页
		}

		@Override
		public void onShowData() {
			// 加载到数据了,可以隐藏空数据页
		}
	});
}
```

### 获取数据
```java
private void getShanPaoZiXunList(int page) {
	ShanPaoZiXunListRequest request = new ShanPaoZiXunListRequest();
	request.setPage(page);
	request.setUser_id(ShanPaoApplication.sUserInfo.getUser_id());
	request.setUser_token(ShanPaoApplication.sUserInfo.getUser_token());
	request.setConsult_cate_id(consult_cate_id);
	Request.post(context, request, new ResCallBack<ShanPaoZiXunListResponse>() {
		@Override
		public void onFailure(int statusCode, String responseString, Throwable throwable) {
			// 关闭刷新动画
			xListViewUtils.stopXlist();
			ToastUtil.showShortToast(context,responseString);
		}

		@Override
		public void onSuccess(ShanPaoZiXunListResponse response, String responseString) {
			// 关闭刷新动画
			xListViewUtils.stopXlist();
			// 设置数据,response 应包含以下方法
			// getList() 当前获取的数据
			// getPerpage() 每页显示数
			// getCount() 总数
			xListViewUtils.setData(response);
		}

		@Override
		public void onErrCode(String code, String result) {
			// 关闭刷新动画
			xListViewUtils.stopXlist();
			Codes.Show(context,code);
		}
	});
}
```
