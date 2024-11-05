package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.tool.dto.area.AreaBo;
import cn.acyou.leo.tool.dto.area.AreaVo;
import cn.acyou.leo.tool.entity.Area;

import java.util.Arrays;

/**
 * @author youfang
 * @version [1.0.0, 2024/11/4]
 **/
public class MainTest60 {
    public static void main(String[] args) {
        AreaVo areaVo = new AreaVo();

        AreaBo areaBo = new AreaBo();
        areaBo.setId("1");
        areaBo.setParentId("1");

        Area area = new Area();
        area.setName("江苏");
        area.setShortName("苏");

        final String[] nullPropertyNames = BeanCopyUtil.getNullPropertyNames(area);
        System.out.println(Arrays.toString(nullPropertyNames));

        //BeanUtils.copyProperties(areaBo, areaVo, getNullPropertyNames(areaBo));
        //BeanUtils.copyProperties(area, areaVo, getNullPropertyNames(area));
        BeanCopyUtil.copyProperties(areaBo, areaVo);
        BeanCopyUtil.copyProperties(area, areaVo);

        System.out.println(areaVo);
    }

    public static void main1(String[] args) {
        AreaBo areaBo = new AreaBo();
        areaBo.setId("1");
        areaBo.setParentId("1");

        AreaBo area = new AreaBo();
        area.setName("江苏");
        area.setShortName("苏");

        BeanCopyUtil.merge(areaBo, area, false);

        System.out.println(area);
        System.out.println(areaBo);

    }



}
