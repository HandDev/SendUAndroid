package sendyou.biz.senduandroid.service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sendyou.biz.senduandroid.data.Orders;

/**
 * Created by pyh42 on 2017-01-03.
 */

public interface OrderList {
    @GET("orders/{Uid}")
    Call<ArrayList<Orders>> getOrderList(@Path("Uid") String Uid);
}
