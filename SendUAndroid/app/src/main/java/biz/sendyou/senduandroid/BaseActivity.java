package biz.sendyou.senduandroid;


import android.app.Application;
import biz.sendyou.senduandroid.Typeface.TypefaceUtil;

/**
 * Created by JunHyeok on 2016. 7. 28..
 */
public class BaseActivity extends Application {

    @Override
    public void onCreate() {
        TypefaceUtil.overrideFont(getApplicationContext(),"SERIF","Roboto-Medium");
    }

}