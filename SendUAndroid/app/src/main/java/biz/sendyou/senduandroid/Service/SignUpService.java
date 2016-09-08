package biz.sendyou.senduandroid.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by JunHyeok on 2016. 9. 8..
 */

public interface SignUpService {
    @POST("insertData")
    Call<Repo> doSignup(
            @Field("userName") String userName,
            @Field("password") String password,
            @Field("email") String email,
            @Field("address") String address,
            @Field("birth") String birth);
}
