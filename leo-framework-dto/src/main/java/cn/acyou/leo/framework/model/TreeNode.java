package cn.acyou.leo.framework.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/8]
 **/
public interface TreeNode<T, E> extends Serializable {
    /**
     * 获取节点id
     *
     * @return 树节点id
     */
    T id();

    /**
     * 获取该节点的父节点id
     *
     * @return 父节点id
     */
    T parentId();

    /**
     * 是否是根节点
     *
     * @return true：根节点
     */
    boolean root();

    /**
     * 设置节点的子节点列表
     *
     * @param children 子节点
     */
    void children(List<E> children);

    /**
     * 获取所有子节点
     *
     * @return 子节点列表
     */
    List<? extends TreeNode<T, E>> children();
}
