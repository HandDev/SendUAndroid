package sendyou.biz.senduandroid.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import sendyou.biz.senduandroid.data.Response;

/**
 * Created by JunHyeok on 2016. 9. 8..
 */

public interface SignUp {
    @FormUrlEncoded
    @POST("user/signup/insertData")
    Call<Response> doSignup(
            @Field("username") String userName,
            @Field("password") String password,
            @Field("email") String uid,
            @Field("numaddress") String numadd,
            @Field("address") String address,
            @Field("birth") String birth);
}
