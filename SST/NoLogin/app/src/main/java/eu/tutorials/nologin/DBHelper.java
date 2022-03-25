package eu.tutorials.nologin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final String TABLE_NAME = "user_list";
    private static final String COL_1 = "num";
    private static final String COL_2 = "name";
    private static final String COL_3 = "birth";
    private static final String COL_4 = "sex";
    private static final String COL_5 = "education";

    public DBHelper(@Nullable Context context, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "(NUM INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, BIRTH TEXT, SEX TEXT, EDUCATION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 사용자 등록
    public long registerUser(String name, String birth, String sex, String education) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, name);
        values.put(COL_3, birth);
        values.put(COL_4, sex);
        values.put(COL_5, education);

        long result = db.insert(TABLE_NAME, null, values);

        return result;
    }
    // 사용자 삭제
    public Integer deleteUser(String name) {
        SQLiteDatabase db = this. getWritableDatabase();
        return db.delete(TABLE_NAME, "name=?", new String[] {name});
    }
}
