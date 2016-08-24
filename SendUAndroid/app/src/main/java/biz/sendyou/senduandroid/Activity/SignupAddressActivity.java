package biz.sendyou.senduandroid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import biz.sendyou.senduandroid.R;

/**
 * Created by Chan_Woo_Kim on 2016-08-24.
 */
public class SignupAddressActivity extends AppCompatActivity {
    String userName, email, password;
    int birth_month, birth_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_address);
        getInfo();

    }

    private void getInfo() {
        Intent mIntent = getIntent();
        userName = mIntent.getStringExtra("userName");
        email = mIntent.getStringExtra("email");
        password = mIntent.getStringExtra("password");
        birth_date = mIntent.getIntExtra("birth_date", 1);
        birth_month = mIntent.getIntExtra("birth_month", 1);
    }
}
