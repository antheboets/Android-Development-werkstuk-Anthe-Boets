package com.example.werkstuk;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.example.werkstuk.Database.TimeInstanceRepository;

import java.util.List;

import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_ID;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

            TimeInstanceRepository repository = new TimeInstanceRepository(((Application) context.getApplicationContext()));
            List<TimeInstance> timeInstanceList = repository.getAllTimeInstances().getValue();
            if(timeInstanceList != null){
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                for (TimeInstance item: timeInstanceList) {
                    if(item.isOn()&& item.hasADay()){
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                        alarmIntent.putExtra(EXTRA_ID, item.getId());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, item.getId(), alarmIntent,0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, item.calMiniSecForNextAlarm(),pendingIntent);
                    }
                }
            }
        }
    }
}
