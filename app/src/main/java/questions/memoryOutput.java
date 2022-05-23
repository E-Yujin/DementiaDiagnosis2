package questions;

import java.util.ArrayList;
import java.util.Arrays;

public class memoryOutput extends question {

    public memoryOutput(){
        this.num = 6;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for (int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.isDone = new boolean[this.num];
        this.score = new Integer(0);
        Arrays.fill(isDone, false);

        this.quiz.add("제가 조금 전에 외우라고 불러드렸던 문장을 다시 한번 말씀해 주세요.");
        this.quiz.add("제가 아까 어떤 사람의 이름을 말했는데 누구일까요?");
        this.quiz.add(("무엇을 타고 갔습니까?"));
        this.quiz.add("어디에 갔습니까?");
        this.quiz.add(("몇 시부터 했습니까?"));
        this.quiz.add(("무엇을 했습니까?"));

        this.crr_ans[0].add("민수");
        this.crr_ans[0].add("자전거");
        this.crr_ans[0].add("공원");
        this.crr_ans[0].add("11시");
        this.crr_ans[0].add("야구");
        this.crr_ans[1].add("민수");
        this.crr_ans[2].add("자전거");
        this.crr_ans[3].add("공원");
        this.crr_ans[4].add("11시");
        this.crr_ans[5].add("야구");
    }
}
