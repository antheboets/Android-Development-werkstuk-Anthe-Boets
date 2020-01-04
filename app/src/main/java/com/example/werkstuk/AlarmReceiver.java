package com.example.werkstuk;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.werkstuk.Database.TimeInstanceRepository;

import java.util.List;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_ID;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        TimeInstanceRepository repository = new TimeInstanceRepository(((Application) context.getApplicationContext()));
        int id = intent.getIntExtra(EXTRA_ID,-1);
        if(id != -1){
            TimeInstance timeInstance = repository.getById(id);
            if(timeInstance != null){





                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                if(timeInstance.isOn()){
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, timeInstance.getId(), alarmIntent,0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInstance.calMiniSecForNextAlarm(),pendingIntent);
                }
            }
        }
    }
}
