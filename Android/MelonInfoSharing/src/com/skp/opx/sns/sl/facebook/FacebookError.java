package com.skp.opx.sns.sl.facebook;

/**
 * @설명 : 페이스북 Error 핸들링 클래스 
 * @클래스명 : FacebookError 
 *
 */
public class FacebookError extends Throwable {

    private static final long serialVersionUID = 1L;

    private int mErrorCode = 0;
    private String mErrorType;

    public FacebookError(String message) {
        super(message);
    }

    public FacebookError(String message, String type, int code) {
        super(message);
        mErrorType = type;
        mErrorCode = code;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorType() {
        return mErrorType;
    }

}
