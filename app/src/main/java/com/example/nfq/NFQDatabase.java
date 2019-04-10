package com.example.nfq;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract  class NFQDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();

    private static NFQDatabase nfqDB;

    public static NFQDatabase getInstance(Context context){
        if(null == nfqDB){
            nfqDB = buildDatabaseINstance(context);
        }

        return nfqDB;
    }

    private static NFQDatabase buildDatabaseINstance(Context context){
        return Room.databaseBuilder(context, NFQDatabase.class, "nfqDB").allowMainThreadQueries().build();
    }
    public void cleanUp(){
        nfqDB = null;
    }
}
