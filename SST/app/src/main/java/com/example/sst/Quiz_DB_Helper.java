package com.example.sst;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Quiz_DB_Helper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Quiz.db";
    private static final String TABLE_NAME = "Quiz_list";
    private static final String COL_1 = "num";
    private static final String COL_2 = "announce";
    private static final String COL_3 = "question";
    private static final String COL_4 = "correct_answer";

    public Quiz_DB_Helper(@Nullable Context context, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "(NUM INTEGER PRIMARY KEY AUTOINCREMENT, ANNOUNCE STRING, QUEST STRING, " +
                "CORR_ANS STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 사용자 등록
    public long registerUser(String announce, String quest, String corr_ans) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, announce);
        values.put(COL_3, quest);
        values.put(COL_4, corr_ans);

        long val = db.insert(TABLE_NAME, null, values);
        return val;
    }

}
