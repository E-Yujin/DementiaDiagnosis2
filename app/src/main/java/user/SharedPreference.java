package user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference {
    static String PREFERENCES_NAME = "pref_user";

    static String pref_user_name = "name";
    static String pref_user_birth = "birth";
    static String pref_user_sex = "sex";
    static String pref_user_edu = "edu";

    static public SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 사용자 정보 저장
    public static void setUserInf(Context ctx, String name, String birth, String sex, String edu) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(pref_user_name, name);
        editor.putString(pref_user_birth, birth);
        editor.putString(pref_user_sex, sex);
        editor.putString(pref_user_edu, edu);
        editor.commit();
    }

    // 저장된 사용자 이름 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_name, "");
    }
    // 저장된 사용자 생년월일 가져오기
    public static String getUserBirth(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_birth, "");
    }
    // 저장된 사용자 성별 가져오기
    public static String getUserSex(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_sex, "");
    }
    // 저장된 사용자 학력 가져오기
    public static String getUserEdu(Context ctx) {
        return getSharedPreferences(ctx).getString(pref_user_edu, "");
    }

    //로그아웃 시 데이터 삭제
    public static void clear_user(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
