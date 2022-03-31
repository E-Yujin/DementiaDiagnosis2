package database;

import android.provider.BaseColumns;

public final class TableContract {

    private TableContract() {}

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "userTable";
        public static final String COL_NAME = "name";
        public static final String COL_BIRTH = "birth";
        public static final String COL_SEX = "sex";
        public static final String COL_EDU = "education";
    }

    public static class EduAgeTable implements BaseColumns {
        public static final String TABLE_NAME = "eduAgeTable";
        public static final String COL_AGE = "age"; // 나이
        public static final String COL_ILLITERATE = "illiterate"; // 비문해
        public static final String COL_UNEDUCATED= "uneducated"; // 무학
        public static final String COL_ELESCHOOL ="elementarySchool"; // 초등졸업
        public static final String COL_SECSCHOOL = "secondarySchool"; // 중등졸업
        public static final String COL_HIGHSCHOOL = "highSchool"; // 고등졸업
        public static final String COL_UNIVERSITY = "university"; // 대학졸업
    }
}
