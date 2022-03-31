package cn.acyou.leo.framework.wx.dto.mp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-09 11:02]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMpTags {
    /**
     * 标签ID
     */
    private Integer id;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 查询标签时，标签的人数
     */
    private Integer count;

    public WxMpTags(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
