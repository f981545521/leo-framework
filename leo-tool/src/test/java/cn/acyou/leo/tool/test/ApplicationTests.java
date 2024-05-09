package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.mapper.ExecuteMapper;
import cn.acyou.leo.framework.model.IdReq;
import cn.acyou.leo.framework.util.EnvironmentHelper;
import cn.acyou.leo.framework.util.ExcelUtil;
import cn.acyou.leo.framework.util.FileUtil;
import cn.acyou.leo.framework.util.StringUtils;
import cn.acyou.leo.tool.entity.Area;
import cn.acyou.leo.tool.entity.Dict;
import cn.acyou.leo.tool.mapper.AreaMapper;
import cn.acyou.leo.tool.mapper.DictMapper;
import cn.acyou.leo.tool.service.AreaService;
import cn.acyou.leo.tool.service.DictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 11:04]
 **/
@SpringBootTest
public class ApplicationTests {
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ExecuteMapper executeMapper;

    @Test
    public void testEnvironmentHelper(){
        String[] activeProfiles = EnvironmentHelper.getActiveProfiles();
        System.out.println(Arrays.toString(activeProfiles));
    }

    @Test
    public void testExecuteMapper(){
        executeMapper.executeDDLSql("CREATE TABLE `student_auto` (\n" +
                "  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',\n" +
                "  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '姓名',\n" +
                "  `age` int DEFAULT '0' COMMENT '年龄',\n" +
                "  `birth` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '生日',\n" +
                "  `ext` json DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Demo For Student';");
        executeMapper.executeDDLSql("ALTER TABLE student_auto MODIFY COLUMN ext varchar(1000) NULL");
        executeMapper.executeInsertSql("INSERT INTO scorpio.student_auto (id, name, age, birth, ext) VALUES(default, '自动插入数据_1', 2, '2022-05-31 09:13:28', NULL)");
        executeMapper.executeDDLSql("truncate table student_auto");
        executeMapper.executeDDLSql("drop table student_auto");
    }

    @Test
    public void test12342(){
        List<LinkedHashMap<String, Object>> linkedHashMaps = executeMapper.executeQuerySql("select * from student");
        System.out.println("查询所有数据：" + linkedHashMaps);
        System.out.println("查询所有数据数量v1：" + executeMapper.executeQuerySql("select count(*) as cnt from student"));
        System.out.println("查询所有数据数量v2(取单个字段)：" + executeMapper.executeIndividualQuerySql("select count(*) as cnt from student"));

        //插入数据
        int i = executeMapper.executeInsertSql("INSERT INTO scorpio.student (id, name, age, birth, ext) VALUES(default, '自动插入数据_1', 2, '2022-05-31 09:13:28', NULL)");
        System.out.println("插入数据：" + i);
        //插入数据（返回ID）
        IdReq idReq = new IdReq();
        executeMapper.executeInsertSqlV2("INSERT INTO scorpio.student (id, name, age, birth, ext) VALUES(default, '自动插入数据_2', 2, '2022-05-31 09:13:28', NULL)", idReq);
        System.out.println("插入数据（返回ID）：" + idReq.getId());

        String maxId = executeMapper.executeIndividualQuerySql("select max(id) from student");
        System.out.println("查询最大的ID：" + maxId);
        List<LinkedHashMap<String, Object>> idItem = executeMapper.executeQuerySql(StringUtils.formatTemplate("select * from student where id = {id}", maxId));
        System.out.println("根据ID查询：" + idItem);
        //修改数据
        int i1 = executeMapper.executeUpdateSql("update student set age = 1 where id = " + maxId);
        System.out.println("修改数据：" + i1);
        idItem = executeMapper.executeQuerySql(StringUtils.formatTemplate("select * from student where id = {id}", maxId));
        System.out.println("根据ID查询：" + idItem);
        //删除数据
        int i2 = executeMapper.executeDeleteSql("delete from student where id = " + maxId);
        System.out.println("删除数据：" + i2);
        idItem = executeMapper.executeQuerySql(StringUtils.formatTemplate("select * from student where id = {id}", maxId));
        System.out.println("根据ID查询：" + idItem);

    }

    @Test
    public void test12342V2() {
        List<LinkedHashMap<String, Object>> linkedHashMaps = executeMapper.executeQuerySql("SHOW COLUMNS FROM student");
        List<String> fields = linkedHashMaps.stream().map(x -> x.get("Field").toString()).collect(Collectors.toList());
        System.out.println("查询表字段列表：" + fields);
        System.out.println(executeMapper.columnIsExist("student", "name"));
        System.out.println(executeMapper.columnIsExist("student", "age"));
        System.out.println(executeMapper.columnIsExist("student", "name1"));
        System.out.println(executeMapper.columnIsExist("student", "age1"));
    }

    @Test
    public void test12342Export() throws Exception{
        List<LinkedHashMap<String, Object>> linkedHashMaps = executeMapper.executeQuerySql("select * from student");
        File file = FileUtil.newFile("D:\\poi\\112344544.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ExcelUtil.exportExcel(fileOutputStream, new ArrayList<>(linkedHashMaps), "列表");
    }


    @Test
    public void test233() {
        List<Area> s = areaService.lambdaQuery().like(Area::getName, "宿").list();
        System.out.println(s);
    }

    @Test
    public void test3() {
        List<Area> areas = areaMapper.selectIncludeParent(320000L);
        List<Area> areas1 = areaMapper.selectIncludeChild(320000L);
        System.out.println("ok");
    }

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
    public void test221() {
        Dict dict = new Dict();
        dict.setCode("AAETGEMNMNMA");
        dict.setParentId(0L);
        dict.setName("好");
        dict.setStatus(0);
        int i = dictMapper.insertWhereNotExist(dict, "select name from t_dict where id = 21560");
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
