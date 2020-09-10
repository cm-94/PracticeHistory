package com.example.btchat.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpAsyncTask extends AsyncTask<Void, Integer, String> implements HttpInterface {
    // Global variables
    public static final String tag = "HttpAsyncTask";

    //	private Map<String, String> mMap;	// Disabled
    private int mType;
    private String mURL = null;
    private int mResultStatus = MSG_HTTP_RESULT_CODE_OK;
    private int mRequestType = REQUEST_TYPE_GET;

    // Context, system
    private HttpListener mListener;

    // Constructor
    public HttpAsyncTask(HttpListener listener, int type, String url, int requestType) {
        mListener = listener;
        mType = type;		// Not used in async task. will be used in callback
        mURL = url;
        mRequestType = requestType;
    }


    protected String doInBackground(Void... unused)
    {
        Log.d(tag, "###### HttpAsyncTask :: Starting HTTP request task ");
        String resultString = null;
        HttpRequester httpRequester = new HttpRequester();

        if(mListener==null || mURL==null) {
            Log.d(tag, "###### Error!!! : mListener==null or mURL==null ");
            return null;
        } else {
            Log.d(tag, "###### Request URL = "+mURL);
        }

        URL url = null;
        try {
            url = new URL(mURL);
        }
        catch (MalformedURLException e1) {
            e1.printStackTrace();
            mResultStatus = MSG_HTTP_RESULT_CODE_ERROR_REQUEST_EXCEPTION;
            Log.d(tag, "###### Error!!! : MalformedURLException ");
            return "";
        }

        // Determine request type
        String reqType = null;
        if(mRequestType == REQUEST_TYPE_GET)
            reqType = REQUEST_TYPE_GET_STRING;
        else if(mRequestType == REQUEST_TYPE_POST)
            reqType = REQUEST_TYPE_POST_STRING;
        else
            reqType = REQUEST_TYPE_GET_STRING;

        // TODO: Manually set response encoding type.
        // Some page doesn't support UTF-8
        String encType = null;
        if(true) {
            encType = ENCODING_TYPE_UTF_8;
        }
//		else if(  ) {
//			encType = ENCODING_TYPE_EUC_KR;
//		}

        // Request
        try {
            resultString = httpRequester.request(url, encType, reqType, null);
            // publishProgress(int);
        } catch (IOException e) {
            e.printStackTrace();
            mResultStatus = MSG_HTTP_RESULT_CODE_ERROR_REQUEST_EXCEPTION;
            Log.d(tag, "###### Error!!! : HttpRequester makes IOException ");
            return "";
        }

        // Check result string
        if(resultString == null || resultString.length() < 1) {
            mResultStatus = MSG_HTTP_RESULT_CODE_ERROR_UNKNOWN;
            Log.d(tag, "###### Error!!! : resultString - invalid result ");
            return "";
        }

        mResultStatus = MSG_HTTP_RESULT_CODE_OK;
        return resultString;
    }

    protected void onProgressUpdate(Integer... progress) {
        // TODO: set progress percentage
        // This code runs on UI thread
    }

    protected void onPostExecute(String result) {
        // This code runs on UI thread
        if(mListener != null) {
            mListener.OnReceiveHttpResponse(mType, result, mResultStatus);
        }
    }
}
