package QuizPage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import questions.LanguageFunc;

public class ComprehensionPage extends AppCompatActivity {

    private ImageView mImg, image2, image3, image4, image5;
    private static final String IMAGEVIEW_TAG = "드래그 이미지";
    private Button sttBtn, submit;
    int resLeft, resRight; // 정답 체크
    LanguageFunc languageFunc;
    TTS tts;
    QuizPage QP;
    TextView question;
    TextView announce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comprehension);

        Log.d("languageFunc", "start");
        mImg = findViewById(R.id.image);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        sttBtn = findViewById(R.id.sttStart);
        submit = findViewById(R.id.btnSubmit);
        question = findViewById(R.id.question);
        announce = findViewById(R.id.textView);
        languageFunc = new LanguageFunc();

        mImg.setTag(IMAGEVIEW_TAG);
        image2.setTag(IMAGEVIEW_TAG);
        image3.setTag(IMAGEVIEW_TAG);
        image4.setTag(IMAGEVIEW_TAG);
        image5.setTag(IMAGEVIEW_TAG);

        mImg.setOnLongClickListener(new LongClickListener());
        image2.setOnLongClickListener(new LongClickListener());
        image3.setOnLongClickListener(new LongClickListener());
        image4.setOnLongClickListener(new LongClickListener());
        image5.setOnLongClickListener(new LongClickListener());

        findViewById(R.id.left).setOnDragListener(
                new DragListener());
        findViewById(R.id.right).setOnDragListener(
                new DragListener());
        findViewById(R.id.ball).setOnDragListener(
                new DragListener());

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "default");
                //tts.UtteranceProgress(announce.getText().toString());
            }
        }, sttBtn, submit);
        QP = new QuizPage(tts, question, announce, sttBtn, submit, 3, languageFunc.quiz);

        sttBtn.setEnabled(false);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(resLeft == 2 && resRight == 3) {
                    languageFunc.score = 1;
                } else {
                    languageFunc.score = 0;
                }
                Intent intent = new Intent(ComprehensionPage.this, LanguagePage.class);
                intent.putExtra("comprehension" , languageFunc.score);
                Log.d("comprehension", "" + languageFunc.score);
                setResult(RESULT_OK, intent);
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
                R.drawable.normal_shape);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable targetShape = getResources().getDrawable(
                R.drawable.target_shape);

        public boolean onDrag(View v, DragEvent event) {
            // 이벤트 시작
            switch (event.getAction()) {
                // 이미지를 드래그 시작될때
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                    break;

                // 드래그한 이미지를 옮길려는 지역으로 들어왔을때
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                    // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                    v.setBackground(targetShape);
                    break;

                // 드래그한 이미지가 영역을 빠져 나갈때
                case DragEvent.ACTION_DRAG_EXITED:
                    if(v == findViewById(R.id.left))
                        resLeft--;
                    else if(v == findViewById(R.id.right))
                        resRight--;
                    Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                    v.setBackground(normalShape);
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
                    Log.d("DragClickListener", "ACTION_DRAG_ENDED");
                    v.setBackground(normalShape); // go back to normal shape

                default:
                    break;
            }
            return true;
        }
    }
}