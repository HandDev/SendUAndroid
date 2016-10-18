package biz.sendyou.senduandroid.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.Usr;
import biz.sendyou.senduandroid.UserInfoManager;

/**
 * Created by pyh42 on 2016-10-06.
 */

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    public static GoogleApiClient mGoogleApiClient;
    private int status = 0;
    private UserInfoManager userInfoManager;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_test);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        putBitmap(R.id.login_background, R.drawable.sp_back2, 8);
        putBitmap(R.id.login_plain, R.drawable.icon, 1);

        TextView signup = (TextView) findViewById(R.id.SignUp2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpDialog signUpDialog = new SignUpDialog();
                signUpDialog.showDialog(v);
            }
        });

        // Google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();

        Button facebook_btn = (Button)findViewById(R.id.facebook_signin_button);
        Button google_btn = (Button)findViewById(R.id.google_signin_button);
        Button email_btn = (Button)findViewById(R.id.email_signin_button);

        facebook_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);
        email_btn.setOnClickListener(this);
    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult result) {

                final GraphRequest request;

                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            Log.e("TAG", "user: " + user.toString());
                            Log.e("TAG", "AccessToken: " + result.getAccessToken().getToken());
                            Log.i("ID", "ID : " + result.getAccessToken().getUserId());
                            Log.e("Name",user.toString());
                            setResult(RESULT_OK);
                            try {
                                userInfoManager.setUserName(user.getString("Name"));
                                userInfoManager.setEmail(user.getString("Email"));
                            } catch (JSONException e) {
                                //TODO 이름확인
                            }
                        }
                    }
                });

                Log.e(TAG, "User ID: " + result.getAccessToken().getUserId());
                Log.e(TAG, "Auth Token: " + result.getAccessToken().getToken());
                intentActivty(SignInActivity.this, NavigationDrawerActivity.class);
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }

    private void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_signin_button :
                Log.w(TAG, "Google signin button clicked");
                status = 1;
                googleLogin();
                break;
            case R.id.facebook_signin_button :
                Log.w(TAG, "Facebook signin button clicked");
                status = 2;
                facebookLogin();
                break;
            case R.id.email_signin_button :
                status = 3;
                startActivity(new Intent(SignInActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    private void googleResult(GoogleSignInResult result) {
        GoogleSignInAccount acct = result.getSignInAccount();

        if(result.isSuccess()) {
            Log.w(TAG, "Google Login Success");
            Log.w(TAG, "User Id : " + acct.getId());

            Usr user = (Usr) getApplicationContext();
            user.setId(acct.getId());

            NavigationDrawerActivity.mGoogleApiClient = mGoogleApiClient;
            intentActivty(SignInActivity.this, NavigationDrawerActivity.class);
        }
        else {
            Log.w(TAG, "Google Login Failed");
            Log.w(TAG, result.toString());

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    Log.w(TAG, "Google Logout");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (status) {
            case 1 :
                googleResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
                break;
            case 2 :
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
            case 3 :
                break;
        }
    }

    public void intentActivty(Context packageContext, Class cls) {
        Intent mIntent = new Intent(packageContext, cls);
        startActivity(mIntent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleView(R.id.login_background);
        recycleView(R.id.login_plain);
    }

    private void putBitmap(int imageViewId, int drawableId, int scale) {
        ImageView imageView = (ImageView)findViewById(imageViewId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), drawableId, options));
    }

    private void recycleView(int id) {
        ImageView view = (ImageView)findViewById(id);

        Drawable d = view.getDrawable();
        if(d instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            view.setImageBitmap(null);
            b.recycle();
            b = null;
        }
        d.setCallback(null);
        System.gc();
        Runtime.getRuntime().gc();
    }
}
