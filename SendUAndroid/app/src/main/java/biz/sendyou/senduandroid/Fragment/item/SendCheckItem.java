package biz.sendyou.senduandroid.Fragment.item;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chan_Woo_Kim on 2016-07-31.
 */
public class SendCheckItem{
    String address, receiver, date, dueDate;
    int status_img;

    public String getAddress() {
        return address;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getDate() {
        return date;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getStatus_img() {
        return status_img;
    }

    public SendCheckItem(String address, String receiver, String date, String dueDate, int status_img) {
        this.address = address;
        this.receiver = receiver;
        this.date = date;
        this.dueDate = dueDate;
        this.status_img = status_img;
    }
}