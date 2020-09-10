package com.example.btchat.http;

public interface HttpInterface {
    //---------- HTTP Requester status
    public static final int REQUESTER_STATUS_ERROR = -1;
    public static final int REQUESTER_STATUS_START = 1;
    public static final int REQUESTER_STATUS_BUSY = 2;
    public static final int REQUESTER_STATUS_COMPLETE = 3;
    public static final int REQUESTER_STATUS_PENDING = 4;

    //---------- HTTP Request message type
    public static final int MSG_HTTP_RESULT_FAIL = 0;
    public static final int MSG_HTTP_RESULT_OK = 1;

    //---------- HTTP response code
    public static final int MSG_HTTP_RESULT_CODE_OK = 0;
    public static final int MSG_HTTP_RESULT_CODE_TIMEOUT = 1;
    public static final int MSG_HTTP_RESULT_CODE_INVALID_URL = 2;
    public static final int MSG_HTTP_RESULT_CODE_INVALID_REQUEST = 3;
    public static final int MSG_HTTP_RESULT_CODE_SERVER_NOT_FOUND = 4;
    public static final int MSG_HTTP_RESULT_CODE_INTERNAL_SERVER_ERROR = 5;
    public static final int MSG_HTTP_RESULT_CODE_ERROR_UNKNOWN = 6;
    public static final int MSG_HTTP_RESULT_CODE_ERROR_REQUEST_EXCEPTION = 7;

    //---------- Request type (GET or POST or FILE)
    public static final int REQUEST_TYPE_GET = 1;
    public static final int REQUEST_TYPE_POST = 2;
    public static final int REQUEST_TYPE_FILE = 3;

    public static final String REQUEST_TYPE_GET_STRING = "GET";
    public static final String REQUEST_TYPE_POST_STRING = "POST";

    //---------- HTTP Request encoding type
    public static final String ENCODING_TYPE_EUC_KR = "euc-kr";
    public static final String ENCODING_TYPE_UTF_8 = "utf-8";
}
