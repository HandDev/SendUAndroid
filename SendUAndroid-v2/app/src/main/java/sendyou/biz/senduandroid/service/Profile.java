package sendyou.biz.senduandroid.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sendyou.biz.senduandroid.data.UserProfile;

/**
 * Created by pyh42 on 2017-01-09.
 */

public interface Profile {
    @GET("/users/")
    Call<UserProfile> getProfile(@Query("q") String uid);
}
