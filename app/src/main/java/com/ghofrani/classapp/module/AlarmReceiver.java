package com.ghofrani.classapp.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ghofrani.classapp.event.Update;

import org.greenrobot.eventbus.EventBus;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        EventBus.getDefault().post(new Update(true, false, false, false, true));

    }

}
