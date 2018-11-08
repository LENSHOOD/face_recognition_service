package zxh.face.recognition.exception;

/**
 * base abstract exception
 * @author zxh
 * @date 2018/03/07
 */
public abstract class BaseException extends RuntimeException {

    /**
     * exception error code
     */
    protected String errorCode;

    /**
     * constructor with error message
     * @param message error message
     */
    public BaseException(String message) {
        super(message);
        setErrorCode();
    }

    /**
     * return the error code
     * @return error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * each child class should be set there own error code
     */
    protected abstract void setErrorCode();
}
