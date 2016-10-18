package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import biz.sendyou.senduandroid.ActivityManager;
import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.User;
import biz.sendyou.senduandroid.Service.Usr;
import biz.sendyou.senduandroid.UserInfoManager;
import biz.sendyou.senduandroid.thread.TemplateDownloadThread;

public class SplashActivity extends AppCompatActivity {

    private final String LOGTAG = "SplashActivity";
    private View container;
    public static Activity activity;
    public SplashActivity splashActivity;
    public static Context splashActivityContext;
    private UserInfoManager userInfoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);

        container = findViewById(R.id.splash_background);

        ActivityManager.getInstance().setSplashActivity(this);
        putBitmap(R.id.background_image, R.drawable.sp_back2, 8);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        splashActivity = this;
        splashActivityContext = getApplicationContext();

        /*boolean isFirstStart = false;
        final SharedPreferences pref;
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
        }*/

        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                if(AccessToken.getCurrentAccessToken() != null) {
                    Log.w(LOGTAG, "Auto login with facebook");
                    Log.w(LOGTAG, AccessToken.getCurrentAccessToken().getUserId());
                    Log.w(LOGTAG, AccessToken.getCurrentAccessToken().getToken());
                    Usr user = (Usr) getApplicationContext();
                    user.setId(AccessToken.getCurrentAccessToken().getUserId());
                    user.setFacebookToken(AccessToken.getCurrentAccessToken().getToken());

                    user.doLogin(user.getId(), "", container);
                    //user.getUserInfo(user.getId(), user.getToken());
                    // 페이스북 첫 로그인시 자동 회원가입 만든 후 해제

                    intentActivty(SplashActivity.this, NavigationDrawerActivity.class);
                }

                else if(pref.getBoolean("autoLogin", false)) {
                    Log.w(LOGTAG, "Auto login with email");

                    Usr user = (Usr) getApplicationContext();
                    user.setId(pref.getString("userId", null));
                    user.setPw(pref.getString("userPw", null));

                    user.doLogin(user.getId(), user.getPw(), container);
                    user.getUserInfo(user.getId(), user.getToken());

                    intentActivty(SplashActivity.this, NavigationDrawerActivity.class);
                }

                else {
                    Log.w(LOGTAG, "No Auto Login");

                    intentActivty(SplashActivity.this, LoginActivity.class);
                }

                /*SharedPreferences pref = getSharedPreferences("ActivityPREF",Context.MODE_PRIVATE);
                final SharedPreferences auto = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Log.e("f",String.valueOf(auto.getBoolean("Auto",false)));
                if(pref.getBoolean("activity_excuted",false)) {
                    if(auto.getBoolean("Auto",false)) {
                        LoginActivity loginActivity = new LoginActivity();
                        Log.e("Password",auto.getString("password",""));
                        loginActivity.doLogin(auto.getString("Email",""),auto.getString("password",""));
                    }
                    else {
                        intentActivty(SplashActivity.this, SignInActivity.class);
                    }

                }
                else {
                    intentActivty(SplashActivity.this, OnBoardingActivity.class);
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("activity_excuted",true);
                    ed.commit();
                }*/
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
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private void putBitmap(int imageViewId, int drawableId, int scale) {
        ImageView imageView = (ImageView)findViewById(imageViewId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), drawableId, options));
    }
}