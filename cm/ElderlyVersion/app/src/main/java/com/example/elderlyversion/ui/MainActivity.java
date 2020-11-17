package com.example.elderlyversion.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elderlyversion.R;
import com.example.elderlyversion.data.ElderlyData;
import com.example.elderlyversion.service.BTCTemplateService;
import com.example.elderlyversion.utils.ApiUtils;
import com.example.elderlyversion.utils.AppSettings;
import com.example.elderlyversion.utils.Constants;
import com.example.elderlyversion.utils.Logs;
import com.example.elderlyversion.utils.RecycleUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.example.elderlyversion.utils.Constants.MESSAGE_BT_ADDING;

public class MainActivity extends Activity {
    private static Boolean checkAdd = false;

    // Debugging
    private static final String TAG = "RetroWatchActivity";

    private BTCTemplateService mService;
    private Handler mActivityHandler;

    // Global

    // UI stuff
    private ImageView mImageBT = null;
    private TextView mTextStatus = null;

    private String receiveMessage = "";
    private TextView stepText = null;
    private TextView pulseText = null;
    private TextView kcalText = null;
    private TextView latiText = null;
    private TextView longText = null;

    // Time to get message from Arduino
//    private static final int NEW_LINE_INTERVAL = 1000;
//    private static final long mLastReceivedTime = 0L;
    // Time to send Data to Server
    private static final int DATA_INTERVAL = 10000; // 600000
    private static long lastTime = 0L;

    // Post to server & Push & Notification
    private static String name;
    private static String homeIot;
    private static int ekey;
    private static String regid;
    NotificationManager manager;
    private static final String CHANNEL_ID = "channel1";
    private static final String CHANNEL_NAME = "Channel1";

    private static ElderlyData elderlyData;

    // Step Check
    Intent manboService;
    BroadcastReceiver receiver;
    boolean flag = true;
    // TextView stepText; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        elderlyData = new ElderlyData();
        elderlyData.setEstat(1);

        mActivityHandler = new ActivityHandler();

        AppSettings.initializeAppSettings(this);
        setContentView(R.layout.activity_main);

        // Get & Set Username from LoginActivity
        Intent intent = getIntent();
        processIntent(intent);

        // UI setting
        uiSetting();

