package com.axter.tools.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;

/**
 * 拷贝Android库文件,修改package引用地址
 * @author zhaobo
 *
 */
public class CopyAndroidWorkSpace {

	// 源文件夹
	static String url1 = "E:/Git/android";
	// 目标文件夹
	static String url2 = "E:/WorkSpace/libs";
	// 还原对比目录
	static String url3 = "E:/WorkSpace/libs2";
	
	static String source = "";
	static String dest = "";
	public static void main(String[] args) throws IOException {
		if(args==null || args.length!=2){
			System.out.println("缺少参数");
			return;
		}
		System.out.println(args[0]+"|"+args[1]);
		source = args[0];
		dest = args[1];
		copyModify(url1, url2);
		
		String temp= source;
		source = dest;
		dest = temp;
		
		copyModify(url2, url3);
	}

	/**
	 * 拷贝修改文件
	 * @throws IOException
	 */
	public static void copyModify(String url1,String url2) throws IOException {
		File souDir = new File(url1);
		File dstDir = new File(url2);
		if (souDir.exists() && souDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdirs();
			}
			File[] files = souDir.listFiles();
			for (File file : files) {
				if (file.isDirectory() && !file.getName().startsWith(".")) {
					// 拷贝文件夹
					copy(file.getAbsolutePath(), url2);
				}
			}
		} else {
			System.out.println("资源文件夹未找到");
		}
	}

	public static void copy(String sdir, String ddir) throws IOException {
		File sDir = new File(sdir);
		File dDir = new File(ddir, sDir.getName());
		// 目标文件存在就删
		if (dDir.exists()) {
			deleteFile(dDir);
		}
		copyFile(sDir, dDir);
	}

	public static void deleteFile(File file) {
		if (file.exists() && file.isFile()) {
			file.delete();
		} else if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files == null || files.length == 0) {
				file.delete();
			} else {
				for (File f : files) {
					deleteFile(f);
				}
			}
		}
	}

	public static void copyFile(File file1, File file2) throws IOException {
		if (file1.isFile()) {
			String name = file1.getName();
			String last = name.substring(name.lastIndexOf(".") + 1);
			if ("jar".equals(last) || "png".equals(last) || "class".equals(last)) {
				fileChannelCopy(file1, file2);
			} else {
				doReplace(file1, file2);
			}
		} else if (file1.isDirectory()) {
			file2.mkdirs();
			File[] files = file1.listFiles();
			for (File f : files) {
				if (file2.isDirectory() && f.getName().equals(source)) {
					copyFile(f, new File(file2, dest));
				} else {
					copyFile(f, new File(file2, f.getName()));
				}
			}
		}
	}

	private static String encoding = "utf-8";
	private static boolean byline = true;

	public static void doReplace(File f, File temp) throws IOException {
		try {
			boolean changes = false;

			InputStream is = new FileInputStream(f);
			try {
				Reader r = encoding != null ? new InputStreamReader(is, encoding) : new InputStreamReader(is);
				OutputStream os = new FileOutputStream(temp);
				try {
					Writer w = encoding != null ? new OutputStreamWriter(os, encoding) : new OutputStreamWriter(os);

					if (byline) {
						r = new BufferedReader(r);
						w = new BufferedWriter(w);

						StringBuffer linebuf = new StringBuffer();
						int c;
						boolean hasCR = false;

						do {
							c = r.read();

							if (c == '\r') {
								if (hasCR) {
									// second CR -> EOL + possibly empty line
									replaceAndWrite(linebuf.toString(), w);
									w.write('\r');

									linebuf = new StringBuffer();
									// hasCR is still true (for the second one)
								} else {
									// first CR in this line
									hasCR = true;
								}
							} else if (c == '\n') {
								// LF -> EOL
								replaceAndWrite(linebuf.toString(), w);
								if (hasCR) {
									w.write('\r');
									hasCR = false;
								}
								w.write('\n');

								linebuf = new StringBuffer();
							} else { // any other char
								if ((hasCR) || (c < 0)) {
									// Mac-style linebreak or EOF (or both)
									replaceAndWrite(linebuf.toString(), w);
									if (hasCR) {
										w.write('\r');
										hasCR = false;
									}

									linebuf = new StringBuffer();
								}

								if (c >= 0) {
									linebuf.append((char) c);
								}
							}
						} while (c >= 0);

					}

					r.close();
					w.close();

				} finally {
					os.close();
				}
			} finally {
				is.close();
			}
			if (changes) {
				// 改名,改最后修改时间
			}
		} finally {

		}
	}

	private static void replaceAndWrite(String a, Writer w) throws IOException {
		if (a.contains(source)) {
			w.append(a.replaceAll(source, dest));
		} else {
			w.append(a);
		}
	}

	public static void fileChannelCopy(File s, File t) {

		FileInputStream fi = null;

		FileOutputStream fo = null;

		FileChannel in = null;

		FileChannel out = null;

		try {

			fi = new FileInputStream(s);

			fo = new FileOutputStream(t);

			in = fi.getChannel();// 得到对应的文件通道

			out = fo.getChannel();// 得到对应的文件通道

			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				fi.close();

				in.close();

				fo.close();

				out.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}
}
