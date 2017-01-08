package sendyou.biz.senduandroid.fragment;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.Data;
import sendyou.biz.senduandroid.data.Orders;
import sendyou.biz.senduandroid.data.TrackListData;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.service.OrderList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackFragment extends Fragment {

    private static final String TAG = "TrackFragment";
    public ListViewAdapter mAdapter = null;

    @BindView(R.id.swipyrefreshlayout) SwipyRefreshLayout refreshLayout;
    @BindView(R.id.listview) ListView mListView;

    public TrackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP) {

                } else if(direction == SwipyRefreshLayoutDirection.BOTTOM) {

                }
            }
        });

        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        mAdapter = new ListViewAdapter(getContext());
        mListView.setAdapter(mAdapter);
        loadList();

        return view;
    }

    private void cleanList() {
        while(true) {
            if(mAdapter.mListData.isEmpty()) {
                break;
            }
            mAdapter.remove(0);
        }
        refreshLayout.setRefreshing(false);
    }

    private void loadList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.checkURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderList orderList = retrofit.create(OrderList.class);

        Call<ArrayList<Orders>> call = orderList.getOrderList(Data.getUserInfo().getUid());

        call.enqueue(new Callback<ArrayList<Orders>>() {
            @Override
            public void onResponse(Call<ArrayList<Orders>> call, Response<ArrayList<Orders>> response) {
                ArrayList<Orders> arrayList = response.body();

                if(arrayList != null) {
                    Log.e("Log", String.valueOf(response.isSuccessful()));
                    Log.e("URL", response.raw().toString());
                    Log.e("Size", String.valueOf(response.body().size()));

                    for (int i = response.body().size() - 1; i >= 0; i--) {
                        int state = 1;
                        switch (arrayList.get(i).getOrderStatus()) {
                            case "Ordered":
                                state = 1;
                                break;
                            case "Deliverying":
                                state = 2;
                                break;
                            case "Completed":
                                state = 3;
                                break;
                        }
                        mAdapter.addItem(arrayList.get(i).getAddress(), arrayList.get(i).getReceiverName(), arrayList.get(i).getOrderDate(), arrayList.get(i).getOrderDate(), state);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Orders>> call, Throwable t) {
                Log.w(TAG, "Failed to get order list : " + t.getStackTrace());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Destory TrackFragment");
        cleanList();
        Fragment mFragment = getFragmentManager().findFragmentByTag("TrackFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
    }

    public static class ViewHolder {
        public ImageView track_state_image;
        public TextView track_state_text;
        public TextView send_date;
        public TextView due_date;
        public TextView target_address;
        public TextView target_name;
    }

    public class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<TrackListData> mListData = new ArrayList<TrackListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if(convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.track_listitem, null);

                holder.track_state_image = (ImageView)convertView.findViewById(R.id.track_state_image);
                holder.track_state_text = (TextView)convertView.findViewById(R.id.track_state_text);
                holder.send_date = (TextView)convertView.findViewById(R.id.sendcheck_date_num);
                holder.due_date = (TextView)convertView.findViewById(R.id.sendcheck_due_date_num);
                holder.target_address = (TextView)convertView.findViewById(R.id.target_address);
                holder.target_name = (TextView)convertView.findViewById(R.id.traget_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            TrackListData mData = mListData.get(position);

            switch (mData.getState()) {
                case 1:
                    holder.track_state_text.setText(getString(R.string.making));
                    holder.track_state_image.setImageResource(R.drawable.track_state_making);
                    break;
                case 2:
                    holder.track_state_text.setText(getString(R.string.moving));
                    holder.track_state_image.setImageResource(R.drawable.track_state_moving);
                    break;
                case 3:
                    holder.track_state_text.setText(getString(R.string.arrive));
                    holder.track_state_image.setImageResource(R.drawable.track_state_arrive);
                    break;
            }

            holder.send_date.setText(mData.getDate());
            holder.due_date.setText(mData.getDueDate());
            holder.target_address.setText(mData.getAddress());
            holder.target_name.setText(mData.getName());

            return convertView;
        }

        public void addItem(String address, String name, String date, String due_date, int state){
            TrackListData addInfo = null;
            addInfo = new TrackListData();

            addInfo.setAddress(address);
            addInfo.setDate(date);
            addInfo.setDueDate(due_date);
            addInfo.setName(name);
            addInfo.setState(state);

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }
    }
}
