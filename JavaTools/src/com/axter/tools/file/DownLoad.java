package com.axter.tools.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class DownLoad {
	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				try {
					download();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void download() throws IOException {
		long begin = System.currentTimeMillis();

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("http://img15.shop-pro.jp/PA01242/155/product/58980244.jpg").build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);

		Headers responseHeaders = response.headers();
		for (int i = 0; i < responseHeaders.size(); i++) {
			System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
		}

		long end = System.currentTimeMillis();
		System.out.println((end - begin));
		InputStream is = response.body().byteStream();
		FileOutputStream fos = new FileOutputStream("e:\\aa.jpg");
		byte[] data = new byte[20480];
		int len = 0;
		while ((len = is.read(data)) != -1) {
			fos.write(data, 0, len);
		}
		fos.flush();
		fos.close();
		is.close();
	}
}
