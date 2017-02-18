package sendyou.biz.senduandroid.service;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sendyou.biz.senduandroid.data.UserInfo;

/**
 * Created by pyh42 on 2017-02-18.
 */

public interface RegisterToken {
    @PUT("users/{Uid}/firebase/{token}")
    Call<UserInfo> register(@Path("Uid") String uid, @Path("token") String token);
}
