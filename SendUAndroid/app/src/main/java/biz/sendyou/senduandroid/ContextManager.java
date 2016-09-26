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
    public static Context la,nda,oba,sua,suaa,sia,sa,p;
    LoginActivity loginActivity;
    NavigationDrawerActivity navigationDrawerActivity;
    OnBoardingActivity onBoardingActivity;
    SignupAddressActivity signupAddressActivity;
    SignupInputActivity signupInputActivity;
    SignupActivity signupActivity;
    SplashActivity splashActivity;

    public void onCreate() {
        super.onCreate();
        p = this;
    }
        /*ContextManager.la = loginActivity.getApplicationContext();
        ContextManager.nda = navigationDrawerActivity.getApplicationContext();
        ContextManager.oba = onBoardingActivity.getApplicationContext();
        ContextManager.sa = splashActivity.getApplicationContext();
        ContextManager.sia = signupInputActivity.getApplicationContext();
        ContextManager.sua = signupActivity.getApplicationContext();
        ContextManager.suaa = signupAddressActivity.getApplicationContext();

    }

    public static Context getLa() {
        return la;
    }

    public static Context getNda() {
        return nda;
    }

    public static Context getOba() {
        return oba;
    }

    public static Context getSa() {
        return sa;
    }

    public static Context getSia() {
        return sia;
    }

    public static Context getSua() {
        return sua;
    }

    public static Context getSuaa() {
        return suaa;
    }*/

    public static Context getP() {
        return p;
    }
}
