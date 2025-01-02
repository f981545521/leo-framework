package cn.acyou.leo.framework.constant;

import cn.acyou.leo.framework.base.EnumEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ClientEnum {

    PC("PC"),
    IOS("苹果"),
    ANDROID("安卓"),
    MINI_PROGRAM("微信小程序")
    ;

    ClientEnum(String description) {
        this.code = "";
        this.description = description;
    }

    ClientEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private final String code;
    private final String description;

    @Override
    public String toString() {
        return this.name() + "_" + this.code + "(" + this.description + ")";
    }

    public static List<EnumEntity> entities() {
        List<EnumEntity> entities = new ArrayList<>();
        for (ClientEnum value : values()) {
            EnumEntity entity = new EnumEntity();
            entity.setName(value.name());
            entity.setCode(value.code);
            entity.setDescription(value.description);
            entities.add(entity);
        }
        return entities;
    }
}
