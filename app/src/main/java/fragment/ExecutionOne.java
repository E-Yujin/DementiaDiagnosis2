package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cbnu.dementiadiagnosis.R;

public class ExecutionOne extends Fragment {

    ImageView result; // 정답 도형이 들어가는 뷰
    ImageButton resSquare, resTriangle, resCircle; // 도형 정답 버튼
    String check = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.execution_shape, container, false);

        result = view.findViewById(R.id.box);
        resSquare = view.findViewById(R.id.btnSquare);
        resTriangle = view.findViewById(R.id.btnTriangle);
        resCircle = view.findViewById(R.id.btnCircle);

        clickShape();
        return view;
    }

    // 정답 선택 함수
    public void clickShape() {
        // 사각형을 선택한 경우
        resSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "사각형";
                result.setImageResource(R.drawable.res_square);
            }
        });
        // 삼각형을 선택한 경우
        resTriangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "삼각형";
                result.setImageResource(R.drawable.res_triangle);
            }
        });
        // 원을 선택한 경우
        resCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "원";
                result.setImageResource(R.drawable.res_circle);
            }
        });
    }
    // 답 전달 함수
    public String oneResult() {
        return check;
    }
}

