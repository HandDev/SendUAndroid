package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import biz.sendyou.senduandroid.Activity.LoginActivity;
import biz.sendyou.senduandroid.Activity.SignupAddressActivity;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Service.DoOrder;
import biz.sendyou.senduandroid.Service.GetUUID;
import biz.sendyou.senduandroid.Service.Repo;
import biz.sendyou.senduandroid.Service.SignUpService;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String uuid;
    private JSONObject jsonParams;
    private static final String URL = "http://52.78.159.163:8080/";
    private RequestBody body;
    private EditText receivername,numAddress,jusoAddress,phoneNumber;

    public OrderCardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderCardFragment newInstance() {
        OrderCardFragment fragment = new OrderCardFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_card, container, false);

        ImageView previous = (ImageView)view.findViewById(R.id.previousstep);
        numAddress = (EditText)view.findViewById(R.id.order_edittext_address_one);
        jusoAddress = (EditText)view.findViewById(R.id.order_edittext_address_two);
        receivername = (EditText)view.findViewById(R.id.order_edittext_name);
        phoneNumber = (EditText)view.findViewById(R.id.order_edittext_phone);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCardFragment createCardFragment = CreateCardFragment.newInstance();

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, createCardFragment).commit();
            }
        });

        ImageView orderCard = (ImageView)view.findViewById(R.id.completeorder);

        orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUUID();
                CashFragment cashFragment = CashFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.mainFrameLayout,cashFragment).commit();
            }
        });

        return view;
    }

    private void getUUID() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetUUID getUUID = retrofit.create(GetUUID.class);

        Call<Repo> call = getUUID.doUUID(LoginActivity.email);

        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                uuid = response.body().getOrderUUID();
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }

    private void doOrder() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            String date = sdf.format(c.getTime());
            jsonParams.put("userUUID",LoginActivity.email);
            jsonParams.put("orderUUID",uuid);
            jsonParams.put("orderDate",date);
            jsonParams.put("receiverName",receivername.getText().toString());
            jsonParams.put("receiverPhone",phoneNumber.getText().toString());
            jsonParams.put("address",jusoAddress.getText().toString());
            jsonParams.put("numAddress",numAddress.getText().toString());
            jsonParams.put("text",CreateCardFragment.letterText);
            jsonParams.put("image","s3");

            body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(jsonParams).toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DoOrder doOrder = retrofit.create(DoOrder.class);


        Call<ResponseBody> call = doOrder.doOrder(LoginActivity.email,uuid,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onPause() {
        Fragment mFragment = getFragmentManager().findFragmentByTag("OrderCardFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onPause();
    }
}
