package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.framework.util.RegexUtil;
import cn.acyou.leo.tool.dto.dict.DictSo;
import cn.acyou.leo.tool.dto.dict.DictVo;
import cn.acyou.leo.tool.entity.Dict;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/11 9:26]
 **/
public class MainTest501 {

    public static void main(String[] args) {
        Dict dict = new Dict();
        dict.setCode("1");
        dict.setName("你好");
        dict.setId(1L);
        List<DictSo> dictSoList = new ArrayList<>();
        DictSo dictSo = new DictSo();
        dictSo.setCode("1");
        dictSo.setName("SUB");
        dictSoList.add(dictSo);
        //dict.setDictSoList(dictSoList);
        System.out.println(dict);
        DictVo copy = BeanCopyUtil.copy(dict, DictVo.class);
        System.out.println(copy);
        //DictVo dictVo = CopyUtil.to(dict, DictVo.class);
        //System.out.println(dictVo);
    }

    public static void main3(String[] args) {
        // JVM试图使用额最大内存量 (单位是字节)
        System.out.println("最大内存量:" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
        //的JVM内存总量(单位是字节)
        System.out.println("目前初始内存量:" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
        //得到JVM中的空闲内存量(单位是字节)
        System.out.println("目前可用的内存量:" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
        //可用处理器的数目
        System.out.println("可用处理器的数目:" + Runtime.getRuntime().availableProcessors());
    }

    public static void main2(String[] args) {
        String s = "(scratchpad) \n1. Introduction: Welcoming the audience and introducing the topic of Mother's Day.\n2. Person1's View: Emphasizing the importance of thoughtful gifts.\n3. Person2's View: Arguing that simple, easily accessible gifts can be just as meaningful.\n4. Fierce Debate: Discussing the pros and cons of different types of gifts.\n5. Conclusion: Wrapping up the conversation with final thoughts and a goodbye message.\n```\n\n<Person1> \"欢迎来到硅基播客！今天我们要讨论一个非常特别的话题——母亲节。你知道吗？今天就是母亲节了！你给妈妈准备礼物了吗？\"</Person1>\n\n<Person2> \"哦，我猜你还没给妈妈买礼物吧？其实有一样东西不费劲，又很轻易买得到。\"</Person2>\n\n<Person1> \"嗯，我觉得母亲节的礼物应该是特别的，不能随便应付。妈妈为我们付出了那么多，难道不应该用心准备吗？\"</Person1>\n\n<Person2> \"我不同意！有时候简单的礼物反而更能打动人心。比如一束鲜花或者一张亲手写的卡片，这些都能表达我们的心意。\"</Person2>\n\n<Person1> \"但是，鲜花和卡片太普通了，妈妈可能会觉得我们不够用心。为什么不花点时间，挑选一件她真正需要或者喜欢的东西呢？\"</Person1>\n\n<Person2> \"你说的也有道理，但并不是所有人都有时间和精力去精心挑选礼物。生活节奏这么快，简单的礼物也能传达我们的爱和感激。\"</Person2>\n\n<Person1> \"可是，母亲节一年就一次，难道不值得我们花点心思吗？比如带妈妈去她喜欢的餐厅吃饭，或者陪她度过一个愉快的下午，这些都比简单的礼物更有意义。\"</Person1>\n\n<Person2> \"嗯，我明白你的意思，但我还是觉得心意最重要。无论礼物贵重与否，只要能让妈妈感受到我们的爱，就足够了。\"</Person2>\n\n<Person1> \"好吧，或许我们可以找到一个平衡点。既有心意，又不需要太多时间和精力。比如，提前准备一些小惊喜，或者在母亲节当天给妈妈一个大大的拥抱。\"</Person1>\n\n<Person2> \"对，这样的想法不错！其实，母亲节的意义就在于表达对妈妈的爱和感激，无论礼物是什么，只要用心就好。\"</Person2>\n\n<Person1> \"没错！希望大家都能在这个特别的日子里，让妈妈感受到我们的爱。感谢大家收听硅基播客，祝所有的妈妈们母亲节快乐！\"</Person1>\n\n<Person2> \"是的，祝所有的妈妈们节日快乐！再见！\"</Person2>";
        String[] split = s.split("\n\n");
        List<JSONObject> talkList = new ArrayList<>();
        for (String s1 : split) {
            if (s1.startsWith("<Person")) {
                String person = s1.substring(1, s1.indexOf(">"));
                String content = s1.substring(s1.indexOf("\"") + 1, s1.lastIndexOf("\""));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("speaker", person);
                jsonObject.put("content", content);
                talkList.add(jsonObject);
            }
        }
        System.out.println(talkList);
        System.out.println("ok");
    }

    public static void main1(String[] args) {
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
