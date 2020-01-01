package com.example.werkstuk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements TimeInstanceListFragment.TimeInstanceFragmentListListener {

    private TimeInstanceListFragment  timeInstanceListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeInstanceListFragment = new TimeInstanceListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment, timeInstanceListFragment).commit();


    }
}
