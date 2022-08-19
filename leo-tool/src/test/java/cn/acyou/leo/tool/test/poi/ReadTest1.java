package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.tool.test.poi.pojo.DemoData;
import cn.acyou.leo.tool.test.poi.pojo.TextBookData;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author fangyou
 * @version [1.0.0, 2021-11-03 16:11]
 */
public class ReadTest1 {
    /**
     * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>
     * 3. 直接读即可
     */
    @Test
    public void repeatedRead() {
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        EasyExcel.read(new File("D:\\poi\\课本表格2.xlsx"), TextBookData.class, new AnalysisEventListener<TextBookData>() {
            @Override
            public void invoke(TextBookData o, AnalysisContext analysisContext) {
                System.out.println("解析到数据：" + o);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("所有数据解析完成！");
            }
        }).doReadAll();
        System.out.println("读取完成");
    }
}
