package com.example.werkstuk;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.werkstuk.Database.TimeInstanceRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_ID;
import static com.example.werkstuk.OptionActivity.LANGUAGE_ID;
import static com.example.werkstuk.OptionActivity.SHARED_PREFERENCES_NAME;
import static com.example.werkstuk.OptionActivity._24HOURS_DAY;

public class AlarmReceiver extends BroadcastReceiver {

    private TextToSpeech mTTS;

    @Override
    public void onReceive(final Context context, Intent intent) {

        TimeInstanceRepository repository = new TimeInstanceRepository(((Application) context.getApplicationContext()));
        int id = intent.getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            TimeInstance timeInstance = repository.getById(id);
            if (timeInstance != null) {
                final SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
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

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (timeInstance.isOn()) {
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, timeInstance.getId(), alarmIntent, 0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInstance.calMiniSecForNextAlarm(), pendingIntent);
                }
            }
        }
    }
}
