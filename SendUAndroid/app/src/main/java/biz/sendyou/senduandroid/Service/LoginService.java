package biz.sendyou.senduandroid.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by JunHyeok on 2016. 9. 8..
 */

public interface LoginService {
    @GET("userAuth/authenticate")
    Call<Repo> doLogin(@Query("email")String x,
                       @Query("password") String key);
}
