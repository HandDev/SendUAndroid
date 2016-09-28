package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

import biz.sendyou.senduandroid.ActivityManager;
import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.LoginService;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.User;
import biz.sendyou.senduandroid.Service.UsrInfo;
import biz.sendyou.senduandroid.URLManager;
import biz.sendyou.senduandroid.UserInfoManager;
import biz.sendyou.senduandroid.thread.TemplateDownloadThread;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    //쓸데 없는 변수는 모두 지웁시다.ㅜㅜ
    private EditText mEditText01;
    private String LOGTAG = "LoginActivity";
    private EditText mEditText02;

    private UserInfoManager userInfoManager;
    //TODO Remove static variable
    //이걸 왜 스태틱으로
    //다른 액티비티에서 참조한다고 하면 차라리 싱글톤 객체 하나만들어서 저장해야 깔끔하지
    //토큰과 이메일은 다른 객체에서도 참조해야하는것이니..
    public static String email,token;
    private ImageView imageView;

    //안쓰는 변수아닌가?
    //private Bitmap background_src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoManager = UserInfoManager.getInstance();

        ActivityManager.getInstance().setLoginActivity(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap background_image = BitmapFactory.decodeResource(getResources(), R.drawable.sp_back1, options);

        imageView = (ImageView)findViewById(R.id.login_background);
        imageView.setImageBitmap(background_image);

        background_image = null;

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
                    doLogin(mEditText01.getText().toString(), mEditText02.getText().toString());
                    //doLogin은 콜백 설정하는건데 왜.. 바로 아래에다가 메서드 호출을..

                    //로그인이 성공해야 그 이메일이 유효하다는 뜻이니 로그인 성공할 경우에 데이터를 저장하져
                    //email = mEditText01.getText().toString();
                }
            }
        });

        //이건 모두 DI로 해결합시다
        TextView mTextView01 = (TextView)findViewById(R.id.SendU);
        TextView mTextView02 = (TextView)findViewById(R.id.SignUp1);
        TextView mTextView03 = (TextView)findViewById(R.id.SignUp2);

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

        //SignUpActivity에서 LoginActivity를 종료하지말고 여기서 종료하면되는거아닌가?
        //LoginActivity.this.finish();
        finish();

    }

    //바로 객체에서 텍스트를 가져오면 가독성도 떨어지고 메서드 재활용성이 낮아질듯
    private void doLogin(final String email, String password) {
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
                    userInfoManager.setEmail(email);
                    //token = repo.getToken();
                    userInfoManager.setToken(repo.getToken());

                    Log.e("token",repo.getToken());
                    getUsrInfo(email,userInfoManager.getToken());
                }
                else{
                    Toast.makeText(ContextManager.getContext(),"로그인에 실패하였습니다. 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                }
            }

            //예외 처리는 필수염
            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

                    Log.e(LOGTAG, "Login on Failure");
                    Toast.makeText(ContextManager.getContext(),"Login On Failure.",Toast.LENGTH_LONG).show();

            }
        });
    }

    //변수이름도 통일해야 가독성이 높아지겠네여
    public void getUsrInfo(String email, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsrInfo usrInfo = retrofit.create(UsrInfo.class);

        Call<ArrayList<User>> call = usrInfo.getUsrInfo(email,token);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                ArrayList<User> user = response.body();

                //로그로 출력하는 정보들은 태그를 통일해야 디버깅이 쉽겠져?
                // + 로그 출력 레벨을 구분시킵시다
                Log.i(LOGTAG,"size : " + String.valueOf(user.size()));

                Log.i(LOGTAG,"user : " + user.get(user.size()-1).getUserName());

                Log.i(LOGTAG,"response : " + response.raw().toString());
                Log.i(LOGTAG,"res : " + response.message());
                Intent mIntent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                mIntent.putExtra("username",user.get(user.size()-1).getUserName());
                mIntent.putExtra("numAddress",user.get(user.size()-1).getNumAddress());
                mIntent.putExtra("address",user.get(user.size()-1).getAddress());
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ContextManager.getContext().startActivity(mIntent);

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



