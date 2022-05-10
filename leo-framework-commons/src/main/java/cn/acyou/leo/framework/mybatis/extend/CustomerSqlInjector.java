package cn.acyou.leo.framework.mybatis.extend;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * 自定义sql注入器，增加通用方法
 *
 * @author youfang
 * @version [1.0.0, 2022/5/10 11:00]
 **/
public class CustomerSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        // 插入数据，如果中已经存在相同的记录，则忽略当前新数据
        methodList.add(new InsertIgnore());
        // 批量插入数据，如果中已经存在相同的记录，则忽略当前新数据
        methodList.add(new InsertIgnoreBatch());
        return methodList;
    }
}