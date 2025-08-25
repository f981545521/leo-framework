package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.framework.util.ExcelUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
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
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\fix2\\33926098912.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheetAt(0), 0);
        for (Map<String, Object> objectMap : dataList) {
            String openId = objectMap.get("openId").toString();
            String body = HttpUtil.createPost("").body("{\n" +
                    "  \"company_id\": 1,\n" +
                    "  \"member_id\": "+openId+"\n" +
                    "}").execute().body();
            String string = JSON.parseObject(body).getJSONObject("data").getString("phone");
            System.out.println(openId + " -> 手机号:" + string);
        }
        System.out.println("解析完成");
    }



}
