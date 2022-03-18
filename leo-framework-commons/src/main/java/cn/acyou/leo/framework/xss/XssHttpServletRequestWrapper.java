package cn.acyou.leo.framework.xss;

import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * XSS过滤处理
 *
 * @author youfang
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] requestBody;
    private Charset charSet;

    private static String[] filter(String[] values) {
        if (values != null) {
            for (int i = 0, len = values.length; i < len; i++) {
                if (values[i] != null && !"".equals(values[i])) {
                    values[i] = filter(values[i]);
                }
            }
        }
        return values;
    }

    private static String filter(String value) {
        if (value != null) {
            value = stripXSS(value);
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
        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
            //缓存请求body
            try {
                String requestBodyStr = getRequestPostStr(request);
                if (StringUtils.hasText(requestBodyStr)) {
                    requestBodyStr = filter(requestBodyStr);
                    requestBody = requestBodyStr.getBytes(charSet);
                } else {
                    requestBody = new byte[0];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                escapseValues[i] = filter(values[i]);
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

    private static String stripXSS(String value) {
        if (value != null) {
            //script标签对
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid eval(...)
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }

}