        // Do data initialization after service started and binded
        doStartService();
    }

    /** Service 로 부터 message를 받음 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.i("test","act : what "+msg.what);
            switch (msg.what) {
                case BTCTemplateService.MSG_SEND_TO_ACTIVITY://444
                    int value1 = msg.getData().getInt("fromService");
                    String value2 = msg.getData().getString("test");
                    Log.i("test","act : value1 "+value1);
                    Log.i("test","act : value2 "+value2);
                    break;
            }
            return false;
        }
    }));

    @Override
    protected void onResume() {
        super.onResume();
//        sendEmergancyData();
//        sendData(elderlyData);
    }

    @Override
    public synchronized void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        finalizeActivity();
    }

    @Override
    public void onLowMemory (){
        super.onLowMemory();
        // onDestroy is not always called when applications are finished by Android system.
        finalizeActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                // Launch the DeviceListActivity to see devices and do scan
                doScan();
                return true;
            case R.id.action_discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();		// TODO: Disable this line to run below code
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        // This prevents reload after configuration changes
        super.onConfigurationChanged(newConfig);
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, "Activity - Service connected");
            mService = ((BTCTemplateService.ServiceBinder) binder).getService();
            // Activity couldn't work with mService until connections are made
            // So initialize parameters and settings here. Do not initialize while running onCreate()
            initialize();
        }
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };
    // Start service if it's not running
    private void doStartService() {
        Log.d(TAG, "# Activity - doStartService()");
        startService(new Intent(this, BTCTemplateService.class));
        bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);
    }
    // Stop the service
    private void doStopService() {
        Log.d(TAG, "# Activity - doStopService()");
        mService.finalizeService();
        stopService(new Intent(this, BTCTemplateService.class));
    }

    // Initialization / Finalization
    private void initialize() {
        Logs.d(TAG, "# Activity - initialize()");
        mService.setupService(mActivityHandler);

        // If BT is not on, request that it be enabled.
        // RetroWatchService.setupBT() will then be called during onActivityResult
        if(!mService.isBluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
        }
    }

    private void finalizeActivity() {
        Logs.d(TAG, "# Activity - finalizeActivity()");

        if(!AppSettings.getBgService()) {
//            doStopService();
        }
        // Clean used resources
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }

    // Launch the DeviceListActivity to see devices and do scan
    private void doScan() {
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CONNECT_DEVICE);
    }

    // Ensure this device is discoverable by others
    private void ensureDiscoverable() {
        if (mService.getBluetoothScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logs.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {
            case Constants.REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = Objects.requireNonNull(data.getExtras()).getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Attempt to connect to the device
                    if (address != null && mService != null)
                        mService.connectDevice(address);
                }
                break;

            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a BT session
                    mService.setupBT();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Logs.e(TAG, "BT is not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                }
                break;
        }    // End of switch(requestCode)
    }

    @SuppressLint("HandlerLeak")
    public class ActivityHandler extends Handler {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what) {
                // Receives BT state messages from service, and updates BT state UI
                case Constants.MESSAGE_BT_STATE_INITIALIZED:
                    mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + getResources().getString(R.string.bt_state_init));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
                    break;
                case Constants.MESSAGE_BT_STATE_LISTENING:
                    mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " +  getResources().getString(R.string.bt_state_wait));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTING:
                    mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + getResources().getString(R.string.bt_state_connect));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_away));
                    break;
                case Constants.MESSAGE_BT_STATE_CONNECTED:
                    if(mService != null) {
                        String deviceName = mService.getDeviceName();
                        if(deviceName != null) {
                            mTextStatus.setText(getResources().getString(R.string.bt_title) + ": " + getResources().getString(R.string.bt_state_connected) + " " + deviceName);
                            mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
                        }
                    }
                    break;
                case Constants.MESSAGE_BT_STATE_ERROR:
                    mTextStatus.setText(getResources().getString(R.string.bt_state_error));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
                    break;

                // BT Command status
                case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:
                    mTextStatus.setText(getResources().getString(R.string.bt_cmd_sending_error));
                    mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
                    break;

                ///////////////////////////////////////////////
                // When there's incoming packets on bluetooth
                // do the UI works like below
                ///////////////////////////////////////////////
                // TODO : Arduino Message -> 화면에 표시
                case Constants.MESSAGE_READ_CHAT_DATA:

                    if(msg.obj != null) {
                        Log.d("__MSG",(String)msg.obj);
                        String str = addMessage((String)msg.obj);
                        if (str == MESSAGE_BT_ADDING || str == " "){
                            return;
                        }else{
                            Log.d("RECEIVE_Data", str);
                            if (messageParsing(str)){
                                messageSetting(elderlyData);
                            }
                        }

                        long current = System.currentTimeMillis();
                        if (checkBPM(elderlyData)){
                            Log.d("RECEIVE_Data","HIGH_BPM!!");
                            elderlyData.setEstat(0);
//                            sendEmergancyData(name,"긴급 버튼!");

//                            sendEmergancyData(name,"심박수 높음!");
                            showNoti(name,"심박수 높음!");
                        }
                        if(elderlyData.getEstat()==0){
                            phoneCall();
//                            sendEmergancyData(name,"긴급 버튼!");
//                            sendData(elderlyData);
                            lastTime = current;
                        }
                        if(current - lastTime > DATA_INTERVAL) {
//                            sendData(elderlyData);
                            lastTime = current;
                        }
                    }
                    receiveMessage = "";
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }	// End of class ActivityHandler

    public void showNoti(String title, String text) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                Intent.FLAG_ACTIVITY_SINGLE_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Log.d(TAG, "Notification호출됨");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 ,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setOngoing(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(2 /* ID of notification */, notificationBuilder.build());
    }

    public void phoneCall(){
        String tellNumber = "tel:11111111119";
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tellNumber));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                Intent.FLAG_ACTIVITY_SINGLE_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public Boolean checkBPM(ElderlyData elderlyData){
        if ( elderlyData.getEpulse()>80) return true;
        else return false;
    }

    // TODO : Arduino Message parsing
    //  "{" 문자부터 "}" 문자까지 데이터(receiveMessage) 붙이기
    public String addMessage(String message) {
        if (message != null && message.length() > 0) {
            for (int i = 0;i<message.length();i++){
                if(message.charAt(i)=='{'){
                    checkAdd = true;
                    break;
                }else if(message.charAt(i)=='}'){
                    checkAdd = false;
                }
            }
            if (checkAdd){
                receiveMessage += message;
                return MESSAGE_BT_ADDING;
            }else{
                String addmessage = receiveMessage;
                receiveMessage = "";
                return addmessage;
            }
//            long current = System.currentTimeMillis();
//            if (current - mLastReceivedTime > NEW_LINE_INTERVAL) {
//                Log.d("ADDED MESSAGE END", receiveMessage);
//                mLastReceivedTime = current;
//                addMessage = receiveMessage;
//                return addMessage;
//            }
        }else {
            return "Message is null or 0";
        }
    }

    public Boolean messageParsing(String receiveMsg){
        if (receiveMsg.length() <  60){
            return false;
        }
        String[] array = receiveMsg.split(",");
        Log.d("Parsing_MSG",receiveMsg);

        if(array[0].equals("0") || array[0].equals("1")){
            Log.d("Parsing_MSG","Start parsing");

            elderlyData.setEstat(Integer.parseInt(array[0]));
            elderlyData.setEpulse(Integer.parseInt(array[1].substring(6)));
            elderlyData.setEstep(Integer.parseInt(array[2].substring(5)));
            elderlyData.setEaltitude(Double.parseDouble(array[3].substring(5,20)));
            elderlyData.setElongitude(Double.parseDouble(array[4].substring(5,20)));
            return true;
        }
        else{
            Log.d("RECEIVE_Parsing_MSG","First Data missing.. : "+ array[0]);
            return false;
        }
    }

    // TODO : Server에 메시지 PUT
    public void sendData(ElderlyData elderlyData){
        // TODO : Elderly Data -> 서버에 전송( 주기 : DATA_INTERVAL 설정 ㄱㄱ)
        ApiUtils.getElderlyService(getApplicationContext()).putElderlyData(elderlyData).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("PUT_SUCCESS", response.message()); // 성공 : OK(200)
                    try {
                        Toast.makeText(getApplicationContext(),"데이터 전송 성공! :"+response.body().string(),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("PUT_NOT_SUCCESS", response.message()); // 실패
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("PUT_FAILURE", call.toString());
//                t.printStackTrace();t.getLocalizedMessage();
            }
        });
    }

    // TODO : Volley로 Push 보내기(Notification & Data)
    public void sendEmergancyData(String title, String text) {
        Log.d("PUSH", "Start");
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://fcm.googleapis.com/fcm/send";
            JSONObject pushData = new JSONObject();

            pushData.put("ekey", elderlyData.getEkey());
            pushData.put("title", title);
            pushData.put("body", text);
            pushData.put("url", "null");

            JSONObject push = new JSONObject();
            push.put("to", regid);
            push.put("data", pushData);

            JsonObjectRequest request = new JsonObjectRequest(url, push, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Emergancy_Push", "success:" + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Emergancy_Push", "error:" + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", Constants.FCM_API_KEY);
                    return headers;
                }
            };
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("PUSH", "End");
    }

    public void messageSetting(ElderlyData elderly){
        stepText.setText(String.valueOf(elderlyData.getEstep()));
        elderlyData.setEkcal(elderlyData.getEstep() * 70 * 5.5 / 10000);
        kcalText.setText(String.valueOf(elderlyData.getEkcal()));

        pulseText.setText(String.valueOf(elderly.getEpulse()));
        latiText.setText(String.valueOf(elderly.getEaltitude()));
        longText.setText(String.valueOf(elderly.getElongitude()));
    }

    // Count Step
    class StepReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("PlayignReceiver", "IN");
        }
    }

    public void getSteps(){
        // TODO Auto-generated method stub
        try {
            IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");
            registerReceiver(receiver, mainFilter);
            startService(manboService);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void processIntent(Intent intent) {
        if (intent != null){
            name = intent.getStringExtra("ename");
            homeIot = intent.getStringExtra("homeIoT");
            ekey = intent.getIntExtra("ekey",-1);
            elderlyData.setEkey(ekey);
            regid = "eTRx-Z31TdCjy00iLSygQB:APA91bHKGYvaPTKc26kIJjhC2Bu_GQf-XPlwnZNMubK4gqptdhxtIEmqdh-r9-RyFClj0BLAoXRQn_xOBN-obMhMsUU__q_JqmKeSN1DCcQlb5zSzgepPzJM6gD_Qwu43S4bpZhhA1Gx";

//            if (intent.getStringExtra("regId")!= null){
//                regid = intent.getStringExtra("regid");
//            }else{
//                regid = "eTRx-Z31TdCjy00iLSygQB:APA91bHKGYvaPTKc26kIJjhC2Bu_GQf-XPlwnZNMubK4gqptdhxtIEmqdh-r9-RyFClj0BLAoXRQn_xOBN-obMhMsUU__q_JqmKeSN1DCcQlb5zSzgepPzJM6gD_Qwu43S4bpZhhA1Gx";
//            }
            Toast.makeText(this,name+"님 환영합니다.",Toast.LENGTH_SHORT).show();
            Log.d("Main_Lgoin", "eKey:"+ekey+", regId:"+regid+", homeIoT:"+homeIot);
        }
    }

    public void uiSetting(){
        // Setup views
        mImageBT = (ImageView) findViewById(R.id.status_title);
        mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
        mTextStatus = (TextView) findViewById(R.id.status_text);
        mTextStatus.setText(getResources().getString(R.string.bt_state_init));
        TextView nameText = findViewById(R.id.nameText);
        // stepText = findViewById(R.id.stepText);
        pulseText = findViewById(R.id.pulseText);
        kcalText = findViewById(R.id.kcalText);
        latiText = findViewById(R.id.latitudeText);
        longText = findViewById(R.id.longitudeText);

        stepText = findViewById(R.id.stepText);


        Button mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        Button homeIoTButton = findViewById(R.id.homeButton);
        homeIoTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeIot));
                startActivity(intent);
            }
        });
    }

    public static ElderlyData getElderlyData() { return elderlyData; }
}