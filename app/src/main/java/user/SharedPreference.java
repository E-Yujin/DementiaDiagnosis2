package user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference {
    static String PREFERENCES_NAME = "pref_user";

    static String pref_user_serial_code = "serial_code";
    static String pref_user_name = "name";
    static String pref_user_birth = "birth";
    static String pref_user_age = "age";
    static String pref_user_sex = "sex";
    static String pref_user_edu = "edu";
    static String pref_user_score = "score";
    static String pref_type = "type";

    static public SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 사용자 정보 저장
    public static void setUserInf(Context ctx, String serial_code, String name, String birth, int age,
                                  String sex, String edu, int score) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(pref_user_serial_code, serial_code);
        editor.putString(pref_user_name, name);
        editor.putString(pref_user_birth, birth);
        editor.putInt(pref_user_age, age);
        editor.putString(pref_user_sex, sex);
        editor.putString(pref_user_edu, edu);
        editor.putInt(pref_user_score, score);
        editor.commit();
    }
    // 사용자 일련코드 정보 저장
    public static void setSerialCodeInf(Context ctx, String serial_code) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(pref_user_serial_code, serial_code);
        editor.apply();
    }
    // 검사 타입 정보 저장
    public static void setTypeInf(Context ctx, String type) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(pref_type, type);
        editor.apply();
    }

    // 저장된 사용자 일련코드 가져오기
    public static String getSerialCode(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_serial_code, "");
    }
    // 저장된 사용자 이름 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_name, "");
    }
    // 저장된 사용자 생년월일 가져오기
    public static String getUserBirth(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_birth, "");
    }
    // 사용자 만 나이
    public static int getUserAge(Context ctx) {
        return getSharedPreferences(ctx).getInt(pref_user_age, 0);
    }
    // 저장된 사용자 성별 가져오기
    public static String getUserSex(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_sex, "");
    }
    // 저장된 사용자 학력 가져오기
    public static String getUserEdu(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_edu, "");
    }
    // 저장된 사용자 치매 기준 점수 가져오기
    public static int getUserScore(Context ctx) {
        return getSharedPreferences(ctx).getInt(pref_user_score, 0);
    }
    // 검사 타입 가져오기
    public static String getSelectType(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_type, "");
    }

    //로그아웃 시 데이터 삭제
    public static void clear_user(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
