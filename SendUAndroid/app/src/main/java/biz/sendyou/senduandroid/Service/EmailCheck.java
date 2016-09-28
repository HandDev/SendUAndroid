package biz.sendyou.senduandroid.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by JunHyeok on 2016. 9. 23..
 */

public interface EmailCheck {
    @GET("user/email/{Email}")
    Call<Repo> emailck(@Path("Email") String email);
}

