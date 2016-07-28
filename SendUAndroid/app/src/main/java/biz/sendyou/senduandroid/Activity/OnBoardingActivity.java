package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import biz.sendyou.senduandroid.R;

public class OnBoardingActivity extends Activity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_on_boarding);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getLayoutInflater());
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}
