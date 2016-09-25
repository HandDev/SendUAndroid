package biz.sendyou.senduandroid.Service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by JunHyeok on 2016. 9. 25..
 */

public interface DoOrder {
    @FormUrlEncoded
    @POST("order/{email}/{orderuuid}")
    Call<ResponseBody> doOrder(@Field("email") String email,
                               @Field("orderuuid") String uuid,
                               @Body RequestBody params);
}
