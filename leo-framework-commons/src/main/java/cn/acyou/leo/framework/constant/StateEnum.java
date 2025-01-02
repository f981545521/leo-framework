package cn.acyou.leo.framework.constant;

public enum StateEnum {

    NORMAL, DISABLE, ENABLE;

    public static final String MESSAGE = "状态";

    public String getName() {
        switch (this) {
            case NORMAL:
                return "正常";
            case DISABLE:
                return "禁用";
            case ENABLE:
                return "启用";
        }
        return "";
    }

}
