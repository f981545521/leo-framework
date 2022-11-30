package cn.acyou.leo.framework.media.util;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2022/11/18 18:05]
 **/
public class ImageInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 文字大小：字节
     * size/1024 -> kb
     */
    private long size;
    /**
     * 高
     */
    private int height;
    /**
     * 宽
     */
    private int width;
    /**
     * 文件后缀名(jpg、png、jpeg)
     */
    private String type;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "size=" + size +
                ", height=" + height +
                ", width=" + width +
                ", type='" + type + '\'' +
                '}';
    }
}
