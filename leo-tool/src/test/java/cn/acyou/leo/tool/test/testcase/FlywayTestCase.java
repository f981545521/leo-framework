package cn.acyou.leo.tool.test.testcase;

import cn.acyou.leo.tool.test.ApplicationBaseTests;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.output.ValidateResult;
import org.flywaydb.core.internal.resolver.ChecksumCalculator;
import org.flywaydb.core.internal.resource.StringResource;
import org.flywaydb.core.internal.util.BomFilter;
import org.flywaydb.core.internal.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/23 9:43]
 **/
@Slf4j
public class FlywayTestCase extends ApplicationBaseTests {
    @Autowired
    private Flyway flyway;

    @Test
    public void test1(){
        MigrationInfoService info = flyway.info();
        System.out.println(info);
        ValidateResult validateResult = flyway.validateWithResult();
        System.out.println(validateResult);
        flyway.migrate();
        System.out.println("end");
    }

    public static void main(String[] args) {
        String sql = "INSERT INTO `student` (`id`, `name`, `age`, `birth`, `ext`) VALUES (default, '关羽', 2, 'now()', NULL);";
        int calculate = ChecksumCalculator.calculate(new StringResource(sql));
        System.out.println("校验和" + calculate);
        CRC32 crc32 = new CRC32();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new StringReader(sql), 4096);
            String line = bufferedReader.readLine();
            if (line != null) {
                line = BomFilter.FilterBomFromString(line);
                do {
                    crc32.update(line.getBytes(StandardCharsets.UTF_8));
                } while((line = bufferedReader.readLine()) != null);
            }
        } catch (IOException var7) {
            throw new FlywayException("Unable to calculate checksum  ");
        } finally {
            IOUtils.close(bufferedReader);
        }

        int checksum = (int)crc32.getValue();
        System.out.println("校验和" + checksum);
    }


}
