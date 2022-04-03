package questions;

import java.util.ArrayList;
import java.util.Arrays;

public class attention extends question{
    public attention(){

        this.num = 3;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for (int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.isDone = new boolean[this.num];
        this.score = new Integer(0);
        Arrays.fill(isDone, false);

        this.quiz.add("제가 불러드리는 숫자를\n그대로 따라 해 주세요.");
        this.quiz.add("한 번 더 제가 불러드리는 숫자를\n그대로 따라 해 주시면 됩니다.");
        this.quiz.add("제가 불러드리는 말을\n끝에서부터 거꾸로 따라 해 주세요.");

        this.crr_ans[0].add("6973");
        this.crr_ans[0].add("육구칠삼");
        this.crr_ans[1].add("57284");
        this.crr_ans[1].add("오칠이팔사");
        this.crr_ans[2].add("산강수금");

    }
}
