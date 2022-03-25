package com.tutorials.testapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tutorials.testapp.database.TableContract.*;
import com.tutorials.testapp.user.EducationAge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final Integer DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        // user table 생성
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " +
                UserTable.TABLE_NAME + " ( " +
                UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserTable.COL_NAME + " TEXT, " +
                UserTable.COL_BIRTH + " TEXT, " +
                UserTable.COL_SEX + " TEXT, " +
                UserTable.COL_EDU + " TEXT" + ")";
        db.execSQL(SQL_CREATE_USER_TABLE);

        // education table 생성
        final String SQL_CREATE_EDU_TABLE = "CREATE TABLE " +
                EduAgeTable.TABLE_NAME + " ( " +
                EduAgeTable.COL_AGE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EduAgeTable.COL_ILLITERATE + " INTEGER, " +
                EduAgeTable.COL_UNEDUCATED + " INTEGER, " +
                EduAgeTable.COL_ELESCHOOL + " INTEGER, " +
                EduAgeTable.COL_SECSCHOOL + " INTEGER, " +
                EduAgeTable.COL_HIGHSCHOOL + " INTEGER, " +
                EduAgeTable.COL_UNIVERSITY + " INTEGER" + ")";
        db.execSQL(SQL_CREATE_EDU_TABLE);
        fillEduAgeTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EduAgeTable.TABLE_NAME);
        onCreate(db);
    }

    // 사용자 등록
    public long registerUser(String name, String birth, String sex, String education) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COL_NAME, name);
        values.put(UserTable.COL_BIRTH, birth);
        values.put(UserTable.COL_SEX, sex);
        values.put(UserTable.COL_EDU, education);

        long result = db.insert(UserTable.TABLE_NAME, null, values);

        return result;
    }

    // 사용자 삭제
    public Integer deleteUser(String name) {
        db = this. getWritableDatabase();
        return db.delete(UserTable.TABLE_NAME, "name=?", new String[] {name});
    }

    // 사용자 체크
    public boolean checkUser(String name, String birth) {
        db = this.getReadableDatabase();
        String [] columns = { UserTable._ID };
        String selection = UserTable.COL_NAME + "=?" + " and " + UserTable.COL_BIRTH + "=?";
        String [] selectionargs = { name, birth };
        Cursor cursor = db.query(UserTable.TABLE_NAME, columns, selection, selectionargs, null, null, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        if (count > 0)
            return true;
        else
            return false;
    }

    // 사용자 만나이 계산
    public int getAge(int year, int month, int day) {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - year;

        // 생일이 지나지 않은 경우 -1
        if(month * 100 + day > currentMonth * 100 + currentDay) {
            age--;
        }
        return age;
    }

    // 무학여부 나이 테이블 데이터
    private void fillEduAgeTable() {
        EducationAge fifty = new EducationAge(50, 0, 0, 22, 24, 26, 27);
        addEduAgeTable(fifty);
        EducationAge sixty = new EducationAge(60, 0, 16, 21, 23, 25, 26);
        addEduAgeTable(sixty);
        EducationAge seventy = new EducationAge(70, 13, 14, 19, 22, 22, 25);
        addEduAgeTable(seventy);
        EducationAge eighty = new EducationAge(80, 10, 11, 16, 18, 20, 22);
        addEduAgeTable(eighty);
    }

    // 무학여부 나이 테이블 데이터 추가
    private void addEduAgeTable(EducationAge educationAge) {
        ContentValues values  = new ContentValues();
        values.put(EduAgeTable.COL_AGE, educationAge.getAge());
        values.put(EduAgeTable.COL_ILLITERATE, educationAge.getIlliterate());
        values.put(EduAgeTable.COL_UNEDUCATED, educationAge.getUneducated());
        values.put(EduAgeTable.COL_ELESCHOOL, educationAge.getElementarySchool());
        values.put(EduAgeTable.COL_SECSCHOOL, educationAge.getSecondarySchool());
        values.put(EduAgeTable.COL_HIGHSCHOOL, educationAge.getHighSchool());
        values.put(EduAgeTable.COL_UNIVERSITY, educationAge.getUniversity());
        db.insert(EduAgeTable.TABLE_NAME, null, values);
    }

    // 무학여부 나이 테이블 데이터 가져오기
    public List<EducationAge> getAllEduTable() {
        List<EducationAge> eduAgeList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EduAgeTable.TABLE_NAME, null);

        if(cursor.moveToFirst()) {
            do {
                EducationAge educationAge = new EducationAge();
                educationAge.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(EduAgeTable.COL_AGE)));
                educationAge.setIlliterate(cursor.getInt(cursor.getColumnIndexOrThrow(EduAgeTable.COL_ILLITERATE)));
                educationAge.setUneducated(cursor.getInt(cursor.getColumnIndexOrThrow(EduAgeTable.COL_UNEDUCATED)));
                educationAge.setElementarySchool(cursor.getInt(cursor.getColumnIndexOrThrow(EduAgeTable.COL_ELESCHOOL)));
                educationAge.setSecondarySchool(cursor.getInt(cursor.getColumnIndexOrThrow(EduAgeTable.COL_SECSCHOOL)));
                educationAge.setHighSchool(cursor.getInt(cursor.getColumnIndexOrThrow(EduAgeTable.COL_HIGHSCHOOL)));
                educationAge.setUniversity(cursor.getInt(cursor.getColumnIndexOrThrow(EduAgeTable.COL_UNIVERSITY)));
                eduAgeList.add(educationAge);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return eduAgeList;
    }
}

