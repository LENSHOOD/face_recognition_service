package zxh.face.recognition.exception.internal;

import zxh.face.recognition.exception.BaseException;

/**
 * wrong image exception
 * @author zxh
 * @date 2018/03/07
 */
public class WrongImageException extends BaseException {

    public WrongImageException(String message) {
        super(message);
    }

    @Override
    protected void setErrorCode() {
        errorCode = "0003";
    }
}
