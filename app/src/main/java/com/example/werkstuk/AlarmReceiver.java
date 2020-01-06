package com.example.werkstuk;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import androidx.core.app.NotificationCompat;

import com.example.werkstuk.Database.TimeInstanceRepository;

import java.util.Date;

import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_DAYS;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_ID;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_NAME;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_START;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_TIMEINTERVAL;
import static com.example.werkstuk.MainActivity.EXTRA_ON;
import static com.example.werkstuk.OptionActivity.NOTIFICATIONS;
import static com.example.werkstuk.OptionActivity.SHARED_PREFERENCES_NAME;

public class AlarmReceiver extends BroadcastReceiver {

    private TextToSpeech mTTS;
    public static final String CHANNEL_ID = "com.example.werkstuk.CHANNEL";
    private SharedPreferences sharedPref;

    @Override
    public void onReceive(final Context context, Intent intent) {

        TimeInstanceRepository repository = new TimeInstanceRepository(((Application) context.getApplicationContext()));
        int id = intent.getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            boolean on = intent.getBooleanExtra(EXTRA_ON, false);

            Date start = new Date(intent.getLongExtra(EXTRA_START, 0));
            Date end = new Date(intent.getLongExtra(AddEditTimeInstanceActivity.EXTRA_END, 0));
            TimeInstance.EnumInterval enumInterval = TimeInstance.EnumInterval.intToEnumInterval(intent.getIntExtra(EXTRA_TIMEINTERVAL, 0));
            boolean[] days = intent.getBooleanArrayExtra(EXTRA_DAYS);
            String name = intent.getStringExtra(EXTRA_NAME);
            TimeInstance timeInstance = new TimeInstance(on, name, start, end, days[0], days[1], days[2], days[3], days[4], days[5], days[6], enumInterval);
            timeInstance.setId(id);
            //if (timeInstance != null) {

            sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                /*
                mTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {

                            Locale locale;
                            switch (sharedPref.getInt(LANGUAGE_ID,0)) {
                                case 0:
                                    locale = Locale.ENGLISH;
                                    break;
                                case 1:
                                    locale = new Locale("BE_NL");
                                    break;
                                default:
                                    locale = Locale.ENGLISH;
                                    break;
                            }

                            int result = mTTS.setLanguage(locale);
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TTS", "Language not supported");
                            }
                        } else {
                            Log.e("TTS", "Initialization failed");
                        }
                    }
                });

                SimpleDateFormat ft;
                if(sharedPref.getBoolean(_24HOURS_DAY, true)){
                    ft = new SimpleDateFormat("kk:mm");
                }
                else{
                    ft = new SimpleDateFormat("KK:mm a");
                }

                String strToSpeak = ft.format(new Date());

                mTTS.speak(strToSpeak, TextToSpeech.QUEUE_FLUSH, null);



                 */
            if (sharedPref.getBoolean(NOTIFICATIONS, true)) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                            "Channel",
                            NotificationManager.IMPORTANCE_HIGH
                    );

                    channel.setDescription("Speaking clock channel");
                    notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_android)
                        .setContentTitle(context.getString(R.string.alarm))
                        .setContentText(timeInstance.getName())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                notificationManager.notify(1, builder.build());
            }
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (timeInstance.isOn() && timeInstance.hasADay()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                alarmIntent.putExtra(EXTRA_ID, id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, timeInstance.getId(), alarmIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInstance.calMiniSecForNextAlarm(), pendingIntent);
            }
        }
    }
}

