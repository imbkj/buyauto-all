package com.buyauto.util.method;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegularUtil {

	/**
	 * 是否为手机号码
	 */
	public static Boolean isTelephone(String val) {
		if(val == null) return false;
		String regExp = "^((13)|(14)|(15)|(18)|(17))\\d{9}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(val);
		return m.matches();
	}

	/**
	 * 验证是否为邮箱
	 */
	public static Boolean isEmaile(String val) {
		if(val == null) return false;
		String regExp = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(val);
		return m.matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isEmaile("  "));
        System.out.println(isTelephone(null));
        System.out.println(isEmaile("kero99@163.com.cn"));
        System.out.println(isEmaile("www。test.b@qq.com"));
	}
}
