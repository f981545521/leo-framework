package cn.acyou.leo.framework.xss;

import cn.acyou.leo.framework.util.html.EscapeUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * XSS过滤处理
 *
 * @author ruoyi
 */
public class XssHttpServletRequestWrapperV3 extends HttpServletRequestWrapper {
    private byte[] requestBody;
    private Charset charSet;

    public XssHttpServletRequestWrapperV3(HttpServletRequest request) {
        super(request);
        //缓存请求body
        try {
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                String requestBodyStr = getRequestPostStr(request);
                if (StringUtils.hasText(requestBodyStr)) {
                    requestBody = requestBodyStr.getBytes(charSet);
                } else {
                    requestBody = new byte[0];
                }
            } else {
                requestBody = getRequestPostStr(request).getBytes(charSet);
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

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapesValues = new String[length];
            for (int i = 0; i < length; i++) {
                // 防xss攻击和过滤前后空格
                escapesValues[i] = EscapeUtil.clean(values[i]).trim();
            }
            return escapesValues;
        }
        return super.getParameterValues(name);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 非json类型，直接返回
        if (!isJsonRequest()) {
            return super.getInputStream();
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
        // 为空，直接返回
        String json = StreamUtils.copyToString(byteArrayInputStream, StandardCharsets.UTF_8);
        if (!StringUtils.hasText(json)) {
            return super.getInputStream();
        }

        // xss过滤
        json = EscapeUtil.clean(json).trim();
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream bis = new ByteArrayInputStream(jsonBytes);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public int available() {
                return jsonBytes.length;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return bis.read();
            }
        };
    }

    /**
     * 是否是Json请求
     */
    public boolean isJsonRequest() {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
        return StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
    }

}