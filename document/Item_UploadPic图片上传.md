## 图片上传Item_UploadPic
> 初始化

```java
uploadPic = new Item_UploadPic(this,new Item_UploadPic.UploadPicCallBack() {
	@Override
	public void uploaded(String data) {
		ToastUtil.showShortToast(context, "提交成功！");
		//拷贝本地文件至缓存
		BitmapCache.displayCache(img_src, uploadPic.getImgPath(), iv_my_bg);
	}

	@Override
	public void uploading(String path) {
		//显示本地图片
		BitmapCache.displayLocale(path, iv_groupicon);
	}

	});
	// 设置启动类Activity 或 Fragment
	uploadPic.setActivity(this);
}
```

> onActivityResult回调

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (uploadPic != null)
		uploadPic.onActivityResult(requestCode, resultCode, data);
}
```

> 显示

```java
uploadPic.showUpload();

```

> 上传

```java
//参数对象,文件类型
uploadPic.upLoad(bg,bg.getFile_type());
```