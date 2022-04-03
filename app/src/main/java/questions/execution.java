package questions;

import java.util.ArrayList;
import java.util.Arrays;

public class execution extends question{

    public execution(){

        this.num = 3;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for (int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.isDone = new boolean[this.num];
        this.score = new Integer(0);
        Arrays.fill(isDone, false);

        this.quiz.add("여기 모양들이 정해진 순서로 나옵니다.\n모양들을 보면서 어떤 순서로 나오는지 생각해 보세요.");
        this.quiz.add("여기 네 칸 중의 한 칸에 별이 하나 있습니다.");
        this.quiz.add("카드에 숫자와 계절이 하나씩 적혀 있습니다.");

        this.crr_ans[0].add("동그라미");
        this.crr_ans[0].add("원");
        this.crr_ans[1].add("");
        this.crr_ans[2].add("4");
        this.crr_ans[2].add("여름");
    }
}
