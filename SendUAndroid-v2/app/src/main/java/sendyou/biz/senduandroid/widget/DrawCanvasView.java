package sendyou.biz.senduandroid.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyh42 on 2016-10-17.
 */

public class DrawCanvasView extends View {

    private static final String TAG = "DrawCanvasView";

    public enum Mode {
        DRAW,
        ERASER,
        TEXT
    }

    private Context mContext = null;
    private Canvas mCanvas = null;
    private Bitmap mBitmap = null;
    private Paint mPaint = null;

    private List<Path> paths = new ArrayList<>();
    private List<Paint> paints = new ArrayList<>();

    private float brushSize;
    private float eraserSize;
    private int paintColor = 0xFF000000, paintAlpha = 255;
    private int historyPointer = 0;
    private int maxHistoryPointer = 0;
    private float mX, mY;
    private Mode mode = Mode.TEXT;

    public DrawCanvasView(Context context){
        super(context);
        this.setupDrawing(context);
    }

    public DrawCanvasView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setupDrawing(context);
    }

    private void setupDrawing(Context context){
        this.mContext = context;

        brushSize = 5;
        eraserSize = 50;
        reset();

        mPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(1440, 2042, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        if(historyPointer > 0) {
            if (this.mode == Mode.DRAW) {
                canvas.drawPath(paths.get(historyPointer - 1), paints.get(historyPointer - 1));
            } else if (this.mode == Mode.ERASER) {
                mCanvas.drawPath(paths.get(historyPointer - 1), paints.get(historyPointer - 1));
            } else if(this.mode == Mode.TEXT) {

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(this.mode == Mode.DRAW || this.mode == Mode.ERASER) {
                    reset();
                    setPressure(event.getPressure());
                    paths.get(historyPointer - 1).moveTo(touchX, touchY);
                    mX = touchX;
                    mY = touchY;
                } else if(this.mode == Mode.TEXT) {

                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(this.mode == Mode.DRAW || this.mode == Mode.ERASER) {
                    setPressure(event.getPressure());
                    paths.get(historyPointer - 1).quadTo(mX, mY, (mX + touchX) / 2, (mY + touchY) / 2);
                    mX = touchX;
                    mY = touchY;
                } else if(this.mode == Mode.TEXT) {

                }
                break;

            case MotionEvent.ACTION_UP:
                if(this.mode == Mode.DRAW || this.mode == Mode.ERASER) {
                    setPressure(event.getPressure());
                    paths.get(historyPointer - 1).quadTo(mX, mY, (mX + touchX) / 2, (mY + touchY) / 2);
                    mCanvas.drawPath(paths.get(historyPointer - 1), paints.get(historyPointer - 1));
                } else if(this.mode == Mode.TEXT) {

                }
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void reset() {
        paths.add(historyPointer, new Path());
        paints.add(historyPointer, new Paint());

        historyPointer++;
        maxHistoryPointer = historyPointer;
        setPaint();
    }

    public void undo() {
        if(historyPointer > 1) {
            historyPointer--;

            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for(int i = 0 ; i < historyPointer ; i++) {
                mCanvas.drawPath(paths.get(i), paints.get(i));
            }
            invalidate();
        }
    }

    public void redo() {
        if(maxHistoryPointer > historyPointer) {
            historyPointer++;

            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for(int i = 0 ; i < historyPointer ; i++) {
                mCanvas.drawPath(paths.get(i), paints.get(i));
            }
            invalidate();
        }
    }

    public void setPaint() {
        if(mode == Mode.DRAW) {
            paints.get(historyPointer - 1).setColor(paintColor);
            paints.get(historyPointer - 1).setXfermode(null);
            paints.get(historyPointer - 1).setAntiAlias(true);
            paints.get(historyPointer - 1).setStrokeWidth(brushSize);
            paints.get(historyPointer - 1).setStyle(Paint.Style.STROKE);
            paints.get(historyPointer - 1).setStrokeJoin(Paint.Join.ROUND);
            paints.get(historyPointer - 1).setStrokeCap(Paint.Cap.ROUND);
        } else if(mode == Mode.ERASER) {
            paints.get(historyPointer - 1).setColor(Color.TRANSPARENT);
            paints.get(historyPointer - 1).setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            paints.get(historyPointer - 1).setAntiAlias(true);
            paints.get(historyPointer - 1).setStrokeWidth(eraserSize);
            paints.get(historyPointer - 1).setStyle(Paint.Style.STROKE);
            paints.get(historyPointer - 1).setStrokeJoin(Paint.Join.ROUND);
            paints.get(historyPointer - 1).setStrokeCap(Paint.Cap.ROUND);
        } else if(this.mode == Mode.TEXT) {

        }
    }

    public void setPressure(float pressure) {
        Log.w(TAG, "Pressure : " + pressure);
//        if(mode == Mode.DRAW) {
//            paints.get(historyPointer - 1).setStrokeWidth(brushSize * pressure);
//        } else if(mode == Mode.ERASER) {
//            paints.get(historyPointer - 1).setStrokeWidth(eraserSize * pressure);
//        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        reset();
    }

    public Mode getMode() {
        return mode;
    }

    public void setColor(String newColor){
        invalidate();
        //check whether color value or pattern name
        if(newColor.startsWith("#")){
            paintColor = Color.parseColor(newColor);
        } else {
            paintColor = Color.parseColor("#" + newColor);
        }
        reset();
    }

    public void setColor(int newColor) {
        paintColor = newColor;
        reset();
    }

    public int getCurrentColor() {
        return paints.get(historyPointer - 1).getColor();
    }

    public void setBrushSize(float brushSize) {
        this.brushSize = brushSize;
    }

    public float getBrushSize() {
        return brushSize;
    }

    public void setEraserSize(float eraserSize) {
        this.eraserSize = eraserSize;
    }

    public float getEraserSize() {
        return eraserSize;
    }
}
