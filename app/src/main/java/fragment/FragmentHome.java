package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cbnu.dementiadiagnosis.FirstActivity;
import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.QuizHOME;
import com.cbnu.dementiadiagnosis.R;

import java.util.Objects;

import simpleTest.S_orientation;
import user.SharedPreference;

public class FragmentHome extends Fragment {

    Button simple, formal, logout;
    ImageView helper_img;
    Helper helper;

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
        logout = view.findViewById(R.id.btnLogout);
        helper = new Helper(helper_img, this);

        helper.setNomal();

        // 간이검사 시작
        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity().getApplication(), S_orientation.class);
                startActivity(intent);
            }
        });

        // 정규검사 시작
        formal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "regular";
                Intent intent = new Intent(requireActivity().getApplication(), QuizHOME.class);
                intent.putExtra("regular_type", type);
                Log.d("this is regular_type", type);
                startActivity(intent);
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
