package cn.acyou.leo.framework.model;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息返回实体
 *
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 */
public class PageData<T> implements Serializable {
    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页")
    private Integer pageNum;

    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

    @ApiModelProperty("总页数")
    private Integer totalPage;

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("有下一页")
    private boolean hasNextPage = true;

    @ApiModelProperty("返回数据")
    private List<T> list = new ArrayList<>();

    @ApiModelProperty("扩展数据信息（用于数据统计等...）")
    private Object extData;

    /**
     * Constructor
     */
    public PageData(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /* GET AND SET **/
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        processNextPage();
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        processNextPage();
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        processNextPage();
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Object getExtData() {
        return extData;
    }

    public void setExtData(Object extData) {
        this.extData = extData;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 处理 hasNextPage 值
     */
    public void processNextPage() {
        if (this.pageNum != null && this.pageSize != null && this.total != null) {
            int totalPage;
            if (pageSize == 0) {
                totalPage = this.total.intValue();
                this.hasNextPage = false;
            } else {
                totalPage = (int) ((total + pageSize - 1) / pageSize);
                if (pageNum >= totalPage) {
                    this.hasNextPage = false;
                }
            }
            this.totalPage = totalPage;
        }
    }

    /**
     * ToString
     */
    @Override
    public String toString() {
        return new StringBuilder()
                .append("分页数据 ： ")
                .append("[")
                .append("第 ").append(pageNum).append(" 页, ")
                .append("每页显示 ").append(pageSize).append(" 条, ")
                .append("当前页 ").append(list != null ? list.size() : 0).append(" 条记录，")
                .append("共 ").append(totalPage).append(" 页，")
                .append("共 ").append(total).append(" 条记录")
                .append("]").toString();
    }

}