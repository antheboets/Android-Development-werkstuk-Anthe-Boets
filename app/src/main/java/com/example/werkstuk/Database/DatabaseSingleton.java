package com.example.werkstuk.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.werkstuk.TimeInstance;

import java.util.Date;

@Database(entities = {TimeInstance.class}, version = 5)
public abstract class DatabaseSingleton extends RoomDatabase {

    private static DatabaseSingleton instance;
    public abstract TimeInstanceDAO timeInstanceDAO();

    static synchronized  DatabaseSingleton getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),DatabaseSingleton.class, "talkingCloack_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(instance).execute();
        }
    };
    private  static class PopulateDbAsync extends AsyncTask<Void,Void,Void>{
        private TimeInstanceDAO timeInstanceDAO;

        public PopulateDbAsync(DatabaseSingleton databaseSingleton) {
            this.timeInstanceDAO = databaseSingleton.timeInstanceDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            timeInstanceDAO.insert(new TimeInstance(true, "thuis naar station", new Date(),new Date(),true,true,true,true,true,false,false,TimeInstance.EnumInterval.ONEHOUR));
            timeInstanceDAO.insert(new TimeInstance(false, "misc", new Date(),new Date(),false,false,false,false,false,true,true,TimeInstance.EnumInterval.ONEHOUR));
            timeInstanceDAO.insert(new TimeInstance(false, "misc", new Date(),new Date(),false,true,true,false,true,false,true,TimeInstance.EnumInterval.ONEHOUR));
            return null;
        }
    }
}