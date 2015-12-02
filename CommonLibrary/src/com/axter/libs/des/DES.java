package com.axter.libs.des;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * <pre>
 * 文件名：	DES.java
 * 作　者：	lijianqiang
 * 描　述：	DES加密解密
 * 
 * </pre>
 */
public final class DES {

	private static String keyString = "267ac2ed67f292ff77c4a0b8";

	private static String vec = "12345678";

	private static String cString = "DESede/CBC/PKCS7Padding";// "DESede/CBC/PKCS7Padding";

	/**
	 * DES加密
	 * 
	 * @param encryptString
	 * @return
	 * @throws Exception
	 */
	public static String encryptDES(String encryptString) throws Exception {
		Key deskey = new SecretKeySpec(keyString.getBytes(), cString);
		Cipher cipher = Cipher.getInstance(cString);
		IvParameterSpec ips = new IvParameterSpec(vec.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(encryptString.getBytes());

		return Base64.encode(bOut);
	}

	/**
	 * DES解密
	 * 
	 * @param decryptString
	 * @return
	 * @throws Exception
	 */
	public static String decryptDES(String decryptString) throws Exception {
		byte[] data = Base64.decode(decryptString);
		Key deskey = new SecretKeySpec(keyString.getBytes(), cString);
		Cipher cipher = Cipher.getInstance(cString);
		IvParameterSpec ips = new IvParameterSpec(vec.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte decryptedData[] = cipher.doFinal(data);
		return new String(decryptedData);
	}

}
