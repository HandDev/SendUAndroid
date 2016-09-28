package biz.sendyou.senduandroid;

/**
 * Created by parkjaesung on 2016. 9. 28..
 */
//singleton pattern
public class UserInfoManager {
    private static UserInfoManager instance = new UserInfoManager();

    private String email;
    private String token;
    private String userName;
    private String numAddress;
    private String jusoAddress;


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

    public String getJusoAddress() {
        return jusoAddress;
    }

    public void setJusoAddress(String jusoAddress) {
        this.jusoAddress = jusoAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumAddress() {
        return numAddress;
    }

    public void setNumAddress(String numAddress) {
        this.numAddress = numAddress;
    }

}
