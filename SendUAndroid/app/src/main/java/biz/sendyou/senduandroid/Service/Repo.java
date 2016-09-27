package biz.sendyou.senduandroid.Service;

/**
 * Created by JunHyeok on 2016. 9. 8..
 */

public class Repo {
    private String message;
    private boolean success;
    private String token;
    private String orderUUID;
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    public String getNumaddress() {
        return numaddress;
    }

    public void setNumaddress(String numaddress) {
        this.numaddress = numaddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String numaddress;
    private String address;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean isSuccess()
    {
        return this.success;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
    public void setOrderUUID(String orderUUID) {
        this.orderUUID = orderUUID;
    }
    public String getOrderUUID() {
        return this.orderUUID;
    }
}
