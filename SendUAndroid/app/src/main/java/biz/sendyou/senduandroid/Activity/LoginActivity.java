package biz.sendyou.senduandroid.Activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.renderscript.Type;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.drivemode.android.typeface.TypefaceHelper;

import org.w3c.dom.Text;

import biz.sendyou.senduandroid.R;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TypefaceHelper.initialize(getApplication());
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        Button mButton = (Button)findViewById(R.id.copybutton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,NavigationDrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView mTextView01 = (TextView)findViewById(R.id.SendU);
        TextView mTextView02 = (TextView)findViewById(R.id.SignUp1);
        TextView mTextView03 = (TextView)findViewById(R.id.SignUp2);

        TypefaceHelper.getInstance().setTypeface(mTextView01,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(mTextView02,"NotoSansCJKkr-Light.otf");
        TypefaceHelper.getInstance().setTypeface(mTextView03,"NotoSansCJKkr-Bold.otf");

        mTextView03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveSignupActivity();
            }
        });


    }


    public void moveSignupActivity(){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }


}
