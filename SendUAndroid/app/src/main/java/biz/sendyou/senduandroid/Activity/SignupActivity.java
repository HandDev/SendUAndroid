package biz.sendyou.senduandroid.Activity;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.drivemode.android.typeface.TypefaceHelper;

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

        TypefaceHelper.getInstance().setTypeface(mTextView01,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(mTextView02,"NotoSansCJKkr-Regular.otf");

    }
}
