package com.example.btchat.http;

import android.os.AsyncTask;
import android.util.Log;

import com.example.btchat.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpFileAsyncTask extends AsyncTask<Void, Integer, String> implements HttpInterface {
    // Global variables
    public static final String tag = "HttpFileAsyncTask";

    private int mType;
    private String mID = null;
    private String mURL = null;
    private String mDir = null;
    private String mFileName = null;
    private int mResultStatus = MSG_HTTP_RESULT_CODE_OK;

    private static final int CONNECTION_TIMEOUT = 5000;

    // Context, system
    private HttpListener mListener;


    // Constructor
    public HttpFileAsyncTask(HttpListener l, int type, String id, String url, String directory, String filename) {
        mListener = l;
        mType = type;			// Not used in async task. will be used in callback
        mID = id;				// Not used in async task. will be used in callback
        mURL = url;
        mDir = directory;
        mFileName = filename;
    }


    protected String doInBackground(Void... unused)
    {
        if(mListener==null || mID==null || mURL==null || mDir==null || mFileName==null) {
            //Logs.d(tag, "###### Error!!! : Parameter is null. Check parameter");
            return "";
        }

        URL url = null;
        try {
            url = new URL(mURL);
        }
        catch (MalformedURLException e1) {
            e1.printStackTrace();
            mResultStatus = MSG_HTTP_RESULT_CODE_ERROR_REQUEST_EXCEPTION;
            Log.d(tag, "# URL = "+url);
            //Logs.d(tag, "###### Error!!! : MalformedURLException ");
            return null;
        }

        if(Utils.checkFileExists(mDir, mFileName))
            return null;
        String filePathAndName = new String(mDir+"/"+mFileName);

        try {
            // Set up connection
            HttpURLConnection conn= (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.connect();

            // Copy input stream (one is for calculate bitmap size and another is for decode bitmap)
            InputStream inputStream = conn.getInputStream();

            // Make file and output stream
            File file = new File(filePathAndName);
            OutputStream outStream = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len = 0;

            while ((len = inputStream.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }
            outStream.close();
            inputStream.close();

            mResultStatus = MSG_HTTP_RESULT_CODE_OK;

        } catch (Exception e) {
            mResultStatus = MSG_HTTP_RESULT_CODE_ERROR_UNKNOWN;
            //Logs.d(tag, "###### Error!!! : Cannot download file... ");
            e.printStackTrace();
            return null;
        }

        return filePathAndName;
    }

    protected void onProgressUpdate(Integer... progress) {
        // TODO: set progress percentage
        // This code runs on UI thread
    }

    protected void onPostExecute(String filename) {
        // This code runs on UI thread
        mListener.OnReceiveFileResponse(mType, mID, filename, mURL, mResultStatus);
    }

}
