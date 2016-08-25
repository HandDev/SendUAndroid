package biz.sendyou.senduandroid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import biz.sendyou.senduandroid.Async.SignUpAsync;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.UserInfo;

/**
 * Created by Chan_Woo_Kim on 2016-08-24.
 */
public class SignupAddressActivity extends AppCompatActivity {
    String userName, email, password,birth;
    private ImageView mImageView01,mImageView02;
    private TextView mTextView01,mTextView02;
    private EditText mEditText01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_address);
        getInfo();

        mImageView02 = (ImageView)findViewById(R.id.signup_address_search_button_imageview);
        mTextView01 = (TextView)findViewById(R.id.signup_auto_address_num_textview);
        mTextView02 = (TextView)findViewById(R.id.signup_auto_address_textview);
        mEditText01 = (EditText)findViewById(R.id.signup_input_address_edittext);

        mImageView01 = (ImageView) findViewById(R.id.signup_enter_imageview);



        mImageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(userName);
                userInfo.setEmail(email);
                userInfo.setPassword(password);
                userInfo.setBirth(birth);
                userInfo.setAddress(mTextView01.getText().toString()+" "+mTextView02.getText().toString()+" "+mEditText01.getText().toString());
                SignUpAsync signUpAsync = new SignUpAsync();
                signUpAsync.execute(userInfo);
            }
        });

    }

    private void getInfo() {
        Intent mIntent = getIntent();
        userName = mIntent.getStringExtra("userName");
        email = mIntent.getStringExtra("email");
        password = mIntent.getStringExtra("password");
        birth = mIntent.getStringExtra("birth");
    }
}
