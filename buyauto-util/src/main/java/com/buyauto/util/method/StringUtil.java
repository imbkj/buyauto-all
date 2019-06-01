package com.buyauto.util.method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @ClassName: StringUtil
 * @Description: String 宸ュ叿绫�
 * @author cxz
 * @date 2016骞�5鏈�19鏃� 涓嬪崍2:23:38
 *
 */
public class StringUtil {

	/**
	 * 
	 * @Title: isEmpty
	 * @Description: 楠岃瘉涓虹┖
	 * @param @param str
	 * @param @return 璁惧畾鏂囦欢
	 * @return boolean 杩斿洖绫诲瀷
	 * @throws
	 */
	public static boolean isEmpty(Object str) {
		if (str == null) {
			return true;
		}
		if ("".equals(str.toString())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: validateFileType
	 * @Description: 楠岃瘉绫诲瀷
	 * @param @param type
	 * @param @return 璁惧畾鏂囦欢
	 * @return boolean 杩斿洖绫诲瀷
	 * @throws
	 */
	public static boolean validateFileType(String type) {
		/* 鏆備笉鍚敤 */
		// String[] fileType = { "JPG", "GIF", "BMP", "PNG" };
		// for (String ot : fileType) {
		// if (ot.equalsIgnoreCase(type)) {
		// return true;
		// }
		// }
		return true;
	}

	public static String getHidePhone(Long ump) {
		String phone = ump.toString();
		if (phone != null && phone.length() >= 5) {
			String start = phone.substring(0, 3);
			String end = phone.substring(phone.length() - 4, phone.length());
			return start + "****" + end;
		}
		return null;
	}

	/**
	 * 
	 * @param bankCardNum
	 * @return
	 * @Description: 閾惰鍗″彿鍔犲瘑
	 * @date 2015-3-26 涓嬪崍3:17:16
	 * @author dingtao
	 * @exception (鏂规硶鏈夊紓甯哥殑璇濆姞)
	 */
	public static String getHideBankCardNum(String bankCardNum) {

		if (bankCardNum != null && bankCardNum.length() >= 16) {
			String start = bankCardNum.substring(0, 3);
			String end = bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length());
			int countHide = bankCardNum.length() - 7;
			for (int i = 0; i < countHide; i++) {
				start = start + "*";
			}
			return start + end;
		}
		return bankCardNum;
	}

	public static String getHideIdCard(String cardNum) {
		if (cardNum != null && cardNum.length() > 3) {
			String start = cardNum.substring(0, 3);
			if (cardNum.length() == 18) {
				return start + "***************";
			} else if (cardNum.length() == 15) {
				return start + "************";
			}
		}

		return null;
	}

	public static String getBirthFromIdcard(String cardNum) throws ParseException {
		if (cardNum == null)
			return null;
		if (cardNum.length() == 15) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
			String data = cardNum.substring(6, 12);
			Date date = sdf.parse(data);
			return NHDateUtils.getY_M_DSF().format(date);
		} else if (cardNum.length() == 18) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String data = cardNum.substring(6, 14);
			Date date = sdf.parse(data);
			return NHDateUtils.getY_M_DSF().format(date);
		}
		return null;
	}

	public static String getHideMail(String mail) {
		if (mail != null && mail.indexOf("@") != -1) {
			String name = mail.substring(0, mail.indexOf("@"));
			if (name.length() > 2) {
				String start = name.substring(0, 2);
				for (int i = 0; i < name.length() - 2; i++) {
					start += "*";
				}
				return start + mail.substring(mail.indexOf("@"));
			} else {
				String start = "";
				for (int i = 0; i < name.length(); i++) {
					start += "*";
				}
				return start + mail.substring(mail.indexOf("@"));
			}
		}
		return null;
	}

	public static String dateAddPart(String strdate, boolean addDay) {
		if (strdate.contains(" ")) {
			return strdate;
		} else {
			if (addDay) {
				return strdate + " 23:59:59.999";
			} else {
				return strdate + " 00:00:00";
			}
		}
	}

	public static String dateAddPart(String strdate, String timePart) {
		if (strdate.contains(" ")) {
			strdate = strdate.split(" ")[0];
		}
		if (timePart != null) {
			return strdate + " " + timePart;
		} else {
			return strdate + " 00:00:00";
		}
	}

	public static Object[] stringToArray(String value, String spilt, Class type) {
		if (StringUtil.isEmpty(value))
			return null;
		String[] strs = null;
		if (!value.contains(spilt)) {
			strs = new String[] { value };
		}
		strs = value.split(spilt);
		if (type == null || type == String.class)
			return strs;
		Object[] os = new Object[strs.length];
		for (int i = 0; i < strs.length; i++) {
			os[i] = type.cast(strs[i]);
		}
		return os;
	}

	/**
	 * 灏嗕笂浼犳枃浠剁殑璺緞杞崲鎴愬甫鏈夋牴璺緞鐨� 瀛楃涓� 锛岀敤浜庢暟鎹簱瀛樺偍
	 * 
	 * @param value
	 *            瑕佽浆鎹㈢殑瀛楃涓�
	 * @param spilt
	 *            鍒嗗壊鏍囩ず
	 * @param rootPath
	 *            鏍硅矾寰�
	 * @return
	 */
	public static StringBuffer stringToUrlString(String value, String spilt, String rootPath) {

		if (",".equals(value.substring(0, 1))) {
			StringBuffer url = new StringBuffer();
			String[] split = value.split(",");
			for (String string : split) {
				if (!(string.length() == 0 && "".equals(string))) {
					url.append(rootPath + "/" + string);
				}
			}
			return url;
		} else {
			return new StringBuffer(value);
		}
	}
	
	/**
	 * 通过字符串转换成相应的整型，并返回。
	 * @param strValue String 待转换的字符串
	 * @return int 转换完成的整型
	 * */
	public static int getStrToInt(String strValue)
	{
		if (null == strValue)
		{
			return 0;
		}
		int iValue = 0;
		try
		{
			iValue = new java.lang.Integer(strValue.trim()).intValue();
		}
		catch (Exception ex)
		{
			iValue = 0;
		}
		return iValue;
	}
}
