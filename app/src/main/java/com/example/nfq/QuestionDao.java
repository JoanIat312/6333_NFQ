package com.example.nfq;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM questions")
    List<Question> getAll();

    @Query("SELECT * FROM notes WHERE id =:id")
    public Note getById(int id);


    @Insert
    void insertAll(Question... question);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void update(Question question);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void delete(Question question);

    /*
     * delete list of objects from database
     * @param note, array of objects to be deleted
     */
    @Delete
    void delete(Question... question);

}
