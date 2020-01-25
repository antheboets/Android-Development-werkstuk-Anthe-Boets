package com.example.werkstuk.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.werkstuk.TimeInstance;

import java.util.List;

public class TimeInstanceRepository {

    private TimeInstanceDAO timeInstanceDAO;
    private LiveData<List<TimeInstance>> timeInstancesList;
    private List<TimeInstance> timeInstancesListIsOn;

    public TimeInstanceRepository(Application application) {
        DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstance(application);
        timeInstanceDAO = databaseSingleton.timeInstanceDAO();
        timeInstancesList = timeInstanceDAO.getAllTimeInstances();
        timeInstancesListIsOn = timeInstanceDAO.getAllTimeInstancesListIsOn();
    }

    public void insert(TimeInstance timeInstance) {
        new InserTimeInstanceAsync(timeInstanceDAO).execute(timeInstance);
    }

    public void update(TimeInstance timeInstance) {
        new UpdateTimeInstanceAsync(timeInstanceDAO).execute(timeInstance);
    }

    public void delete(TimeInstance timeInstance) {
        new DeleteTimeInstanceAsync(timeInstanceDAO).execute(timeInstance);
    }

    public void deleteAll() {
        new DeleteAllTimeInstaceAsync(timeInstanceDAO).execute();
    }

    public LiveData<List<TimeInstance>> getAllTimeInstances() {
        return timeInstancesList;
    }

    public TimeInstance getByIdIsOn(long id){

        if(timeInstancesListIsOn != null){
            for (TimeInstance item: timeInstancesListIsOn) {
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }
    /*
    private static class GetTimeInstanceByeIdAsync extends AsyncTask<Long, Void, TimeInstance>{

        private TimeInstanceDAO timeInstanceDAO;

        public GetTimeInstanceByeIdAsync(TimeInstanceDAO timeInstanceDAO) {
            this.timeInstanceDAO = timeInstanceDAO;
        }

        @Override
        protected TimeInstance doInBackground(Long... longs) {
            return timeInstanceDAO.getByeIdAndIsOn(longs[0]);
        }

        @Override
        protected void onPostExecute(TimeInstance timeInstance) {
            super.onPostExecute(timeInstance);

        }
    }
    */

    private static class InserTimeInstanceAsync extends AsyncTask<TimeInstance, Void, Void>{

        private TimeInstanceDAO timeInstanceDAO;

        public InserTimeInstanceAsync(TimeInstanceDAO timeInstanceDAO) {
            this.timeInstanceDAO = timeInstanceDAO;
        }

        @Override
        protected Void doInBackground(TimeInstance... timeInstances) {
            timeInstanceDAO.insert(timeInstances[0]);
            return null;
        }
    }
    private static class UpdateTimeInstanceAsync extends AsyncTask<TimeInstance, Void, Void>{

        private TimeInstanceDAO timeInstanceDAO;

        public UpdateTimeInstanceAsync(TimeInstanceDAO timeInstanceDAO) {
            this.timeInstanceDAO = timeInstanceDAO;
        }

        @Override
        protected Void doInBackground(TimeInstance... timeInstances) {
            timeInstanceDAO.update(timeInstances[0]);
            return null;
        }
    }
    private static class DeleteTimeInstanceAsync extends AsyncTask<TimeInstance, Void, Void>{

        private TimeInstanceDAO timeInstanceDAO;

        public DeleteTimeInstanceAsync(TimeInstanceDAO timeInstanceDAO) {
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
