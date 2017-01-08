package sendyou.biz.senduandroid.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sendyou.biz.senduandroid.data.Response;

/**
 * Created by pyh42 on 2016-12-31.
 */

public interface CheckAccount {
    @GET("user/email/{uid}")
    Call<Response> checkAccount(@Path("uid") String uid);
}
