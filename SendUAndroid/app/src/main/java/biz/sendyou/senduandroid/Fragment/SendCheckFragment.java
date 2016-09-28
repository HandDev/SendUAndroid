package biz.sendyou.senduandroid.Fragment;

import android.os.Bundle;
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

    private List<SendCheckItem> mList;

    public static SendCheckFragment newInstance(){
        return new SendCheckFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View mView = inflater.inflate(R.layout.fragment_send_check_list, container, false);

        ImageView arrow = (ImageView)mView.findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrontFragment frontFragment = FrontFragment.newInstance();
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                getFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, frontFragment).commit();
            }
        });
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.sendcheck_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mView.getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mList = new ArrayList();
        getOrderList();
        Log.e("size",String.valueOf(mList.size()));
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

        Call<ArrayList<Orders>> call = orderList.getOrderList(LoginActivity.email);
        Log.e("email",LoginActivity.email);

        call.enqueue(new Callback<ArrayList<Orders>>() {
            @Override
            public void onResponse(Call<ArrayList<Orders>> call, Response<ArrayList<Orders>> response) {
                ArrayList<Orders> arrayList = response.body();
                Log.e("Log",String.valueOf(response.isSuccessful()));
                Log.e("Log",response.message());
                Log.e("Log",response.raw().toString());
                for(int i = 0; i < response.body().size(); i++) {
                    int image;
                    if(arrayList.get(i).getOrderStatus().equals("Ordered")) {
                        image = R.drawable.doing;
                    }
                    else if(arrayList.get(i).getOrderStatus().equals("Ordered")) {
                        image = R.drawable.todo;
                    }
                    else if(arrayList.get(i).getOrderStatus().equals("Ordered")) {
                        image = R.drawable.done;
                    }
                    else {
                        image = R.drawable.sample_image;
                    }
                    mList.add(new SendCheckItem("("+arrayList.get(i).getNumAddress()+") "+arrayList.get(i).getAddress(),arrayList.get(i).getReceiverName(),arrayList.get(i).getOrderDate(),arrayList.get(i).getOrderDate(),image));
                }


            }

            @Override
            public void onFailure(Call<ArrayList<Orders>> call, Throwable t) {

            }
        });
    }
}