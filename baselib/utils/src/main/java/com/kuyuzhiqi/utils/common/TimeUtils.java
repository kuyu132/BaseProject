package com.kuyuzhiqi.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TimeUtils
 */
public class TimeUtils {

    /**
     * 精确到年
     **/
    public static final String YEAR_DATE_FORMAT = "yyyy";
    /**
     * 精确到月
     **/
    public static final String MONTH_DATE_FORMAT = "yyyy-MM";
    /**
     * 精确到日
     **/
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 精确到分
     **/
    public static final String MINUTE_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    /**
     * 精确到秒
     **/
    public static final String SECOND_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 文件名使用,精确到毫秒
     **/
    public static final String FILE_NAME_DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss_SSS";
    /**
     * 只显示小时和分钟
     */
    public static final String ONLY_HOUR_AND_MINUTE_FORMAT = "HH:mm";
    /**
     * 只显示月份和天数
     */
    public static final String ONLY_MONTH_AND_DAY_FORMAT = "MM-dd";

    /**
     * 一天包含的毫秒数
     **/
    public static final int MILLIS_IN_ONE_DAY = 1000 * 60 * 60 * 24;

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * 获取剩余的天数,结束日期应该大于开始日期,否则返回-1
     */
    public static int getRemainingDaysCount(long beginTimeInMillis, long endTimeInMillis) {

        if (endTimeInMillis > beginTimeInMillis) {
            Calendar pCalendar = Calendar.getInstance();

            // 设置时间为0时
            pCalendar.setTimeInMillis(beginTimeInMillis);
            pCalendar.set(Calendar.HOUR_OF_DAY, 0);
            pCalendar.set(Calendar.MINUTE, 0);
            pCalendar.set(Calendar.SECOND, 0);
            //获取0时对应的毫秒时间
            beginTimeInMillis = pCalendar.getTimeInMillis();

            pCalendar.setTimeInMillis(endTimeInMillis);
            pCalendar.set(Calendar.HOUR_OF_DAY, 0);
            pCalendar.set(Calendar.MINUTE, 0);
            pCalendar.set(Calendar.SECOND, 0);
            //获取0时对应的毫秒时间
            endTimeInMillis = pCalendar.getTimeInMillis();

            // 得到两个日期相差的天数
            int pDaysCount = new Long((endTimeInMillis - beginTimeInMillis) / MILLIS_IN_ONE_DAY).intValue() + 1;
            return pDaysCount;
        } else {
            return -1;
        }
    }

    /**
     * 判断日期是否同一天
     */
    public static boolean areSameDay(Date dateA, Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 时间取年月日后,获取间隔天数
     */
    public static long getDayInterval(Date dateA, Date dateB) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(dateA);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(dateB);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 将日期的时分秒设置为00：00：00
     */
    public static long setTime2DayBegin(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 将日期的时分秒设置为23：59：59
     */
    public static long setTime2DayEnd(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        return calendar.getTimeInMillis();
    }

    public static String getTimeStr(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static String getTimeStr(Date date) {
        return getTimeStr(date, DEFAULT_DATE_FORMAT);
    }

    public static String getTimeStr(long timeInMillis, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date(timeInMillis));
    }

    public static String getTimeStr(long timeInMillis) {
        return getTimeStr(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    public static String getCurrentTimeInString() {
        return getTimeStr(getCurrentTimeInLong());
    }

    public static String getCurrentTimeInString(String dateFormat) {
        return getTimeStr(getCurrentTimeInLong(), dateFormat);
    }

    public static String getTimeWithYear(long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_DATE_FORMAT);
        return simpleDateFormat.format(mills);
    }

    public static String getTimeWithMonthAndDay(long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ONLY_MONTH_AND_DAY_FORMAT);
        return simpleDateFormat.format(mills);
    }


    //return time format by "yyyy-MM--dd HH:mm:ss"
    public static String getTimeWithSecond(long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SECOND_DATE_FORMAT);
        return simpleDateFormat.format(mills);
    }

    //return time format by "yyyy-MM--dd HH:mm" 24h
    public static String getTimeWithMin(long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MINUTE_DATE_FORMAT);
        return simpleDateFormat.format(mills);
    }

    public static Calendar getCalendarFromTimeStamp(long mills) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mills);
        return cal;
    }

    public static long getTimeStamp(String timeStr, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            long time = simpleDateFormat.parse(timeStr).getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }


    /**
     * 本地获取的时间戳(13位)转化为服务器时间戳(10位)
     *
     * @param mills
     * @return
     */
    public static long toServerTimestamp(long mills) {
        return Math.round(mills / 1000.0);
    }

    /**
     * 获取某个日期的年份
     *
     * @param mills
     * @return
     */
    public static int getYear(long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(simpleDateFormat.format(mills));
    }

    /**
     * 获取某个日期的月份
     *
     * @param mills
     * @return
     */
    public static int getMonth(long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(simpleDateFormat.format(mills));
    }

    /**
     * 获取某个日期的日
     *
     * @param mills
     * @return
     */
    public static int getDay(long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        return Integer.parseInt(simpleDateFormat.format(mills));
    }

    /**
     * 获取某一天的0点的时间戳
     */
    public static long getCurrentStartTimestamp(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getYear(mills), getMonth(mills)-1, getDay(mills));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 根据当前时间获取当前周开始的时间戳
     *
     * @return
     */
    public static long getCurrentWeekStartTimestamp(long mills) {
        Date date = new Date(mills);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        calendar.add(Calendar.DATE, 2 - dayOfWeek);
        return getCurrentStartTimestamp(calendar.getTimeInMillis());
    }

    /**
     * 根据当前时间获取当前月的开始时间戳
     *
     * @return
     */
    public static long getCurrentMonthStartTimestamp(long mills) {
        Date date = new Date(mills);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(getYear(mills), getMonth(mills) - 1, 1);
        return getCurrentStartTimestamp(calendar.getTimeInMillis());
    }

}
