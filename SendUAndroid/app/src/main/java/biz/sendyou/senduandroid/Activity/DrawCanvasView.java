package biz.sendyou.senduandroid.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

/**
 * Created by pyh42 on 2016-10-17.
 */

public class DrawCanvasView extends View implements View.OnTouchListener{

    private static final String TAG = "DrawCanvasView";

    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;

    private Canvas  mCanvas;
    private Path mPath;
    private Paint mPaint;
    private LinkedList<Path> paths = new LinkedList<Path>();

    public DrawCanvasView(Context context) {
        super(context);

        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        Bitmap mBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        paths.add(mPath);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path p : paths){
            canvas.drawPath(p, mPaint);
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        mPath = new Path();
        paths.add(mPath);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
                touch_up();
                invalidate();
                break;
        }

        return true;
    }

    public void clear() {
        paths.clear();
        invalidate();
        paths.add(mPath);
    }

    public Bitmap getCanvasBitmap() {
        this.setDrawingCacheEnabled(true);
        return this.getDrawingCache();

    }
}
