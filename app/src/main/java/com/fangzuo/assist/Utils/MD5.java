package com.fangzuo.assist.Utils;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class MD5 {
	public static final byte[] DESIV = new byte[] { 0x12, 0x34, 0x56, 120, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef };// 向量

	public static AlgorithmParameterSpec iv = null;// 加密算法的参数接口
	public static Key key = null;

	public static String charset = "utf-8";
	private static byte[] m;

	public static String getMD5(String val){
		MessageDigest md5 = null;
		if(val!=null){
			try {
				md5 = MessageDigest.getInstance("MD5");
				md5.update(val.getBytes());
				//加密
				m = md5.digest();

			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return getString(m);
		}
		return getString(m);
	}
	private static String getString(byte[] b) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int a = b[i];
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));

		}
		return buf.toString().substring(8, 24);  //32位
	}

	/**
	 * 初始化
	 * @param deSkey	密钥
	 * @throws Exception
	 */
	public static void Do(String deSkey, String charset) throws Exception {

		DESKeySpec keySpec = new DESKeySpec(deSkey.getBytes(charset));// 设置密钥参数
		iv = new IvParameterSpec(DESIV);// 设置向量
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
		key = keyFactory.generateSecret(keySpec);// 得到密钥对象
	}

}
