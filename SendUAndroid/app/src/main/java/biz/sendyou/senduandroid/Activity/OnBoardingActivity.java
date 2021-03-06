package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.Usr;

public class OnBoardingActivity extends Activity {

    private ViewPager mViewPager;
    private SharedPreferences pref;
    private String LOGTAG = "OnBoardingActivity";
    public static OnBoardingActivity onBoardingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_on_boarding);

        onBoardingActivity = this;
        pref = getSharedPreferences("pref",MODE_PRIVATE);

        Log.i(LOGTAG, "login pref :" + pref.getBoolean("login", false));
        Log.i(LOGTAG, "tutorial pref : " + pref.getBoolean("tutorial", true));
        if(!pref.getBoolean("login",false) && pref.getBoolean("tutorial", true)) {

            Log.i(LOGTAG, "Show Tutorial");
            mViewPager = (ViewPager) findViewById(R.id.viewpager);

            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getLayoutInflater());
            mViewPager.setAdapter(mViewPagerAdapter);

        }else if(!pref.getBoolean("login", false) && !pref.getBoolean("tutorial", true)){
            Log.i(LOGTAG, "Show Login");
            callLoginActivity();
        }
        else{
            callNavigationDrawerActivity();
        }


    }

    @Override
    protected void onDestroy() {
        mViewPager.setAdapter(null);
        super.onDestroy();
    }

    public void callNavigationDrawerActivity() {
        Intent mIntent = new Intent(OnBoardingActivity.onBoardingActivity, NavigationDrawerActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Usr.getContext().startActivity(mIntent);
        finish();
    }

    public void callLoginActivity() {
        Intent mIntent = new Intent(OnBoardingActivity.onBoardingActivity, LoginActivity.class);
        Usr.getContext().startActivity(mIntent);
        finish();
    }


}
