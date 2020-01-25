package com.example.werkstuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Switch;

public class OptionActivity extends AppCompatActivity {

    public static final String LANGUAGE_ID = "com.example.werkstuk.LANGUAGE_ID";
    public static final String _24HOURS_DAY = "com.example.werkstuk._24HOURS_DAY";
    public static final String NOTIFICATIONS = "com.example.werkstuk._24HOURS_DAY";
    public static final String SHARED_PREFERENCES_NAME = "com.example.werkstuk.SHARED_PREFERENCES_NAME";


    private String[] languageList;
    private NumberPicker numberPicker;
    private Switch aSwitch24HourDays;
    private Switch aSwitchNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        languageList = new String[]{getString(R.string.english), getString(R.string.dutch)};

        numberPicker = findViewById(R.id.option_speech_language_numberpicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(languageList.length - 1);
        numberPicker.setDisplayedValues(languageList);
        numberPicker.setValue(sharedPref.getInt(LANGUAGE_ID, 0));

        boolean aaa = sharedPref.getBoolean(_24HOURS_DAY, true);
        boolean bbb = sharedPref.getBoolean(_24HOURS_DAY, false);
        int ccc = sharedPref.getInt(LANGUAGE_ID, -1);

        aSwitch24HourDays = findViewById(R.id.option_24hour_days_switch);
        aSwitch24HourDays.setChecked(sharedPref.getBoolean(_24HOURS_DAY, true));

        aSwitchNotifications = findViewById(R.id.option_notification_switch);
        aSwitchNotifications.setChecked(sharedPref.getBoolean(NOTIFICATIONS, true));

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        setTitle(getString(R.string.Additem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_time_intance_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.save_time_instance:
                saveSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveSettings() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(_24HOURS_DAY, aSwitch24HourDays.isChecked());
        editor.putBoolean(NOTIFICATIONS, aSwitchNotifications.isChecked());
        editor.putInt(LANGUAGE_ID, numberPicker.getValue());
        editor.commit();
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
