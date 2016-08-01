package biz.sendyou.senduandroid.datatype;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by parkjaesung on 2016. 7. 29..
 */
public class Address extends RealmObject implements RealmModel {

    //TODO Re-construct Address class
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
