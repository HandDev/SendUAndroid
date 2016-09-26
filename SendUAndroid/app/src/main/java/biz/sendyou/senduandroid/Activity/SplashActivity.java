package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.thread.TemplateDownloadThread;

public class SplashActivity extends AppCompatActivity {

    private final String LOGTAG = "SplashActivity";

    public static Activity activity;
    public SplashActivity splashActivity;
    public static Context splashActivityContext;
    private static Drawable sBackground;
    private static RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        layout = (RelativeLayout)findViewById(R.id.splash_background);

        if(sBackground == null) {
            Log.w(LOGTAG, "Set BackgroundDrawble");
            sBackground = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.sp_back2));
            layout.setBackgroundDrawable(sBackground);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        splashActivity = this;

        boolean isFirstStart = false;
        SharedPreferences pref;
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        if(!pref.getBoolean("isFirst", false)){
            Log.i(LOGTAG,"isFirst launch after installation");
            //start Template Image Download Thread
            TemplateDownloadThread templateDownloadThread = new TemplateDownloadThread();
            templateDownloadThread.execute();
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
                    Intent mIntent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(mIntent);
                    finish();
                }
                else {
                    Intent mIntent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                    startActivity(mIntent);
                    finish();
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("activity_excuted",true);
                    ed.commit();
                }

            }
        };

        //mHandler.sendEmptyMessageDelayed(0,3000); // Delay 3 sec.

    }

    @Override
    protected void onDestroy() {
        Log.w(LOGTAG, "Destroy background");
        recycleView(findViewById(R.id.splash_background));
        super.onDestroy();
    }

    private void recycleView(View view) {
        if(view != null) {
            Drawable bg = view.getBackground();
            if(bg != null) {
                ((BitmapDrawable)bg).getBitmap().recycle();
                System.gc();
                view.setBackgroundDrawable(null);
            }
            bg.setCallback(null);
        }
    }
}
