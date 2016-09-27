package biz.sendyou.senduandroid.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import biz.sendyou.senduandroid.Activity.LoginActivity;
import biz.sendyou.senduandroid.Fragment.item.SendCheckItem;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.OrderList;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.doOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chan_Woo_Kim on 2016-07-31.
 */
public class SendCheckFragment extends Fragment {

    private static final String URL = "http://52.78.159.163:8080/";

    public static SendCheckFragment newInstance(){
        return new SendCheckFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_send_check_list, container, false);

        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.sendcheck_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mView.getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        getOrderList();

        List<SendCheckItem> mList = new ArrayList();
        mList.add(new SendCheckItem("(15255) 경기도 안산시 단원구 사세충열로 94 한국디지털미디어고등학교", "김수아", "2016.07.25", "2016.07.26", R.drawable.doing));
        mList.add(new SendCheckItem("(15255) 경기도 안산시 단원구 사세충열로 94 한국디지털미디어고등학교", "김지우", "2016.07.25", "2016.07.26", R.drawable.todo));
        mList.add(new SendCheckItem("(15255) 경기도 안산시 단원구 사세충열로 94 한국디지털미디어고등학교", "강진아", "2016.07.25", "2016.07.26", R.drawable.done));
        mList.add(new SendCheckItem("(15255) 경기도 안산시 단원구 사세충열로 94 한국디지털미디어고등학교", "김수아", "2016.07.25", "2016.07.26", R.drawable.doing));
        mList.add(new SendCheckItem("(15255) 경기도 안산시 단원구 사세충열로 94 한국디지털미디어고등학교", "김지우", "2016.07.25", "2016.07.26", R.drawable.todo));
        mList.add(new SendCheckItem("(15255) 경기도 안산시 단원구 사세충열로 94 한국디지털미디어고등학교", "강진아", "2016.07.25", "2016.07.26", R.drawable.done));

        mRecyclerView.setAdapter(new SendCheckRecyclerViewAdapter(mList, mView.getContext()));
        return mView;
    }

    @Override
    public void onPause() {
        Fragment mFragment = getFragmentManager().findFragmentByTag("SendCheckFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onPause();
    }

    private void getOrderList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderList orderList = retrofit.create(biz.sendyou.senduandroid.Service.OrderList.class);

        Call<Repo> call = orderList.getOrderList(LoginActivity.email);
        Log.e("email",LoginActivity.email);

        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Log.e("Log",String.valueOf(response.isSuccessful()));
                Log.e("Log",response.message());
                Log.e("Log",response.raw().toString());
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }
}