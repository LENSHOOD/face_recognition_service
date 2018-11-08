package zxh.face.recognition.exception.internal;

import zxh.face.recognition.exception.BaseException;

/**
 * file upload exception
 * @author zxh
 * @date 2018/03/07
 */
public class FileUploadException extends BaseException {

    public FileUploadException(String message) {
        super(message);
    }

    @Override
    protected void setErrorCode() {
        errorCode = "0001";
    }
}
