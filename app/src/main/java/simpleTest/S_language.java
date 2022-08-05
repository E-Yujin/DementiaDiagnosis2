package simpleTest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import questions.LanguageFunc;

public class S_language extends AppCompatActivity {

    LanguageFunc languageFunc;
    TTS tts;
    TextView question, type;
    String Okay = "";
    ProgressBar pro_bar;
    ImageView mImg, image2, image3, image4, image5;
    ImageButton beforeBtn, nextBtn;
    AppCompatButton donKnow;

    private static final String IMAGEVIEW_TAG = "드래그 이미지";
    private int resLeft = 0, resRight = 0; // 정답 체크
    private long backBtnTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_language);

        Log.d("languageFunc", "start");
        languageFunc = new LanguageFunc();
        mImg = (ImageView) findViewById(R.id.image);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        beforeBtn = (ImageButton) findViewById(R.id.before);
        nextBtn = (ImageButton) findViewById(R.id.next);
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);

        type.setText("언어기능");
        pro_bar.setProgress(80);

        mImg.setTag(IMAGEVIEW_TAG);
        image2.setTag(IMAGEVIEW_TAG);
        image3.setTag(IMAGEVIEW_TAG);
        image4.setTag(IMAGEVIEW_TAG);
        image5.setTag(IMAGEVIEW_TAG);

        mImg.setOnLongClickListener(new S_language.LongClickListener());
        image2.setOnLongClickListener(new S_language.LongClickListener());
        image3.setOnLongClickListener(new S_language.LongClickListener());
        image4.setOnLongClickListener(new S_language.LongClickListener());
        image5.setOnLongClickListener(new S_language.LongClickListener());

        findViewById(R.id.left).setOnDragListener(
                new S_language.DragListener());
        findViewById(R.id.right).setOnDragListener(
                new S_language.DragListener());
        findViewById(R.id.ball).setOnDragListener(
                new S_language.DragListener());

        Intent intent;
        intent = getIntent();
        languageFunc.scores = intent.getIntArrayExtra("scores");

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString());
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.speakOut(question.getText().toString());
            }
        });

        donKnow.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tts.Stop();
                tts.isStopUtt = true;

                languageFunc.Tscore = 0;

                languageFunc.scores[7] = languageFunc.Tscore;

                Intent intent = new Intent(getApplicationContext(), S_fluency_Page.class);
                intent.putExtra("scores", languageFunc.scores);
                startActivity(intent);
                finish();
            }
        });

        beforeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "해당 항목의 첫 문제 입니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.Stop();
                if(resLeft == 2 && resRight == 3) {
                    languageFunc.Tscore = 1;
                } else {
                    languageFunc.Tscore = 0;
                }
                languageFunc.scores[7] = languageFunc.Tscore;

                Intent intent = new Intent(getApplicationContext(), S_fluency_Page.class);
                intent.putExtra("scores", languageFunc.scores);
                startActivity(intent);
                finish();
            }
        });
    }

    private static final class LongClickListener implements
            View.OnLongClickListener {

        public boolean onLongClick(View view) {

            // 태그 생성
            ClipData.Item item = new ClipData.Item(
                    (CharSequence) view.getTag());

            String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
            ClipData data = new ClipData(view.getTag().toString(),
                    mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);

            view.startDrag(data, // data to be dragged
                    shadowBuilder, // drag shadow
                    view, // 드래그 드랍할  Vew
                    0 // 필요없은 플래그
            );

            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

    class DragListener implements View.OnDragListener {
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable normalShape = getResources().getDrawable(
                R.drawable.shape_exe);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable targetShape = getResources().getDrawable(
                R.drawable.target_shape);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable roundNShape = getResources().getDrawable(
                R.drawable.round_button);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable roundTShape = getResources().getDrawable(
                R.drawable.round_gray);

        public boolean onDrag(View v, DragEvent event) {
            // 이벤트 시작
            switch (event.getAction()) {
                // 이미지를 드래그 시작될때
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                    break;

                // 드래그한 이미지를 옮길려는 지역으로 들어왔을때
                case DragEvent.ACTION_DRAG_ENTERED:
                    // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                    if(v == findViewById(R.id.left)) {
                        v.setBackground(targetShape);
                    } else if(v == findViewById(R.id.right)) {
                        v.setBackground(targetShape);
                    } else {
                        v.setBackground(roundTShape);
                    }
                    Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                    break;

                // 드래그한 이미지가 영역을 빠져 나갈때
                case DragEvent.ACTION_DRAG_EXITED:
                    if(v == findViewById(R.id.left)) {
                        v.setBackground(normalShape);
                        resLeft--;
                    } else if(v == findViewById(R.id.right)) {
                        v.setBackground(normalShape);
                        resRight--;
                    } else {
                        v.setBackground(roundTShape);
                    }
                    Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                    break;

                // 이미지를 드래그해서 드랍시켰을때
                case DragEvent.ACTION_DROP:
                    Log.d("DragClickListener", "ACTION_DROP");

                    if (v == findViewById(R.id.left)) {
                        resLeft++;
                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);

                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);

                    } else if (v == findViewById(R.id.right)) {
                        resRight++;
                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);

                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);

                    } else if (v == findViewById(R.id.ball)) {
                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);

                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);

                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        break;
                    }

                case DragEvent.ACTION_DRAG_ENDED:
                    if(v == findViewById(R.id.left)) {
                        v.setBackground(normalShape);
                    } else if(v == findViewById(R.id.right)) {
                        v.setBackground(normalShape);
                    } else {
                        v.setBackground(roundNShape);
                    }
                    Log.d("DragClickListener", "ACTION_DRAG_ENDED");
                    break;

                default:
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "지금 나가시면 진행된 검사가 저장되지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        question.setText(languageFunc.quiz.get(3));
        tts.speakOut(question.getText().toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.Destroy();
    }
}