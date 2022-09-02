package questions;

import java.util.ArrayList;

public class MemoryQuiz extends question{

    public MemoryQuiz() {
        this.num = 6;
        this.quiz = new ArrayList<>();
        this.crr_ans = new ArrayList[this.num];
        for(int i = 0; i < this.num; i++) {
            this.crr_ans[i] = new ArrayList<>();
        }
        this.user_ans = new String[this.num];

        this.quiz.add("초성에 해당하는\n단어 3개를 말씀해주세요");
        this.quiz.add("초성에 해당하는\n단어 3개를 말씀해주세요");
        this.quiz.add("초성에 해당하는\n단어 3개를 말씀해주세요");
        this.quiz.add("초성에 해당하는\n단어 3개를 말씀해주세요");
        this.quiz.add("초성에 해당하는\n단어 3개를 말씀해주세요");
        this.quiz.add("초성에 해당하는\n단어 3개를 말씀해주세요");
    }
}
