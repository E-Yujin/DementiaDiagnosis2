package fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbnu.dementiadiagnosis.InfAdapter;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.RecyclerAdapter;

import java.util.Arrays;
import java.util.List;

import user.Data;

public class FragmentInf extends Fragment {

    View view;
    InfAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inf, container, false);

        init();
        getData();

        return view;
    }

    public void init() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplication());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new InfAdapter();
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getData() {
        List<String> listTitle = Arrays.asList("1 Title", "2 Title", "3 Title", "4 Title", "5 Title");
        List<String> listContent = Arrays.asList("1 description", "2 description", "3 description", "4 description", "5 description");
        List<Integer> listResId = Arrays.asList(R.drawable.ic_check, R.drawable.ic_account, R.drawable.ic_help, R.drawable.ic_forward, R.drawable.ic_home);

        for(int i = 0; i < listTitle.size(); i++) {
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }
}