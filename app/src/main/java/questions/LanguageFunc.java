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
        this.user_ans = new String[this.num];
        this.scores = new int[10];
        this.Tscore = new Integer(0);

        this.quiz.add("아래 그림의 이름을 말씀해주세요\n");
        this.quiz.add("아래 그림의 이름을 말씀해주세요\n");
        this.quiz.add("아래 그림의 이름을 말씀해주세요\n");
        this.quiz.add("아래 5개의 동그라미를\n\n" +
                "1이라고 쓰여있는 공간에\n2개를 옮기고,\n\n" +
                "2이라고 쓰여있는 공간에\n3개를 옮기세요.");

        this.crr_ans[0].add("칫솔");
        this.crr_ans[1].add("그네");
        this.crr_ans[2].add("주사위");
        this.crr_ans[3].add("2");
        this.crr_ans[3].add("3");
    }
}
