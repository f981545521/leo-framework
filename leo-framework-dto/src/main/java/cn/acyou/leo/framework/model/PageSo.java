package cn.acyou.leo.framework.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/6]
 **/
public class PageSo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分页：页码
     */
    @ApiModelProperty("分页页码")
    private Integer pageNum = 1;
    /**
     * 分页：每页显示记录数
     */
    @ApiModelProperty("分页大小")
    private Integer pageSize = 10;
    /**
     * 排序 example: createTime-desc,roleCode-asc}
     */
    @ApiModelProperty("排序规则")
    private String sorts;

    /**
     * 支持排序的字段与对应数据库字段
     * <p>
     * example:
     * <pre>
     *         Map&lt;String, String&gt; supportFieldMap = new HashMap&lt;&gt;();
     *         supportFieldMap.put("createTime", "create_time");
     *         supportFieldMap.put("roleCode", "role_code");
     * </pre>
     * example2 支持别名:
     * <pre>
     *         Map&lt;String, String&gt; supportFieldMap = new HashMap&lt;&gt;();
     *         supportFieldMap.put("createTime", "p.create_time");
     *         supportFieldMap.put("roleCode", "p.role_code");
     * </pre>
     *
     * @return Map k:页面字段 v:排序的数据库字段
     */
    public Map<String, String> supportField() {
        return null;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }
}
