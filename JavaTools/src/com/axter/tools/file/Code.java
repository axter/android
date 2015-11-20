package com.axter.tools.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class Code {
	public static void main(String[] args) {
		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void read() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("e:\\code"), Charset.forName("utf-8")));
		PrintWriter pw = new PrintWriter(new File("e:\\code.o"));
		String temp;
		while ((temp = br.readLine()) != null) {
			pw.println(splite(temp));
		}
		pw.flush();
		pw.close();
		br.close();
	}

	public static String splite(String str) {
		try {
			str = str.trim();
			int begin = str.indexOf("  ");
			int end = str.lastIndexOf("  ");
			String str1 = str.substring(0, begin).trim();
			String str2 = str.substring(begin, end).trim();
			String str3 = str.substring(end).trim();
			return str1 + "##" + str2 + "##" + str3;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
