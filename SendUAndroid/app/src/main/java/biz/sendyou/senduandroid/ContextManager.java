package biz.sendyou.senduandroid;

import android.app.Application;
import android.content.Context;

import biz.sendyou.senduandroid.Activity.LoginActivity;
import biz.sendyou.senduandroid.Activity.NavigationDrawerActivity;
import biz.sendyou.senduandroid.Activity.OnBoardingActivity;
import biz.sendyou.senduandroid.Activity.SignupActivity;
import biz.sendyou.senduandroid.Activity.SignupAddressActivity;
import biz.sendyou.senduandroid.Activity.SignupInputActivity;
import biz.sendyou.senduandroid.Activity.SplashActivity;

/**
 * Created by JunHyeok on 2016. 9. 26..
 */

public class ContextManager extends Application {
    public static Context context;

    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static Context getContext() {
        return context;
    }
}
