package sendyou.biz.senduandroid.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sendyou.biz.senduandroid.data.Response;

/**
 * Created by pyh42 on 2017-02-18.
 */

public interface Order {
    @FormUrlEncoded
    @POST("orders/{Uid}")
    Call<Response> order(@Path("Uid") String Uid,
                         @Field("email") String email,
                         @Field("orderUUID") String orderUUID,
                         @Field("orderDate") String orderDate,
                         @Field("receiverName") String receiverName,
                         @Field("address") String address,
                         @Field("numAddress") String numAddress,
                         @Field("record") String record,
                         @Field("img") String img);
}
