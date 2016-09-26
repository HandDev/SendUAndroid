package biz.sendyou.senduandroid.Service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by JunHyeok on 2016. 9. 25..
 */

public interface DoOrder {
    @POST("order/{email}/{orderuuid}")
    Call<ResponseBody> doOrder(@Path("email") String email,
                               @Path("orderuuid") String uuid,
                               @Body RequestBody requestBody);
}
