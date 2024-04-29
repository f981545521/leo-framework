package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/29 17:20]
 **/
public class MainTest301 {
    public static void main(String[] args) {
        String s = HttpUtil.get("https://www.emojiall.com/zh-hans/all-emojis");
        Document parse = Jsoup.parse(s);
        Elements select = parse.select(".emoji_card_list");
        for (int i = 2; i < select.size(); i++) {
            String h2 = select.get(i).select("h2").text();
            System.out.println("# " + h2);
            Elements select1 = select.get(i).select(".emoji_symbol");
            StringBuilder sb = new StringBuilder("");
            for (Element element : select1) {
                String attr = element.attr("data-clipboard-text");
                sb.append(attr);
            }
            System.out.println("## " + sb.toString());
        }
        System.out.println("ok");
    }
}
