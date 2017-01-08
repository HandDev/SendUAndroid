package sendyou.biz.senduandroid.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sendyou.biz.senduandroid.data.Address;

/**
 * Created by pyh42 on 2017-01-01.
 */

public interface SearchAddress {
    @GET("post/search.php")
    Call<Address> searchAddress(@Query("q") String keyword, @Query("v") String ver, @Query("ref") String link);
}
