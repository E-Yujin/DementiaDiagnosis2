package questions;

import java.util.ArrayList;
import java.util.Arrays;

public class Execution extends question{

    public Execution() {
        this.num = 3;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for(int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.user_ans = new String[this.num];
        this.scores = new int[10];
        this.Tscore = new Integer(0);

        this.quiz.add("여기 모양들이 정해진 순서로 나옵니다.\n");
        this.quiz.add("여기 네 칸 중의 한 칸에\n별이 하나 있습니다.\n");
        this.quiz.add("카드에 숫자와 계절이 하나씩 적혀 있습니다.\n");

        this.crr_ans[0].add("원");
        this.crr_ans[1].add("3");
        this.crr_ans[2].add("사 여름");
        this.crr_ans[2].add("4 여름");
    }
}
