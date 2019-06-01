package com.buyauto.util.method;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @Title: MD5Util.java
 * @Package com.hoyoda.blade.common.util
 * @Description: MD5工具类
 * @author LeoZhang
 * @date 2014-11-21
 * @version V3.0
 */
public class MD5Util {

	private static MessageDigest getMessageDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean checkMD5(byte[] md5Info, byte[] dataPackage) {
		byte[] orgdm5 = MD5Util.getMessageDigest().digest(dataPackage);
		String orgMD5Str = new String(orgdm5);
		return orgMD5Str.equals(new String(md5Info));
	}

	public static byte[] buidMD5Info(byte[] bytes) {
		byte[] md5Info = MD5Util.getMessageDigest().digest(bytes);
		return md5Info;
	}

	/**
	 * 
	 * @param plainText
	 *            明文
	 * @return 32位密文
	 */
	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

}
