/**
 * 
 */
package com.buyauto.util.core.tool.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.velocity.tools.config.DefaultKey;

import com.buyauto.util.method.MD5Util;


/**
 * 
 * @Title: HYDToolbar.java
 * @Package com.zeus.common.velocity
 * @Description: 页面通用方法
 * @author cxz
 * @date 2015-1-12 上午9:36:44
 * @version V3.0
 */
@DefaultKey("VCToolbar")
public class VCToolbar {

	public VCToolbar() {
	}

	/**
	 * <h1>融资金钱格式化</h1>
	 * 
	 * @param num
	 *            金额
	 * @param retain
	 *            是否保留两位小数
	 * @return
	 */
	public String formattingMoneyCN(String num, Boolean retain) {
		try {
			BigDecimal dNum = new BigDecimal(num);
			BigDecimal wan = new BigDecimal(10000);
			BigDecimal qian = new BigDecimal(1000);
			String moneyCn = "";
			if (dNum.divide(wan).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(wan, 0, BigDecimal.ROUND_DOWN) + "万";
				} else {
					moneyCn = dNum.divide(wan, 2, BigDecimal.ROUND_DOWN) + "万";
				}

			} else if (dNum.divide(qian).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(qian, 0, BigDecimal.ROUND_DOWN) + "千";
				} else {
					moneyCn = dNum.divide(qian, 2, BigDecimal.ROUND_DOWN) + "千";
				}
			} else {
				moneyCn = dNum.setScale(0, BigDecimal.ROUND_DOWN) + "元";
			}
			String strMoney = moneyCn.substring(0, moneyCn.length() - 1);
			String last = moneyCn.substring(moneyCn.length() - 1);
			if (strMoney.contains(".")) {
				Double xsd = Double.valueOf(strMoney.split("\\.")[1]);
				if (xsd.equals(new Double("0"))) {
					strMoney = strMoney.split("\\.")[0] + last;
					return strMoney;
				}
			}
			return moneyCn;
		} catch (Exception e) {
			e.printStackTrace();
			return 0 + "元";
		}
	}

	public String formattingMoneyCNWeChat(String num, Boolean retain) {
		try {
			BigDecimal dNum = new BigDecimal(num);
			BigDecimal wan = new BigDecimal(10000);
			String moneyCn = "";
			if (dNum.divide(wan).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(wan, 0, BigDecimal.ROUND_DOWN) + "万";
				} else {
					moneyCn = dNum.divide(wan, 2, BigDecimal.ROUND_DOWN) + "万";
				}

			} else {
				moneyCn = dNum.setScale(0, BigDecimal.ROUND_DOWN) + "元";
			}
			String strMoney = moneyCn.substring(0, moneyCn.length() - 1);
			String last = moneyCn.substring(moneyCn.length() - 1);
			if (strMoney.contains(".")) {
				Double xsd = Double.valueOf(strMoney.split("\\.")[1]);
				if (xsd.equals(new Double("0"))) {
					strMoney = strMoney.split("\\.")[0] + last;
					return strMoney;
				}
			}
			return moneyCn;
		} catch (Exception e) {
			e.printStackTrace();
			return 0 + "元";
		}
	}

	public String formattingMoneyCNWithOutUnit(String num, Boolean retain) {
		try {
			BigDecimal dNum = new BigDecimal(num);
			BigDecimal baiwan = new BigDecimal(1000000);
			BigDecimal wan = new BigDecimal(10000);
			BigDecimal qian = new BigDecimal(1000);
			String moneyCn = "";
			if (dNum.divide(baiwan).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(baiwan, 0, BigDecimal.ROUND_DOWN) + "";
				} else {
					moneyCn = dNum.divide(baiwan, 2, BigDecimal.ROUND_DOWN) + "";
				}
			} else if (dNum.divide(wan).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(wan, 0, BigDecimal.ROUND_DOWN) + "";
				} else {
					moneyCn = dNum.divide(wan, 2, BigDecimal.ROUND_DOWN) + "";
				}

			} else if (dNum.divide(qian).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(qian, 0, BigDecimal.ROUND_DOWN) + "";
				} else {
					moneyCn = dNum.divide(qian, 2, BigDecimal.ROUND_DOWN) + "";
				}
			} else {
				moneyCn = dNum.setScale(0, BigDecimal.ROUND_DOWN) + "";
			}
			String strMoney = moneyCn.toString();
			if (strMoney.contains(".")) {
				Double xsd = Double.valueOf(strMoney.split("\\.")[1]);
				if (xsd.equals(new Double("0"))) {
					strMoney = strMoney.split("\\.")[0];
				}
			}
			return strMoney;
		} catch (Exception e) {
			e.printStackTrace();
			return 0 + "";
		}

	}

	public String formattingMoneyCNUnit(String num, Boolean retain) {
		try {
			BigDecimal dNum = new BigDecimal(num);
			BigDecimal baiwan = new BigDecimal(1000000);
			BigDecimal wan = new BigDecimal(10000);
			BigDecimal qian = new BigDecimal(1000);
			if (dNum.divide(baiwan).doubleValue() >= 1) {
				return "百万";
			} else if (dNum.divide(wan).doubleValue() >= 1) {
				return "万";

			} else if (dNum.divide(qian).doubleValue() >= 1) {
				return "千";
			} else {
				return "元";
			}
		} catch (Exception e) {
			return "元";
		}

	}

	public String formattingMoneyWCN(String num, Boolean retain) {
		try {
			BigDecimal dNum = new BigDecimal(num);
			BigDecimal wan = new BigDecimal(10000);

			String moneyCn = "";

			if (retain) {
				moneyCn = dNum.divide(wan, 0, BigDecimal.ROUND_DOWN) + "";
			} else {
				moneyCn = dNum.divide(wan, 2, BigDecimal.ROUND_DOWN) + "";
			}

			return moneyCn;
		} catch (Exception e) {
			return 0 + "";
		}

	}

	/**
	 * 格式化HTML数据
	 * 
	 * @param str
	 *            转义文字中特殊符号
	 * @return
	 */
	public String formattingStrSymbol(String str) {
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("[\\t\\n\\r]", "</br>");
		str = str.replaceAll("[ ]", "&nbsp;");
		return str;
	}

	/**
	 * 格式化标的名称
	 * 
	 * @param str
	 * 
	 * @return
	 */
	public String formattingBidNameByNumber(String str) {
		int page = str.indexOf("-");
		String number = str.substring(page + 1, str.length());
		return number;
	}

	/**
	 * <h1>数字格式化</h1>
	 * 
	 * @param num
	 *            格式化数字
	 * @param decNum
	 *            保留小数位数
	 * @return
	 */
	public String formattingIntercept(Object num, Integer decNum, Boolean isK, Boolean isA) {
		try {
			BigDecimal dNum = new BigDecimal(num.toString());
			String dNumString = dNum.setScale(decNum, BigDecimal.ROUND_DOWN).toString();
			if (isK) {
				dNumString = formatBigDecimalThousand(dNumString, isA);
			}
			return dNumString;
		} catch (Exception e) {
			return "0";
		}

	}

	public String formattingInterceptWithZero(Object num, Integer decNum) {
		try {
			BigDecimal dNum = new BigDecimal(num.toString());
			String dNumStr = dNum.setScale(decNum, BigDecimal.ROUND_DOWN).toString();
			return dNumStr;
		} catch (Exception e) {
			return "0";
		}

	}

	public static String formattingInterceptBack(Object num, Integer decNum, Boolean isK, Boolean isA) {
		try {
			BigDecimal dNum = new BigDecimal(num.toString());
			String dNumString = dNum.setScale(decNum, BigDecimal.ROUND_DOWN).toString();
			if (isK) {
				dNumString = formatBigDecimalThousandBack(dNumString, isA);
			}
			return dNumString;
		} catch (Exception e) {
			return "0";
		}

	}

	/**
	 * <h1>返回格式化日期 <h1>
	 * <ul>
	 * <li>24小时制使用yyyy/MM/dd HH:mm:ss</li>
	 * <li>12小时制使用yyyy/MM/dd hh:mm:ss</li>
	 * </ul>
	 * 
	 * @param starTime
	 * @param dataType
	 * @return
	 */
	public String formattingDateString(Date starTime, String dataType) {
		if (starTime == null) {
			starTime = new Date();
		}
		try {
			DateFormat format2 = new SimpleDateFormat(dataType);
			String reTime = format2.format(starTime);
			return reTime;
		} catch (Exception e) {
			return "0000-00-00 00:00:00";
		}

	}

	public String formattingDateString(Object starTime, String dataType) {
		if (starTime == null) {
			starTime = new Date();
		}
		try {
			DateFormat format2 = new SimpleDateFormat(dataType);
			String reTime = format2.format(starTime);
			return reTime;
		} catch (Exception e) {
			return "0000-00-00 00:00:00";
		}

	}

	/**
	 * <h1>手机号码格式话替换</h1>
	 * 
	 * @param phoneNum
	 * @return
	 */
	public String formattingPhoneNum(Object phoneNum) {
		try {
			String phone = phoneNum.toString();
			return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * <h1>固定电话格式话替换</h1>
	 * 
	 * @param phoneNum
	 * @return
	 */
	public String formattingGDPhoneNum(Object phoneNum) {
		try {
			String phone = phoneNum.toString();
			return phone.substring(0, phone.length() - 3) + "***";
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * <h1>email格式话替换</h1>
	 * 
	 * @param phoneNum
	 * @return
	 */
	public String formattingEmail(Object phoneNum) {
		try {
			String phone = phoneNum.toString();
			String[] split = phone.split("@");
			String a = "";
			for (int i = 0; i < split.length; i++) {
				a += "*";
			}
			return a + "@" + split[1];
		} catch (Exception e) {
			return "***@***.***";
		}
	}

	/**
	 * <h1>银行卡格式替换</h1>
	 * 
	 * @param bankCakr
	 * @return
	 */
	public String formattingBankCard(Object bankCard) {
		String bankCakr = bankCard.toString();
		if (bankCard != null && bankCakr.length() > 8) {
			return bankCakr.substring(0, 4) + "****" + bankCakr.substring(8, bankCakr.length());
		} else {
			return bankCakr;
		}
	}

	/**
	 * 千位符替换
	 * 
	 * @param num
	 *            格式化数字
	 * @param isB
	 *            是否保留两位小书
	 * @return
	 */
	public String formatBigDecimalThousand(Object num, Boolean isB) {
		try {
			double d1 = Double.valueOf(num.toString());
			double d2 = Math.floor(d1);
			BigDecimal n = new BigDecimal(num.toString());
			java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
			// if (isB || String.valueOf(d1).equals(String.valueOf(d2))) {
			if (isB) {
				df = new java.text.DecimalFormat("#,##0");
			}
			return df.format(n);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}

	}

	public static String formatBigDecimalThousandBack(Object num, Boolean isB) {
		try {
			double d1 = Double.valueOf(num.toString());
			double d2 = Math.floor(d1);
			BigDecimal n = new BigDecimal(num.toString());
			java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
			if (isB || String.valueOf(d1).equals(String.valueOf(d2))) {
				df = new java.text.DecimalFormat("#,##0");
			}
			return df.format(n);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}

	}

	/**
	 * 
	 * @param n
	 * @return
	 * @Description:整数除以一百，得到百分数
	 * @date 2015-3-29 下午3:13:17
	 * @author ZYQ
	 * @exception (方法有异常的话加)
	 */
	public static String formatBigDecimalHundred(Integer n) {
		float num = (float) (n / 100.00);
		DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
		String s = df.format(num);// 返回的是String类型
		return s;
	}

	/**
	 * 千位符替换
	 * 
	 * @param num
	 *            格式化数字
	 * @param isB
	 *            是否保留两位小书
	 * @param isA
	 *            是否保留末尾元
	 * @return
	 */
	public String formatBigDecimalThousand(Object num, Boolean isB, Boolean isA) {
		try {
			BigDecimal n = new BigDecimal(num.toString());
			java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
			if (isB) {
				df = new java.text.DecimalFormat("#,##0");
			}
			if (isA) {
				return df.format(n) + "元";
			}
			return df.format(n);
		} catch (Exception e) {
			return "0";
		}
	}

	public BigDecimal add(Object var1, Object var2) {
		BigDecimal b1 = var1 == null ? new BigDecimal(0) : new BigDecimal(var1.toString());
		BigDecimal b2 = var2 == null ? new BigDecimal(0) : new BigDecimal(var2.toString());
		return b1.add(b2);
	}

	/**
	 * 
	 * @Title: formattingMoneyCNBack
	 * @Description: java后台调用
	 * @param @param num
	 * @param @param retain
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String formattingMoneyCNBack(String num, Boolean retain) {
		try {
			BigDecimal dNum = new BigDecimal(num);
			BigDecimal wan = new BigDecimal(10000);
			BigDecimal qian = new BigDecimal(1000);
			String moneyCn = "";
			if (dNum.divide(wan).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(wan, 0, BigDecimal.ROUND_DOWN) + "万";
				} else {
					moneyCn = dNum.divide(wan, 2, BigDecimal.ROUND_DOWN) + "万";
				}

			} else if (dNum.divide(qian).doubleValue() >= 1) {
				if (retain) {
					moneyCn = dNum.divide(qian, 0, BigDecimal.ROUND_DOWN) + "千";
				} else {
					moneyCn = dNum.divide(qian, 2, BigDecimal.ROUND_DOWN) + "千";
				}
			} else {
				moneyCn = dNum.setScale(0, BigDecimal.ROUND_DOWN) + "元";
			}
			String strMoney = moneyCn.substring(0, moneyCn.length() - 1);
			String last = moneyCn.substring(moneyCn.length() - 1);
			if (strMoney.contains(".")) {
				Double xsd = Double.valueOf(strMoney.split("\\.")[1]);
				if (xsd.equals(new Double("0"))) {
					strMoney = strMoney.split("\\.")[0] + last;
					return strMoney;
				}
			}
			return moneyCn;
		} catch (Exception e) {
			e.printStackTrace();
			return 0 + "元";
		}
	}

	/**
	 * 
	 * 
	 * @Title: htmlToText
	 * @Description: 过滤掉文本中的html标签
	 * @return UploadPath 返回类型
	 * @throws
	 */
	public String htmlToText(String text) {
		return text.replaceAll("</?[^<]+>", "");
	}
	
	
	public String encodeSessionId(String sessionId) {
		return MD5Util.encryption(sessionId + "ThisIsSessionKeyCode");
	}
}