package cn.acyou.leo.tool.test.poi.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fangyou
 * @version [1.0.0, 2021-11-03 14:35]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextBookData {
    @ExcelProperty("类型")
    private String type;
    @ExcelProperty("标题")
    private String title;
    @ExcelProperty("内容")
    private String content;
}
