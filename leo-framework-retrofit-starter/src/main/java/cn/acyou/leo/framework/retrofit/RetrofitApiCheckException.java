package cn.acyou.leo.framework.retrofit;

/**
 * @author youfang
 * @version [1.0.0, 2022/4/2 11:51]
 **/
public class RetrofitApiCheckException extends RuntimeException {
    public RetrofitApiCheckException() {
        super();
    }

    public RetrofitApiCheckException(String message) {
        super(message);
    }
}
