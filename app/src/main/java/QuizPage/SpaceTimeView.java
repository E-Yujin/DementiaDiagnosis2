package QuizPage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaceTimeView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint, dPaint;
    private static final int TOUCH_TOLERANCE_DP = 24;
    private static final int BACKGROUND = 0xFFDDDDDD;
    private List<Point> mPoints = new ArrayList<Point>();
    private int mLastPointIndex;
    private int mTouchTolerance;
    private boolean isPathStarted = false;
    private List<String> answer = new ArrayList<String>();

    public SpaceTimeView(Context context) {
        super(context);
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        dPaint = new Paint();
        dPaint.setColor(Color.BLACK);
        dPaint.setStrokeWidth(20);
        dPaint.setStrokeJoin(Paint.Join.ROUND);
        dPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchTolerance = dp2px(TOUCH_TOLERANCE_DP);

        Point p1 = new Point(50, 20);
        Point p2 = new Point(250, 20);
        Point p3 = new Point(450, 20);
        Point p4 = new Point(650, 20);
        Point p5 = new Point(850, 20);

        Point p6 = new Point(50, 220);
        Point p7 = new Point(250, 220);
        Point p8 = new Point(450, 220);
        Point p9 = new Point(650, 220);
        Point p10 = new Point(850, 220);

        Point p11 = new Point(50, 420);
        Point p12 = new Point(250, 420);
        Point p13 = new Point(450, 420);
        Point p14 = new Point(650, 420);
        Point p15 = new Point(850, 420);

        Point p16 = new Point(50, 620);
        Point p17 = new Point(250, 620);
        Point p18 = new Point(450, 620);
        Point p19 = new Point(650, 620);
        Point p20 = new Point(850, 620);

        Point p21 = new Point(50, 820);
        Point p22 = new Point(250, 820);
        Point p23 = new Point(450, 820);
        Point p24 = new Point(650, 820);
        Point p25 = new Point(850, 820);

        mPoints.add(p1);
        mPoints.add(p2);
        mPoints.add(p3);
        mPoints.add(p4);
        mPoints.add(p5);

        mPoints.add(p6);
        mPoints.add(p7);
        mPoints.add(p8);
        mPoints.add(p9);
        mPoints.add(p10);

        mPoints.add(p11);
        mPoints.add(p12);
        mPoints.add(p13);
        mPoints.add(p14);
        mPoints.add(p15);

        mPoints.add(p16);
        mPoints.add(p17);
        mPoints.add(p18);
        mPoints.add(p19);
        mPoints.add(p20);

        mPoints.add(p21);
        mPoints.add(p22);
        mPoints.add(p23);
        mPoints.add(p24);
        mPoints.add(p25);
    }

    public SpaceTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        dPaint = new Paint();
        dPaint.setColor(Color.BLACK);
        dPaint.setStrokeWidth(20);
        dPaint.setStrokeJoin(Paint.Join.ROUND);
        dPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchTolerance = dp2px(TOUCH_TOLERANCE_DP);

        Point p1 = new Point(50, 50);
        Point p2 = new Point(200, 50);
        Point p3 = new Point(350, 50);
        Point p4 = new Point(500, 50);
        Point p5 = new Point(650, 50);

        Point p6 = new Point(50, 200);
        Point p7 = new Point(200, 200);
        Point p8 = new Point(350, 200);
        Point p9 = new Point(500, 200);
        Point p10 = new Point(650, 200);

        Point p11 = new Point(50, 350);
        Point p12 = new Point(200, 350);
        Point p13 = new Point(350, 350);
        Point p14 = new Point(500, 350);
        Point p15 = new Point(650, 350);

        Point p16 = new Point(50, 500);
        Point p17 = new Point(200, 500);
        Point p18 = new Point(350, 500);
        Point p19 = new Point(500, 500);
        Point p20 = new Point(650, 500);

        Point p21 = new Point(50, 650);
        Point p22 = new Point(200, 650);
        Point p23 = new Point(350, 650);
        Point p24 = new Point(500, 650);
        Point p25 = new Point(650, 650);

        mPoints.add(p1);
        mPoints.add(p2);
        mPoints.add(p3);
        mPoints.add(p4);
        mPoints.add(p5);

        mPoints.add(p6);
        mPoints.add(p7);
        mPoints.add(p8);
        mPoints.add(p9);
        mPoints.add(p10);

        mPoints.add(p11);
        mPoints.add(p12);
        mPoints.add(p13);
        mPoints.add(p14);
        mPoints.add(p15);

        mPoints.add(p16);
        mPoints.add(p17);
        mPoints.add(p18);
        mPoints.add(p19);
        mPoints.add(p20);

        mPoints.add(p21);
        mPoints.add(p22);
        mPoints.add(p23);
        mPoints.add(p24);
        mPoints.add(p25);
    }

    public SpaceTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        dPaint = new Paint();
        dPaint.setColor(Color.BLACK);
        dPaint.setStrokeWidth(20);
        dPaint.setStrokeJoin(Paint.Join.ROUND);
        dPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchTolerance = dp2px(TOUCH_TOLERANCE_DP);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BACKGROUND);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);

        for (Point point : mPoints) {
            canvas.drawPoint(point.x, point.y, dPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x, y);
                invalidate();
                break;
        }
        return true;
    }

    private void touch_start(float x, float y) {

        for (int i = 0; i < 25; i++) {
            mLastPointIndex = i;
            if (checkPoint(x, y, mLastPointIndex)) {
                mPath.reset();
                // user starts from given point so path can be is started
                isPathStarted = true;
                break;
            } else {
                // user starts move from point which doesn't belongs to mPoint list
                isPathStarted = false;
            }
        }

    }

    private void touch_move(float x, float y) {
        // draw line with finger move
        Point p;
        if (isPathStarted) {
            mPath.reset();
            p = mPoints.get(mLastPointIndex);
            mPath.moveTo(p.x, p.y);
            int nextPointIndex;
            if (mLastPointIndex == 0) {
                if (checkPoint(x, y, mLastPointIndex + 1)) {
                    drawPath(p, mLastPointIndex + 1);
                } else if (checkPoint(x, y, mLastPointIndex + 5)) {
                    drawPath(p, mLastPointIndex + 5);
                } else if (checkPoint(x, y, mLastPointIndex + 6)) {
                    drawPath(p, mLastPointIndex + 6);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if (mLastPointIndex == 4) {
                if (checkPoint(x, y, mLastPointIndex - 1)) {
                    drawPath(p, mLastPointIndex - 1);
                } else if (checkPoint(x, y, mLastPointIndex + 4)) {
                    drawPath(p, mLastPointIndex + 4);
                } else if (checkPoint(x, y, mLastPointIndex + 5)) {
                    drawPath(p, mLastPointIndex + 5);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if (mLastPointIndex == 20) {
                if (checkPoint(x, y, mLastPointIndex + 1)) {
                    drawPath(p, mLastPointIndex + 1);
                } else if (checkPoint(x, y, mLastPointIndex - 4)) {
                    drawPath(p, mLastPointIndex - 4);
                } else if (checkPoint(x, y, mLastPointIndex - 5)) {
                    drawPath(p, mLastPointIndex - 5);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if (mLastPointIndex == 24) {
                if (checkPoint(x, y, mLastPointIndex - 1)) {
                    drawPath(p, mLastPointIndex - 1);
                } else if (checkPoint(x, y, mLastPointIndex - 6)) {
                    drawPath(p, mLastPointIndex - 6);
                } else if (checkPoint(x, y, mLastPointIndex - 5)) {
                    drawPath(p, mLastPointIndex - 5);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if (mLastPointIndex > 0 && mLastPointIndex <= 3) {
                if (checkPoint(x, y, mLastPointIndex + 1)) {
                    drawPath(p, mLastPointIndex + 1);
                }
                if (checkPoint(x, y, mLastPointIndex - 1)) {
                    drawPath(p, mLastPointIndex - 1);
                } else if (checkPoint(x, y, mLastPointIndex + 4)) {
                    drawPath(p, mLastPointIndex + 4);
                } else if (checkPoint(x, y, mLastPointIndex + 5)) {
                    drawPath(p, mLastPointIndex + 5);
                } else if (checkPoint(x, y, mLastPointIndex + 6)) {
                    drawPath(p, mLastPointIndex + 6);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if (mLastPointIndex == 5 || mLastPointIndex == 10 || mLastPointIndex == 15) {
                if (checkPoint(x, y, mLastPointIndex + 1)) {
                    drawPath(p, mLastPointIndex + 1);
                }
                if (checkPoint(x, y, mLastPointIndex - 5)) {
                    drawPath(p, mLastPointIndex - 5);
                } else if (checkPoint(x, y, mLastPointIndex - 4)) {
                    drawPath(p, mLastPointIndex - 4);
                } else if (checkPoint(x, y, mLastPointIndex + 5)) {
                    drawPath(p, mLastPointIndex + 5);
                } else if (checkPoint(x, y, mLastPointIndex + 6)) {
                    drawPath(p, mLastPointIndex + 6);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if (mLastPointIndex == 9 || mLastPointIndex == 14 || mLastPointIndex == 19) {
                if (checkPoint(x, y, mLastPointIndex - 1)) {
                    drawPath(p, mLastPointIndex - 1);
                }
                if (checkPoint(x, y, mLastPointIndex - 5)) {
                    drawPath(p, mLastPointIndex - 5);
                } else if (checkPoint(x, y, mLastPointIndex + 4)) {
                    drawPath(p, mLastPointIndex + 4);
                } else if (checkPoint(x, y, mLastPointIndex + 5)) {
                    drawPath(p, mLastPointIndex + 5);
                } else if (checkPoint(x, y, mLastPointIndex - 6)) {
                    drawPath(p, mLastPointIndex - 6);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if (mLastPointIndex > 20 && mLastPointIndex <= 23) {
                if (checkPoint(x, y, mLastPointIndex + 1)) {
                    drawPath(p, mLastPointIndex + 1);
                }
                if (checkPoint(x, y, mLastPointIndex - 1)) {
                    drawPath(p, mLastPointIndex - 1);
                } else if (checkPoint(x, y, mLastPointIndex - 4)) {
                    drawPath(p, mLastPointIndex - 4);
                } else if (checkPoint(x, y, mLastPointIndex - 5)) {
                    drawPath(p, mLastPointIndex - 5);
                } else if (checkPoint(x, y, mLastPointIndex - 6)) {
                    drawPath(p, mLastPointIndex - 6);
                } else {
                    mPath.lineTo(x, y);
                }
            } else if ((mLastPointIndex > 5 && mLastPointIndex <= 8) ||
                    (mLastPointIndex > 10 && mLastPointIndex <= 13) ||
                    (mLastPointIndex > 15 && mLastPointIndex <= 18)) {
                if (checkPoint(x, y, mLastPointIndex + 1)) {
                    drawPath(p, mLastPointIndex + 1);
                }
                if (checkPoint(x, y, mLastPointIndex - 1)) {
                    drawPath(p, mLastPointIndex - 1);
                } else if (checkPoint(x, y, mLastPointIndex + 4)) {
                    drawPath(p, mLastPointIndex + 4);
                } else if (checkPoint(x, y, mLastPointIndex - 4)) {
                    drawPath(p, mLastPointIndex - 4);
                } else if (checkPoint(x, y, mLastPointIndex + 5)) {
                    drawPath(p, mLastPointIndex + 5);
                } else if (checkPoint(x, y, mLastPointIndex - 5)) {
                    drawPath(p, mLastPointIndex - 5);
                } else if (checkPoint(x, y, mLastPointIndex + 6)) {
                    drawPath(p, mLastPointIndex + 6);
                } else if (checkPoint(x, y, mLastPointIndex - 6)) {
                    drawPath(p, mLastPointIndex - 6);
                } else {
                    mPath.lineTo(x, y);
                }
            }
        }
    }

    public void drawPath(Point p, int PointIndex) {
        Log.d("path_start", Integer.toString(mLastPointIndex));
        Log.d("path_end", Integer.toString(PointIndex));
        answer.add(Integer.toString(mLastPointIndex));
        answer.add(Integer.toString(PointIndex));
        int nextPointIndex;
        nextPointIndex = PointIndex;
        p = mPoints.get(nextPointIndex);
        mPath.lineTo(p.x, p.y);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
        mLastPointIndex = nextPointIndex;
    }

    /**
     * Draws line.
     */
    private void touch_up(float x, float y) {
        mPath.reset();
    }

    /**
     * Sets paint
     *
     * @param paint
     */
    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    /**
     * Returns image as bitmap
     *
     * @return
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * Clears canvas
     */
    public void clear() {
        answer.clear();
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(BACKGROUND);
        mCanvas.setBitmap(mBitmap);
        invalidate();
    }

    /**
     * Checks if user touch point with some tolerance
     **/
    private boolean checkPoint(float x, float y, int pointIndex) {
        if (pointIndex >= mPoints.size() || pointIndex < 0) {
            // out of bounds
            return false;
        }
        Point point = mPoints.get(pointIndex);
        //EDIT changed point.y to point.x in the first if statement
        if (x > (point.x - mTouchTolerance) && x < (point.x + mTouchTolerance)) {
            if (y > (point.y - mTouchTolerance) && y < (point.y + mTouchTolerance)) {
                return true;
            }
        }
        return false;
    }


    public List<Point> getPoints() {
        return mPoints;
    }

    public void setPoints(List<Point> points) {
        this.mPoints = points;
    }

    private int dp2px(int dp) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    public int Score_cal(String[] correct) {
        boolean isCorrect = false;
        int isWrong = 0;
        boolean buffer[] = new boolean[correct.length / 2];
        int count = 0;
        Arrays.fill(buffer, false);
        for (int i = 0; i < answer.size(); i += 2) {
            for (int j = 0; j < correct.length; j += 2) {
                if (answer.get(i).equals(correct[j]) && answer.get(i + 1).equals(correct[j + 1])) {
                    isCorrect = true;
                    buffer[j - count] = true;
                    break;
                } else if (answer.get(i).equals(correct[j + 1]) && answer.get(i + 1).equals(correct[j])) {
                    isCorrect = true;
                    buffer[j - count] = true;
                    break;
                }
                count++;
            }
            if (!isCorrect) isWrong++;
            isCorrect = false;
            count = 0;
        }

        for (boolean a : buffer) {
            if (!a) isWrong++;
        }

        if (isWrong > 2) return 0;
        else if(isWrong == 1) return 1;
        else return 2;
    }

    public int getScore(){
        String correct[] = {"1", "5", "5", "11", "1", "7", "7", "12", "11", "12", "11",
                "16", "16", "17", "17", "18", "18", "13", "13", "8", "8", "7", "13", "17"};

        String half_correct[] = {"2", "6", "6", "12", "2", "8", "8", "13", "12", "13", "12",
                "17", "17", "18", "18", "19", "19", "14", "14", "9", "9", "8", "14", "18"};

        String half_correct2[] = {"6", "10", "10", "16", "6", "12", "12", "17", "16", "17", "16",
                "21", "21", "22", "22", "23", "23", "18", "18", "13", "13", "12", "18", "22"};

        String half_correct3[] = {"7", "11", "11", "17", "7", "13", "13", "18", "17", "18", "17",
                "22", "22", "23", "23", "24", "24", "19", "19", "14", "15", "13", "19", "23"};


        if(Score_cal((half_correct)) == 2) return 1;
        else if(Score_cal((half_correct2)) == 2) return 1;
        else if(Score_cal((half_correct3)) == 2) return 1;
        else if(Score_cal(correct) == 2) return 2;
        else if(Score_cal(correct) == 1) return 1;
        else if(Score_cal(correct) == 0) return 0;
        else return 0;
    }
}
