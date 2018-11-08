package zxh.face.recognition.sdk.opencv.exception;

/**
 * wrong input face image exception
 * @author zxh
 * @date 2018/03/06
 */
public class WrongInputFaceImageException extends Exception {

    public WrongInputFaceImageException() {
    }

    public WrongInputFaceImageException(String message) {
        super(message);
    }

    public WrongInputFaceImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongInputFaceImageException(Throwable cause) {
        super(cause);
    }

    public WrongInputFaceImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
