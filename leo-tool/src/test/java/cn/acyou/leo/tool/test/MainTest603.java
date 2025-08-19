package cn.acyou.leo.tool.test;

import cn.hutool.core.util.IdUtil;

/**
 * @author youfang
 * @version [1.0.0, 2025/7/14 11:18]
 **/
public class MainTest603 {
    public static void main(String[] args) {
        System.out.println("e" + IdUtil.getSnowflakeNextIdStr());
        System.out.println("e" + IdUtil.getSnowflakeNextId());
        System.out.println(IdUtil.fastSimpleUUID());
        System.out.println(IdUtil.fastUUID());
        System.out.println(IdUtil.objectId());
        System.out.println(IdUtil.simpleUUID());
        System.out.println(IdUtil.nanoId());
    }
}
