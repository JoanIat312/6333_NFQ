package com.example.nfq;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    List<User> getByUserId(int id);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    List<User> getByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User getUser(String username, String password);

    @Insert
    void insertAll(User user);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void update(User user);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void delete(User user);

    /*
     * delete list of objects from database
     * @param note, array of objects to be deleted
     */
    @Delete
    void delete(User... user);

}
