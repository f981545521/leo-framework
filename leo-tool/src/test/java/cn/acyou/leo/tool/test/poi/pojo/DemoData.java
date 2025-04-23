package cn.acyou.leo.tool.test.poi.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author fangyou
 * @version [1.0.0, 2021-11-03 14:35]
 */
@Data
public class DemoData {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("年龄")
    private Integer age;
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("生日")
    private Date birthday;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
