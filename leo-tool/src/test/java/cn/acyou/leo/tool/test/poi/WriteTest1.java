package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.tool.test.poi.pojo.DemoData;
import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fangyou
 * @version [1.0.0, 2021-11-03 16:03]
 */
public class WriteTest1 {
    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 直接写即可
     */
    @Test
    public void simpleWrite() {
        // 写法1 JDK8+
        // since: 3.0.0-beta1
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(new File("D:\\poi\\easyExcelDemo.xlsx"), DemoData.class)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return data();
                });

        // 写法2
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        //EasyExcel.write(new File("D:\\poi\\easyExcelDemo.xlsx"), DemoData.class).sheet("模板").doWrite(data());

        // 写法3
        // 这里 需要指定写用哪个class去写
        //ExcelWriter excelWriter = null;
        //try {
        //    excelWriter = EasyExcel.write(new File("D:\\poi\\easyExcelDemo.xlsx"), DemoData.class).build();
        //    WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        //    excelWriter.write(data(), writeSheet);
        //} finally {
        //    // 千万别忘记finish 会帮忙关闭流
        //    if (excelWriter != null) {
        //        excelWriter.finish();
        //    }
        //}
    }

    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setName(RandomUtil.randomUserName());
            data.setAge(RandomUtil.randomAge());
            data.setBirthday(DateUtil.randomDate());
            list.add(data);
        }
        return list;
    }
}
