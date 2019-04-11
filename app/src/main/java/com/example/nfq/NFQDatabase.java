package com.example.nfq;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {Note.class, Key.class, Question.class}, version = 3, exportSchema = false)
public abstract  class NFQDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();
    public abstract  KeyDao getKeyDao();
    public abstract  QuestionDao getQuestionDao();

    private static NFQDatabase nfqDB;

    public static NFQDatabase getInstance(Context context){
        if(null == nfqDB){
            nfqDB = buildDatabaseINstance(context);
        }

        return nfqDB;
    }

    private static NFQDatabase buildDatabaseINstance(Context context){
        return  Room.databaseBuilder(context, NFQDatabase.class, "nfqDB")
                .addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3).allowMainThreadQueries().build();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'Keys' ('id' INTEGER NOT NULL, 'keyword' TEXT, 'definition' TEXT, 'note_id' INTEGER NOT NULL, PRIMARY KEY('id'))");
        }

    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3){
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'questions' ('id' INTEGER NOT NULL, 'note_id' INTEGER NOT NULL, 'question' TEXT, " +
                    "'option1' TEXT, 'option2' TEXT, 'option3' TEXT, 'answerNr' INTEGER NOT NULL, PRIMARY KEY('id'))");
        }
    };


    public void cleanUp(){
        nfqDB = null;
    }
}
