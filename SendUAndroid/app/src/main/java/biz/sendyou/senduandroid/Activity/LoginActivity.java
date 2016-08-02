package biz.sendyou.senduandroid.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import biz.sendyou.senduandroid.R;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        Typeface Regular = Typeface.createFromAsset(getAssets(),"NotoSansCJKkr-Regular.otf");
        Typeface Bold = Typeface.createFromAsset(getAssets(),"NotoSansCJKkr-Bold.otf");
        Typeface Light = Typeface.createFromAsset(getAssets(),"NotoSansCJKkr-Light.otf");

        mTextView01.setTypeface(Regular);
        mTextView02.setTypeface(Light);
        mTextView03.setTypeface(Bold);

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
