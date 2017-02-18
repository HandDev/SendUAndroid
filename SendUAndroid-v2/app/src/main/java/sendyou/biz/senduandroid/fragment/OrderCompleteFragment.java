package sendyou.biz.senduandroid.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.OrderData;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderCompleteFragment extends Fragment {

    private static final String TAG = "OrderCompleteFragment";
    private OrderData orderData;

    @BindView(R.id.complete_btn) Button completeButton;

    public static OrderCompleteFragment newInstance(OrderData orderData) {
        OrderCompleteFragment fragment = new OrderCompleteFragment();
        Bundle args = new Bundle();
        args.putSerializable("orderdata", orderData);
        fragment.setArguments(args);
        return fragment;
    }

    public OrderCompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_complete, container, false);
        ButterKnife.bind(this, view);
        this.orderData = (OrderData)getArguments().getSerializable("orderdata");

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, MainFragment.newInstance()).commit();
            }
        });

        return view;
    }

}
