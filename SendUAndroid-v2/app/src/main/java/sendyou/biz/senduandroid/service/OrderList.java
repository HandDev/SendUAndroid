package sendyou.biz.senduandroid.service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sendyou.biz.senduandroid.data.Orders;

/**
 * Created by JunHyeok on 2016. 9. 26..
 */

public interface OrderList {
    @GET("{Uid}/orders")
    Call<ArrayList<Orders>> getOrderList(@Path("Uid") String Uid);
}
