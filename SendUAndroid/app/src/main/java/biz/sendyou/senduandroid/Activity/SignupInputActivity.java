package biz.sendyou.senduandroid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.UserInfo;

/**
 * Created by Chan_Woo_Kim on 2016-08-24.
 */
public class SignupInputActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mFirstNameEditText;
    private EditText mNameEditText;
    private EditText mBirthEditText;
    private EditText mEmailEditText;
    private EditText mPasswordCheckEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_input);

        findViewById(R.id.signup_next_imageview).setOnClickListener(this);

        mFirstNameEditText = (EditText)findViewById(R.id.signup_first_name_edittext);
        mNameEditText = (EditText)findViewById(R.id.signup_name_edittext);
        mBirthEditText = (EditText)findViewById(R.id.signup_birth);
        mEmailEditText = (EditText)findViewById(R.id.signup_email_edittext);
        mPasswordEditText = (EditText)findViewById(R.id.signup_password_edittext);
        mPasswordCheckEditText = (EditText)findViewById(R.id.signup_password_check_edittext);

    }

    @Override
    public void onClick(View view) {
        Intent mIntent = new Intent(this, SignupAddressActivity.class);
        mIntent.putExtra("userName", mFirstNameEditText.getText().toString() + mNameEditText.getText().toString());
        mIntent.putExtra("email", mEmailEditText.getText().toString());
        mIntent.putExtra("password", mPasswordCheckEditText.getText().toString());
        mIntent.putExtra("birth",mBirthEditText.getText().toString());
        startActivity(mIntent);
        finish();
    }
}
