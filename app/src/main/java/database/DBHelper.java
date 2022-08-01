package database;

import static user.SharedPreference.getSharedPreferences;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import database.TableContract.*;
import user.EducationAge;
import user.Score;
import user.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final Integer DATABASE_VERSION = 12;

    private SQLiteDatabase db;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        // user table 생성
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " +
                UserTable.TABLE_NAME + "(" +
                UserTable.COL_SERIAL_CODE + " TEXT PRIMARY KEY, " +
                UserTable.COL_NAME + " TEXT, " +
                UserTable.COL_BIRTH + " TEXT, " +
                UserTable.COL_AGE + " INTEGER, " +
                UserTable.COL_SEX + " TEXT, " +
                UserTable.COL_EDU + " TEXT, " +
                UserTable.COL_SCORE + " INTEGER" + ")";
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

        // score table 생성
        final String SQL_CREATE_SCORE_TABLE = "CREATE TABLE " +
                ScoreTable.TABLE_NAME + "(" +
                ScoreTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScoreTable.COL_ORIENTATION + " INTEGER, " +
                ScoreTable.COL_MEMORY + " INTEGER, " +
                ScoreTable.COL_ATTENTION + " INTEGER, " +
                ScoreTable.COL_SPACETIME + " INTEGER, " +
                ScoreTable.COL_EXECUTION + " INTEGER, " +
                ScoreTable.COL_LANGUAGE + " INTEGER, " +
                ScoreTable.COL_TOTAL + " INTEGER, " +
                ScoreTable.COL_DATE + " TEXT, " +
                ScoreTable.COL_SERIAL_CODE + " TEXT, FOREIGN KEY" + "(" + ScoreTable.COL_SERIAL_CODE + ")" + " REFERENCES " + UserTable.TABLE_NAME +
                "(" + UserTable.COL_SERIAL_CODE + "))";
        db.execSQL(SQL_CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EduAgeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScoreTable.TABLE_NAME);
        onCreate(db);
    }

    // 사용자 등록
    public long registerUser(String serialCode, String name, String birth, int age, String sex, String education, int score) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COL_SERIAL_CODE, serialCode);
        values.put(UserTable.COL_NAME, name);
        values.put(UserTable.COL_BIRTH, birth);
        values.put(UserTable.COL_AGE, age);
        values.put(UserTable.COL_SEX, sex);
        values.put(UserTable.COL_EDU, education);
        values.put(UserTable.COL_SCORE, score);

        long result = db.insert(UserTable.TABLE_NAME, null, values);
        return result;
    }

    // 사용자 삭제
    public Integer deleteUser(String serial_code) {
        db = this.getWritableDatabase();
        return db.delete(UserTable.TABLE_NAME, "serialCode=?", new String[] {serial_code});
    }

    // 사용자 진단 기록 삭제
    public Integer deleteScore(String serial_code) {
        db = this.getWritableDatabase();
        return db.delete(ScoreTable.TABLE_NAME, "serialCode=?", new String[] {serial_code});
    }

    // 사용자 정보 수정
    public long updateData(String serial_code, String name, String birth,
                           int age, String sex, String edu, int score) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COL_NAME, name);
        values.put(UserTable.COL_BIRTH, birth);
        values.put(UserTable.COL_AGE, age);
        values.put(UserTable.COL_SEX, sex);
        values.put(UserTable.COL_EDU, edu);
        values.put(UserTable.COL_SCORE, score);

        long result = db.update(UserTable.TABLE_NAME, values, "serialCode=?", new String[]{ serial_code });
        return result;
    }

    // 사용자 체크
    public boolean checkUser(String name, String birth, String sex, String edu) {
        db = this.getReadableDatabase();
        String [] columns = { UserTable.COL_SERIAL_CODE };
        String selection = UserTable.COL_NAME + "=?" + " and " + UserTable.COL_BIRTH + "=?" + " and " + UserTable.COL_SEX + "=?" + " and " + UserTable.COL_EDU + "=?";
        String [] selectionargs = { name, birth, sex, edu };
        Cursor cursor = db.query(UserTable.TABLE_NAME, columns, selection, selectionargs, null, null, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        if (count > 0)
            return true;
        else
            return false;
    }

    // 사용자 serial_code 저장하기
    public String saveSerialCode(String name, String birth, String sex, String edu) {
        db = this.getReadableDatabase();
        String [] columns = { UserTable.COL_SERIAL_CODE };
        String selection = UserTable.COL_NAME + "=?" + " and " + UserTable.COL_BIRTH + "=?" + " and " + UserTable.COL_SEX + "=?" + " and " + UserTable.COL_EDU + "=?";
        String [] selectionargs = { name, birth, sex, edu };
        Cursor cursor = db.query(UserTable.TABLE_NAME, columns, selection, selectionargs, null, null, null);
        cursor.moveToFirst();
        String result = cursor.getString(0);
        db.close();
        cursor.close();

        return result;
    }

    // 무학여부 나이 테이블 데이터
    private void fillEduAgeTable() {
        // 문맹, 무학, 초등, 중등, 고등, 대학
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

    // 점수 테이블 데이터 추가
    public void scoreAdd(String serialCode, String date, int orientation, int memory, int attention, int spacetime, int execution, int language, int total) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScoreTable.COL_SERIAL_CODE, serialCode);
        values.put(ScoreTable.COL_DATE, date);
        values.put(ScoreTable.COL_ORIENTATION, orientation);
        values.put(ScoreTable.COL_MEMORY, memory);
        values.put(ScoreTable.COL_ATTENTION, attention);
        values.put(ScoreTable.COL_SPACETIME, spacetime);
        values.put(ScoreTable.COL_EXECUTION, execution);
        values.put(ScoreTable.COL_LANGUAGE, language);
        values.put(ScoreTable.COL_TOTAL, total);

        long result = db.insert(ScoreTable.TABLE_NAME, null, values);
    }

    // 진단 결과 점수 출력
    public List<Score> getScoreList(String serial_code) {
        List<Score> scoreList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ScoreTable.TABLE_NAME +  " WHERE "+ ScoreTable.COL_SERIAL_CODE + "=?", new String[] {serial_code});

        if(cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setSerial_code(cursor.getString(cursor.getColumnIndexOrThrow(ScoreTable.COL_SERIAL_CODE)));
                score.setDate(cursor.getString(cursor.getColumnIndexOrThrow(ScoreTable.COL_DATE)));
                score.setOrientation(cursor.getInt(cursor.getColumnIndexOrThrow(ScoreTable.COL_ORIENTATION)));
                score.setMemory(cursor.getInt(cursor.getColumnIndexOrThrow(ScoreTable.COL_MEMORY)));
                score.setAttention(cursor.getInt(cursor.getColumnIndexOrThrow(ScoreTable.COL_ATTENTION)));
                score.setSpacetime(cursor.getInt(cursor.getColumnIndexOrThrow(ScoreTable.COL_SPACETIME)));
                score.setExecution(cursor.getInt(cursor.getColumnIndexOrThrow(ScoreTable.COL_EXECUTION)));
                score.setLanguage(cursor.getInt(cursor.getColumnIndexOrThrow(ScoreTable.COL_LANGUAGE)));
                score.setTotal(cursor.getInt(cursor.getColumnIndexOrThrow(ScoreTable.COL_TOTAL)));
                scoreList.add(score);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return scoreList;
    }

    // 같은 날짜의 진단 기록이 있는지 체크
    public boolean checkScore(String serial_code, String date) {
        db = getReadableDatabase();
        String [] columns = { ScoreTable.COL_SERIAL_CODE };
        String selection = ScoreTable.COL_SERIAL_CODE + "=?" + " and " + ScoreTable.COL_DATE + "=?";
        String [] selectionargs = { serial_code, date };
        Cursor cursor = db.query(ScoreTable.TABLE_NAME, columns, selection, selectionargs, null, null, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count > 0;
    }

    // 같은 날짜의 진단 기록이 있는 경우, 이후의 진단 결과로 점수 갱신
    public void changeScore(String serial_code, String date, int[] score) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScoreTable.COL_ORIENTATION, score[1]);
        values.put(ScoreTable.COL_MEMORY, score[6]);
        values.put(ScoreTable.COL_ATTENTION, score[3]);
        values.put(ScoreTable.COL_SPACETIME, score[4]);
        values.put(ScoreTable.COL_EXECUTION, score[5] + score[8]);
        values.put(ScoreTable.COL_LANGUAGE, score[7]);
        values.put(ScoreTable.COL_TOTAL, score[1] + score[3] + score[4] + score[5] + score[6] + score[7] + score[8]);

        long result = db.update(ScoreTable.TABLE_NAME, values, "serialCode=?" + " and " + "date=?", new String[]{ serial_code , date });
    }

    // 사용자 정보 출력
    public User getUserInf(String serial_code) {
        User user = new User();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + ScoreTable.COL_SERIAL_CODE + "=?", new String[] {serial_code});

        if(cursor.moveToFirst()) {
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_NAME)));
            user.setBirth(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_BIRTH)));
            user.setSex(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_SEX)));
            user.setEdu(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_EDU)));
        }
        cursor.close();
        return user;
    }
}

