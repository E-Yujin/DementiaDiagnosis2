package database;

import android.provider.BaseColumns;

public final class TableContract {

    private TableContract() {}

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "userTable";
        public static final String COL_SERIAL_CODE = "serialCode";
        public static final String COL_NAME = "name";
        public static final String COL_BIRTH = "birth";
        public static final String COL_AGE = "age"; // 만나이
        public static final String COL_SEX = "sex";
        public static final String COL_EDU = "education";
        public static final String COL_SCORE = "score";
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

    public static class ScoreTable implements BaseColumns {
        public static final String TABLE_NAME = "scoreTable";
        public static final String COL_SERIAL_CODE = "serialCode"; // 일련코드
        public static final String COL_DATE = "date"; // 날짜
        public static final String COL_ORIENTATION = "orientation"; // 지남력
        public static final String COL_MEMORY = "memory"; // 기억력
        public static final String COL_ATTENTION = "attention"; // 주의력
        public static final String COL_SPACETIME = "spacetime"; // 시공간기능
        public static final String COL_EXECUTION = "execution"; // 집행기능
        public static final String COL_LANGUAGE = "language"; // 언어기능
        public static final String COL_TOTAL = "total"; // 총 합계
    }
}
