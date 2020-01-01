package com.example.werkstuk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class TimeInstanceListFragment extends Fragment {

    public interface TimeInstanceFragmentListListener {

    }

    private TimeInstanceFragmentListListener listener;
    private TimeInstanceViewModel timeInstanceViewModel;
    private RecyclerView recyclerView;
    private TimeInstaceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.time_instances_list_fragment, container, false);

        //Calendar calendar = Calendar.getInstance();
        //int day = calendar.get(Calendar.DAY_OF_WEEK);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
        //timeInstanceViewModel.insert(new TimeInstance(true, "thuis naar station", new Date(),new Date(),true,true,true,true,true,false,false));

        return view;
    }

    public void updateRecyleView(){
        adapter.setTimeInstanceList(timeInstanceViewModel.getAllTimeInstances().getValue());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof TimeInstanceFragmentListListener){
            listener = (TimeInstanceFragmentListListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement TimeInstanceFragmentListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
