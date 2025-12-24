package cn.acyou.leo.tool.test.poi;

import cn.hutool.http.HttpUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author fangyou
 * @version [1.0.0, 2021-11-04 9:17]
 */
public class MainTest2025V5 {


    Semaphore semaphore = new Semaphore(10);
    ExecutorService executorService = Executors.newFixedThreadPool(semaphore.availablePermits());

    @Test
    public void poiReadFile() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\fixpoint\\pointChange.xlsx"));
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowIndex = sheet.getLastRowNum();
        for (int i = 2; i <= lastRowIndex; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            String cellValue0 = row.getCell(0).getStringCellValue();
            boolean b = semaphore.tryAcquire(1, 2, TimeUnit.HOURS);
            if (b) {
                int finalI = i;
                executorService.submit(() -> {
                    long l = System.currentTimeMillis();
                    String res = "ok";
                    res = HttpUtil.createPost("https://api_url")
                            .body(cellValue0)
                            .header("Access-Token", "1")
                            .execute().body();
                    System.out.println("【" + finalI + "】" + "请求参数" + cellValue0 + " > 请求结果："+res+" 耗时" + (System.currentTimeMillis() - l) + "ms");
                    semaphore.release(1);
                });
            }

        }
        executorService.shutdown();
        boolean finished = executorService.awaitTermination(2, TimeUnit.HOURS);
        if (finished) {
            System.out.println("所有任务已完成");
        } else {
            System.out.println("超时，仍有任务未完成");
        }
    }

}
