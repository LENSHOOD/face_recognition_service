package zxh.face.recognition.exception.handler;

import zxh.face.recognition.exception.BaseException;
import zxh.face.recognition.vo.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * global exception handler
 * @author zxh
 * @date 2018/03/07
 */
@RestControllerAdvice
public class FaceRecognitionExceptionHandler {

    /**
     * handle all exceptions
     * @param e exception
     * @return return the error BaseResponse to caller
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse fileUploadExceptionHandler(Exception e) {

        BaseResponse response = new BaseResponse();
        response.setSuccess(false);
        response.setTimestamp(System.currentTimeMillis());

        if (e instanceof BaseException) {

            response.setErrorCode(((BaseException) e).getErrorCode());
            response.setData(e.getMessage());

        } else {

            response.setErrorCode("0002");
            response.setData("服务器内部错误：" + e.toString());

        }

        return response;
    }
}
