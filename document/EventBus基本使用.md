## EventBus基本使用
```java
//注册监听
@Override
protected void onCreate(Bundle savedInstanceState) {
	if(!EventBus.getDefault().isRegistered(this)){
		EventBus.getDefault().register(this);
	}
}
//注销监听
@Override
protected void onDestroy() {
	if(EventBus.getDefault().isRegistered(this)){
		EventBus.getDefault().unregister(this);
	}
	super.onDestroy();
}
```


```java
@Override
public void onAttach(Activity activity) {
	if(!EventBus.getDefault().isRegistered(this)){
		EventBus.getDefault().register(this);
	}
	super.onAttach(activity);
}

@Override
public void onDestroy() {
	if(EventBus.getDefault().isRegistered(this)){
		EventBus.getDefault().unregister(this);
	}
	super.onDestroy();
}
```


```java
//发送事件
EventBus.getDefault().post(new PostEvent(1,card));

//事件处理
public void onEventMainThread(PostEvent event) {
	
}
```