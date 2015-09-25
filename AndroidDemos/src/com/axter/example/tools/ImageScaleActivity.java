package com.axter.example.tools;

import java.io.File;
import java.util.Calendar;

import com.axter.example.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageScaleActivity extends Activity implements View.OnClickListener {
	/** Called when the activity is first created. */
	private Button selectImageBtn;
	private ImageView imageView;

	private File sdcardTempFile;
	private AlertDialog dialog;
	private int crop = 180;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagescale);

		selectImageBtn = (Button) findViewById(R.id.selectImageBtn);
		imageView = (ImageView) findViewById(R.id.imageView);

		selectImageBtn.setOnClickListener(this);
		sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");

	}

	@Override
	public void onClick(View v) {
		if (v == selectImageBtn) {
			if (dialog == null) {
				dialog = new AlertDialog.Builder(this).setItems(new String[] { "相机", "相册" }, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
							intent.putExtra("output", Uri.fromFile(sdcardTempFile));
							intent.putExtra("crop", "true");
							intent.putExtra("aspectX", 1);// 裁剪框比例
							intent.putExtra("aspectY", 1);
							intent.putExtra("outputX", crop);// 输出图片大小
							intent.putExtra("outputY", crop);
							startActivityForResult(intent, 101);
						} else {
							Intent intent = new Intent("android.intent.action.PICK");
							intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
							intent.putExtra("output", Uri.fromFile(sdcardTempFile));
							intent.putExtra("crop", "true");
							intent.putExtra("aspectX", 1);// 裁剪框比例
							intent.putExtra("aspectY", 1);
							intent.putExtra("outputX", crop);// 输出图片大小
							intent.putExtra("outputY", crop);
							startActivityForResult(intent, 100);
						}
					}
				}).create();
			}
			if (!dialog.isShowing()) {
				dialog.show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK && requestCode == 100) {
			cropImageUri(Uri.fromFile(new File(sdcardTempFile.getAbsolutePath())), 100, 100, 200);
			//Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
			//imageView.setImageBitmap(bmp);
		}
		if (resultCode == RESULT_OK && requestCode == 200) {
			Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
			imageView.setImageBitmap(bmp);
		}
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {

		Intent intent = new Intent("com.android.camera.action.CROP");

		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 2);

		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", outputX);

		intent.putExtra("outputY", outputY);

		intent.putExtra("scale", true);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		intent.putExtra("return-data", false);

		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		intent.putExtra("noFaceDetection", true); // no face detection

		intent.putExtra("output", Uri.fromFile(sdcardTempFile));
		
		startActivityForResult(intent, requestCode);

	}

	public void onclickFun() {
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
		innerIntent.putExtra("aspectX", 1); // 出现放大和缩小
		innerIntent.setType("image/*"); // 查看类型 详细的类型在 com.google.android.mms.ContentType

		// ===============================
		// innerIntent.setType("image/*");
		// innerIntent.putExtra("crop", "true");
		// innerIntent.putExtra("aspectX", 1);//裁剪框比例
		// innerIntent.putExtra("aspectY", 1);
		// innerIntent.putExtra("outputX", 120);//输出图片大小
		// innerIntent.putExtra("outputY", 120);
		// ================================
		File tempFile = new File("/sdcard/ll1x/" + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名
		File temp = new File("/sdcard/ll1x/");// 自已项目 文件夹
		if (!temp.exists()) {
			temp.mkdir();
		}
		innerIntent.putExtra("output", Uri.fromFile(tempFile)); // 专入目标文件
		innerIntent.putExtra("outputFormat", "JPEG"); // 输入文件格式

		Intent wrapperIntent = Intent.createChooser(innerIntent, "选图片"); // 开始 并设置标题
		startActivityForResult(wrapperIntent, 100); // 设返回 码为 1 onActivityResult 中的 requestCode 对应
	}
}