package com.cbnu.dementiadiagnosis;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import user.Data;

import user.Score;
import user.SharedPreference;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private final ArrayList<Score> listScore = new ArrayList<>();
    private Dialog dialog;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_view, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        dialog = new Dialog(parent.getContext());
        dialog.setContentView(R.layout.dialog_full);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 1000;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(params);

        itemViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                TextView score_tv = (TextView) dialog.findViewById(R.id.score);
                ProgressBar barchart_pg = (ProgressBar) dialog.findViewById(R.id.barChart);
                ProgressBar barchart_pg2 = (ProgressBar) dialog.findViewById(R.id.barChart2);
                ProgressBar barchart_pg3 = (ProgressBar) dialog.findViewById(R.id.barChart3);
                ProgressBar barchart_pg4 = (ProgressBar) dialog.findViewById(R.id.barChart4);
                ProgressBar barchart_pg5 = (ProgressBar) dialog.findViewById(R.id.barChart5);
                ProgressBar barchart_pg6 = (ProgressBar) dialog.findViewById(R.id.barChart6);
                TextView ori_score = (TextView) dialog.findViewById(R.id.ori_score);
                TextView mem_score = (TextView) dialog.findViewById(R.id.mem_score);
                TextView att_score = (TextView) dialog.findViewById(R.id.att_score);
                TextView spa_score = (TextView) dialog.findViewById(R.id.spa_score);
                TextView exe_score = (TextView) dialog.findViewById(R.id.exe_score);
                TextView lan_score = (TextView) dialog.findViewById(R.id.lan_score);
                TextView result_tv = (TextView) dialog.findViewById(R.id.result_text);
                Button btn_end = (Button) dialog.findViewById(R.id.endBtn);

                int orientation = listScore.get(itemViewHolder.getAdapterPosition()).getOrientation();
                int memory = listScore.get(itemViewHolder.getAdapterPosition()).getMemory();
                int attention = listScore.get(itemViewHolder.getAdapterPosition()).getAttention();
                int spacetime = listScore.get(itemViewHolder.getAdapterPosition()).getSpacetime();
                int total = listScore.get(itemViewHolder.getAdapterPosition()).getTotal();
                int execution = listScore.get(itemViewHolder.getAdapterPosition()).getExecution();
                int language = listScore.get(itemViewHolder.getAdapterPosition()).getLanguage();

                score_tv.setText(Integer.toString(total));
                ori_score.setText(orientation + "/5");
                mem_score.setText(memory + "/10");
                att_score.setText(attention + "/3");
                spa_score.setText(spacetime + "/2");
                exe_score.setText(execution + "/6");
                lan_score.setText(language + "/4");

                setProgressWithAnimation(barchart_pg, orientation * 20);
                setProgressWithAnimation(barchart_pg2,memory * 10);
                setProgressWithAnimation(barchart_pg3,attention * 33 + 1);
                setProgressWithAnimation(barchart_pg4,spacetime * 50);
                setProgressWithAnimation(barchart_pg5,execution * 16 + 4);
                setProgressWithAnimation(barchart_pg6,language * 25);

                if(SharedPreference.getUserScore(view.getContext()) < total) {
                    result_tv.setText(SharedPreference.getUserName(view.getContext()) + "님은 진단결과 상 정상 범주에 속하는 수준입니다. " +
                            "앞으로도 치매에 관한 꾸준한 관리로 건강한 생활을 유지하시길 바랍니다.");
                }else {
                    result_tv.setText(SharedPreference.getUserName(view.getContext()) + "님은 진단결과 상 구체적인 진단이 필요한 수준입니다. " +
                            "해당 진단기는 비교적 간단한 자가진단이기에 해당 결과로 치매를 확정하지 않으니 " +
                            "정확한 진단을 위해 가까운 병원이나 치매센터에 방문하셔서 보다 정밀한 검사를 받아보시길 바랍니다.");
                }
                dialog.show();

                btn_end.setOnClickListener(v1 -> dialog.dismiss());
            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ItemViewHolder holder, int position) {
        holder.textView1.setText(String.valueOf(listScore.get(position).getTotal()));
        holder.textView2.setText(String.valueOf(listScore.get(position).getDate()));
        if(SharedPreference.getUserScore(holder.itemView.getContext()) < listScore.get(position).getTotal()) {
            holder.imageView.setColorFilter(Color.parseColor("#BBE0FD"));
        } else {
            holder.imageView.setColorFilter(Color.parseColor("#FFCFCF"));
        }
    }

    @Override
    public int getItemCount() {
        return listScore.size();
    }

    public void addItem(Score score) {
        listScore.add(score);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView1;
        private TextView textView2;
        private ImageButton imageButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.color_img);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageButton = itemView.findViewById(R.id.imageBtn);
        }
    }

    public void setProgressWithAnimation(ProgressBar progressBar, int progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
        objectAnimator.setDuration(1300);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
