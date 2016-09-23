package biz.sendyou.senduandroid.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import biz.sendyou.senduandroid.Service.LoginService;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditText01;
    private String LOGTAG = "LoginActivity";
    private EditText mEditText02;
    public static LoginActivity loginActivity;
    public static Context mLoginActivityContext;
    private static final String URL = "http://sendyou.biz/userAuth/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sp_back1, options);
        int bHeight = bitmap.getHeight();
        int bWidth = bitmap.getWidth();

        Bitmap resize = resizeBitmap(bitmap, bWidth, bHeight);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_login_background);
        layout.setBackground(new BitmapDrawable(resize));

        TypefaceHelper.initialize(getApplication());
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();
        loginActivity = this;

        mEditText01 = (EditText)findViewById(R.id.idedit);
        mEditText02 = (EditText)findViewById(R.id.pwedit);
        final CheckBox mCheckBox01 = (CheckBox)findViewById(R.id.autoLogin);
        mLoginActivityContext = LoginActivity.this;

        Button mButton = (Button)findViewById(R.id.loginButton);
        assert mButton != null;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText01.getText().toString().matches("") || mEditText02.getText().toString().matches("")) {
                    Toast.makeText(getBaseContext(),"아이디 또는 비밀번호를 확인해주세요.",Toast.LENGTH_LONG).show();
                }
                else {
                    //doLogin();
                    callNaviation();
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

    private Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        if(height > 720) {
            return Bitmap.createScaledBitmap(bitmap, (width * 720) / height, 720, true);
        }
        return bitmap;
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
                /*
                Log.e("Repo",String.valueOf(repo.isSuccess()));
                Log.e("Repo",response.raw().toString());
                Log.e("Repo",response.message());
                */
                if(repo.isSuccess() ) {
                    callNaviation();
                }
                else{
                    Toast.makeText(LoginActivity.mLoginActivityContext,"로그인에 실패하였습니다. 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }

    public void callNaviation() {
        Intent mIntent = new Intent(LoginActivity.loginActivity, NavigationDrawerActivity.class);
        mLoginActivityContext.startActivity(mIntent);
        finish();
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



