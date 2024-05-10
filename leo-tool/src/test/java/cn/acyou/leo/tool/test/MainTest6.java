package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.ExcelUtil;
import cn.acyou.leo.framework.util.FileUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.util.*;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/10 17:16]
 **/
public class MainTest6 {

    @Test
    public void test1234325() throws Exception {
        ZipSecureFile.setMinInflateRatio(0);
        List<Integer> sheetIndexList = Lists.newArrayList(0);
        for (Integer integer : sheetIndexList) {
            //测试环境：363 生产环境：675
            exportSql(integer, "363");
        }
    }


    /**
     * TTS刷数据SQL
     * @param sheetIndex sheet页
     * @param env   测试环境：363 生产环境：675
     * @throws Exception ex
     */
    public void exportSql(Integer sheetIndex, String env) throws Exception {
        List<Map<String, Object>> dataList = ExcelUtil.importData("D:\\temp\\poi\\TTS导入-模版.xlsx", sheetIndex);
        FileWriter fileWriter = new FileWriter(FileUtil.newFile("D:\\temp\\poi\\TTS导入-模板"+sheetIndex+"数据.sql"));
        FileWriter fileWriterOrder = new FileWriter(FileUtil.newFile("D:\\temp\\poi\\TTS导入-模板"+sheetIndex+"先导入分类.sql"));
        Set<String> insertLabel = new HashSet<>();
        for (Map<String, Object> objectMap : dataList) {
            String classify = (String) objectMap.get("分类");
            String[] split = classify.split("\\|");
            insertLabel.addAll(Arrays.asList(split));
        }
        //新增分类
        fileWriterOrder.write("-- 分类\r\n");
        for (String label_name : insertLabel) {
            String tts_label_order_insert_sql = "INSERT INTO `tts-market`.`tts_label_order` (`label_type`, `label_name`, `order_no`, `is_disable`, `create_time`, `update_time`, `is_deleted`) SELECT 'df_category', '"+label_name+"', 0, 0, now(), now(), 0 FROM DUAL WHERE NOT EXISTS ( SELECT id FROM `tts-market`.`tts_label_order` WHERE label_type = 'df_category' and label_name = '"+label_name+"');";
            fileWriterOrder.write(tts_label_order_insert_sql);
            fileWriterOrder.write("\r\n");
        }
        fileWriter.write("-- 数据\r\n");
        for (Map<String, Object> objectMap : dataList) {
            String tts_speaker = objectMap.get("tts_speaker").toString();
            String desc = (String) objectMap.get("desc");
            String speaker = objectMap.get("speaker").toString();
            String display = (String) objectMap.get("display");
            String dis_type = (String) objectMap.get("dis_type");
            String dis_type_1 = dis_type.split("\\|")[0];
            String lang = (String) objectMap.get("lang");
            String img_url = (String) objectMap.get("img_url");
            String audition_url = (String) objectMap.get("audition_url");
            String ActionArgs = (String) objectMap.get("ActionArgs");
            String features = objectMap.get("features").toString();
            String classify = objectMap.get("分类").toString();
            Integer sex = 1;
            String userCategory = "男声";
            if (features.contains("|")) {
                features = features.replaceAll("\\|",",");
            }
            if (dis_type.contains("女")) {
                sex = 2;
                userCategory = "女声";
            }
            //1、刷新每个模特和发音人的默认热度：取随机数1300～2700之间
            int popularity = RandomUtil.randomRangeNumber(1300, 2700);
            String[] split = classify.split("\\|");
            String tts_model_insert_sql = "INSERT INTO `tts-market`.`tts_model` (`id`, `tts_name`, `tts_introduction`, `tts_scenes`, `tts_speaker`, `tts_features`, `tts_users`, `tts_category`, `user_category`, `tts_audition`, " +
                    "`tts_cover`, `create_by`, `create_time`, `update_by`, `update_time`, `tts_source`, `backup_1`, `backup_2`, `is_allocation`, `is_delete`, `order_no`, `tts_parent`, `tts_level`, `tts_default`, " +
                    "`tts_extend_json`, `fee_product_id`, `features`, `sex`) VALUES " +
                    "(default, '"+display+"', '"+desc+"', '"+dis_type+"', '"+tts_speaker+"', '0', 0, '"+classify+"', '"+userCategory+"', '"+audition_url+"', " +
                    "'"+img_url+"', 'admin', now(), 'admin', now(), 10, '"+lang+"', NULL, 0, 0, 0, '-1', NULL, NULL, " +
                    "'{\\\"volume\\\":0,\\\"f0upKeyFlag\\\":1,\\\"features\\\":\\\""+features+"\\\",\\\"intonation\\\":0,\\\"ssmlFlag\\\":1,\\\"popularity\\\":"+popularity+",\\\"fakePopularity\\\":"+popularity+",\\\"speed\\\":0}', NULL, '"+features+"', "+sex+");";
            fileWriter.write(tts_model_insert_sql);
            fileWriter.write("\r\n");
            for (String s : split) {
                String tts_label_model_insert_sql = "INSERT INTO `tts-market`.`tts_label_model`(`model_id`, `label_id`)\n" +
                        "select id, (SELECT x.id FROM `tts-market`.tts_label_order x  WHERE label_type = 'df_category' and x.label_name = '"+s+"')  from `tts-market`.tts_model t where t.is_delete = 0 and  t.tts_speaker = '"+tts_speaker+"';";
                fileWriter.write(tts_label_model_insert_sql);
                fileWriter.write("\r\n");
            }
            String tb_base_product_insert_sql = "INSERT INTO fee.tb_base_product (product_type, product_id, extend, status, is_deleted, create_time, update_time)\n" +
                    "select 10002,t.id,'',null,0,now(),now()  from  `tts-market`.tts_model t where t.is_delete = 0 and t.tts_speaker = '"+tts_speaker+"';";
            fileWriter.write(tb_base_product_insert_sql);
            fileWriter.write("\r\n");
            String tb_product_props_insert_sql = "INSERT INTO fee.tb_product_props (name, fee_product_id, set_meal_id, strategy_id, period, unit, available_quantity, order_no, is_shared, is_deleted, create_time, update_time, extend_json)\n" +
                    "select '', t.id,"+env+", 4, 2, 4, 0, 0, 0, 0, now(), now(), null from fee.tb_base_product t where t.product_type = 10002 and t.product_id  =" +
                    " (select t2.id from  `tts-market`.tts_model t2 where t2.is_delete = 0 and t2.tts_speaker = '"+tts_speaker+"');";
            fileWriter.write(tb_product_props_insert_sql);
            fileWriter.write("\r\n");
            String tts_model_set_meal_relation_sql = "INSERT INTO `tts-market`.tts_model_set_meal_relation ( model_id, set_meal_id, tag, set_meal_type, create_time, update_time, is_deleted)\n" +
                    "select t.id, "+env+", 401, 3, now() , now(), 0 from `tts-market`.tts_model t where t.is_delete = 0 and t.tts_speaker = '"+tts_speaker+"';";
            fileWriter.write(tts_model_set_meal_relation_sql);
            fileWriter.write("\r\n");
            fileWriter.write("-- \r\n");
        }

        fileWriter.flush();
        fileWriter.close();
        fileWriterOrder.flush();
        fileWriterOrder.close();
        System.out.println("解析完成");
    }
}
