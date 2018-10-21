package com.arrk.starwars.dbConfig;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.arrk.starwars.dbConfig.dao.CharacterDao;
import com.arrk.starwars.models.Character;

@Database(entities = {Character.class}, version = 1,exportSchema = true)
abstract public class AppDatabase extends RoomDatabase {
    public static String DATABASE_NAME = "my_db";

    public abstract CharacterDao characterDao();

}
