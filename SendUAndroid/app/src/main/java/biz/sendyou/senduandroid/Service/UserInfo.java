package biz.sendyou.senduandroid.Service;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JunHyeok on 2016. 9. 27..
 */

public class UserInfo {
    private String id;
    private String address;
    private String birth;
    private String email;
    private String password;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setBirth(String birth){
        this.birth = birth;
    }
    public String getBirth(){
        return this.birth;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

}
