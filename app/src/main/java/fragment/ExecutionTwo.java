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


public class ExecutionTwo extends Fragment {

    ImageButton squareOne, squareTwo, squareThree, squareFour;
    ImageView star1, star2, star3, star4;
    String check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;

        view = inflater.inflate(R.layout.execution_star, container, false);
        squareOne = view.findViewById(R.id.btnOne);
        squareTwo = view.findViewById(R.id.btnTwo);
        squareThree = view.findViewById(R.id.btnThree);
        squareFour = view.findViewById(R.id.btnFour);

        star1 = view.findViewById(R.id.starOne);
        star2 = view.findViewById(R.id.starTwo);
        star3 = view.findViewById(R.id.starThree);
        star4 = view.findViewById(R.id.starFour);
        showStar();

        return view;
    }

    // 정답 영역 클릭 시, 별 모양 표시
    public void showStar() {
        squareOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "1";
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
            }
        });
        squareTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "2";
                star2.setVisibility(View.VISIBLE);
                star1.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
            }
        });
        squareThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "3";
                star3.setVisibility(View.VISIBLE);
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
            }
        });
        squareFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "4";
                star4.setVisibility(View.VISIBLE);
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
            }
        });
    }
    public String twoResult() {
        return check;
    }
}
