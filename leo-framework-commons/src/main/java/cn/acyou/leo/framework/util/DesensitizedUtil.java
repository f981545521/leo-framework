package cn.acyou.leo.framework.util;


/**
 * @author fangyou
 * @version [1.0.0, 2021-10-20 11:25]
 */
public class DesensitizedUtil {
    /**
     * 【身份证号】前1位 和后2位
     *
     * @param idCardNum 身份证
     * @param front     保留：前面的front位数；从1开始
     * @param end       保留：后面的end位数；从1开始
     * @return 脱敏后的身份证
     */
    public static String idCardNum(String idCardNum, int front, int end) {
        //身份证不能为空
        if (!StringUtils.hasText(idCardNum)) {
            return "";
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return "";
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return "";
        }
        return StringUtils.hide(idCardNum, front, idCardNum.length() - end);
    }
    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
     *
     * @param fullName 姓名
     * @return 脱敏后的姓名
     */
    public static String chineseName(String fullName) {
        if (!StringUtils.hasText(fullName)) {
            return "";
        }
        return StringUtils.hide(fullName, 1, fullName.length());
    }
    /**
     * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String email(String email) {
        if (!StringUtils.hasText(email)) {
            return "";
        }
        int index = email.indexOf('@');
        if (index <= 1) {
            return email;
        }
        return StringUtils.hide(email, 1, index);
    }
    /**
     * 【手机号码】前三位，后4位，其他隐藏，比如135****2210
     *
     * @param num 移动电话；
     * @return 脱敏后的移动电话；
     */
    public static String mobilePhone(String num) {
        if (!StringUtils.hasText(num)) {
            return "";
        }
        return StringUtils.hide(num, 3, num.length() - 4);
    }

}
