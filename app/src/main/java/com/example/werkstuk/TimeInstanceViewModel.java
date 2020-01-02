package com.example.werkstuk;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.werkstuk.Database.TimeInstanceRepository;

import java.util.List;

public class TimeInstanceViewModel extends AndroidViewModel {
    private TimeInstanceRepository repository;
    private LiveData<List<TimeInstance>> timeInstacneList;


    public TimeInstanceViewModel(@NonNull Application application) {
        super(application);
        repository = new TimeInstanceRepository(application);
        timeInstacneList = repository.getAllTimeInstances();

    }

    public void insert(TimeInstance timeInstance){
        repository.insert(timeInstance);
    }

    public void update(TimeInstance timeInstance){
        repository.update(timeInstance);
    }

    public void delete(TimeInstance timeInstance){
        repository.delete(timeInstance);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<TimeInstance>> getAllTimeInstances(){
        return timeInstacneList;
    }

}
