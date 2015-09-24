```java
//本地图
getBitmapSync(int rid)
getBitmapSync(String path)
//本地图
displaySync(int uid,View view)
displayLocale(String path,ImageView image)
//缓存区
displayCache(String img_src,String img_path,ImageView imageview)
displayCache(String img_src,String img_path)
//过时的,异步网络图
display(ImageView image,String url)
display(String url,ImageView image,Context context,int id)
//网络图,异步网络图
display(String url,ImageView image)
display(int uid,ImageView image)
display(String url,ImageView image,ImageLoadingListener lis)
display(String url,ImageView imageview,int radius)
//网络默认图
display(String url,ImageView imageview,int default_rid)
```