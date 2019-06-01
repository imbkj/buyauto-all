package com.buyauto.util.method;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 
 * @Title: HYDDateUtils.java
 * @Package com.hoyoda.blade.common.util
 * @Description: 时间工具类
 * @author LeoZhang
 * @date 2014-6-3 上午11:59:01
 * @version V2.0
 */
public class NHDateUtils {
	public static final long ONEDAY = 86400000l;
	public static final long ONEWeek = 86400000l * 7;

	private static SimpleDateFormat HF_YMDSF = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat Y_M_DSF = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat Y_M_DSF_Point = new SimpleDateFormat("yyyy.MM.dd");

	private static SimpleDateFormat Y_M_DCNSF = new SimpleDateFormat("yyyy年MM月dd日");

	private static SimpleDateFormat HYD_YMDHMS_SF_HH = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static SimpleDateFormat SINA_YMDHMS_SF_HH = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final int h = 15;// 24小时制，3点
	private static final int m = 00;// 00分钟，和上述字段共同表示对比时间，三点吗，预留分钟

	private static final SimpleDateFormat dbPrimaryKeyForamte = new SimpleDateFormat("yyyyMMdd");

	private static final DateFormat[] dateFormat = { 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
			new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
			new SimpleDateFormat("yyyy-MM-dd"), 
			new SimpleDateFormat("yyyy/MM/dd"),
			new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK)
			};
	
	
	/**
	 * String类型转Date类型
	 * @param date
	 * @return
	 */
	public static Date parseDateFormat(String date){
		if(StringUtil.isEmpty(date)) return null;
		Date parse = null;
		for (int i = 0; i < dateFormat.length; i++) {
			try {
				parse = dateFormat[i].parse(date);
			} catch (ParseException ex) {
				continue;
			}
			break;
		}
		return parse;
	}
	
	/**
	 * 快速获取yyyyMMdd格式的SimpleDateFormat
	 * 
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getHFYMDSF() {
		return (SimpleDateFormat) NHDateUtils.HF_YMDSF.clone();
	}

	public static SimpleDateFormat getHFYMDCNSF() {
		return (SimpleDateFormat) NHDateUtils.Y_M_DCNSF.clone();
	}

	/**
	 * 快速获取yyyyMMdd格式的SimpleDateFormat
	 * 
	 * @return SimpleDateFormat
	 */
	public static SimpleDateFormat getY_M_DSF() {
		return (SimpleDateFormat) NHDateUtils.Y_M_DSF.clone();
	}

	public static SimpleDateFormat getY_M_DSF_Point() {
		return (SimpleDateFormat) NHDateUtils.Y_M_DSF_Point.clone();
	}

	public static SimpleDateFormat getHYDYMDHMSSFHH() {
		return (SimpleDateFormat) NHDateUtils.HYD_YMDHMS_SF_HH.clone();
	}

	public static SimpleDateFormat getDBDAY() {
		return (SimpleDateFormat) NHDateUtils.dbPrimaryKeyForamte.clone();
	}

	/**
	 * 根据时间转换当前数据主键，一般用于查询
	 * 
	 * @author huanggang@hoyoda.com
	 * @param date
	 *            提供的时间
	 * @param ref
	 *            组建参考值,
	 * @return 主键
	 */
	public static long convertDBPrimaryKey(Date date, String ref) {
		StringBuffer prmaryKey = new StringBuffer();
		prmaryKey.append(((DateFormat) NHDateUtils.dbPrimaryKeyForamte.clone()).format(date));
		prmaryKey.append(ref);
		return Long.valueOf(prmaryKey.toString());
	}

	/**
	 * 数据库开始键查询
	 * 
	 * @author huanggang@hoyoda.com
	 * @param date
	 * @return
	 */
	public static long convertDBPrimaryStartKey(Date date) {
		return NHDateUtils.convertDBPrimaryKey(date, "00000000000");
	}

	/**
	 * 数据库结束键查询
	 * 
	 * @author huanggang@hoyoda.com
	 * @param date
	 * @return
	 */
	public static long convertDBPrimaryEndKey(Date date) {
		return NHDateUtils.convertDBPrimaryKey(date, "99999999999");
	}

	/**
	 * 验证是否过期
	 * 
	 * @param startDate
	 *            启日
	 * @param anotherDate
	 *            止日
	 * @param pastDays
	 *            跨越天数
	 * @return 未过期:true<br/>
	 *         过期:fasle
	 */
	public static boolean underShelfLife(Date startDate, Date anotherDate, int pastDays) {
		long sDate = startDate.getTime();
		long eDate = anotherDate.getTime();
		return (eDate - sDate < NHDateUtils.ONEDAY * pastDays);
	}

	/**
	 * <h1>获得指定日期月份中的剩余天数</h1>
	 * 
	 * @param date
	 * @return 包含指定日期，<li>如传入日期为5.29日，则返回3</li><li>
	 *         如传入日期为5.30日，则返回2</li><li>如传入日期为5.31日，则返回1</li>
	 */
	@Deprecated
	public static int getLeftDaysOfMonthByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int maximumDateOfMonth = cal.getActualMaximum(Calendar.DATE);
		int dateOfMonth = date.getDate();
		return maximumDateOfMonth - dateOfMonth + 1;

	}

	/**
	 * <h1>判断当前时间是否大于设定时间</h1>
	 * 
	 * @return 判断时间<li>超过判断时间，返回true</li><li>未到判断时间，返回false</li>
	 */
	@Deprecated
	public static Boolean judgeExpiredByTime(Date newTime) {

		Calendar now = Calendar.getInstance();
		now.setTime(newTime);
		int hours = now.getTime().getHours();
		int minutes = now.getTime().getMinutes();

		Boolean overdue = false;
		/*
		 * 判断小时大于3点
		 */
		if (hours > NHDateUtils.h) {
			overdue = true;
		} else if (hours == NHDateUtils.h) {
			/*
			 * 判断分钟大于预留分
			 */
			if (minutes > NHDateUtils.m) {
				overdue = true;
			}
		}

		return overdue;
	}

	/**
	 * <h1>计算从两个时间之间相差的月份</h1> 计息中使用
	 * 
	 * @param starTime
	 * @param EndTime
	 * @return 相差月数
	 */
	public static int starToEndAllMon(Date starTime, Date EndTime) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(EndTime);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(starTime);
		int mon = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH)
				- cal2.get(Calendar.MONTH);
		return mon + 1;
	}

	/**
	 * <h1>计算从两个时间之间的天数差</h1> 判断是否可以发起债权转让中使用
	 * 
	 * @param starTime
	 * @param EndTime
	 * @return 相差月数
	 */
	public static int starToEndDay(Date starTime, Date EndTime) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(EndTime);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(starTime);
		int dayDiff = (cal1.get(Calendar.DATE) - cal2.get(Calendar.DATE));
		return dayDiff;
	}

	// public static void main(String[] args) {
	// System.out.println(HYDDateUtils.starToEndAllDate(new Date("2014/10/1"),
	// new Date("2014/11/1")));
	// }

	/**
	 * <h1>计算两个日期之间相差的天数</h1> 计息中使用
	 * 
	 * @param starTime
	 * @param EndTime
	 * @return 相差天数 starToEndAllDate(new Date(), new Date()) 返回0
	 */
	public static int starToEndAllDate(Date starTime, Date EndTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(starTime);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long time1 = cal.getTimeInMillis();
		cal.setTime(EndTime);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * <h1>计算两个日期之间相差的天数</h1>
	 * 
	 * @param starTime
	 * @param EndTime
	 * @return 相差天数
	 */
	public static int starToEndAllDate(Calendar starTime, Calendar EndTime) {
		starTime.set(Calendar.HOUR_OF_DAY, 0);
		starTime.set(Calendar.MINUTE, 0);
		starTime.set(Calendar.SECOND, 0);
		starTime.set(Calendar.MILLISECOND, 0);
		long time1 = starTime.getTimeInMillis();
		EndTime.set(Calendar.HOUR_OF_DAY, 0);
		EndTime.set(Calendar.MINUTE, 0);
		EndTime.set(Calendar.SECOND, 0);
		EndTime.set(Calendar.MILLISECOND, 0);
		long time2 = EndTime.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 根据传入日期返回该月天数 计息中使用
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonthbyDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/*
	 * 格式化日期类型
	 */
	public static String getTimeDateStr(Date now, Boolean isDay) {
		Calendar cs = Calendar.getInstance();
		cs.setTime(now);
		int mon = cs.get(Calendar.MONTH) + 1;
		int day = cs.get(Calendar.DATE);
		String dateTime = "";
		String monStr = "00";
		String dayStr = "00";
		if (mon < 10) {
			monStr = "0" + mon;
		} else {
			monStr = mon + "";
		}
		if (isDay) {
			if (day < 10) {
				dayStr = "0" + day;
			} else {
				dayStr = day + "";
			}
			dateTime = (cs.get(Calendar.YEAR)) + "-" + monStr + "-" + dayStr;
		} else {
			dateTime = (cs.get(Calendar.YEAR)) + "-" + monStr;
		}
		return dateTime;
	}

	/*
	 * 根据日期返回当前下月月的第一个工作日 计息中使用
	 */

	public static Date completeData(Date starTime) {
		Calendar gc = Calendar.getInstance();// 时间加减
		gc.setTime(starTime);
		gc.add(2, 1);// 月份加1
		gc.set(5, 1);// 日期 设置日期为1
		return gc.getTime();
	}

	public static Date completeDataHourMin(Date starTime) {
		Calendar gc = Calendar.getInstance();// 时间加减
		gc.setTime(starTime);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(14, 0);
		return gc.getTime();
	}

	public static Date startDate(Date starTime) {
		Calendar gc = Calendar.getInstance();// 时间加减
		gc.setTime(starTime);
		gc.set(5, 1);// 日期 设置日期为1
		return gc.getTime();
	}

	/**
	 * 获取当前时间所属月份开始日期和时间
	 * 
	 * @return
	 */
	public static Date getNowMonthFirstDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当前时间所属月份的结束日期和时间
	 * 
	 * @return
	 */
	public static Date getNowMonthLastDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Date addDays(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
	public static Date add2Days(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 2);
		return calendar.getTime();
	}
	public static Date add3Days(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 3);
		return calendar.getTime();
	}


	/**
	 * 得到下一年的一月一日
	 * 
	 * @return
	 */
	public static Date getAfterYear() {
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		int yearInt = Integer.parseInt(year);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.YEAR, ++yearInt);
		calendar1.set(Calendar.MONTH, 1);
		calendar1.set(Calendar.DAY_OF_YEAR, 1);
		Long c = calendar1.getTimeInMillis();
		return new Date(c);
	}

	/**
	 * 得到昨天的日期
	 * 
	 * @return
	 */
	public static Date getYesterDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Long c = calendar.getTimeInMillis();
		return new Date(c);
	}

	/**
	 * 得到明天的日期
	 * 
	 * @return
	 */
	public static Date getTomorrowDay(Date now) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, 1);
		Long c = calendar.getTimeInMillis();
		return new Date(c);
	}

	/**
	 * 获得下一个月的第一天
	 * 
	 * @return
	 */
	public static Date getNextMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Long c = calendar.getTimeInMillis();
		Date nextMonth = new Date(c);
		return nextMonth;
	}

	/**
	 * 获得当前月的第一天 月份从零开始算起 1月~12月 = 0~11
	 * 
	 * @return
	 */
	public static Date currentMonthFirstDay(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 获得当前月的最后一天 月份从零开始算起 1月~12月 = 0~11
	 * 
	 * @return
	 */
	public static Date currentMonthLastDay(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 获得起止日期间的各个月的月头和月尾
	 * 
	 * @param startTime
	 *            例:2014-01-03
	 * @param endTime
	 *            例:2014-12-15
	 * @return <ul>
	 *         <li>{2014-01-01 00:00:00,2014-01-31 23:59:59}</li>
	 *         <li>{2014-02-01 00:00:00,2014-02-28 23:59:59}</li>
	 *         <li>...</li>
	 *         <li>...</li>
	 *         <li>{2014-12-01 00:00:00,2014-12-31 23:59:59}</li>
	 *         </ul>
	 * @throws ParseException
	 */
	public static List<Date[]> getSplitTimes(String startTime, String endTime) throws ParseException {
		Date oStartDate = NHDateUtils.getY_M_DSF().parse(startTime);
		Date oEndDate = NHDateUtils.getY_M_DSF().parse(endTime);
		return getSplitTimes(oStartDate, oEndDate);
	}

	/**
	 * 获得起止日期间的各个月的月头和月尾
	 * 
	 * @param startTime
	 *            例:2014-01-03
	 * @param endTime
	 *            例:2014-12-15
	 * @return <ul>
	 *         <li>{2014-01-01 00:00:00,2014-01-31 23:59:59}</li>
	 *         <li>{2014-02-01 00:00:00,2014-02-28 23:59:59}</li>
	 *         <li>...</li>
	 *         <li>...</li>
	 *         <li>{2014-12-01 00:00:00,2014-12-31 23:59:59}</li>
	 *         </ul>
	 * @throws ParseException
	 */
	public static List<Date[]> getSplitTimes(Date oStartDate, Date oEndDate) throws ParseException {
		List<Date[]> result = new ArrayList<Date[]>();
		// 设置
		Calendar cal = Calendar.getInstance();
		cal.setTime(oStartDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date nextStartDate = cal.getTime();// 当前的开始时间
		while (nextStartDate.before(oEndDate)) {
			Date currentStartDate = cal.getTime();//

			// 得到本月出和本月尾巴
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			Date currentEndDate = cal.getTime();
			Date[] dates = { currentStartDate, currentEndDate };
			result.add(dates);
			cal.setTime(currentStartDate);
			cal.add(Calendar.MONTH, 1);
			nextStartDate = cal.getTime();
		}
		return result;
	}

	/**
	 * 
	 * @return
	 * @Description: 获得当日的Calendar并且 时分秒都为0
	 * @date 2015-3-18 上午11:28:51
	 * @author LeoZhang
	 * @exception (方法有异常的话加)
	 */
	public static Calendar getCalendarOfDay() {
		return getCalendarOfDay(null);
	}

	/**
	 * 
	 * @return
	 * @Description: 获得指定日期的Calendar并且 时分秒都为0
	 * @date 2015-3-18 上午11:28:51
	 * @author LeoZhang
	 * @exception (方法有异常的话加)
	 */
	public static Calendar getCalendarOfDay(Date now) {
		now = now == null ? new Date() : now;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/**
	 * 
	 * @return
	 * @Description: 获得指定日期的Date并且 时分秒都为0
	 * @date 2015-3-18 上午11:28:51
	 * @author LeoZhang
	 * @throws ParseException
	 * @exception (方法有异常的话加)
	 */
	public static Date getOnlyDate(Date now) throws ParseException {
		SimpleDateFormat sf = getY_M_DSF();
		String dateNowStr = sf.format(now);
		// System.out.println("格式化后的日期：" + dateNowStr);
		Date today = sf.parse(dateNowStr);
		return today;
	}

	/**
	 * 
	 * @Title: getAfterDaysDate
	 * @Description: 获得当前时间的days天以后的时间
	 * @param @param now
	 * @param @param days
	 * @param @return
	 * @param @throws ParseException 设定文件
	 * @return Date 返回类型
	 * @throws
	 */
	public static Date getAfterDaysDate(Date now, int days) throws ParseException {
		long l = now.getTime() + 24l * 60 * 60 * 1000 * days;
		return new Date(l);
	}

	public static Date addDay(Date date, Long addDay) {
		return new Date(date.getTime() + (1000 * 60 * 60 * 24 * addDay));
	}

	public static String getSinaReqCurrenntTime() {
		SimpleDateFormat sf = (SimpleDateFormat) SINA_YMDHMS_SF_HH.clone();
		return sf.format(new Date());
	}

	public static SimpleDateFormat getSINA_YMDHMS_SF_HH() {
		return SINA_YMDHMS_SF_HH;
	}

	/**
	 * 
	 * @param recordCreateTime
	 *            投资成功时间
	 * @return
	 */
	public static Date getStartLiDate(Date recordCreateTime) {
		Date tenderSuccDate = recordCreateTime;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tenderSuccDate);
		calendar.set(Calendar.HOUR, 15);
		calendar.set(Calendar.MINUTE, 1);
		calendar.set(Calendar.SECOND, 0);
		Date threePm = calendar.getTime();

		if (tenderSuccDate.after(threePm)) {// T+2
			calendar.add(Calendar.DATE, 2);
		} else {// T+1
			calendar.add(Calendar.DATE, 1);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

}
