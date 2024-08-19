package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.base.CommonTable;
import cn.acyou.leo.framework.mapper.CommonTableMapper;
import cn.acyou.leo.framework.mapper.ExecuteMapper;
import cn.acyou.leo.framework.model.IdReq;
import cn.acyou.leo.framework.util.*;
import cn.acyou.leo.framework.util.component.EmailUtil;
import cn.acyou.leo.framework.util.component.EmailUtil2;
import cn.acyou.leo.tool.entity.Area;
import cn.acyou.leo.tool.entity.DataAnalysis;
import cn.acyou.leo.tool.entity.Dict;
import cn.acyou.leo.tool.mapper.AreaMapper;
import cn.acyou.leo.tool.mapper.DictMapper;
import cn.acyou.leo.tool.service.AreaService;
import cn.acyou.leo.tool.service.DataAnalysisService;
import cn.acyou.leo.tool.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
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
    @Autowired
    private CommonTableMapper commonTableMapper;
    @Autowired
    private DataAnalysisService dataAnalysisService;

    @Test
    public void test34453(){
        List<DataAnalysis> list = dataAnalysisService.lambdaQuery().list();
        System.out.println(list);
    }

    @Test
    public void test3445(){
        Map<String, Object> param = new HashMap<>();
        param.put("name", "讲师");
        param.put("sort", "10");
        dictService.removeByMap(param);//DELETE FROM t_dict WHERE name = '讲师' AND sort = '10'
    }

    @Test
    public void test3443(){
        LambdaQueryChainWrapper<Dict> lambdaQuery = dictService.lambdaQuery();
        lambdaQuery.eq(Dict::getName, "讲师");
        lambdaQuery.gt(Dict::getSort, 10);
        dictService.remove(lambdaQuery.getWrapper());//DELETE FROM t_dict WHERE (name = '讲师' AND sort > 10)
    }

    @Test
    public void test3447(){
        LambdaQueryWrapper<Dict> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(Dict::getName, "讲师");
        lambdaQuery.gt(Dict::getSort, 10);
        dictService.remove(lambdaQuery);//DELETE FROM t_dict WHERE (name = '讲师' AND sort > 10)
    }

    @Test
    public void testEnvironmentHelper(){
        String[] activeProfiles = EnvironmentHelper.getActiveProfiles();
        System.out.println(Arrays.toString(activeProfiles));
    }

    @Test
    public void testEmail(){
        EmailUtil emailUtil = SpringHelper.getBean(EmailUtil.class);
        emailUtil.sendEmail("youfang@acyou.cn", "Test2", "2");
    }

    @Test
    public void testEmail2() throws Exception{
        JavaMailSender mailSender = SpringHelper.getBean(JavaMailSender.class);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("iblog_admin@126.com");
        helper.setTo("youfang@acyou.cn");
        helper.setSubject("Test4");
        helper.addAttachment("Picture.txt", new File("D:\\Program Files\\账号.txt"));

        StringBuffer content = new StringBuffer();
        content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        content.append("<body>");

        content.append("<h3> 运营日报-用户维度 </h3>\n");
        content.append("<table  border=\"1\" cellspacing=\"0\" cellpadding=\"0\" style='width: 100%;    text-align: center;'>\n");
        StringBuffer buffer = new StringBuffer();
        appendTh(buffer, "ID");
        appendTh(buffer, "姓名");
        appendTh(buffer, "年龄");
        appendTr(content, buffer.toString());
        buffer = new StringBuffer();
        appendTd(buffer, "1");
        appendTd(buffer, "张飞");
        appendTd(buffer, "32");
        appendTr(content, buffer.toString());
        buffer = new StringBuffer();
        appendTd(buffer, "2");
        appendTd(buffer, "关羽");
        appendTd(buffer, "35");
        appendTr(content, buffer.toString());
        content.append("</table>");

        content.append("</body>");
        //helper.setText("3");
        helper.setText(content.toString(), true);
        mailSender.send(mimeMessage);
    }

    @Test
    public void testEmail3(){
        EmailUtil2.send("youfang@acyou.cn", "Test1", "1");
    }

    private void appendTr(StringBuffer content, String data) {
        content.append("<tr>\n");
        content.append(data);
        content.append("</tr>\n");
    }

    private void appendTh(StringBuffer content, String data) {
        content.append("<th style='background: darkgray;'>\n");
        content.append(data);
        content.append("</th>\n");
    }

    private void appendTd(StringBuffer content, String data) {
        content.append("<td>\n");
        content.append(data);
        content.append("</td>\n");
    }

    @Test
    public void testExecuteMapper3(){
        int res = executeMapper.executeDDLSql("alter table t_dict_v1 rename t_dict");
        System.out.println(res);
    }

    @Test
    public void testExecuteMapper2(){
        List<LinkedHashMap<String, Object>> linkedHashMaps = executeMapper.executeQuerySql("show create table student");
        System.out.println(linkedHashMaps);
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
    public void test345(){
        List<LinkedHashMap<String, Object>> linkedHashMaps = executeMapper.executeQuerySql("select * from student");
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = linkedHashMaps.get(0);
        stringObjectLinkedHashMap.put("序号", 1);
        System.out.println("ok");
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
    public void test12342V3() {
        System.out.println(executeMapper.tableIsExist("student"));
        System.out.println(executeMapper.tableIsExist("student_222"));
    }
    @Test
    public void test12342V4() {
        commonTableMapper.createTable("student_555");
        System.out.println("ok");
    }

    @Test
    public void test23342(){
        String tableName = "t_daily_hot_10";
        commonTableMapper.createTable(tableName);

        CommonTable commonTable = new CommonTable();
        commonTable.setAbbr("1");
        commonTable.setName("1");
        commonTable.setCode(UUID.randomUUID().toString());
        commonTable.setContent("@");
        commonTable.setText("1");
        commonTable.setType(0);
        commonTable.setCreateTime(new Date());
        commonTable.setUpdateTime(new Date());
        commonTableMapper.insertIgnoreSelective(tableName, commonTable);

        List<CommonTable> commonTables = new ArrayList<>();
        CommonTable commonTable1 = BeanCopyUtil.copy(commonTable, CommonTable.class);
        commonTable1.setCode(UUID.randomUUID().toString());
        commonTables.add(commonTable1);
        CommonTable commonTable2 = BeanCopyUtil.copy(commonTable, CommonTable.class);
        commonTable2.setCode(UUID.randomUUID().toString());
        commonTables.add(commonTable2);
        commonTableMapper.insertList(tableName, commonTables);

        List<Long> collect = commonTables.stream().map(CommonTable::getId).collect(Collectors.toList());
        List<CommonTable> commonTableList = commonTableMapper.selectByPrimaryKeyList(tableName, collect);
        System.out.println(commonTableList);
        commonTableMapper.deleteByPrimaryKeyList(tableName, collect);
        System.out.println(commonTable.getId());
    }

    @Test
    public void test12342Export() throws Exception{
        List<LinkedHashMap<String, Object>> linkedHashMaps = executeMapper.executeQuerySql("select * from student");
        File file = FileUtil.newFile("D:\\poi\\112344544.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ExcelUtil.exportExcel(fileOutputStream, new ArrayList<>(linkedHashMaps), "列表");
    }
    @Test
    public void test123424Export() throws Exception{
        File file = FileUtil.newFile("D:\\poi\\1123445443_3.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<>();
        List<Map<String, Object>> data1 = new ArrayList<>();
        Map<String, Object> datamap = new LinkedHashMap<>();
        datamap.put("序号", "1");
        datamap.put("姓名", "1");
        datamap.put("年龄", "1");
        data1.add(datamap);
        dataMap.put("首页1", data1);

        List<Map<String, Object>> data2 = new ArrayList<>();
        Map<String, Object> datamap2 = new LinkedHashMap<>();
        datamap2.put("序号", "1");
        datamap2.put("姓名", "1");
        datamap2.put("年龄", "1");
        data2.add(datamap2);
        dataMap.put("首页2", data2);
        //ExcelUtil.exportExcelMultiSheet(fileOutputStream, dataMap);

        XSSFWorkbook workbook = new XSSFWorkbook();
        ExcelUtil.createBuilder(workbook, "首页1")
                .createRow(0, (short) 400)   //标题行（一级） 行高400
                .createData("0-0-0-0(序号)[247,176,127||false]")
                .createData("0-0-1-1(姓名)[247,176,127||false]")
                .createData("0-0-2-2(年龄)[247,176,127||false]")
                .writeData(1, data1);

        ExcelUtil.createBuilder(workbook, "首页2")
                .createRow(0, (short) 400)   //标题行（一级） 行高400
                .createData("0-0-0-0(序号)[247,176,127||false]")
                .createData("0-0-1-1(姓名)[247,176,127||false]")
                .createData("0-0-2-2(年龄)[247,176,127||false]")
                .writeData(1, data2);

        workbook.write(new FileOutputStream(file));

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
