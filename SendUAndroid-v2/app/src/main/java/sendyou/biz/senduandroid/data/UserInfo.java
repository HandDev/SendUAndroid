package sendyou.biz.senduandroid.data;

/**
 * Created by pyh42 on 2016-12-22.
 */

public class UserInfo {
    private String Uid;
    private String email;
    private String token;
    private String userName;
    private String numAddress;
    private String jusoAddress;
    private String phone;
    private int status;
    private int templateid = 1000;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public String getJusoAddress() {
        return jusoAddress;
    }

    public void setJusoAddress(String jusoAddress) {
        this.jusoAddress = jusoAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTemplateid() {
        return templateid;
    }

    public void setTemplateid(int templateid) {
        this.templateid = templateid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
