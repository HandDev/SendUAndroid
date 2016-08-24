package biz.sendyou.senduandroid.Activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import biz.sendyou.senduandroid.R;

public class SignupActivity extends AppCompatActivity {

    private CallbackManager mCallBackManager;
    private LoginButton mFacebookSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_signup);
        mCallBackManager = CallbackManager.Factory.create();

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.hide();

        TextView mTextView01 = (TextView)findViewById(R.id.sendu);
        TextView mTextView02 = (TextView)findViewById(R.id.Select);

        TypefaceHelper.getInstance().setTypeface(mTextView01,"NotoSansCJKkr-Regular.otf");
        TypefaceHelper.getInstance().setTypeface(mTextView02,"NotoSansCJKkr-Regular.otf");

        mFacebookSignInButton = (LoginButton) findViewById(R.id.fb_btn);
        mGoogleSignInButton = (SignInButton) findViewById(R.id.gmail_btn);
        mFacebookSignInButton.setReadPermissions("email");
        //ImageView mGGButton = (ImageView) findViewById(R.id.gmail_btn);
        //ImageView mEmButton = (ImageView) findViewById(R.id.email_btn);

        mFacebookSignInButton.setBackgroundResource(R.drawable.sg_fb);
        mFacebookSignInButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mFacebookSignInButton.setText("");
        mGoogleSignInButton.setBackgroundResource(R.drawable.sg_gmail);

        mFacebookSignInButton.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final Profile mProfile = Profile.getCurrentProfile();
                Snackbar.make(getCurrentFocus(), mProfile.getName(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });






    }
}
