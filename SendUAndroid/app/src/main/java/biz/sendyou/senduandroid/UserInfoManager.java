package biz.sendyou.senduandroid;

import biz.sendyou.senduandroid.Service.UserInfo;

/**
 * Created by parkjaesung on 2016. 9. 28..
 */
//singleton pattern
public class UserInfoManager {
    private static UserInfoManager instance = new UserInfoManager();

    private String email;
    private String token;

    private UserInfoManager(){

    }

    public static UserInfoManager getInstance(){
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
