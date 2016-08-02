package biz.sendyou.senduandroid.Activity;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import biz.sendyou.senduandroid.R;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        TextView mTextView01 = (TextView)findViewById(R.id.sendu);
        TextView mTextView02 = (TextView)findViewById(R.id.Select);

        Typeface Regular = Typeface.createFromAsset(getAssets(),"NotoSansCJKkr-Regular.otf");

        mTextView01.setTypeface(Regular);
        mTextView02.setTypeface(Regular);

    }
}
