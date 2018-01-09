package cst.gu.util.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cst.gu.util.string.StringUtil;

/**
 * @author guweichao 
 * @version 0.1
 * 本地日期格式工具类（针对CN_ZH的dateutil）<br>
 * 默认的时间格式使用 yyyy-MM-dd HH:mm:ss.SSS<br>
 * @author guweichao 20170520<br>
 *
 */
public final class LocalDateUtil {
	private LocalDateUtil(){}
	
	/** yyyy-MM-dd */
	public static final String format_yMd = "yyyy-MM-dd";
	
	/** yyyy-MM-dd HH:mm:ss */
	public static final String format_yMdHms = "yyyy-MM-dd HH:mm:ss";
	
	/** yyyy-MM-dd HH:mm:ss.SSS */
	public static final String format_yMdHmsS = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * 使用SimpleDateFormat 解析字符串成为时间
	 * 如果未能解析成功,返回null
	 */
	public static Date simpleParse(String date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = null;
		try{
			d = sdf.parse(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * @author guweichao 20170524
	 * 使用SimpleDateFormat 格式化时间 
	 * @param date 时间
	 * @param fomart 时间格式
	 * @return
	 */
	public static String simpleFormat(Date date, String fomart) {
		SimpleDateFormat sdf = new SimpleDateFormat(fomart);
		return sdf.format(date);
	}
	
	/**
	 * @author guweichao
	 * 格式化时间 使用 yyyy-MM-dd
	 */
	public static String simpleFormatDay(Date date) {
		return simpleFormat(date, format_yMd);
	}
	
	/**
	 * 使用SimpleDateFormat 解析 yyyy-MM-dd格式的字符串
	 * 返回对应时间
	 */
	public static Date simpleParseDay(String date) {
		return simpleParse(date, format_yMd);
	}
	
	/**
	 *格式化时间 使用 yyyy-MM-dd HH:mm:ss
	 */
	public static String simpleFormatSecond(Date date) {
		return simpleFormat(date, format_yMdHms);
	}
	
	/**
	 * 使用SimpleDateFormat 解析  yyyy-MM-dd HH:mm:ss格式的字符串
	 * 返回对应时间
	 */
	public static Date simpleParseSecond(String date) {
		return  simpleParse(date, format_yMdHms);
	}
	
	//millisecond
	/**
	 *格式化时间 使用 yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static String simpleFormatMillisecond(Date date) {
		return simpleFormat(date, format_yMdHmsS);
	}
	
	/**
	 * 使用SimpleDateFormat 解析  yyyy-MM-dd HH:mm:ss.SSS格式的字符串
	 * 返回对应时间
	 */
	public static Date simpleParseMillisecond(String date) {
		return  simpleParse(date, format_yMdHmsS);
	}
	
	
	/**
	 * 返回两个时间相差的分钟数
	 * end - start
	 */
	public static long getMinitesBetween(Date start, Date end) {
		return (end.getTime() - start.getTime()) / (1000 * 60);
	}
	
    @SuppressWarnings({ "deprecation" })
    public static int getYearsBetween(Date start,Date end){
        return end.getYear() - start.getYear();
    }
    
    @SuppressWarnings({ "deprecation" })
    public static int getMonthsBetween(Date start,Date end){
        return 12*getYearsBetween(start, end) + end.getMonth() - start.getMonth();
    }
	
    /**
     * 获取两个日期之间相差的周数(以自然日为准,以本周日23:59-下周一00:00为周期差)
     */
    @SuppressWarnings("deprecation")
	public static int getWeeksBetween(Date start,Date end){ 
    	start = new Date(start.getYear(),start.getMonth(),start.getDate());
		end = new Date(end.getYear(),end.getMonth(),end.getDate());
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(start);
		startCal.setFirstDayOfWeek(2);
		startCal.set(Calendar.DAY_OF_WEEK, 1);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		endCal.setFirstDayOfWeek(2);
		endCal.set(Calendar.DAY_OF_WEEK, 1);
		
		long timeInterval = endCal.getTimeInMillis() - startCal.getTimeInMillis();
		int weekTime = 1000 * 60 * 60 * 24 * 7;
		int dayInterval = (int) (timeInterval / weekTime);
		if(timeInterval % weekTime != 0){
			dayInterval ++;
		}
		return dayInterval;
    }

	/**
	 * guweichao 20170420
	 * 获取两个日期之间相差的天数(以自然日为准,以23:59-00:00为日期替换)
	 * @param start
	 * @param end
	 * @return
	 */
    @SuppressWarnings({ "deprecation" })
	public static long getDaysBetween(Date start, Date end) {
		start = new Date(start.getYear(),start.getMonth(),start.getDate());
		end = new Date(end.getYear(),end.getMonth(),end.getDate());
		long timeInterval = end.getTime() - start.getTime();
		int dayTime = 1000 * 60 * 60 * 24;
		int dayInterval = (int) (timeInterval / dayTime);
		if(timeInterval % dayTime != 0){
			dayInterval ++;
		}
		return dayInterval;
	}
	
	/**
	 * 在传入的时间基础上,增加指定天数并返回
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDay(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	/**
	 * 
	 * @param dateStr\
	 * @param format 时间使用的格式
	 * @param days
	 * @return
	 * @throws ParseException 
	 */
	public static String addDay(String dateStr,String format, int days) throws ParseException {
		Date date = simpleParse(dateStr,format);
		date = addDay(date, days);
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 *  在传入的时间基础上,增加指定月数并返回
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date addMonth(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	/**
	 * 在传入的时间基础上,增加指定月数并返回
	 * @param dateStr
	 * @param format 时间使用的格式
	 * @param months
	 * @return
	 */
	public static String addMonth(String dateStr,String format, int months) {
		Date date = simpleParse(dateStr,format);
		date = addMonth(date, months);
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 *  在传入的时间基础上,增加指定年数并返回
	 *  
	 */
	public static Date addYear(Date date, int years) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}
	
	/**
	 *  在传入的时间基础上,增加指定年数并返回
	 */
	public static String addYear(String dateStr,String format, int years) {
		Date date = simpleParse(dateStr,format);
		date = addYear( date , years);
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 *  获取所传入时间所属周的周一
	 *   其他时间可以获取本时间后使用addDate来获取 
	 *   一周的起点为周一(monday)
	 * @param date
	 * @return
	 */
	public static Date getWeekMonday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(2);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return c.getTime();
	}
	
	/**
	 * 获取传入时间所在月份第一天
	 */
	@SuppressWarnings("deprecation")
	public static Date getMonthStart(Date date){
		date.setDate(1);
		return date;
	}
	
	/**
	 * 获取传入时间所在月份最后一天
	 */
	public static Date getMonthEnd(Date date){
		date = addMonth(date, 1); // +1个月
		date = getMonthStart(date);
		date = addDay(date, -1);
		return date;
	}
	
	/**
	 * 获取传入时间所在年第一天
	 * @param date
	 * @return
	 */
	@SuppressWarnings(value = {"deprecation" })
	public static Date getYearStart(Date date){
		date.setDate(1);
		date.setMonth(0);
		return date;
	}
	
	/**
	 * 获取传入时间所在年最后一天
	 * @param date
	 * @return
	 */
	public static Date getYearEnd(Date date){
		date = addYear(date, 1);
		date = getYearStart(date);
		date = addDay(date, -1);
		return date;
	}
	
	/**
	 * isemp3
	 * 返回日期在一周中所处的位置
	 * 1 : 周一
	 * 7 : 周日
	 */
	 public static int getDayOfWeek(Date date){
		 Calendar calendar = Calendar.getInstance(); 
		 calendar.setTime(date); 
		 int weeknum=calendar.get(Calendar.DAY_OF_WEEK);
		 if(weeknum == 1){
			 return 7;
		 }else {
			 return weeknum - 1;
		 }
	 }

	 /**
	  * 返回日期所在的周 在一年中是第几周
	  */
	public static int getWeekOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		return c.get(Calendar.WEEK_OF_YEAR);
	}
	
	
	/**
	 * 获取季度
	 * @param date
	 * @return 1 2 3 4
	 */
	@SuppressWarnings("deprecation")
	public static int getQuarter(Date date){
		return date.getMonth()/3 + 1;
	}
	
	/**
	 * 获取今天的字符串格式
	 * yyyy-MM-dd 00:00:00
	 * @return
	 */
	public static String getToday(){
		return simpleFormatDay(new Date()) + " 00:00:00";
	}
	

	/**
	 * 获取当前时刻的字符串格式
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getNow(){
		return simpleFormatSecond(new Date());
	}
	
	
	/**
	 * 时间转化  
	 * 90m --> 01h30m
	 * @return 
	 * @param minute 分钟数  1233m 或者 1233 
	 * 如果 传入分钟数带有小数点,将丢弃小数点后的值 如 123.55m 将被作为123m处理
	 */
	public static String minute2HourMinute(String minute){
		String rmin = "00h00m";
		if(StringUtil.isTrimBlank(minute)){
			return rmin;
		}
		minute = minute.toLowerCase();
		int x = minute.indexOf(".");
		if( x > -1){
			minute = minute.substring(0,x);
		}
		int y = minute.indexOf("m");
		if(y > -1){
			minute = minute.substring(0,y);
		}
		int minI = StringUtil.tryToInt(minute);
		if(minI <= 0){
			return rmin;
		}
		int hour = minI / 60;
		int min = minI % 60;
		if(hour == 0){
			rmin = "00h";
		}else if(hour < 10){
			rmin = "0"+hour + 'h';
		}else{
			rmin = hour +"h";
		}
		
		if(min == 0){
			rmin += "00m";
		}else if(min < 10){
			rmin = rmin + '0' +min +'m';
		}else{
			rmin = rmin + min + 'm';
		}
		return rmin;
	}
	
	/**
	 * 时间转化  
	 * 小时数转化为时分
	 * 1.5h --> 01h30m
	 * @return 
	 * @param hour 1.5 或者 1.5h
	 */
	public static String hour2HourMinite(String hour) {
		String rmin = "00h00m";
		if (StringUtil.isTrimBlank(hour)) {
			return rmin;
		}
		hour = hour.toLowerCase();
		int x = hour.indexOf("h");
		if (x > -1) {
			hour = hour.substring(0, x);
		}
		double hourD = Double.parseDouble(hour);
		return minute2HourMinute(hourD * 60 + "");
	}
	
	/**
     * 01h30m ---> 90
     * @param hm 时间 标准格式为 12h22m,可以使用12h22  133m 等
     * 其他情况可能返回结果不是预期
     * @return
     */
    public static int hourMinute2Minutes(String hm){
    	if(StringUtil.isTrimBlank(hm)){
    		return 0;
    	}
    	hm = hm.toLowerCase();
    	int indexH = hm.indexOf('h');
    	int indexM = hm.indexOf('m');
    	int h = 0;
    	int m ;
    	if(indexH > -1){ // 12h22m 或 12h22
    		String hourStr = hm.substring(0, indexH);
    		h = Integer.parseInt(hourStr);
    		if(indexM > indexH){
    			String minStr = hm.substring(indexH+1,indexM);
    			m = Integer.parseInt(minStr);
    		}else{
    			String minStr = hm.substring(indexH+1,hm.length());
    			m = Integer.parseInt(minStr);
    		}
    	}else{// 33m 或33
    		if(indexM > -1){
    			hm = hm.substring(0,indexM);
    		}
    		m = Integer.parseInt(hm);
    	}
    	return h*60 + m;
    }
    

	
	/**
	 * @author guweichao:尝试解析时间
	 *  如: 2015-03-04 13:22:22.999 <br>
	 *  2015/3/4 3:12:4 999<br>
	 *  "Time:2014.0304 13.22.22.444"等<br>
	 * 注意: 只能使用阿拉伯数字表示时间 其中所占位置为:(年:4位;月日时分秒:2位;毫秒:3位)<br>
	 * 如果数字与位置大小一致(年占4位,月日时分秒占2位)可以没有分隔符,如果实际长度小于位置长度,则必须有分隔符<br>
	 * 即20150102 2014/1/3-13:22:22等可以解析成功,201512会解析错误<br>
	 * @param time
	 * @return java.util.Date
	 * @throws ParseException:如果内容解析错误
	 */
	public static Date tryParseDate(String date) throws ParseException{
		date = changeFormat(date);
		String format = "yyyy-MM-dd-HH-mm-ss-SSS".substring(0,date.length());
		return simpleParse(date, format);
	}
	
	/**
	 * 将自定义的时间字符串替换其中的分隔符，变为本类使用的格式
	 * @param time
	 */
	private static String changeFormat(String time){ 
		int olen = time.length();
		StringBuilder newTime = new StringBuilder();
		char[] cs = new char[4];
		time = getNumberChars(time, cs);
		int nlen = time.length();
		if( olen > nlen){
			int c = 4;
			newTime.append(cs); // 年 - 4位
			cs = new char[2];
			olen = nlen;
			time = getNumberChars(time, cs);
			nlen = time.length();
			while ( olen > nlen && c > 0){
				c --;
				newTime.append('-').append(cs);//月日时分秒--2位
				olen = nlen;
				time = getNumberChars(time, cs);
				nlen = time.length();
			}
		}
		if(olen > nlen){ //秒 -- 2位
			newTime.append('-').append(cs);
			cs = new char[3];
			olen = nlen ;
			time = getNumberChars(time, cs);
			nlen = time.length();
			if(olen > nlen){
				newTime.append('-').append(cs);// 毫秒
			}
		}
		return newTime.toString();
	}
	
	private static String getNumberChars(String str,char[] chars){
		if(str.length() > 0){
			int start = 0;
			char[] sc = str.toCharArray();
			while(start < sc.length){
				char c = sc[start];
				if( c < '0' || c > '9'){
					start ++ ;
					continue;
				}
				break;
			}
			int end = start + 1;
			int x = 1;
			while(end < sc.length && x < chars.length){
				char c = sc[end];
				if( c < '0' || c > '9'){
					break;
				}
				end ++;
				x ++;
			}
			x = 0;
			int lacks = chars.length - (end - start); // chars的长度和 取到的字符如果不一样,需要补充0
			while(lacks > 0){
				lacks --;
				chars[x++] = '0';
			}
			while(x < chars.length){
				chars[x++] = sc[start++];
			}
			return str.substring(end);
		}
		return str;
		
	}
}
