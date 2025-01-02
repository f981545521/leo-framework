package cn.acyou.leo.tool.test;


import cn.acyou.leo.framework.constant.ClientEnum;
import cn.acyou.leo.framework.constant.StateEnum;
import cn.hutool.core.util.EnumUtil;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author youfang
 * @version [1.0.0, 2025/1/2 9:10]
 **/
public class MainTest502 {

    public static void main(String[] args) throws Exception {
        Class<?> clientEnum = Class.forName("cn.acyou.leo.framework.constant.ClientEnum");
        System.out.println(clientEnum);

        Class<?> clientEnum2 = Class.forName("cn.acyou.leo.*.*.ClientEnum");
        System.out.println(clientEnum2);
        Class.forName("ClientEnum", false, MainTest502.class.getClassLoader());
    }


    public static void main1(String[] args) {
        boolean anEnum = EnumUtil.isEnum(StateEnum.class);
        System.out.println(anEnum);
        LinkedHashMap<String, StateEnum> enumMap = EnumUtil.getEnumMap(StateEnum.class);
        System.out.println(enumMap);
        List<String> names = EnumUtil.getNames(StateEnum.class);
        System.out.println(names);
        List<String> fieldNames = EnumUtil.getFieldNames(StateEnum.class);
        System.out.println(fieldNames);
        String name = StateEnum.DISABLE.getName();
        System.out.println(name);

        System.out.println(ClientEnum.IOS);
        System.out.println(ClientEnum.IOS.name());
        System.out.println(ClientEnum.IOS.getDescription());

        ClientEnum pc = ClientEnum.valueOf("PC");
        System.out.println(pc);
        ClientEnum[] enumConstants = ClientEnum.IOS.getDeclaringClass().getEnumConstants();
        System.out.println(enumConstants);
        System.out.println(ClientEnum.IOS.ordinal());

        List<String> collect = Arrays.stream(ClientEnum.values()).map(x -> x.toString()).collect(Collectors.toList());
        System.out.println(collect);
    }
}
