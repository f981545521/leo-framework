package cn.acyou.leo.tool.test.generater;

import cn.acyou.leo.framework.generator.CustomCodeGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;

/**
 * @author youfang
 * @version [1.0.0, 2022-2-21]
 **/
public class ToolCodeGenerator {
    public static void main(String[] args) {
        CustomCodeGenerator.instance("t_dict", "t_")
                .author("youfang")
                .setDbConfig("com.mysql.cj.jdbc.Driver", "root", "root123")
                .setDbUrl("jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&useSSL=false")
                .packageParent("cn.acyou.leo.tool")
                .put(ConstVal.ENTITY_PATH, "leo-tool", "entity")
                .put(ConstVal.MAPPER_PATH, "leo-tool", "mapper")
                .put(ConstVal.XML_PATH, "leo-tool", "mappers")
                .put(ConstVal.SERVICE_PATH, "leo-tool", "service")
                .put(ConstVal.SERVICE_IMPL_PATH, "leo-tool", "service.impl")
                .put(ConstVal.CONTROLLER_PATH, "leo-tool", "controller")
                .doGenerator();
    }
}
