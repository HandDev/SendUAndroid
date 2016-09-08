package biz.sendyou.senduandroid.Activity;

import android.content.Context;
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
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.SignUpService;
import biz.sendyou.senduandroid.datatype.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chan_Woo_Kim on 2016-08-24.
 */
public class SignupAddressActivity extends AppCompatActivity {
    String userName, email, password,birth;
    private ImageView mImageView01,mImageView02;
    private TextView mTextView01,mTextView02;
    private EditText mEditText01;
    private static final String URL = "http://sendyou.biz/user/signup/";

    public static SignupAddressActivity signupAddressActivity;
    public static Context signupAddressActivityContext;

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
        signupAddressActivity = this;
        signupAddressActivityContext = getApplicationContext();



        mImageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    public static void callLoginActivity() {

        Intent mIntent = new Intent(SignupAddressActivity.signupAddressActivity, LoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        signupAddressActivityContext.startActivity(mIntent);
    }

    private void doSignup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SignUpService signUpService = retrofit.create(SignUpService.class);

        Call<Repo> call = signUpService.doSignup(userName,password,email,mTextView02.getText().toString()+mEditText01.getText().toString(),birth);

        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {

            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }




}
