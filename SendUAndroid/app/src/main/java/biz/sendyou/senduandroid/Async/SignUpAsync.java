package biz.sendyou.senduandroid.Async;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import biz.sendyou.senduandroid.Util.HttpUtil;
import biz.sendyou.senduandroid.datatype.UserInfo;

/**
 * Created by JunHyeok on 2016. 8. 23..
 */
public class SignUpAsync extends AsyncTask<UserInfo, Void, Void> {
    private String URL = "http://sendyou.biz/user/signup/insertData";
    private String res ="";
    @Override
    protected Void doInBackground(UserInfo... params) {

        UserInfo userInfo = (UserInfo)params[0];


        try {
            Map<String,String> param = new HashMap<>();
            param.put("userName",userInfo.getUserName());
            param.put("password",userInfo.getPassword());
            param.put("email",userInfo.getEmail());
            param.put("phone",userInfo.getPhone());
            param.put("address",userInfo.getAddress());
            param.put("id",userInfo.getID());
            param.put("birth",userInfo.getBirth());
            res = HttpUtil.postForm(URL,param);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Toast.makeText(MainActivity.MainActivityContext, res, Toast.LENGTH_LONG).show();
    }
}
