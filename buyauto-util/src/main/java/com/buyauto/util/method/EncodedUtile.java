package com.buyauto.util.method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncodedUtile {

	private static final Logger logger = LoggerFactory.getLogger(EncodedUtile.class);
	
	/**
	 * 中文转Unicode
	 * @param gbString
	 * @return
	 */
	public static String gbEncoding(String val) {   //gbString = "测试"  
		if(val.isEmpty()) return null;
		String unicodeBytes = "";     
        try {
    	   char[] utfBytes = val.toCharArray();   //utfBytes = [测, 试]  
           for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {     
               String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串  
                 if (hexB.length() <= 2) {     
                     hexB = "00" + hexB;     
                }     
                unicodeBytes = unicodeBytes + "\\u" + hexB;     
           }     
           return unicodeBytes;     
		} catch (Exception e) {
			logger.error("中文转Unicode失败!"+val);
			return val;
		}
    }  
	
	/**
	 * Unicode转中文  
	 * @param dataStr
	 * @return
	 */
	public static String decodeUnicode( String val) {  
		if(StringUtil.isEmpty(val)) return null;
		final StringBuffer buffer = new StringBuffer();     
       try {
    	   int start = 0;     
           int end = 0;     
           while (start > -1) {     
               end = val.indexOf("\\u", start + 2);     
               String charStr = "";     
               if (end == -1) {     
                   charStr = val.substring(start + 2, val.length());     
               } else {     
                   charStr = val.substring(start + 2, end);     
               }     
               char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
               buffer.append(new Character(letter).toString());     
               start = end;     
           }     
            return buffer.toString();     
		 }catch (Exception e) {
			logger.error("Unicode转中文失败!"+val);
			return val;
		}
     }  
	
}
