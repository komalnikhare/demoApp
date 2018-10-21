package com.arrk.starwars.dbConfig.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.arrk.starwars.models.Character;

import java.util.List;
@Dao
public interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Character> characters);

    @Query("select * from character")
    LiveData<List<Character>> getCharacters();

    @Query("select * from character where id=:id")
    LiveData<Character> getCharacterById(int id);

    @Query("delete from character")
    void deleteAll();
}
