package biz.sendyou.senduandroid.Service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by JunHyeok on 2016. 9. 26..
 */

public interface OrderList {
    @GET("{Email}/orders")
    Call<ArrayList<Orders>> getOrderList(@Path("Email") String email);
}
