package biz.sendyou.senduandroid.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import biz.sendyou.senduandroid.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Very normal Splash!!
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Intent mIntent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                startActivity(mIntent);
                finish();
            }
        };

        mHandler.sendEmptyMessageDelayed(0,3000); // Delay 3 sec.
    }
}
