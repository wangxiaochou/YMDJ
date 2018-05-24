package com.ypshengxian.daojia.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <p> 时间相关工具类 </p><br>
 *
 * @author lwc
 * @date 2017/3/10 15:59
 * @note -
 * millis2String           : 将时间戳转为时间字符串
 * string2Millis           : 将时间字符串转为时间戳
 * string2Date             : 将时间字符串转为Date类型
 * date2String             : 将Date类型转为时间字符串
 * date2Millis             : 将Date类型转为时间戳
 * millis2Date             : 将时间戳转为Date类型
 * getTimeSpan             : 获取两个时间差（单位：unit）
 * getFitTimeSpan          : 获取合适型两个时间差
 * getNowTimeMills         : 获取当前毫秒时间戳
 * getNowTimeString        : 获取当前时间字符串
 * getNowTimeDate          : 获取当前Date
 * getTimeSpanByNow        : 获取与当前时间的差（单位：unit）
 * getFitTimeSpanByNow     : 获取合适型与当前时间的差
 * getFriendlyTimeSpanByNow: 获取友好型与当前时间的差
 * isSameDay               : 判断是否同一天
 * isLeapYear              : 判断是否闰年
 * getWeek, getWeekIndex   : 获取星期
 * getWeekOfMonth          : 获取月份中的第几周
 * getWeekOfYear           : 获取年份中的第几周
 * getChineseZodiac        : 获取生肖
 * getZodiac               : 获取星座
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
@SuppressWarnings("WeakerAccess")
public class TimeUtils {
    /**
     * <p>在工具类中经常使用到工具类的格式化描述，这个主要是一个日期的操作类，所以日志格式主要使用 SimpleDateFormat的定义格式.</p>
     * 格式的意义如下： 日期和时间模式 <br>
     * <p>日期和时间格式由日期和时间模式字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
     * 被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
     * 表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
     * </p>
     * 定义了以下模式字母（所有其他字符 'A' 到 'Z' 和 'a' 到 'z' 都被保留）： <br>
     * <table border="1" cellspacing="1" cellpadding="1" summary="Chart shows pattern letters, date/time component,
     * presentation, and examples.">
     * <tr>
     * <th align="left">字母</th>
     * <th align="left">日期或时间元素</th>
     * <th align="left">表示</th>
     * <th align="left">示例</th>
     * </tr>
     * <tr>
     * <td><code>G</code></td>
     * <td>Era 标志符</td>
     * <td>Text</td>
     * <td><code>AD</code></td>
     * </tr>
     * <tr>
     * <td><code>y</code> </td>
     * <td>年 </td>
     * <td>Year </td>
     * <td><code>1996</code>; <code>96</code> </td>
     * </tr>
     * <tr>
     * <td><code>M</code> </td>
     * <td>年中的月份 </td>
     * <td>Month </td>
     * <td><code>July</code>; <code>Jul</code>; <code>07</code> </td>
     * </tr>
     * <tr>
     * <td><code>w</code> </td>
     * <td>年中的周数 </td>
     * <td>Number </td>
     * <td><code>27</code> </td>
     * </tr>
     * <tr>
     * <td><code>W</code> </td>
     * <td>月份中的周数 </td>
     * <td>Number </td>
     * <td><code>2</code> </td>
     * </tr>
     * <tr>
     * <td><code>D</code> </td>
     * <td>年中的天数 </td>
     * <td>Number </td>
     * <td><code>189</code> </td>
     * </tr>
     * <tr>
     * <td><code>d</code> </td>
     * <td>月份中的天数 </td>
     * <td>Number </td>
     * <td><code>10</code> </td>
     * </tr>
     * <tr>
     * <td><code>F</code> </td>
     * <td>月份中的星期 </td>
     * <td>Number </td>
     * <td><code>2</code> </td>
     * </tr>
     * <tr>
     * <td><code>E</code> </td>
     * <td>星期中的天数 </td>
     * <td>Text </td>
     * <td><code>Tuesday</code>; <code>Tue</code> </td>
     * </tr>
     * <tr>
     * <td><code>a</code> </td>
     * <td>Am/pm 标记 </td>
     * <td>Text </td>
     * <td><code>PM</code> </td>
     * </tr>
     * <tr>
     * <td><code>H</code> </td>
     * <td>一天中的小时数（0-23） </td>
     * <td>Number </td>
     * <td><code>0</code> </td>
     * </tr>
     * <tr>
     * <td><code>k</code> </td>
     * <td>一天中的小时数（1-24） </td>
     * <td>Number </td>
     * <td><code>24</code> </td>
     * </tr>
     * <tr>
     * <td><code>K</code> </td>
     * <td>am/pm 中的小时数（0-11） </td>
     * <td>Number </td>
     * <td><code>0</code> </td>
     * </tr>
     * <tr>
     * <td><code>h</code> </td>
     * <td>am/pm 中的小时数（1-12） </td>
     * <td>Number </td>
     * <td><code>12</code> </td>
     * </tr>
     * <tr>
     * <td><code>m</code> </td>
     * <td>小时中的分钟数 </td>
     * <td>Number </td>
     * <td><code>30</code> </td>
     * </tr>
     * <tr>
     * <td><code>s</code> </td>
     * <td>分钟中的秒数 </td>
     * <td>Number </td>
     * <td><code>55</code> </td>
     * </tr>
     * <tr>
     * <td><code>S</code> </td>
     * <td>毫秒数 </td>
     * <td>Number </td>
     * <td><code>978</code> </td>
     * </tr>
     * <tr>
     * <td><code>z</code> </td>
     * <td>时区 </td>
     * <td>General time zone </td>
     * <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code> </td>
     * </tr>
     * <tr>
     * <td><code>Z</code> </td>
     * <td>时区 </td>
     * <td>RFC 822 time zone </td>
     * <td><code>-0800</code> </td>
     * </tr>
     * </table>
     * <pre>
     *                          HH:mm    15:44
     *                         h:mm a    3:44 下午
     *                        HH:mm z    15:44 CST
     *                        HH:mm Z    15:44 +0800
     *                     HH:mm zzzz    15:44 中国标准时间
     *                       HH:mm:ss    15:44:40
     *                     yyyy-MM-dd    2016-08-12
     *               yyyy-MM-dd HH:mm    2016-08-12 15:44
     *            yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
     *       yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
     *  EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
     *       yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
     *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
     *   yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
     *                         K:mm a    3:44 下午
     *               EEE, MMM d, ''yy    星期五, 八月 12, '16
     *          hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
     *   yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
     *     EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
     *                  yyMMddHHmmssZ    160812154440+0800
     *     yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
     * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
     * </pre>
     * 注意：SimpleDateFormat不是线程安全的，线程安全需用{@code ThreadLocal<SimpleDateFormat>}
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String[] CHINESE_ZODIAC = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    /** 十二生肖 */
    private static final String[] ZODIAC = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    /** 十二生肖标识 */
    private static final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};

    /**
     * 构造类
     */
    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    public static String millis2String(long millis) {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(millis));
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为pattern</p>
     *
     * @param millis 毫秒时间戳
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String millis2String(long millis, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(millis));
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time) {
        return string2Millis(time, DEFAULT_PATTERN);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将时间字符串转为Date类型
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return Date类型
     */
    public static Date string2Date(String time) {
        return string2Date(time, DEFAULT_PATTERN);
    }

    /**
     * 将时间字符串转为Date类型
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return Date类型
     */
    public static Date string2Date(String time, String pattern) {
        return new Date(string2Millis(time, pattern));
    }

    /**
     * 将String通过默认格式转换成Date，再转换成String
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return String类型
     */
    public static String string2String(String time, String pattern) {
        return date2String(string2Date(time), pattern);
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param date Date类型时间
     * @return 时间字符串
     */
    public static String date2String(Date date) {
        return date2String(date, DEFAULT_PATTERN);
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为pattern</p>
     *
     * @param date Date类型时间
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String date2String(Date date, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
    }

    /**
     * 将Date类型转为时间戳
     *
     * @param date Date类型时间
     * @return 毫秒时间戳
     */
    public static long date2Millis(Date date) {
        return date.getTime();
    }

    /**
     * 将时间戳转为Date类型
     *
     * @param millis 毫秒时间戳
     * @return Date类型时间
     */
    public static Date millis2Date(long millis) {
        return new Date(millis);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time0和time1格式都为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time0 时间字符串0
     * @param time1 时间字符串1
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     * </ul>
     * @return unit时间戳
     */
    public static long getTimeSpan(String time0, String time1, int unit) {
        return getTimeSpan(time0, time1, unit, DEFAULT_PATTERN);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time0和time1格式都为format</p>
     *
     * @param time0 时间字符串0
     * @param time1 时间字符串1
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     * </ul>
     * @param pattern 时间格式
     * @return unit时间戳
     */
    public static long getTimeSpan(String time0, String time1, int unit, String pattern) {
        return ConvertUtils.millis2TimeSpan(Math.abs(string2Millis(time0, pattern) - string2Millis(time1, pattern)), unit);
    }

    /**
     * 获取两个时间差（单位：unit）
     *
     * @param date0 Date类型时间0
     * @param date1 Date类型时间1
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     * </ul>
     * @return unit时间戳
     */
    public static long getTimeSpan(Date date0, Date date1, int unit) {
        return ConvertUtils.millis2TimeSpan(Math.abs(date2Millis(date0) - date2Millis(date1)), unit);
    }

    /**
     * 获取两个时间差（单位：unit）
     *
     * @param millis0 毫秒时间戳0
     * @param millis1 毫秒时间戳1
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     * </ul>
     * @return unit时间戳
     */
    public static long getTimeSpan(long millis0, long millis1, int unit) {
        return ConvertUtils.millis2TimeSpan(Math.abs(millis0 - millis1), unit);
    }

    /**
     * 获取合适型两个时间差
     * <p>time0和time1格式都为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time0 时间字符串0
     * @param time1 时间字符串1
     * @param precision 精度
     * <p>precision = 0，返回null</p>
     * <p>precision = 1，返回天</p>
     * <p>precision = 2，返回天和小时</p>
     * <p>precision = 3，返回天、小时和分钟</p>
     * <p>precision = 4，返回天、小时、分钟和秒</p>
     * <p>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</p>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(String time0, String time1, int precision) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(string2Millis(time0, DEFAULT_PATTERN) - string2Millis(time1, DEFAULT_PATTERN)), precision);
    }

    /**
     * 获取合适型两个时间差
     * <p>time0和time1格式都为pattern</p>
     *
     * @param time0 时间字符串0
     * @param time1 时间字符串1
     * @param precision 精度
     * <p>precision = 0，返回null</p>
     * <p>precision = 1，返回天</p>
     * <p>precision = 2，返回天和小时</p>
     * <p>precision = 3，返回天、小时和分钟</p>
     * <p>precision = 4，返回天、小时、分钟和秒</p>
     * <p>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</p>
     * @param pattern 时间格式
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(String time0, String time1, int precision, String pattern) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(string2Millis(time0, pattern) - string2Millis(time1, pattern)), precision);
    }

    /**
     * 获取合适型两个时间差
     *
     * @param date0 Date类型时间0
     * @param date1 Date类型时间1
     * @param precision 精度
     * <p>precision = 0，返回null</p>
     * <p>precision = 1，返回天</p>
     * <p>precision = 2，返回天和小时</p>
     * <p>precision = 3，返回天、小时和分钟</p>
     * <p>precision = 4，返回天、小时、分钟和秒</p>
     * <p>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</p>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(Date date0, Date date1, int precision) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(date2Millis(date0) - date2Millis(date1)), precision);
    }

    /**
     * 获取合适型两个时间差
     *
     * @param millis0 毫秒时间戳1
     * @param millis1 毫秒时间戳2
     * @param precision 精度
     * <p>precision = 0，返回null</p>
     * <p>precision = 1，返回天</p>
     * <p>precision = 2，返回天和小时</p>
     * <p>precision = 3，返回天、小时和分钟</p>
     * <p>precision = 4，返回天、小时、分钟和秒</p>
     * <p>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</p>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(long millis0, long millis1, int precision) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(millis0 - millis1), precision);
    }

    /**
     * 获取当前毫秒时间戳
     *
     * @return 毫秒时间戳
     */
    public static long getNowTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @return 时间字符串
     */
    public static String getNowTimeString() {
        return millis2String(System.currentTimeMillis(), DEFAULT_PATTERN);
    }

    /**
     * 获取当前时间字符串
     * <p>格式为pattern</p>
     *
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String getNowTimeString(String pattern) {
        return millis2String(System.currentTimeMillis(), pattern);
    }

    /**
     * 获取当前Date
     *
     * @return Date类型时间
     */
    public static Date getNowTimeDate() {
        return new Date();
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}:毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }:秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }:分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}:小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }:天</li>
     * </ul>
     * @return unit时间戳
     */
    public static long getTimeSpanByNow(String time, int unit) {
        return getTimeSpan(getNowTimeString(), time, unit, DEFAULT_PATTERN);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     * </ul>
     * @param pattern 时间格式
     * @return unit时间戳
     */
    public static long getTimeSpanByNow(String time, int unit, String pattern) {
        return getTimeSpan(getNowTimeString(), time, unit, pattern);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     *
     * @param date Date类型时间
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     * </ul>
     * @return unit时间戳
     */
    public static long getTimeSpanByNow(Date date, int unit) {
        return getTimeSpan(new Date(), date, unit);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     *
     * @param millis 毫秒时间戳
     * @param unit 单位类型
     * <ul>
     * <li>{@link ConstUtils.TimeUnit#MSEC}: 毫秒</li>
     * <li>{@link ConstUtils.TimeUnit#SEC }: 秒</li>
     * <li>{@link ConstUtils.TimeUnit#MIN }: 分</li>
     * <li>{@link ConstUtils.TimeUnit#HOUR}: 小时</li>
     * <li>{@link ConstUtils.TimeUnit#DAY }: 天</li>
     * </ul>
     * @return unit时间戳
     */
    public static long getTimeSpanByNow(long millis, int unit) {
        return getTimeSpan(System.currentTimeMillis(), millis, unit);
    }

    /**
     * 获取合适型与当前时间的差
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @param precision 精度
     * <ul>
     * <li>precision = 0，返回null</li>
     * <li>precision = 1，返回天</li>
     * <li>precision = 2，返回天和小时</li>
     * <li>precision = 3，返回天、小时和分钟</li>
     * <li>precision = 4，返回天、小时、分钟和秒</li>
     * <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     * </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(String time, int precision) {
        return getFitTimeSpan(getNowTimeString(), time, precision, DEFAULT_PATTERN);
    }

    /**
     * 获取合适型与当前时间的差
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param precision 精度
     * @param pattern 时间格式
     * <ul>
     * <li>precision = 0，返回null</li>
     * <li>precision = 1，返回天</li>
     * <li>precision = 2，返回天和小时</li>
     * <li>precision = 3，返回天、小时和分钟</li>
     * <li>precision = 4，返回天、小时、分钟和秒</li>
     * <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     * </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(String time, int precision, String pattern) {
        return getFitTimeSpan(getNowTimeString(), time, precision, pattern);
    }

    /**
     * 获取合适型与当前时间的差
     *
     * @param date Date类型时间
     * @param precision 精度
     * <ul>
     * <li>precision = 0，返回null</li>
     * <li>precision = 1，返回天</li>
     * <li>precision = 2，返回天和小时</li>
     * <li>precision = 3，返回天、小时和分钟</li>
     * <li>precision = 4，返回天、小时、分钟和秒</li>
     * <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     * </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(Date date, int precision) {
        return getFitTimeSpan(getNowTimeDate(), date, precision);
    }

    /**
     * 获取合适型与当前时间的差
     *
     * @param millis 毫秒时间戳
     * @param precision 精度
     * <ul>
     * <li>precision = 0，返回null</li>
     * <li>precision = 1，返回天</li>
     * <li>precision = 2，返回天和小时</li>
     * <li>precision = 3，返回天、小时和分钟</li>
     * <li>precision = 4，返回天、小时、分钟和秒</li>
     * <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     * </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(long millis, int precision) {
        return getFitTimeSpan(System.currentTimeMillis(), millis, precision);
    }

    /**
     * 获取友好型与当前时间的差
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(String time) {
        return getFriendlyTimeSpanByNow(time, DEFAULT_PATTERN);
    }

    /**
     * 获取友好型与当前时间的差
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(String time, String pattern) {
        return getFriendlyTimeSpanByNow(string2Millis(time, pattern));
    }

    /**
     * 获取友好型与当前时间的差
     *
     * @param date Date类型时间
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(Date date) {
        return getFriendlyTimeSpanByNow(date.getTime());
    }

    /**
     * 获取友好型与当前时间的差
     *
     * @param millis 毫秒时间戳
     * @return 友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示XXX秒前</li>
     * <li>如果在1小时内，显示XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    @SuppressLint("DefaultLocale")
    public static String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0) {
            return String.format("%tc", millis);// U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
        }
        if (span < 1000) {
            return "刚刚";
        } else if (span < ConstUtils.MIN) {
            return String.format("%d秒前", span / ConstUtils.SEC);
        } else if (span < ConstUtils.HOUR) {
            return String.format("%d分钟前", span / ConstUtils.MIN);
        }
        // 获取当天00:00
        long wee = (now / ConstUtils.DAY) * ConstUtils.DAY - 8 * ConstUtils.HOUR;
        if (millis >= wee) {
            long wez = wee + 12 * ConstUtils.HOUR;
            if (millis > wez) {
                return String.format("下午 %tR", millis);
            } else {
                return String.format("上午 %tR", millis);
            }
        } else if (millis >= wee - ConstUtils.DAY) {
            return String.format("昨天 %tR", millis);
        } else {
            return String.format("%tF", millis);
        }
    }

    /**
     * 判断是否同一天
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSameDay(String time) {
        return isSameDay(string2Millis(time, DEFAULT_PATTERN));
    }

    /**
     * 判断是否同一天
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSameDay(String time, String pattern) {
        return isSameDay(string2Millis(time, pattern));
    }

    /**
     * 判断是否同一天
     *
     * @param date Date类型时间
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSameDay(Date date) {
        return isSameDay(date.getTime());
    }

    /**
     * 判断是否同一天
     *
     * @param millis 毫秒时间戳
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isSameDay(long millis) {
        long wee = (System.currentTimeMillis() / ConstUtils.DAY) * ConstUtils.DAY - 8 * ConstUtils.HOUR;
        return millis >= wee && millis < wee + ConstUtils.DAY;
    }

    /**
     * 判断是否闰年
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(String time) {
        return isLeapYear(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 判断是否闰年
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(String time, String pattern) {
        return isLeapYear(string2Date(time, pattern));
    }

    /**
     * 判断是否闰年
     *
     * @param date Date类型时间
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 判断是否闰年
     *
     * @param millis 毫秒时间戳
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(long millis) {
        return isLeapYear(millis2Date(millis));
    }

    /**
     * 判断是否闰年
     *
     * @param year 年份
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 获取星期
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 星期
     */
    public static String getWeek(String time) {
        return getWeek(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取星期
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 星期
     */
    public static String getWeek(String time, String pattern) {
        return getWeek(string2Date(time, pattern));
    }

    /**
     * 获得当前星期
     *
     * @return 获得当前星期
     */
    public static String getCurrentWeek() {
        int i = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (i == Calendar.SUNDAY) {
            return "星期日";
        } else if (i == Calendar.MONDAY) {
            return "星期一";
        } else if (i == Calendar.TUESDAY) {
            return "星期二";
        } else if (i == Calendar.WEDNESDAY) {
            return "星期三";
        } else if (i == Calendar.THURSDAY) {
            return "星期四";
        } else if (i == Calendar.FRIDAY) {
            return "星期五";
        } else if (i == Calendar.SATURDAY) {
            return "星期六";
        } else {
            return "";
        }
    }

    /**
     * 获得当前月份的天数
     *
     * @return 1月 31
     */
    public static int getMonthDays(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int i = instance.get(Calendar.MONTH);
        if (i == Calendar.FEBRUARY) {
            if (isLeapYear(date)) {
                return 29;
            }
            return 28;
        } else if (i == Calendar.APRIL || i == Calendar.JUNE || i == Calendar.SEPTEMBER || i == Calendar.NOVEMBER) {
            return 30;
        } else {
            return 31;
        }
    }

    /**
     * 获得当前月份的天数
     *
     * @return 1月 31
     */
    public static int getMonthDays(Calendar calendar) {
        int i = calendar.get(Calendar.MONTH);
        if (i == Calendar.FEBRUARY) {
            if (isLeapYear(calendar.getTime())) {
                return 29;
            }
            return 28;
        } else if (i == Calendar.APRIL || i == Calendar.JUNE || i == Calendar.SEPTEMBER || i == Calendar.NOVEMBER) {
            return 30;
        } else {
            return 31;
        }
    }

    /**
     * 获得2个日期间隔的日期,包含这2个日期
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return List<Calendar>
     */
    public static List<Calendar> getIntervalDays(Calendar startDate, Calendar endDate) {
        int intervalYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
        if (intervalYear == 0) {
            return getIntervalDaysWithSameYear(startDate, endDate);
        }

        List<Calendar> list = new ArrayList<>();
        for (int i = 0; i < intervalYear + 1; i++) {
            if (i == 0) {
                Calendar instance = Calendar.getInstance();
                instance.set(startDate.get(Calendar.YEAR), Calendar.DECEMBER, 31);
                list.addAll(getIntervalDaysWithSameYear(startDate, instance));
            } else if (i == intervalYear) {
                Calendar instance = Calendar.getInstance();
                instance.set(endDate.get(Calendar.YEAR), Calendar.JANUARY, 1);
                list.addAll(getIntervalDaysWithSameYear(instance, endDate));
            } else {
                Calendar start = Calendar.getInstance();
                start.set(startDate.get(Calendar.YEAR) + i, Calendar.JANUARY, 1);
                Calendar end = Calendar.getInstance();
                end.set(startDate.get(Calendar.YEAR) + i, Calendar.DECEMBER, 31);
                list.addAll(getIntervalDaysWithSameYear(start, end));
            }
        }
        return list;
    }

    /**
     * 获得2个日期间隔的日期,包含这2个日期,必须是同一年
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return List<Calendar>
     */
    public static List<Calendar> getIntervalDaysWithSameYear(Calendar startDate, Calendar endDate) {
        List<Calendar> list = new ArrayList<>();
        int intervalMonth = endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
        if (intervalMonth == 0) {
            //1.1 同一个月
            int intervalDay = endDate.get(Calendar.DAY_OF_MONTH) - startDate.get(Calendar.DAY_OF_MONTH);
            for (int i = 0; i < intervalDay + 1; i++) {
                list.add(getIntervalDay(startDate, i));
            }
            return list;
        }
        for (int i = 0; i < intervalMonth + 1; i++) {
            //1.2 不同月
            if (i == intervalMonth) {
                //1.2.1 最后一个月
                Calendar instance = Calendar.getInstance();
                instance.setTime(endDate.getTime());
                for (int j = 0; j < endDate.get(Calendar.DAY_OF_MONTH); j++) {
                    instance.set(Calendar.DAY_OF_MONTH, 1);
                    list.add(getIntervalDay(instance, j));
                }
            } else if (i == 0) {
                //1.2.2 第一个月份
                int intervalDay = getMonthDays(startDate.getTime()) - startDate.get(Calendar.DAY_OF_MONTH);
                for (int j = 0; j < intervalDay + 1; j++) {
                    list.add(getIntervalDay(startDate, j));
                }
            } else {
                //1.2.3 正常月份
                Calendar instance = Calendar.getInstance();
                instance.setTime(startDate.getTime());
                instance.set(Calendar.MONTH, startDate.get(Calendar.MONTH) + i);
                for (int j = 0; j < getMonthDays(startDate.getTime()) + 1; j++) {
                    instance.set(Calendar.DAY_OF_MONTH, 1);
                    list.add(getIntervalDay(instance, j));
                }
            }
        }
        return list;
    }

    /**
     * 获得day天后的 Calendar
     *
     * @param startDate 开始时间
     * @param day 间隔天数
     * @return Calendar
     */
    public static Calendar getIntervalDay(Calendar startDate, int day) {
        long lDay = startDate.getTimeInMillis() + ConstUtils.DAY * day;
        Calendar instance = Calendar.getInstance();
        instance.setTime(millis2Date(lDay));
        return instance;
    }

    /**
     * 获取星期
     *
     * @param date Date类型时间
     * @return 星期
     */
    public static String getWeek(Date date) {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
    }

    /**
     * 获取星期
     *
     * @param millis 毫秒时间戳
     * @return 星期
     */
    public static String getWeek(long millis) {
        return getWeek(new Date(millis));
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 1...5
     */
    public static int getWeekIndex(String time) {
        return getWeekIndex(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 1...7
     */
    public static int getWeekIndex(String time, String pattern) {
        return getWeekIndex(string2Date(time, pattern));
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     *
     * @param date Date类型时间
     * @return 1...7
     */
    public static int getWeekIndex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     *
     * @param millis 毫秒时间戳
     * @return 1...7
     */
    public static int getWeekIndex(long millis) {
        return getWeekIndex(millis2Date(millis));
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 1...5
     */
    public static int getWeekOfMonth(String time) {
        return getWeekOfMonth(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 1...5
     */
    public static int getWeekOfMonth(String time, String pattern) {
        return getWeekOfMonth(string2Date(time, pattern));
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param date Date类型时间
     * @return 1...5
     */
    public static int getWeekOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param millis 毫秒时间戳
     * @return 1...5
     */
    public static int getWeekOfMonth(long millis) {
        return getWeekOfMonth(millis2Date(millis));
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 1...54
     */
    public static int getWeekOfYear(String time) {
        return getWeekOfYear(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 1...54
     */
    public static int getWeekOfYear(String time, String pattern) {
        return getWeekOfYear(string2Date(time, pattern));
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param date Date类型时间
     * @return 1...54
     */
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param millis 毫秒时间戳
     * @return 1...54
     */
    public static int getWeekOfYear(long millis) {
        return getWeekOfYear(millis2Date(millis));
    }

    /**
     * 获取生肖
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 生肖
     */
    public static String getChineseZodiac(String time) {
        return getChineseZodiac(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取生肖
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 生肖
     */
    public static String getChineseZodiac(String time, String pattern) {
        return getChineseZodiac(string2Date(time, pattern));
    }

    /**
     * 获取生肖
     *
     * @param date Date类型时间
     * @return 生肖
     */
    public static String getChineseZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
    }

    /**
     * 获取生肖
     *
     * @param millis 毫秒时间戳
     * @return 生肖
     */
    public static String getChineseZodiac(long millis) {
        return getChineseZodiac(millis2Date(millis));
    }

    /**
     * 获取生肖
     *
     * @param year 年
     * @return 生肖
     */
    public static String getChineseZodiac(int year) {
        return CHINESE_ZODIAC[year % 12];
    }

    /**
     * 获取星座
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 生肖
     */
    public static String getZodiac(String time) {
        return getZodiac(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取星座
     * <p>time格式为pattern</p>
     *
     * @param time 时间字符串
     * @param pattern 时间格式
     * @return 生肖
     */
    public static String getZodiac(String time, String pattern) {
        return getZodiac(string2Date(time, pattern));
    }

    /**
     * 获取星座
     *
     * @param date Date类型时间
     * @return 星座
     */
    public static String getZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getZodiac(month, day);
    }

    /**
     * 获取星座
     *
     * @param millis 毫秒时间戳
     * @return 星座
     */
    public static String getZodiac(long millis) {
        return getZodiac(millis2Date(millis));
    }

    /**
     * 获取星座
     *
     * @param month 月
     * @param day 日
     * @return 星座
     */
    public static String getZodiac(int month, int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month - 1]
                ? month - 1
                : (month + 10) % 12];
    }
}