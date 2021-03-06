package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cbnu.dementiadiagnosis.FirstActivity;
import com.cbnu.dementiadiagnosis.HomeActivity;
import com.cbnu.dementiadiagnosis.QuizHOME;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.SimpleTestActivity;

import user.SharedPreference;

public class FragmentHome extends Fragment {

    Button simple, formal, logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        simple = view.findViewById(R.id.simpleTest);
        formal = view.findViewById(R.id.formalTest);
        logout = view.findViewById(R.id.btnLogout);

        // 간이검사 시작
        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SimpleTestActivity.class));
            }
        });

        // 정규검사 시작
        formal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QuizHOME.class));
            }
        });

        // 로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.clear_user(getActivity());
                startActivity(new Intent(getActivity(), FirstActivity.class));
            }
        });

        return view;
    }
}
