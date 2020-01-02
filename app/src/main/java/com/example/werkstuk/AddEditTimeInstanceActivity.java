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

public class AddEditTimeInstanceActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.werkstuk.EXTRA_ID";
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

        timePickerStart.setIs24HourView(true);
        timePickerEnd.setIs24HourView(true);

        timePickerStart.setCurrentHour(0);
        timePickerStart.setCurrentMinute(0);

        timePickerEnd.setCurrentHour(0);
        timePickerEnd.setCurrentMinute(0);

        String[] values = TimeInstance.getIntervalStringArray();
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(values.length - 1);
        numberPicker.setDisplayedValues(values);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();


        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Item");
            textViewName.setText(intent.getStringExtra(EXTRA_NAME));
            Date start = new Date(intent.getLongExtra(EXTRA_START,0));
            Date end = new Date(intent.getLongExtra(EXTRA_END,0));

            timePickerStart.setCurrentMinute(start.getMinutes());
            timePickerStart.setCurrentHour(start.getHours());

            timePickerEnd.setCurrentMinute(end.getMinutes());
            timePickerEnd.setCurrentHour(end.getHours());


            int i = intent.getIntExtra(EXTRA_TIMEINTERVAL,0);
            numberPicker.setValue(i);

        }
        else {
            setTitle("Add Item");
        }
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
        int enumInterval = numberPicker.getValue();
        boolean[] days = new boolean[]{true, true, true, true, true, true, true};

        if (start.getTime() >= end.getTime()) {
            Toast.makeText(this, R.string.add_edit_start_end_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.trim().isEmpty()) {
            Toast.makeText(this, R.string.add_edit_name_error, Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        if(){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
        }
        */

        Intent data = new Intent();
        int id = getIntent().getIntExtra(EXTRA_ID,-1);

        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_START, start.getTime());
        data.putExtra(EXTRA_END, end.getTime());
        data.putExtra(EXTRA_DAYS, days);
        data.putExtra(EXTRA_TIMEINTERVAL, enumInterval);

        setResult(RESULT_OK, data);
        finish();
    }

}
