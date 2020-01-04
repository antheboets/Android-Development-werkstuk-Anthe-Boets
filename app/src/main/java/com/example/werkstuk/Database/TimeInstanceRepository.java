package com.example.werkstuk.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.werkstuk.TimeInstance;

import java.util.List;

public class TimeInstanceRepository {

    private TimeInstanceDAO timeInstanceDAO;
    private LiveData<List<TimeInstance>> timeInstancesList;

    public TimeInstanceRepository(Application application) {
        DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstance(application);
        timeInstanceDAO = databaseSingleton.timeInstanceDAO();
        timeInstancesList = timeInstanceDAO.getAllTimeInstances();


    }
    public void insert(TimeInstance timeInstance) {
        new InserTimeInstaceAsync(timeInstanceDAO).execute(timeInstance);
    }

    public void update(TimeInstance timeInstance) {
        new  UpdateTimeInstaceAsync(timeInstanceDAO).execute(timeInstance);
    }

    public void delete(TimeInstance timeInstance) {
        new DeleteTimeInstaceAsync(timeInstanceDAO).execute(timeInstance);
    }

    public void deleteAll() {
        new DeleteAllTimeInstaceAsync(timeInstanceDAO).execute();
    }

    public LiveData<List<TimeInstance>> getAllTimeInstances() {
        return timeInstancesList;
    }

    public TimeInstance getById(int id){

        List<TimeInstance> list = timeInstancesList.getValue();
        if(list == null){
            return null;
        }
        for (TimeInstance item: list) {
            if(item.getId() == id){
                return item;
            }
        }
        return null;
    }

    private static class InserTimeInstaceAsync extends AsyncTask<TimeInstance, Void, Void>{

        private TimeInstanceDAO timeInstanceDAO;

        public InserTimeInstaceAsync(TimeInstanceDAO timeInstanceDAO) {
            this.timeInstanceDAO = timeInstanceDAO;
        }

        @Override
        protected Void doInBackground(TimeInstance... timeInstances) {
            timeInstanceDAO.insert(timeInstances[0]);
            return null;
        }
    }
    private static class UpdateTimeInstaceAsync extends AsyncTask<TimeInstance, Void, Void>{

        private TimeInstanceDAO timeInstanceDAO;

        public UpdateTimeInstaceAsync(TimeInstanceDAO timeInstanceDAO) {
            this.timeInstanceDAO = timeInstanceDAO;
        }

        @Override
        protected Void doInBackground(TimeInstance... timeInstances) {
            timeInstanceDAO.update(timeInstances[0]);
            return null;
        }
    }
    private static class DeleteTimeInstaceAsync extends AsyncTask<TimeInstance, Void, Void>{

        private TimeInstanceDAO timeInstanceDAO;

        public DeleteTimeInstaceAsync(TimeInstanceDAO timeInstanceDAO) {
            this.timeInstanceDAO = timeInstanceDAO;
        }

        @Override
        protected Void doInBackground(TimeInstance... timeInstances) {
            timeInstanceDAO.delete(timeInstances[0]);
            return null;
        }
    }
    private static class DeleteAllTimeInstaceAsync extends AsyncTask<TimeInstance, Void, Void>{

        private TimeInstanceDAO timeInstanceDAO;

        public DeleteAllTimeInstaceAsync(TimeInstanceDAO timeInstanceDAO) {
            this.timeInstanceDAO = timeInstanceDAO;
        }

        @Override
        protected Void doInBackground(TimeInstance... timeInstances) {
            timeInstanceDAO.deleteAll();
            return null;
        }
    }

}
