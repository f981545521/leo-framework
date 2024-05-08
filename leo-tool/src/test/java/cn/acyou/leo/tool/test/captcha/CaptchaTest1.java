package cn.acyou.leo.tool.test.captcha;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.ImageTransform;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.generator.impl.MultiImageCaptchaGenerator;
import cloud.tianai.captcha.generator.impl.transform.Base64ImageTransform;
import cloud.tianai.captcha.resource.ImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.impl.DefaultImageCaptchaResourceManager;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import cloud.tianai.captcha.validator.impl.BasicCaptchaTrackValidator;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/8 16:22]
 **/
public class CaptchaTest1 {

    @Test
    public void test1() throws InterruptedException {
        ImageCaptchaResourceManager imageCaptchaResourceManager = new DefaultImageCaptchaResourceManager();
        ImageTransform imageTransform = new Base64ImageTransform();
        ImageCaptchaGenerator imageCaptchaGenerator = new MultiImageCaptchaGenerator(imageCaptchaResourceManager,imageTransform).init(true);
        /*
                生成滑块验证码图片, 可选项
                SLIDER (滑块验证码)
                ROTATE (旋转验证码)
                CONCAT (滑动还原验证码)
                WORD_IMAGE_CLICK (文字点选验证码)

                更多验证码支持 详见 cloud.tianai.captcha.common.constant.CaptchaTypeConstant
         */
        ImageCaptchaInfo imageCaptchaInfo = imageCaptchaGenerator.generateCaptchaImage(CaptchaTypeConstant.SLIDER);
        System.out.println(imageCaptchaInfo);

        // 负责计算一些数据存到缓存中，用于校验使用
        // ImageCaptchaValidator负责校验用户滑动滑块是否正确和生成滑块的一些校验数据; 比如滑块到凹槽的百分比值
        ImageCaptchaValidator imageCaptchaValidator = new BasicCaptchaTrackValidator();
        // 这个map数据应该存到缓存中，校验的时候需要用到该数据
        Map<String, Object> map = imageCaptchaValidator.generateImageCaptchaValidData(imageCaptchaInfo);
        System.out.println(map);
    }

    @Test
    public void test2() throws InterruptedException {
        BasicCaptchaTrackValidator sliderCaptchaValidator = new BasicCaptchaTrackValidator();

        ImageCaptchaTrack imageCaptchaTrack = null;
        Map<String, Object> map = null;
        Float percentage = null;
        // 用户传来的行为轨迹和进行校验
        // - imageCaptchaTrack为前端传来的滑动轨迹数据
        // - map 为生成验证码时缓存的map数据
        boolean check = sliderCaptchaValidator.valid(imageCaptchaTrack, map).isSuccess();
//        // 如果只想校验用户是否滑到指定凹槽即可，也可以使用
//        // - 参数1 用户传来的百分比数据
//        // - 参数2 生成滑块是真实的百分比数据
        check = sliderCaptchaValidator.checkPercentage(0.2f, percentage);
    }
}
