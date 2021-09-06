package cn.acyou.leo.framework.util;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/28]
 **/
public class RandomUtil {

    /**
     * 生成随机 字符 or 字符串
     *
     * @param number 是否数字
     * @param length 长度
     * @return 字符串
     */
    public static String createRandom(boolean number, int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder retStr = new StringBuilder();
        String randomPool = number ? "1234567890" : "1234567890abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
        while (retStr.length() < length) {
            retStr.append(randomPool.charAt((int) Math.floor(Math.random() * randomPool.length())));
        }
        return retStr.toString();
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param length 长度
     * @return 字符串
     */
    public static String createRandomStr(int length) {
        return createRandom(false, length);
    }
    /**
     * 生成指定长度的随机数字
     *
     * @return 字符串
     */
    public static int createRandomInt() {
        String random = createRandom(true, 8);
        return new Integer(random);
    }

    /**
     * UUID的字符串
     *
     * @return 字符串
     */
    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 没有-的UUID字符串
     *
     * @return 字符串
     */
    public static String randomUuidWithoutLine() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    private static final String[] TEL_FIRST = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    /**
     * 随机生成手机号码
     *
     * @return {@link String}
     */
    public static String randomTelephone() {
        int index = randomRangeNumber(0, TEL_FIRST.length - 1);
        String first = TEL_FIRST[index];
        String second = String.valueOf(randomRangeNumber(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(randomRangeNumber(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    /**
     * 获取范围内的值 [start, end] 左右包含
     *
     * @param start 开始
     * @param end   结束
     * @return 指定范围内的值
     */
    public static int randomRangeNumber(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    /**
     * 获取范围内的值 [start, end] 左右包含
     *
     * @param start 开始
     * @param end   结束
     * @return 指定范围内的值
     */
    public static long randomRangeLong(long start, long end) {
        return (long) (Math.random() * (end - start + 1) + start);
    }

    /**
     * 随机 0 or 1
     *
     * @return 0 or 1
     */
    public static int random01() {
        return new SecureRandom().nextInt(2);
    }

    /**
     * 获取随机值
     *
     * @param value 范围 [0~value]
     * @return int
     */
    public static int nextInt(int value) {
        return new SecureRandom().nextInt(value);
    }

    /**
     * 获取随机值
     *
     * @param value   范围 范围 [0~value]
     * @param several 格式化几位
     * @return int 如：RandomUtil.nextInt(12, 2) 当小于10时，前面会补充0
     */
    public static String nextInt(int value, int several) {
        String format = "%0" + several + "d";
        return String.format(format, RandomUtil.nextInt(value));
    }

    /**
     * 随机 男 or 女
     *
     * @return 男 or 女
     */
    public static String randomSex() {
        return Math.random() > 0.5 ? "男" : "女";
    }

    /**
     * 随机姓名
     *
     * @return 姓名
     */
    public static String randomUserName() {
        Random random = new SecureRandom();
        String[] surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏",
                "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐",
                "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞",
                "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄",
                "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "梁", "杜", "阮",
                "蓝", "闵", "席", "季"};
        String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉" +
                "琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵" +
                "融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
        String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰" +
                "涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固" +
                "之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
        int index = random.nextInt(surname.length - 1);
        //获得一个随机的姓氏
        String name = surname[index];
        //可以根据这个数设置产生的男女比例
        int i = random.nextInt(3);
        if (i == 2) {
            int j = random.nextInt(girl.length() - 2);
            if (j % 2 == 0) {
                name = name + girl.substring(j, j + 2);
            } else {
                name = name + girl.substring(j, j + 1);
            }
        } else {
            int j = random.nextInt(girl.length() - 2);
            if (j % 2 == 0) {
                name = name + boy.substring(j, j + 2);
            } else {
                name = name + boy.substring(j, j + 1);
            }
        }
        return name;
    }

    /**
     * 随机年龄
     *
     * @return 0~99
     */
    public static Integer randomAge() {
        Random random = new SecureRandom();
        return random.nextInt(100);
    }

    /**
     * 城市区号，城市太多，这里只列举其中一部分城市
     */
    private static final int[] CITIES = {
            320000, 320100, 320102, 320103, 320111, 320104, 320105, 320107, 320116, 320113, 320114, 320115, 320124,
            320125, 320204, 320205, 320206, 320200, 320202, 320203, 320211, 320281, 320282, 320300, 320303, 320311,
            320304, 320305, 320324, 320321, 320322, 320323, 320381, 320382, 320404, 320405, 320400, 320402, 320412,
            320411, 320481, 320482, 320500, 320502, 320503, 320504, 320505, 320506, 320507, 320581, 320582, 320583,
            320584, 320585, 320600, 320602, 320611, 320621, 320623, 320684, 320681, 320682, 320700, 320703, 320705,
            320706, 320724, 320721, 320722, 320723, 320804, 320800, 320802, 320803, 320811, 320829, 320830, 320831,
            320826, 320900, 320902, 320903, 320924, 320925, 320921, 320922, 320923, 320981, 320982, 321000, 321002,
            321003, 321011, 321023, 321084, 321081, 321088, 321100, 321102, 321111, 321112, 321181, 321182, 321183,
            321200, 321202, 321203, 321284, 321281, 321282, 321283, 321300, 321302, 321311, 321324, 321322, 321323
    };

    public static String randomIdCardNo(boolean male) {
        //随机生成生日 1~99岁
        //100年内
        long begin = System.currentTimeMillis() - 3153600000000L;
        //1年内
        long end = System.currentTimeMillis() - 31536000000L;
        long rtn = begin + (long) (Math.random() * (end - begin));
        Date date = new Date(rtn);
        String birth = DateUtil.getDateFormat(date, DateUtil.FORMAT_SHORT_DATE);
        return randomIdCardNo(birth, male);
    }

    /**
     * 随机身份证号
     *
     * @param birth 生日
     * @param male  性别
     * @return 身份证号
     */
    public static String randomIdCardNo(String birth, boolean male) {
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        int value = random.nextInt(CITIES.length);
        sb.append(CITIES[value]);
        sb.append(birth);
        value = random.nextInt(998) + 1;
        if (male && value % 2 == 0) {
            value++;
        }
        if (!male && value % 2 == 1) {
            value++;
        }
        if (value >= 100) {
            sb.append(value);
        } else if (value >= 10) {
            sb.append('0').append(value);
        } else {
            sb.append("00").append(value);
        }
        sb.append(calcTrailingNumber(sb));
        return sb.toString();
    }

    private static final int[] CALC_C = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] CALC_R = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * <p>18位身份证验证</p>
     * 根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * 第十八位数字(校验码)的计算方法为：
     * 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2.将这17位数字和系数相乘的结果相加。
     * 3.用加出来和除以11，看余数是多少？
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     */
    private static char calcTrailingNumber(StringBuilder sb) {
        int[] n = new int[17];
        int result = 0;
        for (int i = 0; i < n.length; i++) {
            n[i] = Integer.parseInt(String.valueOf(sb.charAt(i)));
        }
        for (int i = 0; i < n.length; i++) {
            result += CALC_C[i] * n[i];
        }
        return CALC_R[result % 11];
    }

    /**
     * 从池中获取值
     *
     * @param pool 候选者
     * @return 随机值
     */
    public static int randomFromPool(int... pool) {
        return pool[randomRangeNumber(0, pool.length - 1)];
    }

    /**
     * 从池中获取值
     *
     * @param pool 候选者
     * @return 随机值
     */
    public static String randomFromPool(String... pool) {
        return pool[randomRangeNumber(0, pool.length - 1)];
    }

    /**
     * 随机密码
     * 包括字母/包括数字
     *
     * @param length 长度
     * @return {@link String}
     */
    public static String randomPassword(int length) {
        String str;
        do {
            str = createRandom(false, length);
        } while (!RegexUtil.isStrongPassword(str));
        return str;
    }

}
