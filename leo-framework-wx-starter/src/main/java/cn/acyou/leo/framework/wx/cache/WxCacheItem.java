package cn.acyou.leo.framework.wx.cache;

import lombok.Data;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 14:08]
 */
@Data
public class WxCacheItem {

    /**
     * 缓存值
     */
    private String cacheValue;
    /**
     * 过期时间(null 表示长期)
     */
    private Long expiresTime;

    public WxCacheItem() {
    }

    public WxCacheItem(String cacheValue, Long expiresTime) {
        this.cacheValue = cacheValue;
        this.expiresTime = expiresTime;
    }
}
