package cn.acyou.leo.framework.xss;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * XSS过滤处理
 *
 * @author youfang
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Whitelist whitelist = createWhitelist();

    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    private byte[] requestBody;
    private Charset charSet;

    private static Whitelist createWhitelist() {
        return Whitelist.relaxed()
                .removeProtocols("a", "href", "ftp", "http", "https", "mailto")
                .removeProtocols("img", "src", "http", "https")

                .addAttributes("a", "href", "title", "target")  // 官方默认会将 target 给过滤掉

                /*
                 * 在 Whitelist.relaxed() 之外添加额外的白名单规则
                 */
                .addTags("div", "span", "embed", "object", "param")
                .addAttributes(":all", "style", "class", "id", "name")
                .addAttributes("object", "width", "height", "classid", "codebase")
                .addAttributes("param", "name", "value")
                .addAttributes("embed", "src", "quality", "width", "height", "allowFullScreen", "allowScriptAccess", "flashvars", "name", "type", "pluginspage");
    }

    private static String[] filter(String[] values) {
        if (values != null) {
            for (int i = 0, len = values.length; i < len; i++) {
                if (values[i] != null && !"".equals(values[i])) {
                    values[i] = Jsoup.clean(values[i], "", whitelist, outputSettings).trim();
                }
            }
        }
        return values;
    }

    private static String filter(String value) {
        if (value != null) {
            value = Jsoup.clean(value, "", whitelist, outputSettings).trim();
        }
        return value;
    }

    /**
     * RequestBody Xss 过滤
     *
     * @param request 请求
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        //缓存请求body
        try {
            String requestBodyStr = getRequestPostStr(request);
            if (StringUtils.isNotBlank(requestBodyStr)) {
                requestBodyStr = filter(requestBodyStr);
                JSONObject resultJson = JSONObject.parseObject(requestBodyStr);
                requestBody = resultJson.toString().getBytes(charSet);
            } else {
                requestBody = new byte[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        String charSetStr = request.getCharacterEncoding();
        if (charSetStr == null) {
            charSetStr = "UTF-8";
        }
        charSet = Charset.forName(charSetStr);
        return StreamUtils.copyToString(request.getInputStream(), charSet);
    }

    /**
     * RequestParam Xss 过滤
     *
     * @param name 参数名
     * @return 参数值
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                // 防xss攻击和过滤前后空格
                escapseValues[i] = Jsoup.clean(values[i], "", whitelist, outputSettings).trim();
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    public ServletInputStream getInputStream() {
        if (requestBody == null) {
            requestBody = new byte[0];
        }

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

}