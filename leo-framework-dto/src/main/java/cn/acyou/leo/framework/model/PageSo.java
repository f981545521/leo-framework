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

    @ApiModelProperty(value = "包含总数查询", notes = "默认包含总数查询 如果设置为false则不执行count查询，返回总页数为-1", hidden = true)
    private Boolean includeCountQuery = true;

    @ApiModelProperty(value = "偏移量", hidden = true)
    private Integer pageOffset;

    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum != null) {
            if (pageNum < 0) {
                throw new IllegalPageArgumentException("pageNum 必须大于0！");
            }
            this.pageNum = pageNum;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Boolean getIncludeCountQuery() {
        return includeCountQuery;
    }

    public void setIncludeCountQuery(Boolean includeCountQuery) {
        this.includeCountQuery = includeCountQuery;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize != null) {
            if (pageSize < 0 || pageSize > 500) {
                throw new IllegalPageArgumentException("pageSize 取值范围不正确！合法范围：[0~500]");
            }
            this.pageSize = pageSize;
        }
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
