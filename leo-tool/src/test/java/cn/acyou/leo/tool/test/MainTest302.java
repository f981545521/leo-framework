package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.UrlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/29 17:20]
 **/
public class MainTest302 {
    public static void main(String[] args) throws Exception {
        //Document vplayDocument = Jsoup.connect("https://cddys.me/vodplay/502348-1-12.html").get();
        Document vplayDocument = Jsoup.connect("https://www.myd03.com/vodplay/10047-2-1.html").get();
        String protocolHost = UrlUtil.getProtocolHost(vplayDocument.location());
        Elements scripts = vplayDocument.select("script");
        JSONObject playInfo = new JSONObject();
        Map<String, String> vodData = new HashMap<>();
        for (int j = 0; j < scripts.size(); j++) {
            Element elementPlay = scripts.get(j);
            String html = elementPlay.html();
            if (html.contains("vod_name") && html.contains("vod_image")) {
                String[] split = html.replaceAll("var", "").replaceAll("'", "").replaceAll(" ", "").split(",");
                for (String s : split) {
                    String[] split1 = s.split("=");
                    vodData.put(split1[0], split1[1]);
                }
            }
            if (html.contains("player_aaaa")) {
                playInfo = JSON.parseObject(html.substring(html.indexOf("{"), html.lastIndexOf("}") + 1));
            }
        }
        playInfo.getJSONObject("vod_data").putAll(vodData);
        String url = UrlUtil.decode(playInfo.getString("url"));
        String link_next = UrlUtil.decode(playInfo.getString("link_next"));

        System.out.println("ok");
        String vod_data = playInfo.getJSONObject("vod_data").toJSONString();
        System.out.println(vod_data);
        Elements elements = vplayDocument.select(".module-play-list-link.active").get(0).parent().children();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String text = element.text();
            String href = element.attr("href");
            boolean isCurrent = false;
            if (element.html().contains("playon")) {
                isCurrent = true;
            }
            String playUrl = getPlayUrl(protocolHost + href);
            System.out.println(text + "=>" + (protocolHost + href) + "("+playUrl+")" + (isCurrent ? "【当前】" : ""));
        }
    }

    public static String getPlayUrl(String url) throws Exception{
        Document vplayDocument = Jsoup.connect(url).get();
        Elements scripts = vplayDocument.select("script");
        JSONObject playInfo = new JSONObject();
        Map<String, String> vodData = new HashMap<>();
        for (int j = 0; j < scripts.size(); j++) {
            Element elementPlay = scripts.get(j);
            String html = elementPlay.html();
            if (html.contains("vod_name") && html.contains("vod_image")) {
                String[] split = html.replaceAll("var", "").replaceAll("'", "").replaceAll(" ", "").split(",");
                for (String s : split) {
                    String[] split1 = s.split("=");
                    vodData.put(split1[0], split1[1]);
                }
            }
            if (html.contains("player_aaaa")) {
                playInfo = JSON.parseObject(html.substring(html.indexOf("{"), html.lastIndexOf("}") + 1));
            }
        }
        playInfo.getJSONObject("vod_data").putAll(vodData);
        return UrlUtil.decode(playInfo.getString("url"));
    }

}
