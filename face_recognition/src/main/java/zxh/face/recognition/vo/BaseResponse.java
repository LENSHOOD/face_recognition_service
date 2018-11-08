package zxh.face.recognition.vo;

/**
 * base response
 * @author zxh
 * @date 2018/03/05
 */
public class BaseResponse {

    /**
     * response status
     */
    private Boolean success;

    /**
     * response timestamp
     */
    private Long timestamp;

    /**
     * error code: normal 0000; exception error 000x
     */
    private String errorCode;

    /**
     * response data
     */
    private Object data;

    public BaseResponse() {
        //null
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long time) {
        this.timestamp = time;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
