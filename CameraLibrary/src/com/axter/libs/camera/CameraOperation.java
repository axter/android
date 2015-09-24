package com.axter.libs.camera;


import android.hardware.Camera.PictureCallback;

import com.axter.libs.camera.CameraContainer.TakePictureListener;

/** 
 * @ClassName: CameraOperation 
 * @Description:相机操作接口，用以统一CameraContainer和CameraView的功能
 * @author LinJ
 * @date 2015-1-26 下午2:41:31 
 *  
 */
public interface CameraOperation {
	/**  
	 *   切换前置和后置相机
	 */
	public void switchCamera();
	/**  
	 *  拍照
	 *  @param callback 拍照回调函数 
	 *  @param listener 拍照动作监听函数  
	 */
	public void takePicture(PictureCallback callback,TakePictureListener listener);
}
