package com.fangzuo.assist.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

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
}
