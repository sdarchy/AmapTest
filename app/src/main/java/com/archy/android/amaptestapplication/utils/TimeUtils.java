package com.archy.android.amaptestapplication.utils;

import android.content.Context;
import android.text.format.DateFormat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class TimeUtils {

    public static final long TIME_SECOND = 1000;
    public static final long TIME_MINUTE = 60 * TIME_SECOND;
    public static final long TIME_HOUR = 60 * TIME_MINUTE;
    public static final long TIME_DAY = 24 * TIME_HOUR;
    public static final long TIME_HALF_AN_HOUR = 30 * TIME_MINUTE;

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT_VOUCHER=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat TIME_MONTH_FORMAT=new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat TIME_FORMAT_SECOND = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat TIME_FROMAT_MINUTE_SECOND=new SimpleDateFormat("mm:ss");
    public static final SimpleDateFormat TIME_STRING_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat TIME_YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyyMM");
    public static final SimpleDateFormat DATE_SHORT_TIME_STRING_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss");
    public static final SimpleDateFormat CHINESE_MM_DD = new SimpleDateFormat("MM月dd日 HH:mm");

    //获取当天的开始时间
    public static Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    //获取当天的结束时间
    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    /**
     * 获取当前时区信息
     *
     * @return
     */
    public static String getCurrentTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        return createGMTFormatString(true, true, timeZone.getRawOffset());
    }

    private static String createGMTFormatString(boolean includeGmt,
                                                boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }

    public static long date2TimeStamp(String dateStr, SimpleDateFormat sdf) {
        try {
            return  sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String changeTimeShowType(String dateStr, SimpleDateFormat currentType, SimpleDateFormat wantType){
        return getTime(date2TimeStamp(dateStr,currentType),wantType);
    }


    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }
    public static String getTimeMin(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT2);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 过去多少天
     *
     * @param day 天数
     */
    public static boolean pastDay(long time, int day) {
        return System.currentTimeMillis()-time >= TIME_DAY * day;
    }

    public static boolean pastDayNotContent(long time, int day) {
        return System.currentTimeMillis()-time > TIME_DAY * day;
    }

    public static int pastDay(long time) {
        return (int) (time / TIME_DAY);
    }

    /**
     * 过去多少小时
     *
     * @param hour 小时数
     */
    public static boolean pastOneHour(long time, int hour) {
        return Math.abs(time - System.currentTimeMillis()) >= TIME_HOUR * hour;
    }

    /**
     * 过去多少分钟
     *
     * @param minute 分钟数
     */
    public static boolean pastOneMinute(long time, int minute) {
        return Math.abs(time - System.currentTimeMillis()) >= TIME_MINUTE * minute;
    }

    /**
     * 过去几分钟不包含边界
     * @param time
     * @param minute
     * @return
     */
    public static boolean pastOneMinuteNoContent(long time, int minute){
        return Math.abs(time - System.currentTimeMillis()) > TIME_MINUTE * minute;
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * @return minute
     */
    public static int getCurrentMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * @return hour
     */
    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }


    /**
     * @return day
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回起始时间与当前时间的时间差
     *
     * @param time 起始时间
     * @return
     */
    public static long getTimeIntervalToNow(long time) {
        return getCurrentTimeInLong() - time;
    }

    /**
     * 结束时间当前时间的时间差
     *
     * @param time 结束时间
     * @return
     */
    public static long getEndTimeToNow(long time) {
        return time - getCurrentTimeInLong();
    }

    /**
     * 获取日期类型时间
     *
     * @param timeInMillis
     * @return
     */
    public static String getTimeInData(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * 是否在夜间 22点到7点
     */
    public static boolean isNight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour >= 22 && hour <= 7;
    }


    /**
     * @return true if clock is set to 24-hour mode
     */
    public static boolean get24HourMode(final Context context) {
        return DateFormat.is24HourFormat(context);
    }

    public static int[] convertToTime(long time) {
        int hour = (int) (time / TIME_HOUR);
        int minutes = (int) ((time % TIME_HOUR) / TIME_MINUTE);
        int second = (int) ((time % TIME_MINUTE) / TIME_SECOND);
        return new int[]{hour, minutes,second};
    }
    public static String[] convertToTimeStr(long time) {
        String hourStr="00";
        String minStr="00";
        String sedStr="00";
        int hour = (int) (time / TIME_HOUR);
        if (hour<=0){
            hourStr="00";
        }else if (hour<10){
            hourStr="0"+hour;
        }else {
            hourStr= String.valueOf(hour);
        }
        int minutes = (int) ((time % TIME_HOUR) / TIME_MINUTE);
        if (minutes<=0){
            minStr="00";
        }else if (minutes<10){
            minStr="0"+minutes;
        }else {
            minStr= String.valueOf(minutes);
        }
        int second = (int) ((time % TIME_MINUTE) / TIME_SECOND);
        if (second<=0){
            sedStr="00";
        }else if (second<10){
            sedStr="0"+second;
        }else {
            sedStr= String.valueOf(second);
        }
        return new String[]{hourStr, minStr,sedStr};
    }
    /***红包雨时间计算**/
    public static String[] convertHongBaoTimeStr(long time) {
        time=time/1000;
        String dayStr;
        String hourStr;
        String minStr;
        String sedStr;
        long day1=time/(24*3600);
        long hour1=time%(24*3600)/3600;
        long minutes=time%3600/60;
        long second=time%60;

        if (day1==0){
            dayStr="00";
        }else if (hour1<10){
            dayStr="0"+day1;
        }else {
            dayStr= String.valueOf(day1);
        }
        if (hour1==0){
            hourStr="00";
        }else if (hour1<10){
            hourStr="0"+hour1;
        }else {
            hourStr= String.valueOf(hour1);
        }

        if (minutes==0){
            minStr="00";
        }else if (minutes<10){
            minStr="0"+minutes;
        }else {
            minStr= String.valueOf(minutes);
        }
        if (second==0){
            sedStr="00";
        }else if (second<10){
            sedStr="0"+second;
        }else {
            sedStr= String.valueOf(second);
        }
        return new String[]{dayStr,hourStr, minStr,sedStr};
    }
   public static String getMonthLastDay(){
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       String lastday;
       Calendar cale = null;

       // 获取前月的最后一天
       cale = Calendar.getInstance();
       cale.add(Calendar.MONTH, 1);
       cale.set(Calendar.DAY_OF_MONTH, 0);
       lastday = format.format(cale.getTime());
       return lastday;

   }
   public static String getNextMonthLastDay(){
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       String lastday;
       Calendar cale = null;

       // 获取前月的最后一天
       cale = Calendar.getInstance();
       cale.add(Calendar.MONTH, 2);
       cale.set(Calendar.DAY_OF_MONTH, 0);
       lastday = format.format(cale.getTime());
       return lastday;
   }


    /**
     * 获取当月开始时间戳
     *
     * @param
     * @param timeZone  如 GMT+8:00
     * @return
     */
    public static Long getMonthStartTime(String timeZone, boolean isCurrentMonth) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.add(Calendar.YEAR, 0);
        if(isCurrentMonth){
        calendar.add(Calendar.MONTH, 0);
        }
        else {
        calendar.add(Calendar.MONTH, 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月的结束时间戳
     *
     * @param
     * @param timeZone  如 GMT+8:00
     * @return
     */
    public static Long getMonthEndTime(String timeZone, boolean isCurrentMonth) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.add(Calendar.YEAR, 0);
        if(isCurrentMonth){
            calendar.add(Calendar.MONTH, 0);
        }
        else {
            calendar.add(Calendar.MONTH, 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 判断是否是今年
     * @param day
     * @param simpleDateFormat
     * @return
     * @throws Exception
     */
    public static boolean isThisYear(String day, SimpleDateFormat simpleDateFormat) throws Exception {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();

        Date date = simpleDateFormat.parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是今天
     * @param day 传入的时间
     * @param simpleDateFormat 时间的格式
     * @return 是否是今天
     * @throws ParseException
     */
    public static boolean IsToday(String day, SimpleDateFormat simpleDateFormat) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();

        Date date = simpleDateFormat.parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 加上几天时间
     * @param startTime 起始时间
     * @param day 天数
     * @param changeSimpleDateFormat
     * @return
     */
    public static String addDay(long startTime, int day, SimpleDateFormat changeSimpleDateFormat){
        long endTime = startTime + TIME_DAY * day;
        String format = changeSimpleDateFormat.format(endTime);
        return format;
    }

    /**
     * 加上几个月
     * @param startTime 起始时间
     * @param month 月份
     * @param changeSimpleDateFormat
     * @return
     */
    public static String addMonth(long startTime, int month, SimpleDateFormat changeSimpleDateFormat){
        Date date = new Date(startTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        cal.add(Calendar.MONTH, month);//增加一个月
        String format = changeSimpleDateFormat.format(cal.getTime());
        return format;
    }

    /**
     * 加上几年的时间
     * @param startTime 起始时间
     * @param year 年
     * @param changeSimpleDateFormat
     * @return
     */
    public static String addYear(long startTime, int year, SimpleDateFormat changeSimpleDateFormat){
        Date date = new Date(startTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        cal.add(Calendar.YEAR, year);//增加一年
        String format = changeSimpleDateFormat.format(cal.getTime());
        return format;
    }
}
