package cn.acyou.leo.tool.dto.dict;

import cn.acyou.leo.framework.model.TreeNode;
import lombok.Data;

import java.util.List;


/**
 * @author youfang
 * @version [1.0.0, 2020/7/16]
 **/
@Data
public class DictTreeVo extends DictVo implements TreeNode<Long, DictTreeVo> {

    /**
     * 子节点
     */
    private List<DictTreeVo> childNodes;

    /**
     * 获取节点id
     *
     * @return 树节点id
     */
    @Override
    public Long id() {
        return super.getId();
    }

    /**
     * 获取该节点的父节点id
     *
     * @return 父节点id
     */
    @Override
    public Long parentId() {
        return super.getParentId();
    }

    /**
     * 是否是根节点
     *
     * @return true：根节点
     */
    @Override
    public boolean root() {
        return super.getParentId() == 0;
    }

    /**
     * 设置节点的子节点列表
     *
     * @param children 子节点
     */
    @Override
    public void children(List<DictTreeVo> children) {
        this.childNodes = children;
    }

    /**
     * 获取所有子节点
     *
     * @return 子节点列表
     */
    @Override
    public List<? extends TreeNode<Long, DictTreeVo>> children() {
        return childNodes;
    }
}
