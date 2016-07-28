package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashMap;
import java.util.Map;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Util.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    String URL = "http://sendyou.biz/userAuth/signup/insertData";
    String res = "";

    private int mSignInRequestCode = 0;
    private GoogleApiClient mGoogleApiClient;
    private LoginButton mFacebookSignInButton;
    private CallbackManager mCallBackManager;


    private OnFragmentInteractionListener mListener;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallBackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.googleSignIn_button).setOnClickListener(this);
        getActivity().findViewById(R.id.googleSignOut_button).setOnClickListener(this);
        getActivity().findViewById(R.id.faceBookSignIn_button).setOnClickListener(this);

        mFacebookSignInButton = (LoginButton) getActivity().findViewById(R.id.faceBookSignIn_button);
        mFacebookSignInButton.setReadPermissions("user_friends");
        mFacebookSignInButton.setFragment(this);

        mFacebookSignInButton.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                final Profile mProfile = Profile.getCurrentProfile();
                Snackbar.make(getView(), mProfile.getName(), Snackbar.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        try {
                            Map<String, String> params = new HashMap();
                            params.put("username", "하준혁하아하아");
                            params.put("password", loginResult.getAccessToken().getUserId());
                            res = HttpUtil.postForm(URL, params);
                            Log.w("Log",res);
                        } catch (Exception e){
                            Log.w("Log",e);
                            Toast.makeText(getActivity().getBaseContext(),e+"",Toast.LENGTH_LONG).show();
                        }

                    }
                }).start();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(),"Canceled",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.w("Log",error);
            }
        });

        updateUI(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // UI 변경
    private void updateUI(boolean bool) {
        if (bool) {
            getActivity().findViewById(R.id.googleSignOut_button).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.googleSignIn_button).setVisibility(View.GONE);
        } else {
            getActivity().findViewById(R.id.googleSignOut_button).setVisibility(View.GONE);
            getActivity().findViewById(R.id.googleSignIn_button).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.googleSignIn_button:
            case R.id.googleSignOut_button:
                if (mGoogleApiClient == null) {
                    GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();

                    mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                            .enableAutoManage((FragmentActivity) getActivity(), this)
                            .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                            .build();
                }

                if (view.getId() == R.id.googleSignIn_button) {
                    googleSignIn();
                } else {
                    googleSignOut();
                }
                break;
        }
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Snackbar.make(getView(), "SignOut", Snackbar.LENGTH_SHORT).show();
                updateUI(false);
            }
        });
    }

    private void googleSignIn() {
        Intent mSignIn = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mSignIn, mSignInRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mSignInRequestCode) {
            GoogleSignInResult mGoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (mGoogleSignInResult.isSuccess()) {
                GoogleSignInAccount mGoogleSignInAccount = mGoogleSignInResult.getSignInAccount();
                Snackbar.make(getView(), mGoogleSignInAccount.getDisplayName(), Snackbar.LENGTH_SHORT).show();

                updateUI(true);
            }
        }
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
