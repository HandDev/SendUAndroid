package biz.sendyou.senduandroid;

import android.graphics.Bitmap;

/**
 * Created by parkjaesung on 2016. 11. 6..
 */

public class BitmapManager {

    private static BitmapManager instance = new BitmapManager();
    public Bitmap bitmap;

    public static BitmapManager getInstance(){
        return instance;
    }

    private BitmapManager(){

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
