package questions;

import java.util.ArrayList;
import java.util.Arrays;

public class memoryInput extends question {

    public memoryInput(){
        this.num = 5;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for (int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.isDone = new boolean[this.num];
        this.score = new Integer(0);
        Arrays.fill(isDone, false);

        this.quiz.add("지금부터외우셔야 하는\n문장 하나를 불러드리겠습니다.\n" +
                "끝까지 잘 듣고 따라 해 보세요.");
        this.quiz.add("잘 하셨습니다.\n제가 다시 한번 불러드리겠습니다.\n" +
                "이번에도 다시 여쭈어 볼테니\n잘 듣고 따라 해 보세요.");
        this.quiz.add(("제가 이 문장을 나중에 여쭤보겠습니다.\n" +
                "잘 기억하세요."));
        this.quiz.add("잘 하셨습니다. 제가 다시 한번 불러드리겠습니다.\n" +
                "이번에도 다시 여쭈어 볼테니 잘 듣고 따라 해 보세요.");
        this.quiz.add(("제가 이 문장을 나중에 여쭤보겠습니다.\n" +
                "잘 기억하세요."));

        this.crr_ans[0].add("민수");
        this.crr_ans[1].add("자전거");
        this.crr_ans[2].add("공원");
        this.crr_ans[3].add("11시");
        this.crr_ans[4].add("야구");
    }
}
