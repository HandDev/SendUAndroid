package biz.sendyou.senduandroid.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import biz.sendyou.senduandroid.ActivityManager;
import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.LoginService;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.User;
import biz.sendyou.senduandroid.Service.Usr;
import biz.sendyou.senduandroid.Service.UsrInfo;
import biz.sendyou.senduandroid.URLManager;
import biz.sendyou.senduandroid.UserInfoManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditText01;
    private String LOGTAG = "LoginActivity";
    private EditText mEditText02;
    private CheckBox mCheckBox01;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private UserInfoManager userInfoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(Usr.getContext());
        editor = prefs.edit();

        editor.putBoolean("Auto",false);
        editor.putBoolean("SAVED",false);
        editor.commit();

        putBitmap(R.id.login_background, R.drawable.sp_back2, 8);
        putBitmap(R.id.plane, R.drawable.icon, 1);

        userInfoManager = UserInfoManager.getInstance();

        ActivityManager.getInstance().setLoginActivity(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        mEditText01 = (EditText)findViewById(R.id.idedit);
        mEditText02 = (EditText)findViewById(R.id.pwedit);
        
        mCheckBox01 = (CheckBox)findViewById(R.id.autoLogin);

        Button mButton = (Button)findViewById(R.id.loginButton);
        assert mButton != null;

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText01.getText().toString().matches("") || mEditText02.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(),"아이디 또는 비밀번호를 확인해주세요.",Toast.LENGTH_LONG).show();
                } else {
                    editor.putBoolean("Auto",mCheckBox01.isChecked()).commit();
                    Log.e("CheckBox",String.valueOf(mCheckBox01.isChecked()));
                    Log.e("Auto",String.valueOf(prefs.getBoolean("Auto",false)));
                    doLogin(mEditText01.getText().toString(), mEditText02.getText().toString());
                }
            }
        });

        //TextView mTextView01 = (TextView)findViewById(R.id.SendU);
        TextView mTextView02 = (TextView)findViewById(R.id.SignUp1);
        TextView mTextView03 = (TextView)findViewById(R.id.SignUp2);

        mTextView03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpDialog signUpDialog = new SignUpDialog();
                signUpDialog.showDialog(view);
            }
        });

    }

    public void moveSignupActivity(){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);

        finish();
    }

    public void doLogin(final String email, final String password) {
        Log.i(LOGTAG, "doLogin Method");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService loginService = retrofit.create(LoginService.class);

        Call<Repo> call = loginService.doLogin(email, password);
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Repo repo = response.body();
                if(repo.isSuccess()) {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Usr.getContext());
                    SharedPreferences.Editor editor = pref.edit();
                    Log.e("auto",String.valueOf(pref.getBoolean("Auto",false)));
                    Log.e("SAVED",String.valueOf(pref.getBoolean("SAVED",false)));
                    Log.e("EMail",pref.getString("Email",""));
                    //TODO if문 수정 필요
                    if(pref.getBoolean("Auto",false)) {
                        if(pref.getBoolean("SAVED",false)) {
                            UserInfoManager userInfoManager = UserInfoManager.getInstance();
                            userInfoManager.setEmail(pref.getString("Email",""));
                            userInfoManager.setToken(pref.getString("Token",""));
                            getUsrInfo(userInfoManager.getEmail(),userInfoManager.getToken());
                        }
                        else {
                            userInfoManager.setEmail(email);
                            userInfoManager.setToken(repo.getToken());
                            getUsrInfo(email,userInfoManager.getToken());
                        }
                        editor.putString("Email",email);
                        editor.putString("password",password);
                        editor.putString("Token",repo.getToken());
                        editor.putBoolean("SAVED",true);
                        editor.commit();
                    }
                    else {
                        userInfoManager.setEmail(email);
                        userInfoManager.setToken(repo.getToken());
                        getUsrInfo(email,repo.getToken());
                    }
                }
                else{
                    Toast.makeText(Usr.getContext(),"로그인에 실패하였습니다. 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

                    Log.e(LOGTAG, "Login on Failure");
                    Toast.makeText(Usr.getContext(),"Login On Failure.",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getUsrInfo(String email, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsrInfo usrInfo = retrofit.create(UsrInfo.class);

        Call<ArrayList<User>> call = usrInfo.getUsrInfo(email, token);

        call.enqueue(new Callback<ArrayList<User>>() {

            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                ArrayList<User> user = response.body();

                Log.i(LOGTAG, "size : " + String.valueOf(user.size()));
                Log.i(LOGTAG, "user : " + user.get(user.size() - 1).getUserName());
                Log.i(LOGTAG, "response : " + response.raw().toString());
                Log.i(LOGTAG, "res : " + response.message());
                UserInfoManager.getInstance().setUserName(user.get(user.size() - 1).getUserName());
                UserInfoManager.getInstance().setNumAddress(user.get(user.size() - 1).getNumAddress());
                UserInfoManager.getInstance().setJusoAddress(user.get(user.size() - 1).getAddress());
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ContextManager.getContext());
                if (pref.getBoolean("SAVED", false)) {
                    callNavigationFromSplash();
                } else {
                    callNavigation();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void callNavigation() {
        Intent mIntent = new Intent(ActivityManager.getInstance().getLoginActivity(), NavigationDrawerActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Usr.getContext().startActivity(mIntent);
        finish();
    }

    private void callNavigationFromSplash() {
        Intent mIntent = new Intent(ActivityManager.getInstance().getSplashActivity(), NavigationDrawerActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Usr.getContext().startActivity(mIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.w(LOGTAG, "Destroy background");
        recycleView(R.id.login_background);
        recycleView(R.id.plane);
        recycleView(R.id.id_icon);
        recycleView(R.id.pw_icon);
        super.onDestroy();
    }

    private void recycleView(int id) {
        ImageView view = (ImageView)findViewById(id);
        Drawable d = view.getDrawable();
        if(d instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            b.recycle();
            view.setImageBitmap(null);
            b = null;
        }
        d.setCallback(null);
        System.gc();
        Runtime.getRuntime().gc();
    }

    private void putBitmap(int imageViewId, int drawableId, int scale) {
        ImageView imageView = (ImageView)findViewById(imageViewId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), drawableId, options));
    }
}



