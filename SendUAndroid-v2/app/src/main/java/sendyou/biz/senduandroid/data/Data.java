package sendyou.biz.senduandroid.data;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by pyh42 on 2016-12-22.
 */

public class Data extends Application {
    private static UserInfo userInfo = new UserInfo();

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        Data.userInfo = userInfo;
    }
}
