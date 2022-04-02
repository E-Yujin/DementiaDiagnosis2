package questions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class orientation extends question{
    private String Date[];
    private String day;

    public orientation(){

        this.num = 5;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for (int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.isDone = new boolean[this.num];
        this.score = new Integer(0);
        Arrays.fill(isDone, false);

        long getDate = System.currentTimeMillis();
        Date DateData = new Date(getDate);
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp = dFormat.format(DateData);
        Date = temp.split("-");

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateData);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }

        this.quiz.add("올해는 몇 년도입니까?");
        this.quiz.add("지금은 몇 월입니까?");
        this.quiz.add("오늘은 며칠입니까?");
        this.quiz.add("오늘은 무슨 요일입니까?");
        this.quiz.add("지금 있는 곳은 어디입니까?");

        this.crr_ans[0].add(Date[0]);
        this.crr_ans[1].add(returnMONTH());
        this.crr_ans[2].add(returnDATE());
        this.crr_ans[3].add(day);
        this.crr_ans[4].add("미정");

    }



    public String returnYEAR(){
        return Date[0];
    }

    public String returnMONTH(){
        if(Date[1].indexOf("0") == 0) return Date[1].replace("0", "");
        else return Date[1];
    }

    public String returnDATE(){
        if(Date[2].indexOf("0") == 0) return Date[2].replace("0", "");
        else return Date[2];
    }

    public String returnDAY(){
        return day;
    }
}
