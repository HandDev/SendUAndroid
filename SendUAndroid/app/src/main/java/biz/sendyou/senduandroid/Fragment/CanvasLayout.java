package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;

import biz.sendyou.senduandroid.R;

/**
 * Created by pyh42 on 2016-08-17.
 */
public class CanvasLayout extends View {

    private static final String TAG = "DragDropLayout";

    private Bitmap mBitmap = null; // background bitmap
    private Rect mMeasuredRect;

    private static class CanvasObject {
        int posX, posY;
        int width, height;

        String text = null;
        int image_id;

        CanvasObject(int posX, int posY, int width, int height, String text) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.text = text;
        }

        CanvasObject(int posX, int posY, int width, int height, int image_id) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.image_id = image_id;
        }

        @Override
        public String toString() {
            return "Object[" + posX + ", " + posY + ", " + width + ", " + height + "]";
        }
    }

    private Paint mObjectPaint = new Paint();

    private HashSet<CanvasObject> mObjects = new HashSet<CanvasObject>();
    private SparseArray<CanvasObject> mObjectsPointer = new SparseArray<CanvasObject>();

    public CanvasLayout(final Context context) {
        super(context);

        init(context);
    }

    public CanvasLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        createObject(10, 300, 300, 500, R.drawable.sample_image);
        createObject(310, 300, 300, 500, "TEXT");


        init(context);
    }

    public CanvasLayout(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(final Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mn_wallpaper);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, null, mMeasuredRect, null);

        for(CanvasObject object : mObjects) {
            if(object.text == null) {
                Bitmap oBitmap = BitmapFactory.decodeResource(getContext().getResources(), object.image_id);
                canvas.drawBitmap(oBitmap, null, new Rect(object.posX, object.posY, object.posX + object.width, object.posY + object.height), null);
            }
            else {
                Paint mTextPaint = new Paint();
                mTextPaint.setTextSize(20.0f);

                Rect rect = new Rect(object.posX, object.posY, object.posX + object.width, object.posY + object.height);

                String t = "TEXT";
                int mWidth, mHeight;
                mWidth = (int)mObjectPaint.measureText("TEXT");
                mHeight = Math.abs((int)mObjectPaint.ascent()) + Math.abs((int)mObjectPaint.descent());

                int w = getWidth(), h = getHeight();
                float x = object.posX + (rect.width() - mWidth) / 2, y = object.posY + (rect.height() - mHeight) / 2;

                rect.setEmpty();

                //canvas.drawRect(rect, mObjectPaint);
                canvas.drawText("TEXT", x, y, mTextPaint);
            }
        }
        // draw Objects
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // touchEvent

        int xTouch;
        int yTouch;

        xTouch = (int) event.getX(0);
        yTouch = (int) event.getY(0);

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_UP:
                CanvasObject touchedObject = getTouchedObject(xTouch, yTouch);
                if(touchedObject != null) {
                    Log.w(TAG, "Object Touched");
                    if(touchedObject.text == null) {
                    }
                    else {
                        // Show Dialog Fragment
                    }
                }
                break;
            //case MotionEvent.ACTION_DOWN : break;
            //case MotionEvent.ACTION_MOVE : break;
            case MotionEvent.ACTION_CANCEL : break;
        }

        return super.onTouchEvent(event) || true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    public void createObject(int x, int y, int width, int height, String text) {
        mObjects.add(new CanvasObject(x, y, width, height, text));
    }
    public void createObject(int x, int y, int width, int height, int image_id) {
        mObjects.add(new CanvasObject(x, y, width, height, image_id));
    }

    public CanvasObject getTouchedObject(int xTouch, int yTouch) {
        for(CanvasObject object : mObjects) {
            if(object.posX <= xTouch && xTouch <= object.posX + object.width && object.posY <= yTouch && yTouch <= object.posY + object.height) {
                return object;
            }
        }
        return null;
    }
}
