package com.example.mybtchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataSendService extends Service {
    public DataSendService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
