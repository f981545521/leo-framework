package cn.acyou.leo.framework.constant;

/**
 * 数据权限类型
 *
 * @author youfang
 * @version [1.0.0, 2020/7/13]
 **/
public class DataScopeConstant {
    /**
     * 全部数据权限
     */
    public static final int DATA_SCOPE_ALL = 10;
    /**
     * 所拥有部门与下属部门的数据权限
     */
    public static final int DATA_SCOPE_STORAGE_AND_SUB = 15;
    /**
     * 所拥有部门的数据权限
     */
    public static final int DATA_SCOPE_STORAGE_ONLY = 20;
    /**
     * 自己创建的数据
     */
    public static final int DATA_SCOPE_OWN = 30;

}
