package biz.sendyou.senduandroid.Fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import biz.sendyou.senduandroid.Activity.LoginActivity;
import biz.sendyou.senduandroid.Fragment.item.SendCheckItem;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.OrderList;
import biz.sendyou.senduandroid.Service.Orders;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.doOrder;
import biz.sendyou.senduandroid.URLManager;
import biz.sendyou.senduandroid.UserInfoManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chan_Woo_Kim on 2016-07-31.
 */
public class SendCheckFragment extends Fragment {

    //TODO 시간 형식 변경 및 듀데이트 반환 필요.

    private List<SendCheckItem> mList;
    private RecyclerView mRecyclerView;
    private View mView;

    public static SendCheckFragment newInstance(){
        return new SendCheckFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        mView = inflater.inflate(R.layout.fragment_send_check_list, container, false);

        ImageView arrow = (ImageView)mView.findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrontFragment frontFragment = FrontFragment.newInstance();
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                getFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, frontFragment).commit();
            }
        });
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.sendcheck_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mView.getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mList = new ArrayList();
        getOrderList();
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
                .baseUrl(URLManager.checkURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderList orderList = retrofit.create(biz.sendyou.senduandroid.Service.OrderList.class);

        Call<ArrayList<Orders>> call = orderList.getOrderList(UserInfoManager.getInstance().getEmail());


        call.enqueue(new Callback<ArrayList<Orders>>() {
            @Override
            public void onResponse(Call<ArrayList<Orders>> call, Response<ArrayList<Orders>> response) {
                ArrayList<Orders> arrayList = response.body();
                Log.e("Log",String.valueOf(response.isSuccessful()));
                Log.e("URL",response.raw().toString());
                Log.e("Size",String.valueOf(response.body().size()));
                for(int i = response.body().size()-1; i >= 0; i--) {
                    int image;
                    if(arrayList.get(i).getOrderStatus().equals("Ordered")) {
                        image = R.drawable.todo;
                    }
                    else if(arrayList.get(i).getOrderStatus().equals("Deliverying")) {
                        image = R.drawable.doing;
                    }
                    else if(arrayList.get(i).getOrderStatus().equals("Completed")) {
                        image = R.drawable.done;
                    }
                    else {
                        image = R.drawable.sample_image;
                    }
                    mList.add(new SendCheckItem("("+arrayList.get(i).getNumAddress()+") "+arrayList.get(i).getAddress(),arrayList.get(i).getReceiverName(),arrayList.get(i).getOrderDate(),arrayList.get(i).getOrderDate(),image));
                    mRecyclerView.setAdapter(new SendCheckRecyclerViewAdapter(mList, mView.getContext()));
                }


            }

            @Override
            public void onFailure(Call<ArrayList<Orders>> call, Throwable t) {

            }
        });
    }
}