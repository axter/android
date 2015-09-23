package com.imohoo.libs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BitmapCache {
	private static Context mContext;
	private static String cachePath;
	private static final String DRAWABLE = "drawable://";
	private static final String FILE = "file://";
	/**
	 * 初始化ImageLoader
	 * @param context
	 * @param path
	 */
	public static void init(Context context,String path) {
		mContext = context;
		cachePath = path;
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		if(path != null && path.length() > 0)
			config.diskCache(new UnlimitedDiskCache(new File(path)));//自定义缓存路径
		ImageLoader.getInstance().init(config.build());
	}
	
	//======================================================
	// 缓存图片显示策略
	private static Hashtable<String, SoftReference<DisplayImageOptions>> dfs = new Hashtable<String, SoftReference<DisplayImageOptions>>();
	private static final String cacheOnDisk = "cacheOnDisk";
	private static final String cacheOnMem = "default";
	private static DisplayImageOptions getDisplayImageOptions(String key) {
		SoftReference<DisplayImageOptions> d = dfs.get(key);
		if (d == null || d.get() == null) {
			if (cacheOnDisk.equals(key)) {
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.cacheOnDisk(true).build();
				dfs.put(key, new SoftReference<DisplayImageOptions>(options));
				return options;
			} else if(cacheOnMem.equals(key)){
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.cacheInMemory(false).build();
				dfs.put(key, new SoftReference<DisplayImageOptions>(options));
				return options;
			} else{
				return getDisplayImageOptions(cacheOnMem);
			}
		}
		return d.get();
	}
	private static DisplayImageOptions getDisplayImageOptions(int key) {
		SoftReference<DisplayImageOptions> d = dfs.get(""+key);
		if (d == null || d.get() == null) {
	        DisplayImageOptions options = new DisplayImageOptions.Builder()
	        		.showImageOnLoading(key)
	        		.showImageForEmptyUri(key)
					.cacheOnDisk(true).build();
			dfs.put(""+key, new SoftReference<DisplayImageOptions>(options));
			return options;
		}
		return d.get();
	}
	
	private static DisplayImageOptions getDisplayImageOptions(int key,int radius) {
		SoftReference<DisplayImageOptions> d = dfs.get(key+"|"+radius);
		if (d == null || d.get() == null) {
	        DisplayImageOptions options = new DisplayImageOptions.Builder()
	        		.showImageOnLoading(key)
	        		.showImageForEmptyUri(key)
	        		.displayer(new RoundedBitmapDisplayer(radius))
					.cacheOnDisk(true).build();
			dfs.put(key+"|"+radius, new SoftReference<DisplayImageOptions>(options));
			return options;
		}
		return d.get();
	}

	//======================================================
	/**
	 * 清空图片缓存
	 */
	public static void clearDiskCache(){
		ImageLoader.getInstance().getDiskCache().clear();
	}
	public static void clearMemoryCache(){
		ImageLoader.getInstance().clearMemoryCache();		
	}
	//======================================================
	// 过时兼容方法
	//======================================================
	@Deprecated
	public static void display(ImageView imageview,String url){
		display(url, imageview);
	}
	//======================================================
	//	网络图,异步加载
	//======================================================
	public static void display(String url,ImageView imageview){
		if(url==null || url.length()==0){
			return;
		}
		ImageLoader.getInstance().displayImage(url, imageview, getDisplayImageOptions(cacheOnDisk));
	}
	public static void display(String url,ImageView imageview,ImageLoadingListener lis){
		if(url==null || url.length()==0){
			return;
		}
		ImageLoader.getInstance().displayImage(url, imageview, getDisplayImageOptions(cacheOnDisk) , lis);
	}
	public static void display(String url,ImageView imageview,int default_rid){
		ImageLoader.getInstance().displayImage(url, imageview, getDisplayImageOptions(default_rid));
	}
	/**
	 * 设置圆角图片
	 * @param url
	 * @param imageview
	 * @param default_rid
	 * @param radius 角度,-1为圆形图
	 */
	public static void display(String url,ImageView imageview,int default_rid,int radius){
		ImageLoader.getInstance().displayImage(url, imageview, getDisplayImageOptions(default_rid,radius));
	}
	public static void display(int rid,ImageView imageview){
		ImageLoader.getInstance().displayImage(DRAWABLE+rid, imageview, getDisplayImageOptions(cacheOnMem));
	}
	//=======================================================
	//	本地图
	//=======================================================
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") 
	public static void displaySync(int rid,View view){
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(DRAWABLE+rid,getDisplayImageOptions(cacheOnMem));
		BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(),bitmap);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			//版本>=16
			view.setBackground(drawable);
		}else{
			view.setBackgroundDrawable(drawable);
		}
	}
	public static void displaySync(int rid,ImageView imageview){
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(DRAWABLE+rid,getDisplayImageOptions(cacheOnMem));
		imageview.setImageBitmap(bitmap);
	}
	public static void displayLocale(String path,ImageView imageview){
		ImageLoader.getInstance().displayImage(FILE+path, imageview, getDisplayImageOptions(cacheOnMem));
	}
	//=======================================================
	//	本地图
	//=======================================================
	public static Bitmap getBitmapSync(int rid){
		return ImageLoader.getInstance().loadImageSync(DRAWABLE+rid,getDisplayImageOptions(cacheOnMem));
	}
	
	public static Bitmap getBitmapSync(String path){
		return ImageLoader.getInstance().loadImageSync(FILE+path,getDisplayImageOptions(cacheOnMem));
	}
	//==================================================================
	//		本地文件拷贝至缓存区
	//==================================================================
	/**
	 * 将本地文件拷贝至缓存区显示
	 * @param img_src
	 * @param img_path
	 * @param imageview
	 */
	public static void displayCache(String img_src,String img_path,ImageView imageview){
		String filename = new HashCodeFileNameGenerator().generate(img_src);
		copyFile(img_path, cachePath+File.separator+filename);
		display(img_src, imageview);
	}
	public static void displayCache(String img_src,String img_path){
		String filename = new HashCodeFileNameGenerator().generate(img_src);
		copyFile(img_path, cachePath+File.separator+filename);
	}
	//==================================================================
	/**
	 * 拷贝文件至新的路径下
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
