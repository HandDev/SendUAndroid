package biz.sendyou.senduandroid.Service;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by JunHyeok on 2016. 9. 26..
 */

public interface UsrInfo {
    @GET("api/user/{Email}")
    Call<ArrayList<User>> getUsrInfo(
            @Path("Email") String email,
            @Query("token") String token);
}
