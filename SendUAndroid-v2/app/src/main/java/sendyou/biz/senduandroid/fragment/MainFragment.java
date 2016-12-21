package sendyou.biz.senduandroid.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    @BindView(R.id.main_viewflipper) ViewFlipper viewFlipper;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
