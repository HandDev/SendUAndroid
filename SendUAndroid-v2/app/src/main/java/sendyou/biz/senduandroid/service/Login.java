package sendyou.biz.senduandroid.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sendyou.biz.senduandroid.data.Response;

/**
 * Created by pyh42 on 2017-01-03.
 */

public interface Login {
    @GET("userAuth/authenticate")
    Call<Response> doLogin(@Query("email")String x,
                           @Query("password") String key);
}
