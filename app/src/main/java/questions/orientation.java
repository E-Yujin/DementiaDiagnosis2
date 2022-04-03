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

    public String KorTran(String num){
        String tem = num.replace(" ", "")
                .replace("년", "")
                .replace("도", "")
                .replace("일", "")
                .replace("시월", "십월")
                .replace("유월", "육월")
                .replace("월", "");

        int result = 0;
        List<String> kor = new ArrayList<>();
        kor.add("일");
        kor.add("이");
        kor.add("삼");
        kor.add("사");
        kor.add("오");
        kor.add("육");
        kor.add("칠");
        kor.add("팔");
        kor.add("구");

        if(tem.contains("천")){
            int position = tem.indexOf("천");
            if(position != 0){
                String sub = tem.substring(position-1, position);
                for(String s : kor){
                    if(sub.contains(s)){
                        sub = KtoD(sub);
                        int sub_result = Integer.parseInt(sub)*1000;
                        result += sub_result;
                        break;
                    }
                }
                if(!Character.isDigit(sub.charAt(0))) result += 1000;
            }
            else result += 1000;
        }
        if(tem.contains("백")){
            int position = tem.indexOf("백");
            if(position != 0){
                String sub = tem.substring(position-1, position);
                for(String s : kor){
                    if(sub.contains(s)){
                        sub = KtoD(sub);
                        int sub_result = Integer.parseInt(sub)*100;
                        result += sub_result;
                        break;
                    }
                }
                if(!Character.isDigit(sub.charAt(0))) result += 100;
            }
            else result += 100;
        }
        if(tem.contains("십")){
            int position = tem.indexOf("십");
            if(position != 0){
                String sub = tem.substring(position-1, position);
                for(String s : kor){
                    if(sub.contains(s)){
                        sub = KtoD(sub);
                        int sub_result = Integer.parseInt(sub)*10;
                        result += sub_result;
                        break;
                    }
                }
                if(!Character.isDigit(sub.charAt(0))) result += 10;
            }
            else result += 10;
        }

        for(int i = tem.length() - 1; i >= 0; i--){
            String sub = tem.substring(i, i+1);
            for(String s : kor){
                if(sub.contains(s)){
                    sub = KtoD(sub);
                    result += Integer.parseInt(sub);
                    break;
                }
            }
            if(Character.isDigit(sub.charAt(0))) break;
        }
        int p1000 = tem.indexOf("천");
        int p100 = tem.indexOf("백");
        int p10 = tem.indexOf("십");

        if(p10 != -1 && p100 != -1) {
            if(p10 < p100) return "-1";
            else return Integer.toString(result);
        }
        else if(p10 != -1 && p1000 != -1) {
            if(p10 < p1000) return "-1";
            else return Integer.toString(result);
        }
        else if(p100 != -1 && p1000 != -1) {
            if(p100 < p1000) return "-1";
            else return Integer.toString(result);
        }
        else return Integer.toString(result);
    }

    public String KtoD(String num){
        switch (num){
            case "일":
                return "1";
            case "이":
                return "2";
            case "삼":
                return "3";
            case "사":
                return "4";
            case "오":
                return "5";
            case "육":
                return "6";
            case "칠":
                return "7";
            case "팔":
                return "8";
            case "구":
                return "9";
            default:
                return "";
        }
    }
}
