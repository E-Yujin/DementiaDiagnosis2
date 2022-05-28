package questions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class orientation extends question {
    private String Date[];
    private String day;

    public orientation() {

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


    public String returnYEAR() {
        return Date[0];
    }

    public String returnMONTH() {
        if (Date[1].indexOf("0") == 0) return Date[1].replace("0", "");
        else return Date[1];
    }

    public String returnDATE() {
        if (Date[2].indexOf("0") == 0) return Date[2].replace("0", "");
        else return Date[2];
    }

    public String returnDAY() {
        return day;
    }

    public String KorTran(String num) {
        String input = num.replace("년", "")
                .replace("도", "")
                .replace("일", "")
                .replace("시월", "십월")
                .replace("유월", "육월")
                .replace("월", "");

        String splited[] = input.split(" ");

        StringTokenizer st;
        String token = "";
        int check;
        long result = 0;
        long tmpResult = 0;
        long number = 0;
        long total = 0;
        boolean have_to_num = false;
        boolean have_to_unit = false;
        final String NUMBER = "영일이삼사오육칠팔구";
        final String UNIT = "십백천만억조";
        final long[] UNIT_NUM = {10, 100, 1000, 10000, (long) Math.pow(10, 8), (long) Math.pow(10, 12)};
        for (String split : splited) {
            st = new StringTokenizer(split, UNIT, true);
            StringTokenizer tem = new StringTokenizer(split, UNIT, true);
            String tok = tem.nextToken();
            if (NUMBER.indexOf(tok) != -1 || (UNIT.indexOf(tok) != -1 && split.length() == 1)) {
                if (have_to_num && NUMBER.indexOf(tok) == -1) continue;
                if (have_to_unit && UNIT.indexOf(tok) == -1) continue;
                if (tem.hasMoreTokens()) {
                    tok = tem.nextToken();
                }
                if (!(split.length() == 1 && NUMBER.indexOf(tok) != -1)) { //한 글자의 숫자가 아니면 실행
                    while (st.hasMoreTokens()) {
                        token = st.nextToken();
                        check = NUMBER.indexOf(token);
                        //숫자인지, 단위(UNIT)인지 확인
                        if (check == -1) { //단위인 경우
                            if ("만억조".indexOf(token) == -1) {
                                tmpResult += (number != 0 ? number : 1) * UNIT_NUM[UNIT.indexOf(token)];
                            } else {
                                tmpResult += number;
                                result += (tmpResult != 0 ? tmpResult : 1) * UNIT_NUM[UNIT.indexOf(token)];
                                tmpResult = 0;
                            }
                            number = 0;
                        } else { //숫자인 경우
                            number = check;
                        }
                    }
                    total = result + tmpResult + number;
                    if (NUMBER.indexOf(token) != -1) {
                        have_to_unit = true;
                        have_to_num = false;
                    } else if (UNIT.indexOf(token) != -1) {
                        have_to_num = true;
                        have_to_unit = false;
                    }
                } else { // 한 글자의 숫자인 경우 실행
                    while (st.hasMoreTokens()) {
                        token = st.nextToken();
                        check = NUMBER.indexOf(token);
                        number = check;
                    }
                    total += number;
                    have_to_unit = true;
                    have_to_num = false;
                }
            }
        }
        return String.valueOf(total);
    }
}
