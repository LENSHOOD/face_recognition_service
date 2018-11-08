package zxh.face.recognition.exception.internal;

import zxh.face.recognition.exception.BaseException;

/**
 * server internal exception
 * @author zxh
 * @date 2018/03/07
 */
public class ServerInternalException extends BaseException {

    public ServerInternalException(String message) {
        super(message);
    }

    @Override
    protected void setErrorCode() {
        errorCode = "0002";
    }
}
