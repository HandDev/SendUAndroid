package sendyou.biz.senduandroid.data;

/**
 * Created by pyh42 on 2017-01-09.
 */

public class UserProfile {
    private String _id;

    private String address;

    private String email;

    private String __v;

    private String userName;

    private String numAddress;

    private String password;

    private String phone;

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String get__v ()
    {
        return __v;
    }

    public void set__v (String __v)
    {
        this.__v = __v;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getNumAddress ()
    {
        return numAddress;
    }

    public void setNumAddress (String numAddress)
    {
        this.numAddress = numAddress;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [_id = "+_id+", address = "+address+", email = "+email+", __v = "+__v+", userName = "+userName+", numAddress = "+numAddress+", password = "+password+", phone = "+phone+"]";
    }
}
