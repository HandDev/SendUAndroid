package biz.sendyou.senduandroid;

import android.app.Activity;

import biz.sendyou.senduandroid.Activity.LoginActivity;

/**
 * Created by JunHyeok on 2016. 9. 28..
 */

public class ActivityManager {
    private static ActivityManager activityManager = new ActivityManager();

    private Activity loginActivity;
    private Activity signupInputActivity;

    private ActivityManager() {

    }

    public static ActivityManager getInstance(){
        return activityManager;
    }

    public void setLoginActivity(Activity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void setSignupInputActivity(Activity signupInputActivity) {
        this.signupInputActivity = signupInputActivity;
    }

    public Activity getSignupInputActivity() {
        return signupInputActivity;
    }

    public Activity getLoginActivity() {
        return loginActivity;
    }


}
