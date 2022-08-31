package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.cbnu.dementiadiagnosis.FirstActivity;
import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.PermissionSupport;
import com.cbnu.dementiadiagnosis.QuizHOME;
import com.cbnu.dementiadiagnosis.R;

import memoryQuiz.StartActivity;
import simpleTest.S_orientation;
import user.SharedPreference;

public class FragmentHome extends Fragment {

    Button simple, formal, quiz;
    AppCompatButton logout;
    ImageView helper_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        helper_img = view.findViewById(R.id.img);
        simple = view.findViewById(R.id.simpleTest);
        formal = view.findViewById(R.id.formalTest);
        quiz = view.findViewById(R.id.rememberQuiz);
        logout = view.findViewById(R.id.btnLogout);

        // 간이검사 시작
        simple.setOnClickListener(v -> {
            SharedPreference.setTypeInf(getActivity(), "simple");
            startActivity(new Intent(requireActivity(), S_orientation.class));
        });

        // 정규검사 시작
        formal.setOnClickListener(v -> {
            SharedPreference.setTypeInf(getActivity(), "regular");
            startActivity(new Intent(requireActivity(), QuizHOME.class));
        });

        // 기억력 향상 테스트
        quiz.setOnClickListener(v -> startActivity(new Intent(requireActivity(), StartActivity.class)));

        // 로그아웃
        logout.setOnClickListener(v -> {
            SharedPreference.clear_user(getActivity());
            startActivity(new Intent(requireActivity(), FirstActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}
