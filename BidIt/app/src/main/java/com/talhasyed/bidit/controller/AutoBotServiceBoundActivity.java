package com.talhasyed.bidit.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.talhasyed.bidit.service.AutoBotService;

/**
 * Created by Talha.Syed on 10/31/2016.
 */

public abstract class AutoBotServiceBoundActivity extends AppCompatActivity implements ServiceConnection {
    public AutoBotService.LocalBinder binder;
    public boolean isBound = false;

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
        isBound = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, AutoBotService.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, AutoBotService.class);
        bindService(intent, this, Context.BIND_ABOVE_CLIENT);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        binder = (AutoBotService.LocalBinder) service;
        // bleService = binder.getService();
        isBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBound = false;
        binder = null;
        //bleService = null;
    }
}
