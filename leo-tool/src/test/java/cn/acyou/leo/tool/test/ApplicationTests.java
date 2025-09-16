package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.base.CommonTable;
import cn.acyou.leo.framework.base.TableFields;
import cn.acyou.leo.framework.mapper.CommonTableMapper;
import cn.acyou.leo.framework.mapper.ExecuteMapper;
import cn.acyou.leo.framework.model.IdReq;
import cn.acyou.leo.framework.util.*;
import cn.acyou.leo.framework.util.component.EmailUtil;
import cn.acyou.leo.framework.util.component.EmailUtil2;
import cn.acyou.leo.framework.util.function.Task;
import cn.acyou.leo.tool.dto.dict.DictSaveReq;
import cn.acyou.leo.tool.entity.Area;
import cn.acyou.leo.tool.entity.Dict;
import cn.acyou.leo.tool.entity.ScheduleJob;
import cn.acyou.leo.tool.entity.User;
import cn.acyou.leo.tool.feign.HttpbinClient;
import cn.acyou.leo.tool.mapper.AreaMapper;
import cn.acyou.leo.tool.mapper.DictMapper;
import cn.acyou.leo.tool.mapper.ScheduleJobMapper;
import cn.acyou.leo.tool.mapper.UserMapper;
import cn.acyou.leo.tool.service.AreaService;
import cn.acyou.leo.tool.service.DictService;
import cn.acyou.leo.tool.service.UserService;
import cn.acyou.leo.tool.util.MapperUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private HttpbinClient httpbinClient;
    @Autowired
    private ScheduleJobMapper scheduleJobMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test_typeHandler() {
        User mpDefault = userService.getById(1000);
        System.out.println(mpDefault);
        //@select annotation
        User u = userMapper.getById(1000L);
        System.out.println(u);
        //resultType
        User u2 = userMapper.getByIdV2(1000L);
        System.out.println(u2);
        //resultMap
        User u3 = userMapper.getByIdV3(1000L);
        System.out.println(u3);
        System.out.println("end");
    }

    @Test
    public void contextLoads() {
        List<Dict> list = dictService.lambdaQuery().select(Dict::getId).list();
        System.out.println(list);
        List<Object> objects = dictMapper.selectObjs(new QueryWrapper<Dict>().select("name").eq("parent_id", 0));
        System.out.println("end");
        Dict one = dictService.lambdaQuery().eq(Dict::getParentId, 2220).last("limit 1").one();
        System.out.println(one);
    }

    @Test
    public void tst() {
        Dict dict = dictMapper.selectDictV2();
        System.out.println(dict);
    }

    @Test
    public void testJdbcTemplate() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from sys_dict");
        System.out.println(maps);
    }

    @Test
    public void testReadOnly() {
        DictSaveReq dictSaveReq = new DictSaveReq();
        dictSaveReq.setParentId(0L);
        dictSaveReq.setCode("ok111");
        dictSaveReq.setName("OK");
        /*
         * 在只读事务里进行写操作会报错
         *
         * org.springframework.dao.TransientDataAccessResourceException:
         * ### Error updating database.  Cause: java.sql.SQLException: Connection is read-only. Queries leading to data modification are not allowed
         *
         */
        dictService.testSaveReadOnly(dictSaveReq);
    }

    @Test
    public void test手动管理事务() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("MyTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(def);
        try {
            List<Dict> forUpdate = dictService.lambdaQuery().in(Dict::getId, Arrays.asList(2175, 2176, 2177)).last("for update").list();
            System.out.println(forUpdate);
            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
        }
    }

    @Test
    public void test手动管理事务2() {
        doTransaction(() -> {
            List<Dict> forUpdate = dictService.lambdaQuery().in(Dict::getId, Arrays.asList(2175, 2176, 2177)).last("for update").list();
            System.out.println(forUpdate);
        });
    }


    void doTransaction(Task task){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("MyTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(def);
        try {
            task.run();
            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
        }
    }

    @Test
    @Rollback
    public void test悲观锁ForUpdate(){
        List<Dict> forUpdate = dictService.lambdaQuery().in(Dict::getId, Arrays.asList(2175, 2176, 2177)).last("for update").list();
        System.out.println(forUpdate);
    }

    @Test
    public void test手动执行SQL() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from sys_dict limit 1");
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        System.out.println(resultSet);
        connection.close();
        sqlSession.close();
    }

    @Test
    public void testResultMap查询() {
        List<ScheduleJob> scheduleJobs = scheduleJobMapper.selectLimit10();
        System.out.println(scheduleJobs);
        ScheduleJob scheduleJob = scheduleJobMapper.selectByJobId(10L);
        System.out.println(scheduleJob);
        ScheduleJob scheduleJobV2 = scheduleJobMapper.selectByJobIdV2(10L);
        System.out.println(scheduleJobV2);
        ScheduleJob scheduleJobV3 = scheduleJobMapper.selectByJobIdV3(10L);
        System.out.println(scheduleJobV3);
    }

    @Test
    public void test获取单个字段() {
        List<Object> objects = dictService.listObjs(new LambdaQueryWrapper<Dict>().select(Dict::getName).eq(Dict::getParentId, 0));
        System.out.println(objects);
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Dict::getId);
        queryWrapper.eq(Dict::getParentId, 0);
        List<Object> objects1 = dictService.listObjs(queryWrapper);
        System.out.println(objects1);
    }

    /**
     * 在设计数据库和应用程序时，考虑是否需要允许某些字段被更新为 null。有时，将字段设置为 null 可能是不必要的，或者可能违反业务规则。在这种情况下，最好在应用程序层面进行检查，防止将 null 值传递给 MyBatis Plus 进行更新。
     */

    @Test
    public void 测试更新为NULL值(){
        Dict dict = dictService.getById(3191);
        dict.setName(dict.getName() + "1");
        dict.setRemark(null);
        dictMapper.updateRemark(dict);

        //注解方法：
        //@TableField(updateStrategy = FieldStrategy.IGNORED)
        //updateStrategy = FieldStrategy.IGNORED 表示在更新操作时忽略更新策略，允许将 NULL 值更新到数据库中。

    }

    @Test
    public void 测试批量更新(){
        List<Dict> dicts = dictService.list();
        for (Dict dict : dicts) {
            dict.setRemark(RandomUtil.randomUuid());
        }
        int i = MapperUtils.batchUpdate(dicts, dictMapper::updateRemark);
        System.out.println(i);
    }

    @Test
    public void 测试批量更新2(){
        List<Dict> dicts = dictService.list();
        for (Dict dict : dicts) {
            dict.setRemark(RandomUtil.randomUuid());
        }
        boolean b = dictService.updateBatchById(dicts);
        System.out.println(b);
    }

    @Test
    public void 测试更新为NULL值2(){
        //执行SQL：[UPDATE sys_dict SET sort = sort + 1 WHERE (id = 3191)]
        dictService.lambdaUpdate()
                .setSql("sort = sort + 1")
                .eq(Dict::getId, 3191)
                .update();
    }

    @Test
    public void 测试更新为NULL值3(){
        Dict dict = dictService.getById(3191);
        dict.setName(dict.getName() + "1");
        UpdateWrapper<Dict> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("remark", null);
        updateWrapper.eq("id", dict.getId());
        // 执行SQL： [UPDATE sys_dict SET name='主任医师1', code='主任医师', parent_id=2667, sort=1, remark='GGG', status=1, create_time='2024-10-12 09:35:28',  update_time='2024-10-12 10:07:54', remark=null WHERE (id = 3191)]
        dictService.saveOrUpdate(dict, updateWrapper);
    }

    @Test
    public void 测试更新为NULL值4(){
        Dict dict = dictService.getById(3191);
        dict.setId(null);
        dict.setName(dict.getName() + "1");
        dict.setRemark(null);
        UpdateWrapper<Dict> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("remark", null);
        updateWrapper.eq("id", dict.getId());
        //执行SQL：[UPDATE sys_dict SET name='主任医师11', code='主任医师', parent_id=2667, sort=1, remark='ooo', status=1, create_time='2024-10-12 09:35:28', update_time='2024-10-12 10:11:44', remark=null WHERE (id = null)]
        //执行SQL：[INSERT INTO sys_dict ( name, code, parent_id, sort, remark, status, create_time, update_time ) VALUES ( '主任医师11', '主任医师', 2667, 1, 'ooo', 1, '2024-10-12 09:35:28', '2024-10-12 10:11:44' )]
        //先根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法。（存在性验证之后的saveOrUpdate操作）
        dictService.saveOrUpdate(dict, updateWrapper);
        //
        //dictService.update(dict, updateWrapper);
    }


    @Test
    public void 测试更新为NULL值5(){
        Dict dict = dictService.getById(3191);
        dict.setName(dict.getName() + "1");
        //执行SQL：[UPDATE sys_dict SET name='主任医师11', code='主任医师', parent_id=2667, sort=1, remark='FFFF', status=1, create_time='2024-10-12 09:35:28', update_time='2024-10-12 10:29:52', remark=null WHERE (id = 3191)]
        dictService.lambdaUpdate()
                .set(Dict::getRemark, null)
                .eq(Dict::getId, dict.getId())
                .update(dict);
    }

    @Test
    public void test33442(){
        Dict dict = dictService.getById(3191);
        dict.setName(dict.getName() + "1");
        //dict.setRemark(null);
        //dictService.saveOrUpdate(dict);
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        UpdateWrapper<Dict> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("remark", null);
        //条件为空 更新所有表！！！！！！！！
        dictService.saveOrUpdate(dict, updateWrapper);
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    }

    @Test
    public void 测试删除(){
        dictService.lambdaUpdate()
                .eq(Dict::getCode, "主任医师")
                .eq(Dict::getStatus, "0")
                .remove();
    }

    @Test
    public void 测试删除2(){
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getCode, "主任医师");
        queryWrapper.eq(Dict::getStatus, "0");
        dictService.remove(queryWrapper);
    }

    @Test
    public void 测试查询2(){
        //Dict dict = new Dict();
        Dict dict = null;
        Dict one = dictService.lambdaQuery()
                .eq(Dict::getId, 1132)
                //.eq(dict != null && dict.getParentId() != null, Dict::getParentId, dict.getParentId())//value会直接执行计算
                .eq(dict != null && dict.getParentId() != null, Dict::getParentId, dict !=null? dict.getParentId():0)
                .one();
        System.out.println(one);
    }

    @Test
    public void test334342(){
        Dict dict = dictService.getById(3191);
        dict.setId(null);
        dict.setRemark(null);
        dictService.saveOrUpdate(dict);
    }
    @Test
    public void test3344(){
        String ip = httpbinClient.ip();
        System.out.println(ip);
        String ip2 = httpbinClient.ip2();
        System.out.println(ip2);
    }

    @Test
    public void test3445(){
        Map<String, Object> param = new HashMap<>();
        param.put("name", "讲师");
        param.put("sort", "10");
        dictService.removeByMap(param);//DELETE FROM sys_dict WHERE name = '讲师' AND sort = '10'
    }

    @Test
    public void test3443(){
        LambdaQueryChainWrapper<Dict> lambdaQuery = dictService.lambdaQuery();
        lambdaQuery.eq(Dict::getName, "讲师");
        lambdaQuery.gt(Dict::getSort, 10);
        dictService.remove(lambdaQuery.getWrapper());//DELETE FROM sys_dict WHERE (name = '讲师' AND sort > 10)
    }

    @Test
    public void test3447(){
        LambdaQueryWrapper<Dict> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(Dict::getName, "讲师");
        lambdaQuery.gt(Dict::getSort, 10);
        dictService.remove(lambdaQuery);//DELETE FROM sys_dict WHERE (name = '讲师' AND sort > 10)
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
        int res = executeMapper.executeDDLSql("alter table sys_dict_v1 rename sys_dict");
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
    public void test123424(){
        List<TableFields> student = executeMapper.executeDescribe("sys_user");
        System.out.println(student);
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
        int i = dictMapper.insertWhereNotExist(dict, "select name from sys_dict where id = 21560");
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
