package biz.sendyou.senduandroid.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drivemode.android.typeface.TypefaceHelper;

import biz.sendyou.senduandroid.Async.LoginAsync;
import biz.sendyou.senduandroid.Async.UserInfo;
import biz.sendyou.senduandroid.R;


public class LoginActivity extends AppCompatActivity {

    private EditText mEditText01;
    private String LOGTAG = "LoginActivity";
    private EditText mEditText02;
    public static Context loginActivityContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TypefaceHelper.initialize(getApplication());
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        final UserInfo userInfo = new UserInfo();

        mEditText01 = (EditText)findViewById(R.id.idedit);
        mEditText02 = (EditText)findViewById(R.id.pwedit);
        final CheckBox mCheckBox01 = (CheckBox)findViewById(R.id.autoLogin);

        loginActivityContext = getApplicationContext();

        Button mButton = (Button)findViewById(R.id.loginButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(mEditText01.getText())==null || String.valueOf(mEditText02.getText())==null) {
                    Toast.makeText(getBaseContext(),"Hello",Toast.LENGTH_LONG).show();
                }
                else {
                    userInfo.setUserName(String.valueOf(mEditText01.getText()));
                    userInfo.setPassword(String.valueOf(mEditText02.getText()));

                    LoginAsync loginAsync = new LoginAsync();
                    loginAsync.execute(userInfo);
                }
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



