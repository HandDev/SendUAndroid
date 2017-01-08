package sendyou.biz.senduandroid.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.gson.Gson;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.net.URL;
import java.util.Arrays;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.Data;
import sendyou.biz.senduandroid.data.Response;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.service.CheckAccount;
import sendyou.biz.senduandroid.service.Login;

/**
 * Created by pyh42 on 2016-12-20.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private static final String TWITTER_KEY = "lYgQUfbXOCEj5cLtYHWsKlBh2";
    private static final String TWITTER_SECRET = "xLgqKCiOgwGJuEVz1hZis4XwMuzLR3h8dZzEncnvjmvOr67CXb";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager facebook_callbackManager;
    private TwitterAuthClient mTwitterAuthClient;
    private Data mData = (Data)getApplication();

    @BindView(R.id.login_background) ImageView login_background;
    @BindView(R.id.login_logo) ImageView login_logo;
    @BindView(R.id.google_login_button) Button google_login_btn;
    @BindView(R.id.facebook_login_button) Button facebook_login_btn;
    @BindView(R.id.twitter_login_button) Button twitter_login_btn;

    @BindString(R.string.exit_q) String exit_q;
    @BindString(R.string.confirm) String confirm;
    @BindString(R.string.cancel) String cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Log.w(TAG, "Set image resources in views");
        Glide.with(this).load(R.drawable.background).into(login_background);
        Glide.with(this).load(R.drawable.icon).into(login_logo);

        google_login_btn.setOnClickListener(this);
        facebook_login_btn.setOnClickListener(this);
        twitter_login_btn.setOnClickListener(this);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        facebook_callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebook_callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "Login");
                        mData.getUserInfo().setStatus(2);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        mTwitterAuthClient = new TwitterAuthClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.w(TAG, "Uid :" + user.getUid());
                    Log.w(TAG, "Name :" + user.getDisplayName());
                    Log.w(TAG, "Email :" + user.getEmail());

                    mData.getUserInfo().setUid(user.getUid());
                    mData.getUserInfo().setUserName(user.getDisplayName());

                    checkId(user.getUid());
                } else {
                    Log.w(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.w(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.w(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.w(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.w(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void google_SignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void facebook_SignIn() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    private void twitter_SignIn() {
        mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Log.w(TAG, "Twitter login success");
                mData.getUserInfo().setStatus(3);
                handleTwitterSession(twitterSessionResult.data);
            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException exception) {
                Log.w(TAG, "Twitter login failed");
                exception.printStackTrace();
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkId(final String uid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CheckAccount checkAccount = retrofit.create(CheckAccount.class);

        Call<Response> call = checkAccount.checkAccount(uid);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                final Response res = response.body();
                Log.w(TAG, res.isSuccess() + "");

                if(res.isSuccess()) {
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                } else {
                    Retrofit retrofit1 = new Retrofit.Builder()
                            .baseUrl(URLManager.authURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Login login = retrofit1.create(Login.class);

                    Call<Response> call1 = login.doLogin(uid, "sendu1234");

                    call1.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            Response res1 = response.body();

                            Log.w(TAG, "Login success " + res1.toString());

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            Log.w(TAG, "Login failed" + t.getStackTrace());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.w(TAG, "Login failed" + t.getStackTrace());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_login_button :
                google_SignIn();
                break;
            case R.id.facebook_login_button :
                facebook_SignIn();
                break;
            case R.id.twitter_login_button :
                twitter_SignIn();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebook_callbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                mData.getUserInfo().setStatus(1);
            } else {
                Log.w(TAG, "Google login failed " + result.getStatus());
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, connectionResult.getErrorMessage());
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "LoginActivity Destroy");
        recycleView(login_background);
        recycleView(login_logo);
        super.onDestroy();
    }

    private void recycleView(View view) {
        ImageView imageView = null;
        Log.w(TAG, "Recycle " + getResources().getResourceEntryName(view.getId()));

        if(view instanceof ImageView) {
            imageView = (ImageView)view;
            Drawable d = imageView.getDrawable();
            if(d instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable) d).getBitmap();
                imageView.setImageBitmap(null);
                b.recycle();
                b = null;
            }
            d.setCallback(null);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(exit_q)
                .setCancelable(false)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}