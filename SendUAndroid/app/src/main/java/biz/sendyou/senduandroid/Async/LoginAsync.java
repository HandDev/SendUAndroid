package biz.sendyou.senduandroid.Async;

/**
 * Created by JunHyeok on 2016. 6. 10..
 */
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import biz.sendyou.senduandroid.Activity.OnBoardingActivity;
import biz.sendyou.senduandroid.Util.HttpUtil;
import biz.sendyou.senduandroid.datatype.UserInfo;

public class LoginAsync extends AsyncTask<UserInfo, Void, Void> {

    private String URL = "http://sendyou.biz/userAuth/authenticate";
    private String res ="";
    private final String LOGTAG = "LOGINASYNC";
    private boolean isAutoLogin = false;
    private UserInfo userInfo = null;

    @Override
    protected Void doInBackground(UserInfo... params) {

        UserInfo userInfo = (UserInfo)params[0];

        URL = URL + "?email=" + userInfo.getEmail() + "&password=" + userInfo.getPassword();

        try {
            /*Map<String,String> param = new HashMap<>();
            param.put("username",userInfo.getUserName());
            param.put("password",userInfo.getPassword());
            res = HttpUtil.postForm(URL,param);
            Log.e(LOGTAG,res);*/
            Log.e(LOGTAG,URL);
            res = HttpUtil.get(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Toast.makeText(LoginActivity.loginActivityContext, res, Toast.LENGTH_LONG).show();
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