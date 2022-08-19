package cn.acyou.leo.pay.dto;

import cn.acyou.leo.pay.config.PayPalBean;
import lombok.extern.slf4j.Slf4j;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/19 10:51]
 **/
@Slf4j
public class TestDestroyBean implements Runnable {
    private PayPalBean payPalBean;
    private Byte[] bytes = new Byte[1024*1024*50];

    public TestDestroyBean(PayPalBean payPalBean) {
        this.payPalBean = payPalBean;
    }

    @Override
    protected void finalize() throws Throwable {
        log.info("=======================finalize 销毁了==============================");
    }

    @Override
    public void run() {
        log.info("运行中...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("运行结束");
    }
}
