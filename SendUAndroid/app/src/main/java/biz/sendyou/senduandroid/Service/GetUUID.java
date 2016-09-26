package biz.sendyou.senduandroid.Service;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by JunHyeok on 2016. 9. 23..
 */

public interface GetUUID {
    @POST("orderuuid/{email}")
    Call<Repo> doUUID(@Query("email") String email);
}
