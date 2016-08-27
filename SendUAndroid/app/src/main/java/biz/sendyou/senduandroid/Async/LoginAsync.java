package biz.sendyou.senduandroid.Async;

/**
 * Created by JunHyeok on 2016. 6. 10..
 */
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;

import biz.sendyou.senduandroid.Activity.LoginActivity;
import biz.sendyou.senduandroid.Activity.OnBoardingActivity;
import biz.sendyou.senduandroid.Util.HttpUtil;
import biz.sendyou.senduandroid.datatype.UserInfo;

public class LoginAsync extends AsyncTask<UserInfo, Void, Void> {

    private String URL = "http://sendyou.biz/userAuth/authenticate";
    private String res ="";
    private final String LOGTAG = "LOGINASYNC";
    private boolean isAutoLogin = false;
    private UserInfo userInfo = null;

    private ProgressDialog mProgressDialog;


    //TODO Create Loading Alert


    @Override
    protected void onPreExecute() {
        mProgressDialog = ProgressDialog.show(LoginActivity.mLoginActivityContext, "", "로그인 중", true);
    }

    @Override
    protected Void doInBackground(UserInfo... params) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UserInfo userInfo = (UserInfo)params[0];

        URL = URL + "?email=" + userInfo.getEmail() + "&password=" + userInfo.getPassword();

        try {
            Log.e(LOGTAG,URL);
            res = HttpUtil.get(URL);
        } catch (IOException e){
            e.printStackTrace();

            //TODO alert user that phone isn't connected to network
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Toast.makeText(LoginActivity.mLoginActivityContext, res, Toast.LENGTH_LONG).show();

        //close progress dialog
        mProgressDialog.dismiss();

        Boolean isLoginSuccess = false;

        try {
            JSONObject jsonObject = new JSONObject(res);
            Log.e(LOGTAG,res);
            isLoginSuccess = jsonObject.getBoolean("success");
            Log.i(LOGTAG, isLoginSuccess.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(isLoginSuccess) {
            OnBoardingActivity.callNavigationDrawerActivity();
        }
    }


}