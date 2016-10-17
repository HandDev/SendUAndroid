package biz.sendyou.senduandroid.Service;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import biz.sendyou.senduandroid.URLManager;
import biz.sendyou.senduandroid.UserInfoManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pyh42 on 2016-10-08.
 */

public class Usr extends Application {

    private final static String TAG = "User";
    private String name;
    private String id;
    private String pw;
    private String email;
    private String birth;
    private String address;
    private String numAddress;
    private String facebookToken;
    private String googleToken;
    private String token;
    public static Context context;

    @Override
    public void onCreate() {
        name = null;
        id = null;
        pw = null;
        email = null;
        birth = null;
        address = null;
        numAddress = null;
        facebookToken = null;
        googleToken = null;
        token = null;
        context = this;
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getBirth() {
        return birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getNumAddress() {
        return numAddress;
    }
    public void setNumAddress(String numAddress) {
        this.numAddress = numAddress;
    }
    public String getFacebookToken() {
        return facebookToken;
    }
    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public static Context getContext() {
        return context;
    }
    public String getGoogleToken() {
        return googleToken;
    }
    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public void doLogin(final String email, final String password, final View view) {
        Log.w(TAG, "Login Start");

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
                if (repo.isSuccess()) {
                    Log.w(TAG, "Login Success");
                    Usr user = (Usr)getApplicationContext();
                    user.setToken(repo.getToken());
                }
                else {
                    Log.w(TAG, "Login Failed");
                    Snackbar.make(view, "로그인에 실패하였습니다. 다시 확인해주세요.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.w(TAG, "Login onFailure");
                Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserInfo(String email, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsrInfo usrInfo = retrofit.create(UsrInfo.class);

        Call<ArrayList<User>> call = usrInfo.getUsrInfo(email, token);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                Log.w(TAG, "Get user info");

                ArrayList<User> user = response.body();

                Usr usr = (Usr)getApplicationContext();
                usr.setName(user.get(user.size()-1).getUserName());
                usr.setNumAddress(user.get(user.size()-1).getNumAddress());
                usr.setAddress(user.get(user.size()-1).getAddress());
                usr.setBirth(user.get(user.size()-1).getBirth());
                usr.setEmail(user.get(user.size()-1).getEmail());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.w(TAG, "Failed to get user info : " + t.getMessage());
            }
        });
    }
}
