package cn.acyou.leo.framework.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/14 10:35]
 **/
public class VersionUtil {
    private static final Comparator<Integer> INTEGER_COMPARATOR = Integer::compareTo;
    private static final Comparator<String> STRING_COMPARATOR = String::compareTo;
    /**
     * 比较版本
     * <pre>
     * compareVersion("1.1.0.RELEASE", "1.1.0.RELEASE") //0  when versionA = versionB
     * compareVersion("1.1.0.RELEASE", "1.2.0.RELEASE") //-1 when versionA < versionB
     * compareVersion("1.1.0.RELEASE", "1.0.0.RELEASE")  //1 when versionA > versionB
     * </pre>
     * 注意：
     * It's illegal to compare. example : [1.1.0.RELEASE & 1.1.0]
     * @param versionA versionA
     * @param versionB versionB
     * @return int 比较结果
     */
    public static int compareVersion(String versionA, String versionB) {
        String[] sA = versionA.split("\\.");
        String[] sB = versionB.split("\\.");
        try {
            for (int i = 0; i < Math.max(sA.length, sB.length); i++) {
                if (NumberUtils.isDigits(sA[i]) && NumberUtils.isDigits(sB[i])) {
                    int h = Objects.compare(Integer.valueOf(sA[i]), Integer.valueOf(sB[i]), INTEGER_COMPARATOR);
                    if (h != 0) {
                        return h;
                    }
                }else {
                    int h = Objects.compare(sA[i], sB[i], STRING_COMPARATOR);
                    if (h != 0) {
                        return h;
                    }
                }
            }
            return 0;
        }catch (Exception e) {
            //ignore
        }
        return 0;
    }

}
