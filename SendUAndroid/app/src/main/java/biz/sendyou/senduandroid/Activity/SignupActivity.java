package biz.sendyou.senduandroid.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drivemode.android.typeface.TypefaceHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.Usr;

public class SignupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private CallbackManager mCallBackManager;
    private LoginButton mFacebookSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleSignInButton;
    private int mSignInRequestCode = 0;
    public static SignupActivity mSignupActivity;
    private String LOGTAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(Usr.getContext());
        setContentView(R.layout.activity_signup);
        mCallBackManager = CallbackManager.Factory.create();

        findViewById(R.id.gmail_btn).setOnClickListener(this);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        TextView mTextView02 = (TextView) findViewById(R.id.Select);

        mFacebookSignInButton = (LoginButton) findViewById(R.id.fb_btn);
        mGoogleSignInButton = (SignInButton) findViewById(R.id.gmail_btn);
        ImageView mEmailSignInButton = (ImageView) findViewById(R.id.email_btn);
        mFacebookSignInButton.setReadPermissions("email");
        mFacebookSignInButton.setReadPermissions("public_profile");
        //ImageView mGGButton = (ImageView) findViewById(R.id.gmail_btn);
        //ImageView mEmButton = (ImageView) findViewById(R.id.email_btn);

        mFacebookSignInButton.setBackgroundResource(R.drawable.sg_fb);
        mFacebookSignInButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mFacebookSignInButton.setText("");
        mGoogleSignInButton.setBackgroundResource(R.drawable.sg_gmail);
        //mGoogleSignInButton.setVisibility(View.INVISIBLE);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignActivity();
            }
        });

        mFacebookSignInButton.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final Profile mProfile = Profile.getCurrentProfile();
                callSignUpInputActivity();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gmail_btn:
                if (mGoogleApiClient == null) {
                    GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();

                    mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                            //.enableAutoManage(SignupActivity.mSignupActivity, this)
                            .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                            .build();
                }

                if (view.getId() == R.id.gmail_btn) {
                    googleSignIn();
                } else {

                }
                break;
        }
    }

    private void googleSignIn() {
        Intent mSignIn = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mSignIn, mSignInRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mSignInRequestCode) {
            Log.e(LOGTAG,String.valueOf(requestCode));
            Log.e(LOGTAG,String.valueOf(mSignInRequestCode));
            GoogleSignInResult mGoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data); //수정필요한듯
            Log.e(LOGTAG,Boolean.toString(mGoogleSignInResult.isSuccess()));
            if (mGoogleSignInResult.isSuccess()) {
                GoogleSignInAccount mGoogleSignInAccount = mGoogleSignInResult.getSignInAccount();
                callSignUpInputActivity();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static void callSignUpInputActivity() {
        Intent mIntent = new Intent(SignupActivity.mSignupActivity, SignupInputActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Usr.getContext().startActivity(mIntent);
    }

    private void callSignActivity() {
        Intent mIntent = new Intent(SignupActivity.this,SignupInputActivity.class);
        startActivity(mIntent);
        finish();
    }
}
