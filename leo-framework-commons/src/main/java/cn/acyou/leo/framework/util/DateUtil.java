package cn.acyou.leo.framework.util;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
public final class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    public static final String FORMAT_SHORT_DATE = "yyyyMMdd";
    public static final String FORMAT_DEFAULT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DEFAULT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_TIME_2 = "yyyy-M-d HH:mm:ss";
    public static final String FORMAT_DATE_TIME_UNSIGNED = "yyyyMMddHHmmss";
    public static final String DATE_DAY_MIN_TIME = " 00:00:00";
    public static final String DATE_DAY_MAX_TIME = " 23:59:59";
    public static final char[] UPPER_NUMBER = "〇一二三四五六七八九十".toCharArray();
    /**
     * 一小时的秒数 = 60*60
     */
    public static final long ONE_HOUR_SECOND = 3600;
    public static final int DAYS_PER_WEEKEND = 2;
    public static final int WEEK_START = DateTimeConstants.MONDAY;
    public static final int WEEK_END = DateTimeConstants.FRIDAY;

    public enum Unit {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND;
    }

    private DateUtil() {

    }

    /**
     * 获取指定日期   yyyy-MM-dd HH:mm:ss格式
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String getDateFormat(Date date) {
        return getDateFormat(date, FORMAT_DATE_TIME);
    }
    /**
     * 获得当前日期   yyyy-MM-dd HH:mm:ss格式
     *
     * @return {@link String}
     */
    public static String getCurrentDateFormat() {
        return getDateFormat(new Date(), FORMAT_DATE_TIME);
    }
    /**
     * 获取指定日期   yyyyMMdd格式
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String getDateShortFormat(Date date) {
        return getDateFormat(date, FORMAT_SHORT_DATE);
    }
    /**
     * 获取指定日期   yyyy-MM-dd格式
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String getDateDefaultFormat(Date date) {
        return getDateFormat(date, FORMAT_DEFAULT_DATE);
    }
    /**
     * 获得当前日期    yyyyMMdd格式
     *
     * @return {@link String}
     */
    public static String getCurrentDateShortFormat() {
        return getDateFormat(new Date(), FORMAT_SHORT_DATE);
    }
    /**
     * 获得当前日期    yyyy-MM-dd格式
     *
     * @return {@link String}
     */
    public static String getCurrentDateDefaultFormat() {
        return getDateFormat(new Date(), FORMAT_DEFAULT_DATE);
    }
    /**
     * 获得当前日期   指定格式
     *
     * @param format 格式
     * @return {@link String}
     */
    public static String getCurrentDateFormat(String format) {
        return getDateFormat(new Date(), format);
    }
    /**
     * 获取指定日期   指定格式
     *
     * @param date   日期
     * @param format 格式
     * @return {@link String}
     */
    public static String getDateFormat(Date date, String format) {
        if (date == null){
            return "";
        }
        return new DateTime(date).toString(format);
    }
    /**
     * 解析日期     以"yyyy-MM-dd"格式
     *
     * @param dateStr str日期
     * @return {@link Date}
     */
    public static Date parseDefaultDate(String dateStr) {
        return parseDate(dateStr, FORMAT_DEFAULT_DATE);
    }

    /**
     * 创建日期日期（年月日 时分秒）
     *
     * @param year           年
     * @param monthOfYear    月
     * @param dayOfMonth     日
     * @param hourOfDay      时
     * @param minuteOfHour   分
     * @param secondOfMinute 秒
     * @return 日期
     */
    public static Date newDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute) {
        return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute).toDate();
    }
    /**
     * 创建日期日期（年月日 00:00:00）
     *
     * @param year           年
     * @param monthOfYear    月
     * @param dayOfMonth     日
     * @return {@link Date}
     */
    public static Date newDate(int year, int monthOfYear, int dayOfMonth) {
        return new DateTime(year, monthOfYear, dayOfMonth, 0, 0, 0).toDate();
    }

    /**
     * 解析具体的时间
     * @param dateStr yyyy-MM-dd HH:mm:ss 类型
     * @return Date类型
     */
    public static Date parseSpecificDateTime(String dateStr) {
        return parseDate(dateStr, FORMAT_DATE_TIME);
    }
    /**
     * 解析日期     以指定格式
     *
     * @param dateStr str日期
     * @param format  格式
     * @return {@link Date}
     */
    public static Date parseDate(String dateStr, String format) {
        return DateTimeFormat.forPattern(format).parseDateTime(dateStr).toDate();
    }


    /**
     * 给日期添加时间
     *
     * @param date   指定日期
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return 日期
     */
    public static Date add(Date date, int year, int month, int day, int hour, int minute, int second) {
        return new DateTime(date).plusYears(year).plusMonths(month).plusDays(day).plusHours(hour).plusMillis(minute).plusSeconds(second).toDate();
    }

    /**
     * 给日期减少时间
     *
     * @param date   指定日期
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return 日期
     */
    public static Date minus(Date date, int year, int month, int day, int hour, int minute, int second) {
        return new DateTime(date).minusYears(year).minusMonths(month).minusDays(day).minusHours(hour).minusMillis(minute).minusSeconds(second).toDate();
    }

    /**
     * 向日期增加天数
     *
     * @param date 日期
     * @param day  天数
     * @return {@link Date}
     */
    public static Date addDay(Date date, int day) {
        return new DateTime(date).plusDays(day).toDate();
    }

    /**
     * 向日期减少天数
     *
     * @param date 日期
     * @param day  天数
     * @return {@link Date}
     */
    public static Date minusDay(Date date, int day) {
        return new DateTime(date).minusDays(day).toDate();
    }

    /**
     * 向日期增加月数
     *
     * @param date  日期
     * @param month 月
     * @return {@link Date}
     */
    public static Date addMonth(Date date, int month) {
        return new DateTime(date).plusMonths(month).toDate();
    }
    /**
     * 向日期增加年数
     *
     * @param date  日期
     * @param year 年
     * @return {@link Date}
     */
    public static Date addYear(Date date, int year) {
        return new DateTime(date).plusYears(year).toDate();
    }

    /**
     * 向日期减少月数
     *
     * @param date  日期
     * @param month 月
     * @return {@link Date}
     */
    public static Date minusMonth(Date date, int month) {
        return new DateTime(date).minusMonths(month).toDate();
    }

    /**
     * 向日期增加小时
     *
     * @param date 日期
     * @param hour 小时
     * @return {@link Date}
     */
    public static Date addHour(Date date, int hour) {
        return new DateTime(date).plusHours(hour).toDate();
    }

    /**
     * 向日期减少小时
     *
     * @param date 日期
     * @param hour 小时
     * @return {@link Date}
     */
    public static Date minusHour(Date date, int hour) {
        return new DateTime(date).minusHours(hour).toDate();
    }

    /**
     * 向日期增加分钟
     *
     * @param date    日期
     * @param minutes 分钟
     * @return {@link Date}
     */
    public static Date addMinutes(Date date, int minutes) {
        return new DateTime(date).plusMinutes(minutes).toDate();
    }

    /**
     * 向日期减少分钟
     *
     * @param date    日期
     * @param minutes 分钟
     * @return {@link Date}
     */
    public static Date minusMinutes(Date date, int minutes) {
        return new DateTime(date).minusMinutes(minutes).toDate();
    }

    /**
     * 向日期增加秒
     *
     * @param date    日期
     * @param seconds 秒
     * @return {@link Date}
     */
    public static Date addSeconds(Date date, int seconds) {
        return new DateTime(date).plusSeconds(seconds).toDate();
    }

    /**
     * 向日期减少秒
     *
     * @param date    日期
     * @param seconds 秒
     * @return {@link Date}
     */
    public static Date minusSeconds(Date date, int seconds) {
        return new DateTime(date).minusSeconds(seconds).toDate();
    }

    /**
     * 在日期范围内的随机日期
     *
     * @param startStr 开始str
     * @param endStr   str结束
     * @return {@link Date}
     */
    public static Date randomRangeDate(String startStr, String endStr) {
        long startTime = new DateTime(startStr).toDate().getTime();
        long endTime = new DateTime(endStr).toDate().getTime();
        double randomDate = Math.random() * (endTime - startTime) + startTime;
        DateTime random = new DateTime(Math.round(randomDate));
        return random.toDate();
    }

    /**
     * 比较两个时间相差多少秒
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return long
     */
    public static long getDiffSeconds(Date startDate, Date endDate) {
        return Math.abs((endDate.getTime() - startDate.getTime()) / 1000);
    }

    /**
     * 比较两个时间相差多少分钟
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return long
     */
    public static long getDiffMinutes(Date startDate, Date endDate) {
        long diffSeconds = getDiffSeconds(startDate, endDate);
        return diffSeconds / 60;
    }

    /**
     * 比较两个时间相差多少小时
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return long
     */
    public static long getDiffHour(Date startDate, Date endDate) {
        long diffMinutes = getDiffMinutes(startDate, endDate);
        return diffMinutes / 60;
    }

    public static BigDecimal getDiff(Date startDate, Date endDate, Unit unit) {
        long timeStart = startDate.getTime();
        long timeEnd = endDate.getTime();
        switch (unit) {
            case MILLISECOND:
                return new BigDecimal(timeEnd - timeStart);
            case SECOND:
                return new BigDecimal(timeEnd - timeStart)
                        .divide(new BigDecimal(1000), 10, RoundingMode.FLOOR);
            case MINUTE:
                return new BigDecimal(timeEnd - timeStart)
                        .divide(new BigDecimal(1000), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR);
            case HOUR:
                return new BigDecimal(timeEnd - timeStart)
                        .divide(new BigDecimal(1000), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR);
            case DAY:
                return new BigDecimal(timeEnd - timeStart)
                        .divide(new BigDecimal(1000), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(24), 10, RoundingMode.FLOOR);
            case MONTH:
                return new BigDecimal(timeEnd - timeStart)
                        .divide(new BigDecimal(1000), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(24), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(30), 10, RoundingMode.FLOOR);
            case YEAR:
                return new BigDecimal(timeEnd - timeStart)
                        .divide(new BigDecimal(1000), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(60), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(24), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(30), 10, RoundingMode.FLOOR)
                        .divide(new BigDecimal(365), 10, RoundingMode.FLOOR);

        }
        return new BigDecimal(0);
    }

    /**
     * 比较两个时间相差多少天
     * 如果开始时间 小于 结束时间 返回天数为正值
     * 如果开始时间 大于 结束时间 返回天数为负值
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return long
     */
    public static long getDiffDay(Date startDate, Date endDate) {
        long between = Math.abs((startDate.getTime() - endDate.getTime()) / 1000);
        long day = between / 60 / 60 / 24;
        if (startDate.after(endDate)) {
            return (long) -Math.floor(day);
        }
        return (long) Math.floor(day);
    }

    /**
     * 比较两个时间相差多少天
     * 如果开始时间 小于 结束时间 返回天数为正值
     * 如果开始时间 大于 结束时间 返回天数为负值
     *
     * @param startDate 开始时间
     * @return long
     */
    public static long getCurrentDiffDay(Date startDate) {
        return getDiffDay(startDate, new Date());
    }

    /**
     * 比较两个时间相差多少天
     * 如果开始时间 小于 结束时间 返回天数为正值
     * 如果开始时间 大于 结束时间 返回天数为负值
     *
     * @param startDate 开始时间
     * @return long
     */
    public static long getCurrentDiffMinutes(Date startDate) {
        return getDiffMinutes(startDate, new Date());
    }

    /**
     * 获取两个时间相差月份
     *
     * @param start 开始
     * @param end   结束
     * @return int
     */
    public static int getDiffMonth(Date start, Date end) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        return (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12
                + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }

    /**
     * 获取两个时间相差年份
     *
     * @param start 开始
     * @param end   结束
     * @return int
     */
    public static int getDiffYear(Date start, Date end) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        return (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR));
    }

    /**
     * 返回传入时间月份的第一天
     *
     * @param date 日期
     * @return Date
     */
    public static Date firstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }

    /**
     * 返回传入时间月份的最后一天
     *
     * @param date 日期
     * @return {@link Date}
     */
    public static Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }

    /**
     * 判断是否为闰年
     *
     * @param year 一年
     * @return boolean
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
     *
     * @param birthDay      生日
     * @param dateToCompare 需要对比的日期
     * @return 年龄
     */
    public static int age(Date birthDay, Date dateToCompare) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToCompare);

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("Birthday is after date " + getDateFormat(birthDay) + "!");
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int age = year - cal.get(Calendar.YEAR);

        int monthBirth = cal.get(Calendar.MONTH);
        if (month == monthBirth) {
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth < dayOfMonthBirth) {
                // 如果生日在当月，但是未达到生日当天的日期，年龄减一
                age--;
            }
        } else if (month < monthBirth) {
            // 如果当前月份未达到生日的月份，年龄计算减一
            age--;
        }

        return age;
    }

    public static Date randomDate() {
        return randomRangeDate("1990-01-01", "2018-12-31");
    }




    /**
     * 获取两个日期之间的工作日
     *
     * @param d1 startDate
     * @param d2 endDate
     * @return 工作日天数
     */
    public static int workdayDiff(Date d1, Date d2) {
        LocalDate start = LocalDate.fromDateFields(d1);
        LocalDate end = LocalDate.fromDateFields(d2);
        start = toWorkday(start);
        end = toWorkday(end);
        int daysBetween = Days.daysBetween(start, end).getDays();
        int weekendsBetween = Weeks.weeksBetween(start.withDayOfWeek(WEEK_START), end.withDayOfWeek(WEEK_START)).getWeeks();
        return daysBetween - (weekendsBetween * DAYS_PER_WEEKEND);
    }

    /**
     * 获取指定日期的 yyyy-MM-dd 23:59:59 格式
     *
     * @param date 日期
     * @return  yyyy-MM-dd 23:59:59
     */
    public static String getDateFormatMaxTime(Date date) {
        String dateFormat = getDateFormat(date, FORMAT_DEFAULT_DATE);
        return dateFormat + DATE_DAY_MAX_TIME;
    }

    /**
     * 获取指定日期的 yyyy-MM-dd 00:00:00 格式
     *
     * @param date 日期
     * @return  yyyy-MM-dd 00:00:00
     */
    public static String getDateFormatMinTime(Date date) {
        String dateFormat = getDateFormat(date, FORMAT_DEFAULT_DATE);
        return dateFormat + DATE_DAY_MIN_TIME;
    }


    /**
     * 根据小写数字格式的日期转换成大写格式的日期
     * 支持yyyy-MM-dd、yyyy/MM/dd、yyyyMMdd等格式
     *
     * @param date 日期
     * @return 二〇二〇年八月五日
     */
    public static String getUpperDate(Date date) {
        return getUpperDate(getDateShortFormat(date));
    }

    /**
     * 获取当前日期
     * 根据小写数字格式的日期转换成大写格式的日期
     * 支持yyyy-MM-dd、yyyy/MM/dd、yyyyMMdd等格式
     *
     * @return 二〇二〇年八月五日
     */
    public static String getUpperDate() {
        return getUpperDate(new Date());
    }

    /**
     * 根据小写数字格式的日期转换成大写格式的日期
     * 支持yyyy-MM-dd、yyyy/MM/dd、yyyyMMdd等格式
     *
     * @param date 字符串日期
     * @return 二〇二〇年八月五日
     */
    public static String getUpperDate(String date) {
        if (date == null) {
            return "";
        }
        //非数字的都去掉
        date = date.replaceAll("\\D", "");
        if (date.length() != 8) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {//年
            sb.append(UPPER_NUMBER[Integer.parseInt(date.substring(i, i + 1))]);
        }
        sb.append("年");//拼接年
        int month = Integer.parseInt(date.substring(4, 6));
        if (month <= 10) {
            sb.append(UPPER_NUMBER[month]);
        } else {
            sb.append("十").append(UPPER_NUMBER[month % 10]);
        }
        sb.append("月");//拼接月

        int day = Integer.parseInt(date.substring(6));
        if (day <= 10) {
            sb.append(UPPER_NUMBER[day]);
        } else if (day < 20) {
            sb.append("十").append(UPPER_NUMBER[day % 10]);
        } else {
            sb.append(UPPER_NUMBER[day / 10]).append("十");
            int tmp = day % 10;
            if (tmp != 0) {
                sb.append(UPPER_NUMBER[tmp]);
            }
        }
        sb.append("日");//拼接日
        return sb.toString();
    }


    public static LocalDate toWorkday(LocalDate d) {
        if (d.getDayOfWeek() > WEEK_END) {
            return d.plusDays(DateTimeConstants.DAYS_PER_WEEK - d.getDayOfWeek() + 1);
        }
        return d;
    }

    /**
     * 根据月份获取英文月份名称
     *
     * @param month 月份 1~12
     * @return 英文月份名称
     */
    public static String getMonthEn(int month) {
        String[] allMonths = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        if (0 < month && month < 13) {
            return allMonths[month - 1];
        }
        return "";
    }
    /**
     * 根据月份获取中文月份名称
     *
     * @param month 月份 1~12
     * @return 中文月份名称
     */
    public static String getMonthZh(int month) {
        String[] allMonths = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        if (0 < month && month < 13) {
            return allMonths[month - 1];
        }
        return "";
    }
    /**
     * 根据月份获取中文月份名称
     *
     * @param ymd yyyy-MM-dd格式的年月日
     * @return 中文月份名称
     */
    public static String getMonthZh(String ymd) {
        int monthOfYear = DateTimeFormat.forPattern(FORMAT_DEFAULT_DATE).parseDateTime(ymd).getMonthOfYear();
        return getMonthZh(monthOfYear);
    }

    /**
     * 获取英文出生日期
     *
     * @param date 日期
     * @return April 1.1979
     */
    private static String getEnBirthDay(Date date) {
        DateTime dateTime = new DateTime(date);
        String monthEn = getMonthEn(dateTime.getMonthOfYear());
        return monthEn + " " + dateTime.getDayOfMonth() + "." + dateTime.getYear();
    }

    /**
     * 现在的时间在指定的时间范围内
     * <pre>
     *      DateUtil.nowInTimeRange("08:00:00", "17:00:00") == 当前时间是不是在这个范围内
     * </pre>
     *
     * @param timeStart 时间开始  in format "hh:mm:ss"
     * @param timeEnd   时间结束  in format "hh:mm:ss"
     * @return boolean 如果时间格式不正确直接返回false
     */
    public static boolean nowInTimeRange(String timeStart, String timeEnd){
        if (RegexUtil.isTime(timeStart) && RegexUtil.isTime(timeEnd)){
            Date nowDate = new Date();
            String dateFormat = getDateFormat(nowDate, FORMAT_DEFAULT_DATE);
            Date startDate = parseSpecificDateTime(dateFormat + " " + timeStart);
            Date endDate = parseSpecificDateTime(dateFormat + " " + timeEnd);
            return nowDate.after(startDate) && nowDate.before(endDate);
        }else {
            log.error("计算现在的时间在指定的时间范围内错误！{}-{}", timeStart, timeEnd);
        }
        return false;
    }

    /**
     * 获取上月的第一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-01
     * @return {@link Date}
     */
    public static Date getPreviousMonthFirstDayToDate() {
        return LocalDateTime.now().minusMonths(1).dayOfMonth().withMinimumValue().toDate();
    }
    /**
     * 获取上月的最后一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-30
     * @return {@link Date}
     */
    public static Date getPreviousMonthLastDayToDate() {
        return LocalDateTime.now().minusMonths(1).dayOfMonth().withMaximumValue().toDate();
    }
    /**
     * 获取上月的第一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-01
     * @return {@link String}
     */
    public static String getPreviousMonthFirstDayToString() {
        return new DateTime(getPreviousMonthFirstDayToDate()).toString(FORMAT_DEFAULT_DATE);
    }
    /**
     * 获取上月的最后一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-30
     * @return {@link String}
     */
    public static String getPreviousMonthLastDayToString() {
        return new DateTime(getPreviousMonthLastDayToDate()).toString(FORMAT_DEFAULT_DATE);
    }
    /**
     * 获取当月的第一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-01
     * @return {@link Date}
     */
    public static Date getCurrentMonthFirstDayToDate() {
        return LocalDateTime.now().dayOfMonth().withMinimumValue().toDate();
    }
    /**
     * 获取当月的最后一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-30
     * @return {@link Date}
     */
    public static Date getCurrentMonthLastDayToDate() {
        return LocalDateTime.now().dayOfMonth().withMaximumValue().toDate();
    }
    /**
     * 获取当月的第一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-01
     * @return {@link String}
     */
    public static String getCurrentMonthFirstDayToString() {
        return new DateTime(getCurrentMonthFirstDayToDate()).toString(FORMAT_DEFAULT_DATE);
    }
    /**
     * 获取当月的最后一天
     * if today - will 2020-09-21 14:14:14 == 2020-09-30
     * @return {@link String}
     */
    public static String getCurrentMonthLastDayToString() {
        return new DateTime(getCurrentMonthLastDayToDate()).toString(FORMAT_DEFAULT_DATE);
    }
    /**
     * 获取下月的第一天
     * if today - will 2020-09-21 14:14:14 == 2020-10-01
     * @return {@link Date}
     */
    public static Date getNextMonthFirstDayToDate() {
        return LocalDateTime.now().plusMonths(1).dayOfMonth().withMinimumValue().toDate();
    }
    /**
     * 获取下月的最后一天
     * if today - will 2020-09-21 14:14:14 == 2020-10-31
     * @return {@link Date}
     */
    public static Date getNextMonthLastDayToDate() {
        return LocalDateTime.now().plusMonths(1).dayOfMonth().withMaximumValue().toDate();
    }
    /**
     * 获取下月的第一天
     * if today - will 2020-09-21 14:14:14 == 2020-10-01
     * @return {@link String}
     */
    public static String getNextMonthFirstDayToString() {
        return new DateTime(getNextMonthFirstDayToDate()).toString(FORMAT_DEFAULT_DATE);
    }
    /**
     * 获取下月的最后一天
     * if today - will 2020-09-21 14:14:14 == 2020-10-31
     * @return {@link String}
     */
    public static String getNextMonthLastDayToString() {
        return new DateTime(getNextMonthLastDayToDate()).toString(FORMAT_DEFAULT_DATE);
    }
    /**
     * 解析时间
     *
     * @param date    指定日期
     * @param timeStr 时间字符串  in format "hh:mm:ss"
     * @return {@link Date}
     */
    public static Date parseTime(Date date, String timeStr) {
        String dateFormat = getDateFormat(date, FORMAT_DEFAULT_DATE);
        return parseSpecificDateTime(dateFormat + " " + timeStr);
    }

    /**
     * 判断日期是否为当日指定时间之后
     * <pre>
     *     //current time is 09:27:01
     *     System.out.println(beforeTime(new Date(), "12:00:00"));//true
     *     System.out.println(beforeTime(new Date(), "09:00:00"));//false
     *     System.out.println(afterTime(new Date(), "12:00:00"));//false
     *     System.out.println(afterTime(new Date(), "09:00:00"));//true
     * </pre>
     *
     * @param date    指定日期
     * @param timeStr 时间字符串 in format "hh:mm:ss"
     * @return 是否
     */
    public static boolean afterTime(Date date, String timeStr) {
        Date when = parseTime(date, timeStr);
        return date.after(when);
    }

    /**
     * 判断日期是否为当日指定时间之前
     * <pre>
     *     //current time is 09:27:01
     *     System.out.println(beforeTime(new Date(), "12:00:00"));//true
     *     System.out.println(beforeTime(new Date(), "09:00:00"));//false
     *     System.out.println(afterTime(new Date(), "12:00:00"));//false
     *     System.out.println(afterTime(new Date(), "09:00:00"));//true
     * </pre>
     *
     * @param date    指定日期
     * @param timeStr 时间字符串 in format "hh:mm:ss"
     * @return 是否
     */
    public static boolean beforeTime(Date date, String timeStr) {
        Date when = parseTime(date, timeStr);
        return date.before(when);
    }

    /**
     * 范围内每个月的第一天集合
     *  6.1 小于 7.1 8.1 9.1 小于 10.1 === 6789月份来算环比
     *
     *  monthFirstDayInRange("2020-12-01", "2020-10-01") == [2020-12-01]
     *  monthFirstDayInRange("2020-07-01", "2020-10-01") == [2020-07-01, 2020-08-01, 2020-09-01]
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return [startDate, endDate) 左闭右开
     */
    public static List<String> monthFirstDayInRange(String startDate, String endDate) {
        List<String> stringList = new ArrayList<>();
        DateTime startDateTime = DateTimeFormat.forPattern(FORMAT_DEFAULT_DATE).parseDateTime(startDate).dayOfMonth().withMinimumValue();
        DateTime endDateTime = DateTimeFormat.forPattern(FORMAT_DEFAULT_DATE).parseDateTime(endDate).dayOfMonth().withMinimumValue();
        stringList.add(startDateTime.toString(FORMAT_DEFAULT_DATE));
        if (endDateTime.compareTo(startDateTime) < 0){
            return stringList;
        }
        while (startDateTime.plusMonths(1).compareTo(endDateTime) < 0){
            startDateTime = startDateTime.plusMonths(1);
            stringList.add(startDateTime.toString(FORMAT_DEFAULT_DATE));
        }
        return stringList;
    }

    /**
     * 去年的今天
     * lastYearTodayMonth("2020-07-01") == 2019-07-01
     * lastYearTodayMonth("2020-10-30") == 2019-10-01
     * @param todayMonth 今天的月份
     * @return 去年的今天的月份
     */
    public static String lastYearTodayMonth(String todayMonth) {
        return DateTimeFormat.forPattern(DateUtil.FORMAT_DEFAULT_DATE).parseDateTime(todayMonth)
                .minusYears(1).dayOfMonth().withMinimumValue().toString(DateUtil.FORMAT_DEFAULT_DATE);
    }


    /**
     * 是合法的时间
     *
     * @param pattern 模式
     * @param time    时间
     * @return boolean
     */
    public static boolean isLegalTime(String pattern, String time){
        try {
            DateTimeFormat.forPattern(pattern).parseDateTime(time);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    /**
     * 是合法的时间
     *
     * @param time   时间
     * @return boolean
     */
    public static boolean isLegalTime(String time){
        return isLegalTime(FORMAT_DEFAULT_TIME, time);
    }

    /**
     * 提取日期str
     * 多个匹配时，取第一个
     * <code>
     *     extractDateStr("发表于： 2021-04-08 12:12:12 编辑于： 2020-12-12 12:12:12", DateRegex.DateTime);
     *     //2021-04-08 12:12:12
     *     extractDateStr("发表于： 2020-12-08 的清晨", DateRegex.Date);
     *     //2020-12-08
     * </code>
     * @param sourceStr 源str
     * @param dateRegex 日期正则表达式
     * @return {@link String}
     */
    public static String extractDateStr(String sourceStr, DateRegex dateRegex) {
        if (StringUtils.isNotBlank(sourceStr)) {
            Pattern pattern = dateRegex.getPattern();
            Matcher matcher = pattern.matcher(sourceStr);
            if (matcher.find()) {
                return matcher.group(0);
            }
        }
        return "";
    }

    public enum DateRegex {
        /**
         * yyyy-MM-dd HH:mm:ss
         */
        DateTime("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])"),
        /**
         * yyyy-M-d HH:mm:ss
         */
        DateTime2("[1-9]\\d{3}-([1-9]|1[0-2])-([1-9]|[1-2][0-9]|3[0-1]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])"),
        /**
         * yyyy-MM-dd
         */
        Date("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])");
        private final Pattern pattern;

        DateRegex(String patternStr) {
            this.pattern = Pattern.compile(patternStr);
        }

        public Pattern getPattern() {
            return this.pattern;
        }
    }

    //内部维护的值
    private static long prevUseTimeStamp = 0;

    /**
     * 获取顺序化时间毫秒数
     *
     * @return long
     */
    public static long getSerialTimeMillis(){
        return getSerialTimeMillis(1).get(0);
    }

    /**
     * 批量获取顺序化时间毫秒数(同步方法防止并发)
     *
     * 会记录上一个获取的时间戳 {@see prevUseTimeStamp}，如果并发会取这个时间+1
     *
     * @param count 数量
     * @return {@link List<Long>}
     */
    public static synchronized List<Long> getSerialTimeMillis(int count){
        List<Long> resList = new ArrayList<>();
        long nowTime =  System.currentTimeMillis();
        //当前时间戳小于上次运行的时间戳 --> 使用上次的时间戳
        if (prevUseTimeStamp > 0 && nowTime < prevUseTimeStamp) {
            nowTime = prevUseTimeStamp;
        }
        for (int i = 0; i < count; i++) {
            resList.add(nowTime);
            nowTime++;
        }
        prevUseTimeStamp = nowTime;
        return resList;
    }


    /**
     * 找寻最大日期
     *
     * @param dateArr 日期列表
     * @return {@link Date}
     */
    public static Date max(Date... dateArr) {
        Date maxDate = dateArr[0];
        for (Date date : dateArr) {
            if (date.after(maxDate)) {
                maxDate = date;
            }
        }
        return maxDate;
    }
    /**
     * 找寻最小日期
     *
     * @param dateArr 日期列表
     * @return {@link Date}
     */
    public static Date min(Date... dateArr) {
        Date maxDate = dateArr[0];
        for (Date date : dateArr) {
            if (date.before(maxDate)) {
                maxDate = date;
            }
        }
        return maxDate;
    }

    /**
     * 解析带utc时区的时间
     * <pre>
     *     parseUTC("2021-12-06T07:58:03Z")   -> +8到北京的时间：2021-12-06 15:58:03
     * </pre>
     *
     * @param dateStr str日期：使用yyyy-MM-dd'T'HH:mm:ssZ的格式
     * @return {@link Date}
     */
    public static Date parseUTC(String dateStr){
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZoneUTC().parseDateTime(dateStr).toDate();
    }

    /**
     * 得到utc日期(带时区的时间)
     * <pre>
     *     getDateUTC(new Date())    ->   2021-12-06T09:10:14+0000
     * </pre>
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String getDateUTC(Date date) {
        return new DateTime(date).withZone(DateTimeZone.UTC).toString("yyyy-MM-dd'T'HH:mm:ssZ");
    }


    public static void main(String[] args) {
        System.out.println(beforeTime(new Date(), "12:00:00"));
        System.out.println(beforeTime(new Date(), "09:00:00"));
        System.out.println(afterTime(new Date(), "12:00:00"));
        System.out.println(afterTime(new Date(), "09:00:00"));
    }

}
