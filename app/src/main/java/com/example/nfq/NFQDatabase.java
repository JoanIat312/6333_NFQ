package com.example.nfq;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Note.class, Key.class, User.class}, version = 4, exportSchema = false)
public abstract  class NFQDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();
    public abstract  KeyDao getKeyDao();
    public abstract  UserDao getUserDao();

    private static NFQDatabase nfqDB;

    public static NFQDatabase getInstance(Context context){
        if(null == nfqDB){
            nfqDB = buildDatabaseINstance(context);
        }

        return nfqDB;
    }

    private static NFQDatabase buildDatabaseINstance(Context context){
        return  Room.databaseBuilder(context, NFQDatabase.class, "nfqDB")
                .addMigrations(MIGRATION_1_2).addMigrations(Migration_2_3).addMigrations(MIGRATION_3_4).allowMainThreadQueries().build();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'Keys' ('id' INTEGER NOT NULL, 'keyword' TEXT, 'definition' TEXT, 'note_id' INTEGER NOT NULL, PRIMARY KEY('id'))");
        }

    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4){
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'Notes' ADD COLUMN 'user_id' INTEGER NOT NULL DEFAULT -1");
        }
    };

    static final Migration Migration_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'Users' ('id' INTEGER NOT NULL, 'username' TEXT NOT NULL, 'password' TEXT NOT NULL, PRIMARY KEY('id'))");
        }
    };


    public void cleanUp(){
        nfqDB = null;
    }
}
