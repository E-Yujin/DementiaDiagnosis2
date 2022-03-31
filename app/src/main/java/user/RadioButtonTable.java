package user;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class RadioButtonTable extends TableLayout implements
        View.OnClickListener {

    private RadioButton mBtnCurrentRadio;

    public RadioButtonTable(Context context) {
        super(context);
    }

    public RadioButtonTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        final RadioButton mBtnRadio = (RadioButton) v;

        if (mBtnCurrentRadio != null) {
            mBtnCurrentRadio.setChecked(false);
        }
        mBtnRadio.setChecked(true);
        mBtnCurrentRadio = mBtnRadio;
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow) child);
    }

    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i = 0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if (v instanceof RadioButton) {
                v.setOnClickListener(this);
            }
        }
    }

    // 무학여부 값 가져오기
    public String getEduValue() {
        return mBtnCurrentRadio.getText().toString();
    }
}



