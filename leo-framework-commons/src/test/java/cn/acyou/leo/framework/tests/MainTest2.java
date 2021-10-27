package cn.acyou.leo.framework.tests;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;

/**
 * @author fangyou
 * @version [1.0.0, 2021-10-27 10:23]
 */
public class MainTest2 {
    public static void main(String[] args) {
        PageData<Object> empty = PageQuery.empty();
        System.out.println(empty);
    }
}
