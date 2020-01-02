package com.example.werkstuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;

public class AddTimeInstanceActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.werkstuk.EXTRA_NAME";
    public static final String EXTRA_START = "com.example.werkstuk.EXTRA_START";
    public static final String EXTRA_END = "com.example.werkstuk.EXTRA_END";
    public static final String EXTRA_DAYS = "com.example.werkstuk.EXTRA_DAYS";
    public static final String EXTRA_TIMEINTERVAL = "com.example.werkstuk.EXTRA_TIMEINTERVAL";

    private TextView textViewName;
    private TimePicker timePickerStart;
    private TimePicker timePickerEnd;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_instance);

        textViewName = findViewById(R.id.edit_edit_text_name);
        timePickerStart = findViewById(R.id.edit_time_picker_start);
        timePickerEnd = findViewById(R.id.edit_time_picker_end);
        numberPicker = findViewById(R.id.edit_number_picker_time_interval);

        timePickerStart.setCurrentHour(0);
        timePickerStart.setCurrentMinute(0);

        timePickerEnd.setCurrentHour(0);
        timePickerEnd.setCurrentMinute(0);

        String[] values = TimeInstance.getIntervalStringArray();
        numberPicker.setMinValue(0);
        numberPicker.setMinValue(values.length - 1);
        numberPicker.setDisplayedValues(values);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Cloak");


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
                saveTimeInstance();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTimeInstance() {
        String name = textViewName.getText().toString();

        Date start = new Date(0);
        start.setHours(timePickerStart.getCurrentHour());
        start.setMinutes(timePickerStart.getCurrentMinute());
        Date end = new Date(0);
        end.setHours(timePickerEnd.getCurrentHour());
        end.setMinutes(timePickerEnd.getCurrentMinute());
        TimeInstance.EnumInterval enumInterval = TimeInstance.EnumInterval.intToEnumInterval(numberPicker.getValue());
        boolean[] days = new boolean[]{true, true, true, true, true, true, true};

        if (start.getTime() >= end.getTime()) {
            Toast.makeText(this, R.string.edit_fragment_start_end_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.trim().isEmpty()) {
            Toast.makeText(this, R.string.edit_fragment_name_error, Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        if(){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
        }
        */

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_START, start.getTime());
        data.putExtra(EXTRA_END, end.getTime());
        data.putExtra(EXTRA_DAYS, days);
        data.putExtra(EXTRA_TIMEINTERVAL, enumInterval);

        setResult(RESULT_OK, data);
        finish();
    }

}
