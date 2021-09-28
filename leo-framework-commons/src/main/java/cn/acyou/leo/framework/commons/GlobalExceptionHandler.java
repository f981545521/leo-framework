package cn.acyou.leo.framework.commons;

import cn.acyou.leo.framework.constant.CommonErrorEnum;
import cn.acyou.leo.framework.context.AppContext;
import cn.acyou.leo.framework.exception.*;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.prop.LeoProperty;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 统一异常处理，返回JSON
 *
 * @author youfang
 * @version [1.0.0, 2020-4-20 下午 09:43]
 **/
@Slf4j
@ControllerAdvice
@EnableConfigurationProperties(value = LeoProperty.class)
public class GlobalExceptionHandler {

    @Autowired
    private LeoProperty leoProperty;

    /** 文件大小超过限制 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public Result<Object> handleMaxUploadSizeExceededException(HttpServletRequest request, Exception e){
        Result<Object> resultInfo = Result.error(CommonErrorEnum.E_MAX_UPLOAD_SIZE_EXCEEDED);
        //org.springframework.web.multipart.MaxUploadSizeExceededException: Maximum upload size exceeded
        AppContext.setExceptionResult(resultInfo);
        return resultInfo;
    }
    /** method 不支持异常 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpServletRequest request, Exception e){
        Result<Object> resultInfo = Result.error();
        //org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' not supported
        resultInfo.setMessage(e.getMessage());
        AppContext.setExceptionResult(resultInfo);
        return resultInfo;
    }
    /** 请求参数转换错误 (RequestBody 接收的字符串转换不了Bean) */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Result<Object> handleHttpMessageNotReadableException(HttpServletRequest request, Exception e){
        Result<Object> resultInfo = Result.error();
        //org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Unexpected character ('"' (code 34)): ...
        resultInfo.setMessage("请求参数格式转换错误，请检查！");
        printErrorStackTraceInResultData(e, resultInfo);
        return resultInfo;
    }
    /** 类型转换失败 (RequestParam 接收的类型不正确 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Result<Object> handleMethodArgumentTypeMismatchException(HttpServletRequest request, Exception e){
        Result<Object> resultInfo = Result.error();
        //Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: "999X" ...
        resultInfo.setMessage("请求参数格式转换错误，请检查！");
        printErrorStackTraceInResultData(e, resultInfo);
        return resultInfo;
    }
    /** 断言异常 */
    @ExceptionHandler(AssertException.class)
    @ResponseBody
    public Result<Object> handleAssertException(HttpServletRequest request, Exception e){
        Result<Object> resultInfo = Result.error();
        resultInfo.setMessage(e.getMessage());
        AppContext.setExceptionResult(resultInfo);
        return resultInfo;
    }

    /** 违反数据库唯一约束：Duplicate entry 'DEMO:KEY' for key 'idx_param_config_code' */
    private static final Pattern MESSAGE_MATCHER = Pattern.compile("'([^']+)'");
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public Result<Object> handleDuplicateKeyException(HttpServletRequest request, Exception e) {
        Result<Object> resultInfo = Result.error();
        Throwable rootCause = Throwables.getRootCause(e);
        String rootMessage = rootCause.getMessage();
        if (rootMessage.matches("Duplicate entry(.*)")) {
            Matcher m = MESSAGE_MATCHER.matcher(rootMessage);
            if (m.find()) {
                resultInfo.setMessage(m.group() + "已经存在，请更换重试！");
            }
        }
        printErrorStackTraceInResultData(e, resultInfo);
        return resultInfo;
    }
    /** 违反数据库外键约束：Cannot delete or update a parent row: a foreign key constraint fails */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Result<Object> handleDataIntegrityViolationException(HttpServletRequest request, Exception e) {
        Result<Object> resultInfo = Result.error();
        resultInfo.setMessage("违反数据完整性异常，请检查！");
        e.printStackTrace();
        printErrorStackTraceInResultData(e, resultInfo);
        return resultInfo;
    }
    /** Controller 缺少`@RequestParam`参数：
     *  默认不能为空，如果想为空可以使用：`@RequestParam(required = false)`
     *  org.springframework.web.bind.MissingServletRequestParameterException: Required String parameter 'thirdAccountType' is not present
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result<Object> handleMissingServletRequestParameterException(HttpServletRequest request, Exception e) {
        Result<Object> resultInfo = Result.error(CommonErrorEnum.E_PARAM_ERROR);
        e.printStackTrace();
        printErrorStackTraceInResultData(e, resultInfo);
        return resultInfo;
    }
    /** 需要确认 */
    @ExceptionHandler(DoConfirmException.class)
    @ResponseBody
    public Result<Object> handleNeedSureException(HttpServletRequest request, Exception e) {
        Result<Object> resultInfo = Result.error(CommonErrorEnum.E_NEED_SURE);
        resultInfo.setMessage(e.getMessage());
        AppContext.setExceptionResult(resultInfo);
        return resultInfo;
    }
    /** 需要刷新页面 */
    @ExceptionHandler(DoRefreshException.class)
    @ResponseBody
    public Result<Object> handleDoRefreshException(HttpServletRequest request, Exception e) {
        Result<Object> resultInfo = Result.error(CommonErrorEnum.E_DO_REFRESH);
        resultInfo.setMessage(e.getMessage());
        AppContext.setExceptionResult(resultInfo);
        return resultInfo;
    }
    /**处理自定义服务异常*/
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result<Object> handleServiceException(HttpServletRequest request, Exception e) {
        Result<Object> resultInfo = Result.error();
        if (e instanceof ServiceException){
            ServiceException serviceException = (ServiceException) e;
            if (serviceException.getResult() != null){
                return serviceException.getResult();
            }else {
                resultInfo.setMessage(e.getMessage());
                printErrorStackTraceInResultData(e, resultInfo);
            }
        }
        return resultInfo;
    }
    /** 上面只能捕获RootCause是自身的，对RootCause不是自身的在这里判断。 **/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> handleException(HttpServletRequest request, Exception e) {
        Result<Object> resultInfo = Result.error();
        log.error("统一未知异常处理 => 请求路径：" + request.getRequestURI() + "，异常信息：" + e.getMessage());
        e.printStackTrace();
        Throwable rootCause = Throwables.getRootCause(e);
        if (rootCause instanceof ModifiedByAnotherUserException){
            resultInfo = Result.error(CommonErrorEnum.E_OPTMISTIC_MODIFIED);
        }
        if (rootCause instanceof RemovedByAnotherUserException){
            resultInfo = Result.error(CommonErrorEnum.E_OPTMISTIC_REMOVE);
        }
        if (rootCause instanceof DataPermissionDeniedException){
            resultInfo = Result.error(CommonErrorEnum.E_DATA_PERMISSION_DENIED);
            String message = rootCause.getMessage();
            if (StringUtils.isNotBlank(message)) {
                resultInfo.setMessage(message);
            }
        }
        printErrorStackTraceInResultData(e, resultInfo);
        return resultInfo;
    }
    /**
     * 打印错误堆栈跟踪结果数据
     *
     * @param e          e
     * @param resultInfo 返回信息
     */
    private void printErrorStackTraceInResultData(Exception e, Result<Object> resultInfo){
        //未知异常打印堆栈信息到data中。
        try {
            if (resultInfo.getData() == null){
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                e.printStackTrace(new PrintWriter(buf, true));
                buf.close();
                resultInfo.setData(buf.toString());
                AppContext.setExceptionResult(BeanCopyUtil.copy(resultInfo, Result.class));
                if (!leoProperty.isPrintToResult()) {
                    resultInfo.setData(null);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
