package cn.acyou.leo.framework.model;

import cn.acyou.leo.framework.model.base.DTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * Page Search Object
 * 分页查询对象，不管是GET还是POST 必须继承此类！！！
 *
 * @author youfang
 * @version [1.0.0, 2020/7/6]
 **/
public class PageSo extends DTO {

    /**
     * 分页：页码
     */
    @ApiModelProperty(value = "分页页码", required = true, example = "1")
    private Integer pageNum = 1;
    /**
     * 分页：每页显示记录数
     */
    @ApiModelProperty(value = "分页大小", required = true, example = "10")
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
        if (pageNum == null) {
            throw new IllegalPageArgumentException("pageNum 不能为空！");
        }
        if (pageNum < 0) {
            throw new IllegalPageArgumentException("pageNum 必须大于0！");
        }
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            throw new IllegalPageArgumentException("pageSize 不能为空！");
        }
        if (pageSize < 0 || pageSize > 100) {
            throw new IllegalPageArgumentException("pageSize 取值范围不正确！合法范围：[0~100]");
        }
        this.pageSize = pageSize;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    /**
     * 页面参数异常
     *
     * @author fangyou
     * @version [1.0.0, 2021/11/12]
     */
    public static class IllegalPageArgumentException extends RuntimeException {
        public IllegalPageArgumentException(String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        return "PageSo{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", sorts='" + sorts + '\'' +
                '}';
    }
}
