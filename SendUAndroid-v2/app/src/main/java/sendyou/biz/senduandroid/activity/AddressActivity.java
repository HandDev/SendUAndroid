package sendyou.biz.senduandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.Address;
import sendyou.biz.senduandroid.data.AddressListData;
import sendyou.biz.senduandroid.data.AddressResult;
import sendyou.biz.senduandroid.data.TrackListData;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.fragment.TrackFragment;
import sendyou.biz.senduandroid.service.SearchAddress;

public class AddressActivity extends AppCompatActivity {

    private static final String TAG = "AddressActivity";
    private ProgressDialog mProgressDialog;
    private ListViewAdapter mAdapter;

    @BindView(R.id.actionbar_title) TextView title;
    @BindView(R.id.search_address) EditText search_address;
    @BindView(R.id.search_address_btn) Button search_address_btn;
    @BindView(R.id.address_list) ListView address_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        title.setText("주소 검색");

        mAdapter = new ListViewAdapter(this);
        address_list.setAdapter(mAdapter);
        address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressListData addressListData = (AddressListData)mAdapter.getItem(i);

                Intent intent = new Intent();
                intent.putExtra("address", addressListData.getAddress());
                intent.putExtra("address_num", addressListData.getAddress_num());

                setResult(9001, intent);
                finish();
            }
        });

        search_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog = ProgressDialog.show(AddressActivity.this, "", "검색 중입니다.",true);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URLManager.address_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                SearchAddress searchAddress = retrofit.create(SearchAddress.class);

                Call<Address> call = searchAddress.searchAddress(search_address.getText().toString(), "3.0.0-sendu", "sendu.kr");

                call.enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        Address res = response.body();

                        cleanList();

                        for(int i = 0 ; i < res.getResults().length ; i++) {
                            AddressResult address = res.getResults()[i];
                            mAdapter.addItem(address.getKo_common() + " " + address.getKo_doro(), address.getPostcode5());
                            Log.w(TAG, address.toString());
                        }

                        mAdapter.dataChange();
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        Log.w(TAG, "Get address failed" + t.getStackTrace().toString());
                    }
                });
            }
        });
    }

    private void cleanList() {
        while(true) {
            if(mAdapter.mListData.isEmpty()) {
                break;
            }
            mAdapter.remove(0);
        }
    }

    public static class ViewHolder {
        public TextView first_address;
        public TextView second_address;
    }

    public class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<AddressListData> mListData = new ArrayList<AddressListData>();

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
                convertView = inflater.inflate(R.layout.address_listitem, null);

                holder.first_address = (TextView)convertView.findViewById(R.id.first_address);
                holder.second_address = (TextView)convertView.findViewById(R.id.second_address);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            AddressListData mData = mListData.get(position);

            holder.first_address.setText(mData.getAddress());
            holder.second_address.setText(mData.getAddress_num());

            return convertView;
        }

        public void addItem(String address, String address_num){
            AddressListData addInfo = null;
            addInfo = new AddressListData();

            addInfo.setAddress(address);
            addInfo.setAddress_num(address_num);

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
