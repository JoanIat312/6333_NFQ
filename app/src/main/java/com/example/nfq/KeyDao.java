package com.example.nfq;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface KeyDao {

    @Query("SELECT * FROM keys")
    List<Key> getAll();

    @Insert
    void insertAll(Key key);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void update(Key key);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void delete(Key key);

    /*
     * delete list of objects from database
     * @param note, array of objects to be deleted
     */
    @Delete
    void delete(Key... key);
}
