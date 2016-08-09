package kr.hs.dimigo.dudgns0507.canvas_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by pyh42 on 2016-08-09.
 */
public class ColorBall {

    Bitmap mBitmap;
    Context mContext;
    Point mPoint;
    int id;
    static int count = 0;

    public ColorBall(Context mContext, int resourceId, Point mPoint) {
        this.id = count++;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), resourceId);
        this.mContext = mContext;
        this.mPoint = mPoint;
    }

    public int getWidthOfBall() {
        return mBitmap.getWidth();
    }

    public int getHeightOfBall() {
        return mBitmap.getHeight();
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public int getX() {
        return mPoint.x;
    }

    public int getY() {
        return mPoint.y;
    }

    public int getId() {
        return id;
    }

    public void setX(int x) {
        mPoint.x = x;
    }
    public void setY(int y) {
        mPoint.y = y;
    }
}
