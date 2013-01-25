package com.skp.opx.sns.sl.facebook;

/**
 * @설명 : 페이스북 Error 팝업 다이어로그 클래스
 * @클래스명 : DialogError
 */
public class DialogError extends Throwable {

    private static final long serialVersionUID = 1L;
    private int mErrorCode;
    private String mFailingUrl;

	/** 
	 * 생성자 
	 * */
    public DialogError(String message, int errorCode, String failingUrl) {
        super(message);
        mErrorCode = errorCode;
        mFailingUrl = failingUrl;
    }

	/** 
	 * 에러코드를 반환한다.
	 * */
    int getErrorCode() {
        return mErrorCode;
    }

	/** 
	 * 송수신 실패시 해당 URL을 반환한다.
	 * */
    String getFailingUrl() {
        return mFailingUrl;
    }
}
