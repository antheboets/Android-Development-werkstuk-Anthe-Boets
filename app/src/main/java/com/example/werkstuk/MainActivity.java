package com.example.werkstuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_DAYS;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_ID;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_NAME;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_START;
import static com.example.werkstuk.AddEditTimeInstanceActivity.EXTRA_TIMEINTERVAL;
import static com.example.werkstuk.OptionActivity.LANGUAGE_ID;
import static com.example.werkstuk.OptionActivity.SHARED_PREFERENCES_NAME;
import static com.example.werkstuk.OptionActivity._24HOURS_DAY;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TIME_INSTANCE_REQUEST = 1;
    public static final int EDIT_TIME_INSTANCE_REQUEST = 2;
    public static final int SETTINS_REQUEST = 3;

    public static final String EXTRA_ON = "com.example.werkstuk.EXTRA_ON";

    private FloatingActionButton floatingActionButton;
    private TimeInstanceViewModel timeInstanceViewModel;
    private RecyclerView recyclerView;
    private TimeInstaceAdapter adapter;

    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TimeInstaceAdapter(getPreferences(Context.MODE_PRIVATE).getBoolean(_24HOURS_DAY,true));
        recyclerView.setAdapter(adapter);

        timeInstanceViewModel = ViewModelProviders.of(this).get(TimeInstanceViewModel.class);
        timeInstanceViewModel.getAllTimeInstances().observe(this, new Observer<List<TimeInstance>>() {
            @Override
            public void onChanged(List<TimeInstance> timeInstances) {
                adapter.setTimeInstanceList(timeInstances);
                resetAllAlarms(timeInstances);
            }
        });

        floatingActionButton = findViewById(R.id.floatingActionButton);

        //timeInstanceViewModel.insert(new TimeInstance(true, "thuis naar station", new Date(),new Date(),true,true,true,true,true,false,false,TimeInstance.EnumInterval.ONEHOUR));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditTimeInstanceActivity.class);
                startActivityForResult(intent, ADD_TIME_INSTANCE_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                timeInstanceViewModel.delete(adapter.getTimeInstanceAt(viewHolder.getAdapterPosition()));
                deleteAlarm(adapter.getTimeInstanceAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TimeInstaceAdapter.OnClickListener(){

            @Override
            public void onItemClick(TimeInstance timeInstance) {
                Intent intent = new Intent(MainActivity.this, AddEditTimeInstanceActivity.class);
                intent.putExtra(EXTRA_ID, timeInstance.getId());
                intent.putExtra(EXTRA_NAME, timeInstance.getName());
                intent.putExtra(EXTRA_START, timeInstance.getStart().getTime());
                intent.putExtra(AddEditTimeInstanceActivity.EXTRA_END, timeInstance.getEnd().getTime());
                intent.putExtra(EXTRA_DAYS, timeInstance.getDaysArray());
                intent.putExtra(EXTRA_TIMEINTERVAL, timeInstance.getIntfromEnum());
                startActivityForResult(intent, EDIT_TIME_INSTANCE_REQUEST);
            }
        });

        adapter.setOnSwitchChange(new TimeInstaceAdapter.OnSwitchChangeListener() {

            @Override
            public void onSwitchChange(TimeInstance timeInstance) {
                timeInstanceViewModel.update(timeInstance);


            }
        });

        /*
        timeInstanceListFragment = new TimeInstanceListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment, timeInstanceListFragment).commit();
        */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TIME_INSTANCE_REQUEST && resultCode == RESULT_OK) {
            String name = getString(R.string.name);
            if (data.getStringExtra(EXTRA_NAME) != null) {
                name = data.getStringExtra(EXTRA_NAME);
            }
            Date start = new Date(data.getLongExtra(EXTRA_START, 0));
            Date end = new Date(data.getLongExtra(AddEditTimeInstanceActivity.EXTRA_END, 0));
            TimeInstance.EnumInterval enumInterval = TimeInstance.EnumInterval.intToEnumInterval(data.getIntExtra(EXTRA_TIMEINTERVAL, 0));
            boolean[] days = data.getBooleanArrayExtra(EXTRA_DAYS);
            TimeInstance timeInstance = new TimeInstance(true, name, start, end, days[0], days[1], days[2], days[3], days[4], days[5], days[6], enumInterval);
            timeInstanceViewModel.insert(timeInstance);
            //resetAllAlarms(timeInstanceViewModel.getAllTimeInstances().getValue());
            Toast.makeText(this, getString(R.string.created), Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_TIME_INSTANCE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(EXTRA_ID, - 1);
            if(id == -1){
                Toast.makeText(this, getString(R.string.main_cant_update_error_toast), Toast.LENGTH_SHORT).show();
            }
            String name = getString(R.string.name);
            if (data.getStringExtra(EXTRA_NAME) != null) {
                name = data.getStringExtra(EXTRA_NAME);
            }
            Date start = new Date(data.getLongExtra(EXTRA_START, 0));
            Date end = new Date(data.getLongExtra(AddEditTimeInstanceActivity.EXTRA_END, 0));
            TimeInstance.EnumInterval enumInterval = TimeInstance.EnumInterval.intToEnumInterval(data.getIntExtra(EXTRA_TIMEINTERVAL, 0));
            boolean[] days = data.getBooleanArrayExtra(EXTRA_DAYS);
            TimeInstance timeInstance = new TimeInstance(true, name, start, end, days[0], days[1], days[2], days[3], days[4], days[5], days[6], enumInterval);
            timeInstance.setId(id);
            timeInstanceViewModel.update(timeInstance);
            //resetAlarm(timeInstance);
            Toast.makeText(this, getString(R.string.main_update_toast), Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == SETTINS_REQUEST && resultCode == RESULT_OK){
            SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_NAME,MODE_PRIVATE);
            adapter.set24HoursDay(sharedPref.getBoolean(_24HOURS_DAY, true));
            adapter.resetListView();
            Toast.makeText(this, getString(R.string.settingsSaved), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_menu_delete_all_time_instances:
                timeInstanceViewModel.deleteAll();
                //deleteAllAlarms(timeInstanceViewModel.getAllTimeInstances().getValue());
                Toast.makeText(this, getString(R.string.main_delete_all_toast), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.main_activity_menu_item_settings:
                Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                startActivityForResult(intent, SETTINS_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void deleteAlarm(TimeInstance timeInstance){
        if(timeInstance.isOn() && timeInstance.hasADay()){
            removeAlarm(timeInstance);
        }
    }
    public void deleteAllAlarms(List<TimeInstance> list){
        for(TimeInstance instance: list){
            if(instance.isOn() && instance.hasADay()){
                removeAlarm(instance);
            }
        }
    }
    public void resetAlarm(TimeInstance timeInstance){
        if(timeInstance.isOn() && timeInstance.hasADay()){
            removeAlarm(timeInstance);
            setAlarm(timeInstance);
        }
    }
    public void resetAllAlarms(List<TimeInstance> list){
        for(TimeInstance instance: list){
            if(instance.isOn() && instance.hasADay()){
                removeAlarm(instance);
                setAlarm(instance);
            }
        }
    }
    public void setAlarm(TimeInstance timeInstance){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(EXTRA_ID, timeInstance.getId());
        intent.putExtra(EXTRA_NAME, timeInstance.getName());
        intent.putExtra(EXTRA_START, timeInstance.getStart());
        intent.putExtra(EXTRA_DAYS, timeInstance.getEnd());
        intent.putExtra(EXTRA_DAYS, timeInstance.getDaysArray());
        intent.putExtra(EXTRA_TIMEINTERVAL, timeInstance.getIntfromEnum());
        intent.putExtra(EXTRA_ON, timeInstance.isOn());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInstance.calMiniSecForNextAlarm(), pendingIntent);
    }
    public void removeAlarm(TimeInstance timeInstance){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
