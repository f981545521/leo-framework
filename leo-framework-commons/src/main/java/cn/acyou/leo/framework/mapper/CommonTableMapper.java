package cn.acyou.leo.framework.mapper;

import cn.acyou.leo.framework.base.CommonTable;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonTableMapper {

    /**
     * 创建表
     *
     * @param tableName 表
     */
    @Select("        CREATE TABLE `${tableName}`\n" +
            "        (\n" +
            "            `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '参数主键(PK)',\n" +
            "            `abbr`          varchar(200) COLLATE utf8mb4_bin DEFAULT '' COMMENT '简称',\n" +
            "            `name`          varchar(200) COLLATE utf8mb4_bin DEFAULT '' COMMENT '名称',\n" +
            "            `code`          varchar(200) COLLATE utf8mb4_bin DEFAULT '' COMMENT '编码',\n" +
            "            `content`       varchar(200) COLLATE utf8mb4_bin DEFAULT '' COMMENT '内容',\n" +
            "            `amount`        int(11)                        DEFAULT NULL COMMENT '数量',\n" +
            "            `price`         decimal(10, 2)                 DEFAULT NULL COMMENT '金额',\n" +
            "            `type`          int(11) NOT NULL DEFAULT '0' COMMENT '类型',\n" +
            "            `field1`        text COLLATE utf8mb4_bin         DEFAULT NULL COMMENT '',\n" +
            "            `field2`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `field3`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `field4`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `field5`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `number_field1` decimal(10, 2)                   DEFAULT NULL COMMENT '',\n" +
            "            `number_field2` decimal(10, 2)                   DEFAULT NULL COMMENT '',\n" +
            "            `number_field3` decimal(10, 2)                   DEFAULT NULL COMMENT '',\n" +
            "            `number_field4` decimal(10, 2)                   DEFAULT NULL COMMENT '',\n" +
            "            `number_field5` decimal(10, 2)                   DEFAULT NULL COMMENT '',\n" +
            "            `field6`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `field7`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `field8`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `field9`        varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `field10`       varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '',\n" +
            "            `text`          varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',\n" +
            "            `remark`        varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',\n" +
            "            `sort`          int(11) DEFAULT NULL COMMENT '排序值',\n" +
            "            `is_delete`     int(11) DEFAULT '0' COMMENT '是否删除  0-正常 1-删除',\n" +
            "            `create_time`   datetime NOT NULL                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认当前创建时间',\n" +
            "            `create_user`   bigint(20) DEFAULT NULL COMMENT '创建人',\n" +
            "            `update_time`   datetime NOT NULL                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',\n" +
            "            `update_user`   bigint(20) DEFAULT NULL COMMENT '最后修改人',\n" +
            "            PRIMARY KEY (`id`) USING BTREE,\n" +
            "            UNIQUE KEY `idx_code_type` (`code`, `type`) USING BTREE\n" +
            "        ) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='${tableName}_自动公共表'")
    void createTable(@Param("tableName") String tableName);

    @Insert("<script>" +
            "        INSERT INTO ${tableName}\n" +
            "            <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
            "                abbr,name,code,content,amount,price,type,field1,field2,field3,field4,field5,number_field1,number_field2,number_field3,number_field4,number_field5,field6,field7,field8,field9,field10,text,remark,sort,is_delete,create_time,create_user,update_time,update_user,\n" +
            "            </trim>\n" +
            "        VALUES\n" +
            "        <foreach collection=\"list\" item=\"record\" separator=\",\">\n" +
            "            <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
            "                #{record.abbr},#{record.name},#{record.code},#{record.content},#{record.amount},#{record.price},#{record.type},#{record.field1},#{record.field2},#{record.field3},#{record.field4},#{record.field5},#{record.numberField1},#{record.numberField2},#{record.numberField3},#{record.numberField4},#{record.numberField5},#{record.field6},#{record.field7},#{record.field8},#{record.field9},#{record.field10},#{record.text},#{record.remark},#{record.sort},#{record.isDelete},#{record.createTime},#{record.createUser},#{record.updateTime},#{record.updateUser},\n" +
            "            </trim>\n" +
            "        </foreach>"+
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "list.id")
    int insertList(@Param("tableName") String tableName, @Param("list") List<CommonTable> list);

    @Insert("<script>" +
            "         INSERT IGNORE INTO ${tableName}\n" +
            "        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
            "            <if test=\"record.id != null\">id,</if>\n" +
            "            <if test=\"record.abbr != null and record.abbr != '' \">abbr,</if>\n" +
            "            <if test=\"record.name != null and record.name != '' \">name,</if>\n" +
            "            <if test=\"record.code != null and record.code != '' \">code,</if>\n" +
            "            <if test=\"record.content != null and record.content != '' \">content,</if>\n" +
            "            <if test=\"record.amount != null\">amount,</if>\n" +
            "            <if test=\"record.price != null\">price,</if>\n" +
            "            <if test=\"record.type != null\">type,</if>\n" +
            "            <if test=\"record.field1 != null and record.field1 != '' \">field1,</if>\n" +
            "            <if test=\"record.field2 != null and record.field2 != '' \">field2,</if>\n" +
            "            <if test=\"record.field3 != null and record.field3 != '' \">field3,</if>\n" +
            "            <if test=\"record.field4 != null and record.field4 != '' \">field4,</if>\n" +
            "            <if test=\"record.field5 != null and record.field5 != '' \">field5,</if>\n" +
            "            <if test=\"record.numberField1 != null\">number_field1,</if>\n" +
            "            <if test=\"record.numberField2 != null\">number_field2,</if>\n" +
            "            <if test=\"record.numberField3 != null\">number_field3,</if>\n" +
            "            <if test=\"record.numberField4 != null\">number_field4,</if>\n" +
            "            <if test=\"record.numberField5 != null\">number_field5,</if>\n" +
            "            <if test=\"record.field6 != null and record.field6 != '' \">field6,</if>\n" +
            "            <if test=\"record.field7 != null and record.field7 != '' \">field7,</if>\n" +
            "            <if test=\"record.field8 != null and record.field8 != '' \">field8,</if>\n" +
            "            <if test=\"record.field9 != null and record.field9 != '' \">field9,</if>\n" +
            "            <if test=\"record.field10 != null and record.field10 != '' \">field10,</if>\n" +
            "            <if test=\"record.text != null and record.text != '' \">text,</if>\n" +
            "            <if test=\"record.remark != null and record.remark != '' \">remark,</if>\n" +
            "            <if test=\"record.sort != null\">sort,</if>\n" +
            "            <if test=\"record.isDelete != null\">is_delete,</if>\n" +
            "            <if test=\"record.createTime != null\">create_time,</if>\n" +
            "            <if test=\"record.createUser != null\">create_user,</if>\n" +
            "            <if test=\"record.updateTime != null\">update_time,</if>\n" +
            "            <if test=\"record.updateUser != null\">update_user,</if>\n" +
            "        </trim>\n" +
            "        <trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">\n" +
            "            <if test=\"record.id != null\">#{record.id},</if>\n" +
            "            <if test=\"record.abbr != null and record.abbr != '' \">#{record.abbr},</if>\n" +
            "            <if test=\"record.name != null and record.name != '' \">#{record.name},</if>\n" +
            "            <if test=\"record.code != null and record.code != '' \">#{record.code},</if>\n" +
            "            <if test=\"record.content != null and record.content != '' \">#{record.content},</if>\n" +
            "            <if test=\"record.amount != null\">#{record.amount},</if>\n" +
            "            <if test=\"record.price != null\">#{record.price},</if>\n" +
            "            <if test=\"record.type != null\">#{record.type},</if>\n" +
            "            <if test=\"record.field1 != null and record.field1 != '' \">#{record.field1},</if>\n" +
            "            <if test=\"record.field2 != null and record.field2 != '' \">#{record.field2},</if>\n" +
            "            <if test=\"record.field3 != null and record.field3 != '' \">#{record.field3},</if>\n" +
            "            <if test=\"record.field4 != null and record.field4 != '' \">#{record.field4},</if>\n" +
            "            <if test=\"record.field5 != null and record.field5 != '' \">#{record.field5},</if>\n" +
            "            <if test=\"record.numberField1 != null\">#{record.numberField1},</if>\n" +
            "            <if test=\"record.numberField2 != null\">#{record.numberField2},</if>\n" +
            "            <if test=\"record.numberField3 != null\">#{record.numberField3},</if>\n" +
            "            <if test=\"record.numberField4 != null\">#{record.numberField4},</if>\n" +
            "            <if test=\"record.numberField5 != null\">#{record.numberField5},</if>\n" +
            "            <if test=\"record.field6 != null and record.field6 != '' \">#{record.field6},</if>\n" +
            "            <if test=\"record.field7 != null and record.field7 != '' \">#{record.field7},</if>\n" +
            "            <if test=\"record.field8 != null and record.field8 != '' \">#{record.field8},</if>\n" +
            "            <if test=\"record.field9 != null and record.field9 != '' \">#{record.field9},</if>\n" +
            "            <if test=\"record.field10 != null and record.field10 != '' \">#{record.field10},</if>\n" +
            "            <if test=\"record.text != null and record.text != '' \">#{record.text},</if>\n" +
            "            <if test=\"record.remark != null and record.remark != '' \">#{record.remark},</if>\n" +
            "            <if test=\"record.sort != null\">#{record.sort},</if>\n" +
            "            <if test=\"record.isDelete != null\">#{record.isDelete},</if>\n" +
            "            <if test=\"record.createTime != null\">#{record.createTime},</if>\n" +
            "            <if test=\"record.createUser != null\">#{record.createUser},</if>\n" +
            "            <if test=\"record.updateTime != null\">#{record.updateTime},</if>\n" +
            "            <if test=\"record.updateUser != null\">#{record.updateUser},</if>\n" +
            "        </trim>"+
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "record.id")
    int insertIgnoreSelective(@Param("tableName") String tableName, @Param("record") CommonTable record);

    @Update("<script>" +
            "        <if test=\"list==null or list.size()==0\">select 0 from dual</if>\n" +
            "        <foreach collection=\"list\" item=\"it\">update ${tableName}\n" +
            "            <set>\n" +
            "                <if test=\"it.abbr != null and it.abbr != '' \">abbr = #{it.abbr},</if>\n" +
            "                <if test=\"it.name != null and it.name != '' \">name = #{it.name},</if>\n" +
            "                <if test=\"it.code != null and it.code != '' \">code = #{it.code},</if>\n" +
            "                <if test=\"it.content != null and it.content != '' \">house_key = #{it.content},</if>\n" +
            "                <if test=\"it.amount != null\">amount = #{it.amount},</if>\n" +
            "                <if test=\"it.price != null\">price = #{it.price},</if>\n" +
            "                <if test=\"it.type != null\">type = #{it.type},</if>\n" +
            "                <if test=\"it.field1 != null and it.field1 != '' \">field1 = #{it.field1},</if>\n" +
            "                <if test=\"it.field2 != null and it.field2 != '' \">field2 = #{it.field2},</if>\n" +
            "                <if test=\"it.field3 != null and it.field3 != '' \">field3 = #{it.field3},</if>\n" +
            "                <if test=\"it.field4 != null and it.field4 != '' \">field4 = #{it.field4},</if>\n" +
            "                <if test=\"it.field5 != null and it.field5 != '' \">field5 = #{it.field5},</if>\n" +
            "                <if test=\"it.numberField1 != null\">number_field1 = #{it.numberField1},</if>\n" +
            "                <if test=\"it.numberField2 != null\">number_field2 = #{it.numberField2},</if>\n" +
            "                <if test=\"it.numberField3 != null\">number_field3 = #{it.numberField3},</if>\n" +
            "                <if test=\"it.numberField4 != null\">number_field4 = #{it.numberField4},</if>\n" +
            "                <if test=\"it.numberField5 != null\">number_field5 = #{it.numberField5},</if>\n" +
            "                <if test=\"it.field6 != null and it.field6 != '' \">field6 = #{it.field6},</if>\n" +
            "                <if test=\"it.field7 != null and it.field7 != '' \">field7 = #{it.field7},</if>\n" +
            "                <if test=\"it.field8 != null and it.field8 != '' \">field8 = #{it.field8},</if>\n" +
            "                <if test=\"it.field9 != null and it.field9 != '' \">field9 = #{it.field9},</if>\n" +
            "                <if test=\"it.field10 != null and it.field10 != '' \">field10 = #{it.field10},</if>\n" +
            "                <if test=\"it.text != null and it.text != '' \">text = #{it.text},</if>\n" +
            "                <if test=\"it.remark != null and it.remark != '' \">remark = #{it.remark},</if>\n" +
            "                <if test=\"it.sort != null\">sort = #{it.sort},</if>\n" +
            "                <if test=\"it.isDelete != null\">is_delete = #{it.isDelete},</if>\n" +
            "                <if test=\"it.createTime != null\">create_time = #{it.createTime},</if>\n" +
            "                <if test=\"it.createUser != null\">create_user = #{it.createUser},</if>\n" +
            "                <if test=\"it.updateTime != null\">update_time = #{it.updateTime},</if>\n" +
            "                <if test=\"it.updateUser != null\">update_user = #{it.updateUser},</if>\n" +
            "            </set>\n" +
            "            WHERE id = #{it.id};\n" +
            "        </foreach>"+
            "</script>")
    int updateListSelective(@Param("tableName") String tableName, @Param("list") List<CommonTable> list);

    @Select("<script>" +
            "        SELECT\n" +
            "        id,abbr,name,code,content,amount,price,type,field1,field2,field3,field4,field5,number_field1,number_field2,number_field3,number_field4,number_field5,field6,field7,field8,field9,field10,text,remark,sort,is_delete,create_time,create_user,update_time,update_user\n" +
            "        FROM ${tableName} where id in\n" +
            "        <foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id}</foreach>"+
            "</script>")
    List<CommonTable> selectByPrimaryKeyList(@Param("tableName") String tableName, @Param("list") List<?> list);

    @Delete("<script>" +
            "DELETE FROM ${tableName}  where id in <foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\" >#{id}</foreach>"+
            "</script>")
    int deleteByPrimaryKeyList(@Param("tableName") String tableName, @Param("list") List<?> list);
}
