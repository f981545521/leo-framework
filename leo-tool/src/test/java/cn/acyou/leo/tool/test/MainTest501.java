package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.RegexUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/11 9:26]
 **/
public class MainTest501 {

    public static void main(String[] args) {
        String text = "继蔚小理后，又一家中国造车新势力登陆美股。\n" +
                "\n" +
                "北京时间5月10日晚，极氪智能科技控股有限公司（以下简称“极氪”）正式在美国纽交所挂牌上市，股票代码为“ZK”。据悉，因获超额认购，极氪扩大了IPO规模，以每股21美元的价格累计发行2100万股美国存托股票（ADS，每份ADS对应10份普通股），募资约4.41亿美元，若承销商行使其超额配售权，则发行规模将进一步扩大至2415万股ADS，募资约 5.07亿美元。\n" +
                "\n" +
                "蓝鲸新闻从极氪内部人士处独家获悉，在当晚的敲钟仪式上，共有11人参与敲钟，其中除了极氪智能科技CEO安聪慧外，还有吉利汽车控股有限公司行政总裁及执行董事桂生悦、吉利控股集团CEO李东辉、吉利高级副总裁杨学良、极氪CFO袁璟等。\n" +
                "\n" +
                "随着极氪的上市，被冠以“汽车狂人”之称的吉利控股集团创始人李书福，迎来了第九个IPO。此前，吉利系已有吉利汽车、沃尔沃汽车、钱江摩托、极星、亿咖通、汉马科技、力帆科技、路特斯8家上市公司。\n" +
                "\n" +
                "从上市时间上看，极氪从2021年04月15日品牌发布到IPO，用时37个月。据招股书披露，2021年、2022年、2023年极氪总营业收入分别为65亿元、319亿元、517亿元。" +
                "其营收构成主要来源于三块业务，2021/31/10包括整车销售、三电业务、研发及其他服务。截至2013-01-08，其已累计交付车辆超24万台。";
        System.out.println(getDateStr(text));

    }

    /**
     * 提取日期
     * 2021年04月15日
     * yyyy-MM-dd
     * yyyy/MM/dd
     * @param text 字符
     * @return {@link List}<{@link String}>
     */
    private static List<String> getDateStr(String text){
        List<String> matchStr = RegexUtil.getMatchStr(text, "\\d{4}(年)\\d{1,2}(月)\\d{1,2}(日)");
        matchStr.addAll(RegexUtil.getMatchStr(text, "\\d{4}(-|/)\\d{1,2}(-|/)\\d{1,2}"));
        return matchStr;
    }
}
