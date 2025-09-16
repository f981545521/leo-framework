package cn.acyou.leo.tool.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * @author youfang
 * @version [1.0.0, 2025/6/23 15:21]
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class MapperUtils {

    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 默认批次提交数量
     */
    private static final int DEFAULT_BATCH_SIZE = 1000;

    @Autowired
    public void setRedisTemplate(SqlSessionFactory sqlSessionFactory) {
        MapperUtils.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 批量更新
     *
     * @param list           更新的列表
     * @param updateFunction 更新的方法
     * @param <T>            实体
     * @return 影响的行数
     */
    public static <T> int batchUpdate(List<T> list, Function<T, Integer> updateFunction) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int affectedRows = 0;
        try {
            for (int i = 0; i < list.size(); i++) {
                T item = list.get(i);
                affectedRows += updateFunction.apply(item);
                if (i % DEFAULT_BATCH_SIZE == 0 || i == list.size() - 1) {
                    sqlSession.flushStatements();
                    sqlSession.clearCache();
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
        return affectedRows;
    }

}
