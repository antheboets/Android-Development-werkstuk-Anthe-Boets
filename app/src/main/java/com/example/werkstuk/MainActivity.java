package com.example.werkstuk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TIME_INSTANCE_REQUEST = 1;

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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTimeInstanceActivity.class);
                startActivityForResult(intent, ADD_TIME_INSTANCE_REQUEST);
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
        if(requestCode == ADD_TIME_INSTANCE_REQUEST && resultCode == RESULT_OK){
            String name = getString(R.string.name);
            if(data.getStringExtra(AddTimeInstanceActivity.EXTRA_NAME) != null){
                name = data.getStringExtra(AddTimeInstanceActivity.EXTRA_NAME);
            }
            Date start = new Date(data.getLongExtra(AddTimeInstanceActivity.EXTRA_START,0));
            Date end = new Date(data.getLongExtra(AddTimeInstanceActivity.EXTRA_END,0));
            TimeInstance.EnumInterval enumInterval = TimeInstance.EnumInterval.intToEnumInterval(data.getIntExtra(AddTimeInstanceActivity.EXTRA_TIMEINTERVAL,0));
            boolean[] days = data.getBooleanArrayExtra(AddTimeInstanceActivity.EXTRA_DAYS);
            TimeInstance timeInstance = new TimeInstance(true,name,start,end,days[0],days[1],days[2],days[3],days[4],days[5],days[6],enumInterval);
            timeInstanceViewModel.insert(timeInstance);
            Toast.makeText(this, getString(R.string.created), Toast.LENGTH_SHORT).show();
        }
    }
}
