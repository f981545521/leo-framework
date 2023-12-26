package cn.acyou.leo.framework.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author youfang
 * @version [1.0.0, 2023/12/26 11:09]
 **/
@Data
public class ColorVo implements Serializable {

    private static final long serialVersionUID = 7691135714124230831L;

    @ApiModelProperty("红 0~255")
    private int red;
    @ApiModelProperty("绿 0~255")
    private int green;
    @ApiModelProperty("蓝 0~255")
    private int blue;
    @ApiModelProperty("透明度 0~255")
    private int alpha;

    @ApiModelProperty("红 16进制")
    private String redHex;
    @ApiModelProperty("绿 16进制5")
    private String greenHex;
    @ApiModelProperty("蓝 16进制")
    private String blueHex;
    @ApiModelProperty("透明度 16进制")
    private String alphaHex;
    @ApiModelProperty("透明度 比例：0.78")
    private String alphaRatio;

    @ApiModelProperty("rgb 颜色值")
    private String rgbColor;
    @ApiModelProperty("rgba 颜色值")
    private String rgbaColor;
    @ApiModelProperty("10进制颜色值（无透明）")
    private Integer decimalColor;
    @ApiModelProperty("16进制颜色值（无透明）")
    private String hexColor;
    @ApiModelProperty("16进制颜色值（有透明）")
    private String hexColorAlpha;


    public ColorVo(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 255;
        afterSet();
    }

    public ColorVo(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        afterSet();
    }

    private void afterSet() {
        redHex = Integer.toHexString(red).length() == 1 ? "0" + Integer.toHexString(red) : Integer.toHexString(red);
        greenHex = Integer.toHexString(green).length() == 1 ? "0" + Integer.toHexString(green) : Integer.toHexString(green);
        blueHex = Integer.toHexString(blue).length() == 1 ? "0" + Integer.toHexString(blue) : Integer.toHexString(blue);
        alphaHex = Integer.toHexString(alpha).length() == 1 ? "0" + Integer.toHexString(alpha) : Integer.toHexString(alpha);
        alphaRatio = new BigDecimal(alpha + "").divide(new BigDecimal("255"), 2, RoundingMode.FLOOR).toString();
        rgbColor = String.format("%s,%s,%s", red, green, blue);
        rgbaColor = String.format("%s,%s,%s,%s", red, green, blue, alphaRatio);
        hexColor = "#" + redHex + greenHex + blueHex;
        hexColorAlpha = "#" + redHex + greenHex + blueHex + alphaHex;
        decimalColor = Integer.parseInt(redHex + greenHex + blueHex, 16);
    }
}
