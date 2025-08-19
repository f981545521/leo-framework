package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.framework.util.ExcelUtil;
import cn.acyou.leo.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/17 17:35]
 **/
@Slf4j
public class MainTest1234V4 {


    @Test
    public void 数据修复() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\fix2\\111.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheetAt(0), 0);
        for (Map<String, Object> objectMap : dataList) {
            String customer_id = objectMap.get("customer_id").toString();
            String customer_phone = StringUtils.toStr(objectMap.get("customer_phone")).trim();
            String sql = "update member set phone = '"+customer_phone+"' where phone is null and e_id = "+customer_id + ";";
            System.out.println(sql);
        }
        System.out.println("解析完成");
    }



}
