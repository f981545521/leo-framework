package cn.acyou.leo.framework.base;

import org.springframework.util.StringUtils;

/**
 * @author fangyou
 * @version [1.0.0, 2021-10-15 13:34]
 */
public enum ClientLanguage {

    //中文
    CHINESE("zh", 0),
    //英文
    ENGLISH("en", 1),
    ;
    private final String name;

    private final int index;

    ClientLanguage(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static ClientLanguage getLanguage(String language) {
        if (!StringUtils.hasText(language)) {
            return CHINESE;
        }
        for (ClientLanguage value : ClientLanguage.values()) {
            if (value.getName().equals(language)) {
                return value;
            }
        }
        return CHINESE;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
