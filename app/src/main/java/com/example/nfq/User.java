package com.example.nfq;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username" )
    @NonNull
    private String userName;

    @ColumnInfo(name = "password")
    @NonNull private String userPwd;

    public User(String userName, String userPwd){
        super();
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public String getUserName(){
        return userName;
    }

    public  void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserPwd()
    {
        return userPwd;
    }

    public void setUserPwd(String userPwd){
        this.userPwd = userPwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
