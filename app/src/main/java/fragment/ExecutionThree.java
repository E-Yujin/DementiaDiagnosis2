package fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cbnu.dementiadiagnosis.R;


public class ExecutionThree extends Fragment {

    ImageView resOne, resTwo;
    int cntOne = 0, cntTwo = 0;

    public static ExecutionThree newInstance(int one, int two) {
        ExecutionThree executionThree = new ExecutionThree();
        Bundle bundle = new Bundle();
        bundle.putInt("count1", one);
        bundle.putInt("count2", two);
        executionThree.setArguments(bundle);
        return executionThree;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            cntOne = getArguments().getInt("count1");
            cntTwo = getArguments().getInt("count2");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.execution_season, container, false);

        Log.d("c1", String.valueOf(cntOne));
        Log.d("c2", String.valueOf(cntTwo));

        resOne = view.findViewById(R.id.result_one);
        resTwo = view.findViewById(R.id.result_two);

        if(cntOne == 1 && cntTwo == 0) {
            resOne.setBackgroundResource(R.drawable.shape_anim);
            AnimationDrawable anim = (AnimationDrawable) resOne.getBackground();
            anim.start();
        } else if(cntOne == 0 && cntTwo == 1) {
            resTwo.setBackgroundResource(R.drawable.shape_anim);
            AnimationDrawable anim = (AnimationDrawable) resTwo.getBackground();
            anim.start();
        }

        return view;
    }
}
