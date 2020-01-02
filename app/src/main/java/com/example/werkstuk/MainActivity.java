package com.example.werkstuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TIME_INSTANCE_REQUEST = 1;
    public static final int EDIT_TIME_INSTANCE_REQUEST = 2;

    private FloatingActionButton floatingActionButton;
    private TimeInstanceViewModel timeInstanceViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TimeInstaceAdapter adapter = new TimeInstaceAdapter();
        recyclerView.setAdapter(adapter);


        timeInstanceViewModel = ViewModelProviders.of(this).get(TimeInstanceViewModel.class);
        timeInstanceViewModel.getAllTimeInstances().observe(this, new Observer<List<TimeInstance>>() {
            @Override
            public void onChanged(List<TimeInstance> timeInstances) {
                adapter.setTimeInstanceList(timeInstances);
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
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TimeInstaceAdapter.OnClickListener(){

            @Override
            public void onItemClick(TimeInstance timeInstance) {
                Intent intent = new Intent(MainActivity.this, AddEditTimeInstanceActivity.class);
                intent.putExtra(AddEditTimeInstanceActivity.EXTRA_ID, timeInstance.getId());
                intent.putExtra(AddEditTimeInstanceActivity.EXTRA_NAME, timeInstance.getName());
                intent.putExtra(AddEditTimeInstanceActivity.EXTRA_START, timeInstance.getStart().getTime());
                intent.putExtra(AddEditTimeInstanceActivity.EXTRA_END, timeInstance.getEnd().getTime());
                intent.putExtra(AddEditTimeInstanceActivity.EXTRA_DAYS, timeInstance.getDaysArray());
                intent.putExtra(AddEditTimeInstanceActivity.EXTRA_TIMEINTERVAL, timeInstance.getIntfromEnum());
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
    /*
    public void openAddFragment(){
        AddTimeInstaceFragment fragment = AddTimeInstaceFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.mainActivityFragment, fragment, "BLANK_FRAGMENT").commit();
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TIME_INSTANCE_REQUEST && resultCode == RESULT_OK) {
            String name = getString(R.string.name);
            if (data.getStringExtra(AddEditTimeInstanceActivity.EXTRA_NAME) != null) {
                name = data.getStringExtra(AddEditTimeInstanceActivity.EXTRA_NAME);
            }
            Date start = new Date(data.getLongExtra(AddEditTimeInstanceActivity.EXTRA_START, 0));
            Date end = new Date(data.getLongExtra(AddEditTimeInstanceActivity.EXTRA_END, 0));
            TimeInstance.EnumInterval enumInterval = TimeInstance.EnumInterval.intToEnumInterval(data.getIntExtra(AddEditTimeInstanceActivity.EXTRA_TIMEINTERVAL, 0));
            boolean[] days = data.getBooleanArrayExtra(AddEditTimeInstanceActivity.EXTRA_DAYS);
            TimeInstance timeInstance = new TimeInstance(true, name, start, end, days[0], days[1], days[2], days[3], days[4], days[5], days[6], enumInterval);
            timeInstanceViewModel.insert(timeInstance);
            Toast.makeText(this, getString(R.string.created), Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_TIME_INSTANCE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditTimeInstanceActivity.EXTRA_ID, - 1);
            if(id == -1){
                Toast.makeText(this, getString(R.string.main_cant_update_error_toast), Toast.LENGTH_SHORT).show();
            }
            String name = getString(R.string.name);
            if (data.getStringExtra(AddEditTimeInstanceActivity.EXTRA_NAME) != null) {
                name = data.getStringExtra(AddEditTimeInstanceActivity.EXTRA_NAME);
            }
            Date start = new Date(data.getLongExtra(AddEditTimeInstanceActivity.EXTRA_START, 0));
            Date end = new Date(data.getLongExtra(AddEditTimeInstanceActivity.EXTRA_END, 0));
            TimeInstance.EnumInterval enumInterval = TimeInstance.EnumInterval.intToEnumInterval(data.getIntExtra(AddEditTimeInstanceActivity.EXTRA_TIMEINTERVAL, 0));
            boolean[] days = data.getBooleanArrayExtra(AddEditTimeInstanceActivity.EXTRA_DAYS);
            TimeInstance timeInstance = new TimeInstance(true, name, start, end, days[0], days[1], days[2], days[3], days[4], days[5], days[6], enumInterval);
            timeInstance.setId(id);
            timeInstanceViewModel.update(timeInstance);
            Toast.makeText(this, getString(R.string.main_update_toast), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, getString(R.string.main_delete_all_toast), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
