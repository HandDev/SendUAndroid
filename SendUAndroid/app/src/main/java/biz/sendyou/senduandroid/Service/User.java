package biz.sendyou.senduandroid.Service;


import java.util.List;

/**
 * Created by JunHyeok on 2016. 9. 27..
 */

public class User {

    private String id;
    private String address;

    public String getNumAddress() {
        return numAddress;
    }

    public void setNumAddress(String numAddress) {
        this.numAddress = numAddress;
    }

    private String numAddress;
    private String birth;
    private String email;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

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

