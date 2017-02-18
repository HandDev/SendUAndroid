package sendyou.biz.senduandroid.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.activity.AddressActivity;
import sendyou.biz.senduandroid.activity.MainActivity;
import sendyou.biz.senduandroid.data.Data;
import sendyou.biz.senduandroid.data.OrderData;
import sendyou.biz.senduandroid.data.Response;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.service.Order;
import sendyou.biz.senduandroid.service.cInAppBillingHelper;
import sendyou.biz.senduandroid.util.AWSManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    private static final String TAG = "AddressFragment";
    private OrderData orderData;
    private Data mData;
    private cInAppBillingHelper mHelper;

    @BindView(R.id.sender_name) EditText senderName;
    @BindView(R.id.sender_phonenum) EditText senderPhone;
    @BindView(R.id.first_address) EditText firstAddress;
    @BindView(R.id.second_address) EditText secondAddress;
    @BindView(R.id.address_num) EditText numAddress;
    @BindView(R.id.recipent_name) EditText recipentName;
    @BindView(R.id.order_btn) Button orderButton;

    public static AddressFragment newInstance(OrderData orderData) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putSerializable("orderdata", orderData);
        fragment.setArguments(args);
        return fragment;
    }

    public AddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        ButterKnife.bind(this, view);
        mData = (Data) getActivity().getApplication();
        this.orderData = (OrderData)getArguments().getSerializable("orderdata");

        if(mData.getUserInfo().getUserName() != null || !mData.getUserInfo().getUserName().equals("")) {
            senderName.setText(mData.getUserInfo().getUserName());
        }
        if (mData.getUserInfo().getPhone() != null || !mData.getUserInfo().getPhone().equals("")) {
            senderPhone.setText(mData.getUserInfo().getPhone());
        }

        firstAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivityForResult(intent, 9001);
            }
        });

        numAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivityForResult(intent, 9001);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order();
            }
        });

        return view;
    }

    private void order() {
        if(senderName.getText().toString().equals("")) {
            Toast.makeText(getContext(), "보내는 사람의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(senderPhone.getText().toString().equals("")) {
            Toast.makeText(getContext(), "보내는 사람의 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(recipentName.getText().toString().equals("")) {
            Toast.makeText(getContext(), "받는 사람 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(firstAddress.getText().toString().equals("")) {
            Toast.makeText(getContext(), "받는 사람의 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(secondAddress.getText().toString().equals("")) {
            Toast.makeText(getContext(), "받는 사람의 추가 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        orderData.setSenderName(senderName.getText().toString());
        orderData.setSenderPhone(senderPhone.getText().toString());
        orderData.setReceiverName(recipentName.getText().toString());
        orderData.setFirstAddress(firstAddress.getText().toString());
        orderData.setSecondAddress(secondAddress.getText().toString());
        orderData.setNumAddress(numAddress.getText().toString());

        AWSManager awsManager = AWSManager.getInstance(getContext());

        File file = new File(Environment.getExternalStorageDirectory().toString()+"/SendU/OrderTemp/" + orderData.getContentsName());
        awsManager.uploadFile(file);

        MainActivity activity = (MainActivity)getActivity();
        mHelper = activity.getHelper();

        //mHelper.buy("default_letter");

        final ProgressDialog mProgressDialog = ProgressDialog.show(getActivity(), "", "주문 중입니다.",true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLManager.authURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Order order = retrofit.create(Order.class);

        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String strCurDate = CurDateFormat.format(date);
        Call<Response> call = order.order(mData.getUserInfo().getUid(), mData.getUserInfo().getUid(), orderData.getContentsName(), strCurDate, orderData.getReceiverName(), orderData.getFirstAddress() + " " + orderData.getSecondAddress(), orderData.getNumAddress(), "", orderData.getImageNum() + "");

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                mProgressDialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, OrderCompleteFragment.newInstance(orderData)).commit();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w(TAG, "requestCode " + requestCode);
        Log.w(TAG, "resultCode " + resultCode);
        Log.w(TAG, "data " + data.getAction());

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 9001) {
            firstAddress.setText(data.getStringExtra("address"));
            numAddress.setText(data.getStringExtra("address_num"));
        } else {
            mHelper.activityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
