package com.arrk.starwars.dbConfig;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.arrk.starwars.models.Character;

import java.util.List;

public class DatabaseCreator {
    private static DatabaseCreator sInstance;
    private AppDatabase appDatabase;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    public synchronized static DatabaseCreator getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new DatabaseCreator(context);
                }
            }
        }
        return sInstance;
    }

    private DatabaseCreator(){}
    private DatabaseCreator(Context context){
        appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME).build();

    }

    public void insertAll(final List<Character> characters){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    appDatabase.characterDao().insertAll(characters);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void deleteAll(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.characterDao().deleteAll();
                return null;
            }

        }.execute();
    }

    public LiveData<Character> getCharacterById(final int id){
        return appDatabase.characterDao().getCharacterById(id);
    }

    public LiveData<List<Character>> getCharacters(){
        return appDatabase.characterDao().getCharacters();
    }
}
