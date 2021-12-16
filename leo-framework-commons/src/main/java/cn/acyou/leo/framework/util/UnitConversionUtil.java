package cn.acyou.leo.framework.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

/**
 * 单位换算工具
 *
 * @author youfang
 * @version [1.0.0, 2020-4-6 下午 09:02]
 **/
public class UnitConversionUtil {
    /**
     * 字节转换 为可读值
     * <pre>
     *     convertFileSize(200000L);    = 195 KB
     * </pre>
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }


    /**
     * <p>转换可读时间，为：时/分/秒/毫秒</p>
     *
     * <pre>
     *     convertReadableTime(333333, TimeUnit.MILLISECONDS) = |0h 5m 33.333s|
     * </pre>
     *
     * @param duration 持续时间
     * @param unit     单位
     * @return 可读的时间
     */
    public static String convertReadableTime(long duration, TimeUnit unit) {
        long milliseconds = TimeUnit.MILLISECONDS.convert(duration, unit);
        long hours = TimeUnit.HOURS.convert(milliseconds, TimeUnit.MILLISECONDS);
        if (hours > 0) {
            milliseconds = milliseconds - (hours * 60 * 60 * 1000);
        }
        long minutes = TimeUnit.MINUTES.convert(milliseconds, TimeUnit.MILLISECONDS);
        if (minutes > 0) {
            milliseconds = milliseconds - (minutes * 60 * 1000);
        }
        long seconds = TimeUnit.SECONDS.convert(milliseconds, TimeUnit.MILLISECONDS);
        if (seconds > 0) {
            milliseconds = milliseconds - (seconds * 1000);
        }
        return String.format("|%sh %sm %s.%ss|", hours, minutes, seconds, milliseconds);
    }


    /**
     * 转换时间 支持：(天、时、分、秒、微秒)
     *
     * @param duration   持续时间
     * @param sourceUnit 源单位
     * @param targetUnit 目标单位
     * @return 转换后的时间
     */
    public static BigDecimal convertTime(long duration, TimeUnit sourceUnit, TimeUnit targetUnit){
        long milliseconds = TimeUnit.MILLISECONDS.convert(duration, sourceUnit);
        if (TimeUnit.MILLISECONDS.equals(targetUnit)) {
            return new BigDecimal(milliseconds);
        }
        if (TimeUnit.SECONDS.equals(targetUnit)) {
            return new BigDecimal(milliseconds).divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP);
        }
        if (TimeUnit.MINUTES.equals(targetUnit)) {
            return new BigDecimal(milliseconds).divide(new BigDecimal(60 * 1000), 2, RoundingMode.HALF_UP);
        }
        if (TimeUnit.HOURS.equals(targetUnit)) {
            return new BigDecimal(milliseconds).divide(new BigDecimal(60 * 60 * 1000), 2, RoundingMode.HALF_UP);
        }
        if (TimeUnit.DAYS.equals(targetUnit)) {
            return new BigDecimal(milliseconds).divide(new BigDecimal(24 * 60 * 60 * 1000), 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

}
