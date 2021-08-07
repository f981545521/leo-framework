package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.base.ClientType;
import cn.acyou.leo.framework.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求客户端类型
 *
 * @author youfang
 * @version [1.0.0, 2020/7/8]
 **/
public class SourceUtil {
    /**
     * 请求头KEY
     */
    public static final String SOURCE_KEY = "source";

    /**
     * 获取请求客户端类型（根据User-Agent）
     *
     * @param request 请求
     * @return {@link ClientType}
     */
    public static ClientType getClientTypeByUserAgent(HttpServletRequest request) {
        if (UserAgentUtil.isWeChat(request)){
            return ClientType.MINI_PROGRAM;
        }
        if (UserAgentUtil.isAndroid(request)){
            return ClientType.ANDROID;
        }
        if (UserAgentUtil.isPcWeb(request)){
            return ClientType.WEB_MANAGER;
        }
        throw new ServiceException("不支持的设备类型，请检查！");
    }

    /**
     * 获取请求客户端类型（根据 header中的SOURCE_KEY）
     *
     * @param request 请求
     * @return {@link ClientType}
     */
    public static ClientType getClientTypeBySource(HttpServletRequest request) {
        String header = request.getHeader(SOURCE_KEY);
        if (StringUtils.isNotEmpty(header) && NumberUtils.isParsable(header)) {
            int i = Integer.parseInt(header);
            for (ClientType clientType : ClientType.values()) {
                if (i == clientType.getCode()) {
                    return clientType;
                }
            }
        }
        return ClientType.UNKNOWN;
    }

}