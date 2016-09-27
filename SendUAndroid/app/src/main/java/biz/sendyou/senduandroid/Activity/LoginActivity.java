package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.Service.LoginService;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.thread.TemplateDownloadThread;
import biz.sendyou.senduandroid.Service.UsrInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditText01;
    private String LOGTAG = "LoginActivity";
    private EditText mEditText02;
    private static final String URL = "http://52.78.159.163:3000/";
    private String jsonStr = null;
    public static LoginActivity loginActivity;
    public static Activity la;
    private String usrName, numAdd,address;
    private ImageView imageView;
    public static String email,token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap background_image = BitmapFactory.decodeResource(getResources(), R.drawable.sp_back1, options);

        imageView = (ImageView)findViewById(R.id.login_background);
        imageView.setImageBitmap(background_image);

        background_image = null;

        loginActivity = this;
        la = this;

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
                    email = mEditText01.getText().toString();
                }
            }
        });

        TextView mTextView01 = (TextView)findViewById(R.id.SendU);
        TextView mTextView02 = (TextView)findViewById(R.id.SignUp1);
        TextView mTextView03 = (TextView)findViewById(R.id.SignUp2);

        mTextView03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveSignupActivity();
            }
        });

        Log.i(LOGTAG, "Get S3 lists");
        TemplateDownloadThread templateDownloadThread = new TemplateDownloadThread();
        try {
            templateDownloadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(LOGTAG, "raw_keys : ");
        Log.i(LOGTAG, templateDownloadThread.getRaw_keys().toString());

        Log.i(LOGTAG, "thumb_keys :");
        Log.i(LOGTAG, templateDownloadThread.getThumb_keys().toString());

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

        Call<Repo> call = usrInfo.getUsrInfo(mail,tk);

        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Repo repo = response.body();

                Intent mIntent = new Intent(LoginActivity.loginActivity, NavigationDrawerActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtra("username",repo.getUserName());
                mIntent.putExtra("numad",repo.getNumaddress());
                mIntent.putExtra("add",repo.getAddress());
                ContextManager.getP().startActivity(mIntent);
                finish();
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    protected void onDestroy() {
        Log.w(LOGTAG, "Destroy background");
        recycleView(imageView);
        super.onDestroy();
    }

    private void recycleView(ImageView view) {
        Drawable d = view.getDrawable();
        if(d instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            b.recycle();
            view.setImageBitmap(null);
            b = null;
        }
        d.setCallback(null);
        System.gc();
    }
}



