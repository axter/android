if (mCamera == null) {
	openCamera();
}
if (mCamera != null) {
	setCameraParameters();
	mCamera.setPreviewDisplay(getHolder());
	mCamera.startPreview();
}


updateCameraOrientation();



if (mCamera != null) {
	mCamera.stopPreview();
	mCamera.release();
	mCamera = null;
}





-- 切换摄像头
openCamera();
if (mCamera != null) {
	setCameraParameters();
	updateCameraOrientation();
	try {
		mCamera.setPreviewDisplay(getHolder());
		mCamera.startPreview();
	} catch (IOException e) {
		e.printStackTrace();
	}
}



openCamera();
setCameraParameters();
mCamera.setPreviewDisplay(getHolder());
mCamera.startPreview();
updateCameraOrientation();