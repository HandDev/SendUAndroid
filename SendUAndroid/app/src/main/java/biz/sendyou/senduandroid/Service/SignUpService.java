package biz.sendyou.senduandroid.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by JunHyeok on 2016. 9. 8..
 */

public interface SignUpService {
    @FormUrlEncoded
    @POST("user/signup/insertData")
    Call<Repo> doSignup(
            @Field("username") String userName,
            @Field("password") String password,
            @Field("email") String email,
            @Field("numaddress") String numadd,
            @Field("address") String address,
            @Field("birth") String birth);
}
