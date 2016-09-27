package biz.sendyou.senduandroid.Service;

/**
 * Created by JunHyeok on 2016. 9. 8..
 */

public class Repo {
    private String message;
    private boolean success;
    private String token;
    private String orderUUID;

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
