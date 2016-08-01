package biz.sendyou.senduandroid.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import biz.sendyou.senduandroid.Fragment.item.SendCheckItem;
import biz.sendyou.senduandroid.R;

/**
 * Created by Chan_Woo_Kim on 2016-07-31.
 */
public class SendCheckFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_send_check_list, container, false);

        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.sendcheck_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mView.getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

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
}