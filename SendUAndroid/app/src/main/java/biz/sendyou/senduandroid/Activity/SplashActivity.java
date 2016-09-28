package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import biz.sendyou.senduandroid.ActivityManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.thread.TemplateDownloadThread;

public class SplashActivity extends AppCompatActivity {

    private final String LOGTAG = "SplashActivity";

    public static Activity activity;
    public SplashActivity splashActivity;
    public static Context splashActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        putBitmap(R.id.background_image, R.drawable.sp_back2, 8);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        splashActivity = this;
        splashActivityContext = getApplicationContext();

        boolean isFirstStart = false;
        SharedPreferences pref;
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        if(!pref.getBoolean("isFirst", false)){
            Log.i(LOGTAG,"isFirst launch after installation");
            //start Template Image Download Thread
            TemplateDownloadThread templateDownloadThread = new TemplateDownloadThread();
            Log.i(LOGTAG, "Thread execute");

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean("workCheckBox", true);
            edit.putBoolean("isFirst", true);
            edit.commit();
        }

        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                SharedPreferences pref = getSharedPreferences("ActivityPREF",Context.MODE_PRIVATE);
                if(pref.getBoolean("activity_excuted",false)) {
                    if(pref.getBoolean("Auto",false)) {
                        LoginActivity loginActivity = new LoginActivity();
                        loginActivity.doLogin(pref.getString("email",null),pref.getString("password",null));
                    }
                    else {
                        intentActivty(SplashActivity.this, LoginActivity.class);
                    }

                }
                else {
                    intentActivty(SplashActivity.this, OnBoardingActivity.class);
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("activity_excuted",true);
                    ed.commit();
                }
            }
        };

        mHandler.sendEmptyMessageDelayed(0,3000); // Delay 3 sec.

    }

    @Override
    protected void onDestroy() {
        Log.w(LOGTAG, "Destroy background");
        recycleView(R.id.background_image);
        super.onDestroy();
    }

    private void recycleView(int id) {
        ImageView view = (ImageView)findViewById(id);

        Drawable d = view.getDrawable();
        if(d instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            view.setImageBitmap(null);
            b.recycle();
            b = null;
        }
        d.setCallback(null);
        System.gc();
        Runtime.getRuntime().gc();
    }

    public void intentActivty(Context packageContext, Class cls) {
        Intent mIntent = new Intent(packageContext, cls);
        startActivity(mIntent);
        finish();
    }

    private void putBitmap(int imageViewId, int drawableId, int scale) {
        ImageView imageView = (ImageView)findViewById(imageViewId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), drawableId, options));
    }
}