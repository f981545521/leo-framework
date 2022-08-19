package cn.acyou.leo.framework.util;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MongoDB ID生成策略
 * <br/>
 * 此类非`public` 请使用: {@link IdUtil#objectId()}
 * @author fangyou
 * @version [1.0.0, 2021-09-03 16:43]
 */
class ObjectId {

    /**
     * 线程安全的下一个随机数,每次生成自增+1
     */
    private final static AtomicInteger nextInc = new AtomicInteger((new Random()).nextInt());

    /**
     * 机器信息
     */
    private static final int machine;

    /*
     * 初始化机器信息 = 机器码 + 进程码
     */
    static {
        try {
            // 机器码
            int machinePiece;
            try {
                StringBuilder netSb = new StringBuilder();
                // 返回机器所有的网络接口
                Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                // 遍历网络接口
                while (e.hasMoreElements()) {
                    NetworkInterface ni = e.nextElement();
                    // 网络接口信息
                    netSb.append(ni.toString());
                }
                // 保留后两位
                machinePiece = netSb.toString().hashCode() << 16;
            } catch (Throwable e) {
                // 出问题随机生成,保留后两位
                machinePiece = (new Random().nextInt()) << 16;
            }
            // 进程码
            // 因为静态变量类加载可能相同,所以要获取进程ID + 加载对象的ID值
            final int processPiece;
            // 进程ID初始化
            int processId = new java.util.Random().nextInt();
            try {
                // 获取进程ID
                processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
            } catch (Throwable t) {
                //ignore
            }
            ClassLoader loader = ObjectId.class.getClassLoader();
            // 返回对象哈希码,无论是否重写hashCode方法
            int loaderId = loader != null ? System.identityHashCode(loader) : 0;
            // 进程ID + 对象加载ID
            String processSb = Integer.toHexString(processId) +
                    Integer.toHexString(loaderId);
            // 保留前2位
            processPiece = processSb.hashCode() & 0xFFFF;
            // 生成机器信息 = 取机器码的后2位和进程码的前2位
            machine = machinePiece | processPiece;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取一个objectId
     *
     * @return objectId
     */
    public static String next() {
        byte[] b = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        bb.putInt((int) (System.currentTimeMillis() / 1000));//4位
        bb.putInt(machine);//4位
        bb.putInt(nextInc.getAndIncrement());//4位
        StringBuilder buf = new StringBuilder(24);
        // 原来objectId格式化太慢
        for (byte t : bb.array()) {
            // 小于两位左端补0
            int i = t & 0xff;
            if (i < 16) {
                buf.append("0").append(Integer.toHexString(i));
            } else {
                buf.append(Integer.toHexString(i));
            }
        }
        return buf.toString();
    }
}
