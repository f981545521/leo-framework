package cn.acyou.leo.pay.generator;

import cn.acyou.leo.framework.generator.CustomCodeGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;

/**
 * @author youfang
 * @version [1.0.0, 2022-2-21]
 **/
public class PayCodeGenerator {
    public static void main(String[] args) {
        CustomCodeGenerator.instance("t_point_rule", "t_")
                .author("youfang")
                .setDbConfig("com.mysql.cj.jdbc.Driver", "root", "root123")
                .setDbUrl("jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&useSSL=false")
                .packageParent("cn.acyou.leo.pay")
                .put(ConstVal.ENTITY_PATH, "leo-pay", "entity")
                .put(ConstVal.MAPPER_PATH, "leo-pay", "mapper")
                .put(ConstVal.XML_PATH, "leo-pay", "mappers")
                .put(ConstVal.SERVICE_PATH, "leo-pay", "service")
                .put(ConstVal.SERVICE_IMPL_PATH, "leo-pay", "service.impl")
                .put(ConstVal.CONTROLLER_PATH, "leo-pay", "controller")
                .doGenerator();
    }
}
