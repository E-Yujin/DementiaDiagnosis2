package fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.RecyclerAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import database.DBHelper;
import user.Score;
import user.SharedPreference;

public class FragmentChart extends Fragment {

    RecyclerAdapter adapter;
    ArrayList<Entry> values;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        values = new ArrayList<>();

        LineChart chart = view.findViewById(R.id.linechart);
        chart.animateY(1000);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(true);
        chart.setBorderColor(Color.LTGRAY);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yLAxis = chart.getAxisLeft();
        yLAxis.setDrawGridLines(false);
        yLAxis.setDrawAxisLine(false);

        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawGridLines(false);
        yRAxis.setDrawAxisLine(false);

        // 결과 목록 생성
        init();
        getData();

        LineDataSet set1;
        set1 = new LineDataSet(values, "진단 점수");
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setDrawFilled(true);
        set1.setDrawHighlightIndicators(false);

        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(requireActivity().getApplication(), R.drawable.fade_red);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        set1.setColor(Color.parseColor("#A0BDE1"));
        set1.setCircleColor(Color.BLACK);

        chart.setData(data);

        return view;
    }

    // 결과 목록 생성
    public void init() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplication());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    // 데이터 추가
    @SuppressLint("NotifyDataSetChanged")
    public void getData() {
        List<Score> scoreList = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(requireActivity().getApplication());

        scoreList = dbHelper.getScoreList(SharedPreference.getSerialCode(requireActivity().getApplication()));
        for(int i = 0; i < scoreList.size(); i++) {
            Score score = new Score();
            score.setOrientation(scoreList.get(i).getOrientation());
            score.setMemory(scoreList.get(i).getMemory());
            score.setAttention(scoreList.get(i).getAttention());
            score.setSpacetime(scoreList.get(i).getSpacetime());
            score.setExecution(scoreList.get(i).getExecution());
            score.setLanguage(scoreList.get(i).getLanguage());
            score.setDate(scoreList.get(i).getDate());
            score.setTotal(scoreList.get(i).getTotal());
            values.add(new Entry(i, scoreList.get(i).getTotal()));
            adapter.addItem(score);
        }
        adapter.notifyDataSetChanged();
    }
}
