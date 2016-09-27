package biz.sendyou.senduandroid.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by parkjaesung on 2016. 9. 27..
 */

public class BitmapFromURLThread extends Thread {

    private Bitmap bitmap;
    private String urlString;

    public BitmapFromURLThread(String url){
        this.urlString = url;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(urlString);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            InputStream is = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
