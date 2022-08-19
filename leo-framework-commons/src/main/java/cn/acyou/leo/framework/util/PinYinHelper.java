package cn.acyou.leo.framework.util;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author youfang
 */
@Slf4j
public class PinYinHelper {
    private static final Logger logger = LoggerFactory.getLogger(PinYinHelper.class);
    private static final String EMPTY_STR = "";
    private static final String COMMA = ",";
    public static final String INITIAL_OTHER = "#";

    /**
     * 中文转拼音 返回拼音首字母 如 中华人民共和国 返回 ZHRMGHG
     *
     * @param str 字符
     * @return 拼音
     */
    public static String transferToPinYin(String str) {
        try {
            return PinyinHelper.getShortPinyin(str).toUpperCase();
        } catch (PinyinException e) {
            logger.warn(e.getMessage(), e);
        }
        return EMPTY_STR;
    }


    /**
     * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，
     * 生成方式如（长沙市长:cssc,zssz,zssc,cssz）
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        try {
            return PinyinHelper.getShortPinyin(chines);
        } catch (PinyinException e) {
            logger.warn(e.getMessage(), e);
        }
        return EMPTY_STR;
    }

    /**
     * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
     * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
     * ,chongdangshen,zhongdangshen,chongdangcan）
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        try {
            return PinyinHelper.convertToPinyinString(chines, EMPTY_STR, PinyinFormat.WITHOUT_TONE);
        } catch (PinyinException e) {
            logger.warn(e.getMessage(), e);
        }
        return EMPTY_STR;
    }

    /**
     * parsePinyinAndHead 生成拼音和首字母组合
     * 如：内存 result ：内存,neicun,nc
     *
     * @param str str
     * @return String
     */
    public static String parsePinyinAndHead(String str) {
        if (StringUtils.hasText(str)) {
            return str + COMMA + converterToSpell(str) + COMMA + converterToFirstSpell(str);
        } else {
            return EMPTY_STR;
        }
    }


    /**
     * 生成字符串拼音首字母
     * Example:
     * <pre>
     *     parsePinyinInitial("内存") return: n
     *     parsePinyinInitial("SKU") return: S
     * </pre>
     * 若首字母是非字母： #
     *
     * @param str 字符串
     * @return String 首字母
     */
    public static String parsePinyinInitial(String str) {
        if (StringUtils.hasText(str)) {
            try {
                String shortPy = PinyinHelper.getShortPinyin(str);
                if (StringUtils.hasText(shortPy)) {
                    char c = shortPy.charAt(0);
                    if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                        return shortPy.substring(0, 1);
                    }
                }
            } catch (PinyinException e) {
                log.error(e.getMessage(), e);
            }
        }
        return INITIAL_OTHER;
    }

    /**
     * 生成字符串拼音首字母（大写） 参考：{@link #parsePinyinInitial}
     * Example:
     * <pre>
     *     parsePinyinInitial("内存") return: N
     *     parsePinyinInitial("SKU") return: S
     * </pre>
     * 若首字母是非字母： #
     *
     * @param str 字符串
     * @return String 首字母大写
     */
    public static String parsePinyinInitialUpperCase(String str) {
        return parsePinyinInitial(str).toUpperCase();
    }

    /**
     * 生成字符串拼音首字母（小写） 参考：{@link #parsePinyinInitial}
     * Example:
     * <pre>
     *     parsePinyinInitial("内存") return: n
     *     parsePinyinInitial("SKU") return: s
     * </pre>
     * 若首字母是非字母： #
     *
     * @param str 字符串
     * @return String 首字母小写
     */
    public static String parsePinyinInitialLowerCase(String str) {
        return parsePinyinInitial(str).toLowerCase();
    }


    public static void main(String[] args) throws Exception {
        System.out.println(converterToSpell("干一行,行一行，一行行，行行行"));
        //带声调标记
        System.out.println(PinyinHelper.convertToPinyinString("干一行,行一行，一行行，行行行", " ", PinyinFormat.WITH_TONE_MARK));
        //不带声调标记
        System.out.println(PinyinHelper.convertToPinyinString("干一行,行一行，一行行，行行行", " ", PinyinFormat.WITHOUT_TONE));
        //带音号
        System.out.println(PinyinHelper.convertToPinyinString("干一行,行一行，一行行，行行行", " ", PinyinFormat.WITH_TONE_NUMBER));
        //首字母
        System.out.println(PinyinHelper.getShortPinyin("干一行,行一行，一行行，行行行"));
    }


}
