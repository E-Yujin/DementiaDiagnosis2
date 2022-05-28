package questions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class spaceTime extends question {

    public spaceTime() {

        this.num = 4;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for (int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.isDone = new boolean[this.num];
        this.score = new Integer(0);
        Arrays.fill(isDone, false);

        this.quiz.add("여기 점을 연결하여 그린 그림이 있습니다.");
        this.quiz.add("이 그림과 똑같이 되도록");
        this.quiz.add("같은 위치에 그려보세요.");
        this.quiz.add("점을 연결해서 그리시면 됩니다.");
    }
}
