package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drivemode.android.typeface.TypefaceHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.Service.LoginService;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.User;
import biz.sendyou.senduandroid.Service.UserInfo;
import biz.sendyou.senduandroid.Service.Usr;
import biz.sendyou.senduandroid.Service.UsrInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditText01;
    private String LOGTAG = "LoginActivity";
    private EditText mEditText02;
    private static final String URL = "http://52.78.164.69:3000/";
    private String jsonStr = null;
    public static LoginActivity loginActivity;
    public static Activity la;
    private String usrName, numAdd,address;
    public static String email,token;
    private static Drawable sBackground;
    private static RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout = (RelativeLayout)findViewById(R.id.activity_login_background);

        if(sBackground == null) {
            sBackground = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.sp_back2));
            layout.setBackgroundDrawable(sBackground);
        }

        loginActivity = this;
        la = this;

        TypefaceHelper.initialize(getApplication());
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        mEditText01 = (EditText)findViewById(R.id.idedit);
        mEditText02 = (EditText)findViewById(R.id.pwedit);
        final CheckBox mCheckBox01 = (CheckBox)findViewById(R.id.autoLogin);

        Button mButton = (Button)findViewById(R.id.loginButton);
        assert mButton != null;

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText01.getText().toString().matches("") || mEditText02.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(),"아이디 또는 비밀번호를 확인해주세요.",Toast.LENGTH_LONG).show();
                }
                else {
                    doLogin();
                    //getUsrInfo("enoxaiming@naver.com","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0NzQ4OTc4NzIsImV4cCI6MTQ3NDk4NDI3Mn0.g7JIoTwzxOuHmq4AUAiNwcjvLHRrPbTv2pCsaR1U6ks");
                    email = mEditText01.getText().toString();

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

    private void doLogin() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService loginService = retrofit.create(LoginService.class);

        Call<Repo> call = loginService.doLogin(mEditText01.getText().toString(),mEditText02.getText().toString());
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Repo repo = response.body();
                if(repo.isSuccess()) {
                    token = repo.getToken();
                    Log.e("toke",repo.getToken());
                    getUsrInfo(mEditText01.getText().toString(),token);
                }
                else{
                    Toast.makeText(ContextManager.getP(),"로그인에 실패하였습니다. 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }

    public void getUsrInfo(String mail, String tk) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsrInfo usrInfo = retrofit.create(UsrInfo.class);

        Call<ArrayList<User>> call = usrInfo.getUsrInfo(mail,tk);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                ArrayList<User> user = response.body();

                Log.e("size",String.valueOf(user.size()));

                Log.e("user",user.get(user.size()-1).getUserName());

                Log.e("response",response.raw().toString());
                Log.e("res",response.message());
                Intent mIntent = new Intent(LoginActivity.loginActivity, NavigationDrawerActivity.class);
                mIntent.putExtra("username",user.get(user.size()-1).getUserName());
                mIntent.putExtra("numAddress",user.get(user.size()-1).getNumAddress());
                mIntent.putExtra("address",user.get(user.size()-1).getAddress());
                String userName = user.get(user.size()-1).getUserName();
                String numAddress = user.get(user.size()-1).getNumAddress();
                String address = user.get(user.size()-1).getAddress();
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ContextManager.getP().startActivity(mIntent);

                View view = (View)getLayoutInflater().inflate(R.layout.nav_header_navigation_drawer,null);
                TextView usrName = (TextView) view.findViewById(R.id.username);
                usrName.setText(userName);
                TextView place = (TextView) view.findViewById(R.id.textView3);
                place.setText(address);
                TextView num = (TextView) view.findViewById(R.id.textView);
                num.setText(numAddress);

                finish();
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    protected void onDestroy() {
        Log.w(LOGTAG, "Destroy background");
        recycleView(findViewById(R.id.activity_login_background));
        super.onDestroy();
    }

    private void recycleView(View view) {
        if(view != null) {
            Drawable bg = view.getBackground();
            if(bg != null) {
                ((BitmapDrawable)bg).getBitmap().recycle();
                System.gc();
                view.setBackgroundDrawable(null);
            }
            bg.setCallback(null);
        }
    }

}



