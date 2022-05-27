package questions;

import java.util.ArrayList;
import java.util.Arrays;

public class LanguageFunc extends question{

    public LanguageFunc() {
        this.num = 4;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for(int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.isDone = new boolean[this.num];
        this.score = 0;
        Arrays.fill(isDone, false);

        this.quiz.add("여기 있는 그림의 이름을 말씀해주세요\n");
        this.quiz.add("여기 있는 그림의 이름을 말씀해주세요\n");
        this.quiz.add("여기 있는 그림의 이름을 말씀해주세요\n");
        this.quiz.add("여기 있는 그림의 이름을 말씀해주세요\n");

        this.crr_ans[0].add("칫솔");
        this.crr_ans[1].add("그네");
        this.crr_ans[2].add("주사위");
        this.crr_ans[3].add("오른쪽");
    }
}
