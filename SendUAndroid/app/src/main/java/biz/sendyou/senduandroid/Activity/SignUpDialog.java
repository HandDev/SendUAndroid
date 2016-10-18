package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.EmailCheck;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.Usr;
import biz.sendyou.senduandroid.URLManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JunHyeok on 2016. 9. 29..
 */

public class SignUpDialog extends AppCompatActivity {

    private EditText mNameEditText,mEmailEditText,mPasswordEditText,mPasswordCheckEditText;
    private LayoutInflater inflater;
    private AlertDialog alertBuilder;
    private View layout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void showDialog(View view) {
        inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.activity_signup_dialog, null);



        Animation anim = AnimationUtils.loadAnimation(Usr.getContext(), R.anim.riseup);
        layout.startAnimation(anim);

        alertBuilder = new AlertDialog.Builder(view.getContext()).create();
        alertBuilder.setCanceledOnTouchOutside(true);
        alertBuilder.setView(layout);
        alertBuilder.show();



        ImageView close = (ImageView)layout.findViewById(R.id.close_btn);
        mNameEditText = (EditText)layout.findViewById(R.id.signup_name_edittext);
        mEmailEditText = (EditText) layout.findViewById(R.id.signup_email_edittext);
        mPasswordEditText = (EditText) layout.findViewById(R.id.signup_password_edittext);
        mPasswordCheckEditText = (EditText) layout.findViewById(R.id.signup_password_check_edittext);
        Button mSignupButton = (Button) layout.findViewById(R.id.signup_next_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBuilder.dismiss();
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNameEditText.getText().toString().matches("") || mEmailEditText.getText().toString().matches("") || mPasswordCheckEditText.getText().toString().matches("") || mPasswordEditText.getText().toString().matches("")) {
                    Toast.makeText(Usr.getContext(), "빈칸이 있는지 확인해주세요.", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(mEmailEditText.getText().toString())) {
                    Toast.makeText(Usr.getContext(), "이메일의 형식이 아닙니다. 다시 한번 확인해주세요.", Toast.LENGTH_LONG).show();
                } else if (mPasswordEditText.getText().length() < 8) {
                    Toast.makeText(Usr.getContext(), "비밀번호가 너무 짧습니다. 8자 이상으로 작성해주세요.", Toast.LENGTH_LONG).show();
                } else if (!mPasswordEditText.getText().toString().matches(mPasswordCheckEditText.getText().toString())) {
                    Toast.makeText(Usr.getContext(), "비밀번호가 일치하지 않습니다. 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    emailck();
                }
            }

        });


    }

    private static boolean isValidEmail(String s) {
        String anim = s.toLowerCase();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(anim).matches();
    }

    private void emailck() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmailCheck emailCheck = retrofit.create(EmailCheck.class);

        Call<Repo> call = emailCheck.emailck(mEmailEditText.getText().toString());

        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Repo repo = response.body();

                if(repo.isSuccess()){
                    //TODO Move to another dialog
                    final View next = inflater.inflate(R.layout.activity_signup_address_dialog, null);
                    alertBuilder.setView(next);
                    alertBuilder.show();
                    /*Intent mIntent = new Intent(signupInputActivity, SignupAddressActivity.class);
                    mIntent.putExtra("userName", mFirstNameEditText.getText().toString() + mNameEditText.getText().toString());
                    mIntent.putExtra("email", mEmailEditText.getText().toString());
                    mIntent.putExtra("password", mPasswordCheckEditText.getText().toString());
                    mIntent.putExtra("birth",mBirthEditText.getText().toString());
                    startActivity(mIntent);*/
                }

                else {
                    Toast.makeText(SignUpDialog.this, "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }


}
