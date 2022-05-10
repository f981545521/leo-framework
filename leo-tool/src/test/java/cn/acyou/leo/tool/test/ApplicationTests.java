package cn.acyou.leo.tool.test;

import cn.acyou.leo.tool.entity.Dict;
import cn.acyou.leo.tool.mapper.DictMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 11:04]
 **/
@SpringBootTest
public class ApplicationTests {
    @Autowired
    private DictMapper dictMapper;

    @Test
    public void test1() {
        Dict dict = new Dict();
        dict.setCode("AAA");
        dict.setParentId(0L);
        dict.setName("好");
        dict.setStatus(0);
        int i = dictMapper.insertIgnore(dict);
        System.out.println(i);
    }

    @Test
    public void test2() {
        List<Dict> dictList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Dict dict = new Dict();
            dict.setCode("AAA");
            dict.setParentId(0L);
            dict.setName("好");
            dict.setStatus(0);
            dictList.add(dict);
        }
        int i = dictMapper.insertIgnoreBatch(dictList);
        System.out.println(i);
    }
}
