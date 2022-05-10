package cn.acyou.leo.framework.mybatis.extend;

import lombok.Getter;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 10:58]
 **/
@Getter
public enum CustomerSqlMethod {
    /**
     * 插入
     */
    INSERT_IGNORE_ONE("insertIgnore", "插入一条数据（选择字段插入），如果中已经存在相同的记录，则忽略当前新数据", "<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>"),

    /**
     * 批量插入
     */
    INSERT_IGNORE_BATCH("insertIgnoreBatch", "插入一条数据（选择字段插入），如果中已经存在相同的记录，则忽略当前新数据（批量）", "<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>");


    private final String method;
    private final String desc;
    private final String sql;

    CustomerSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }
}
