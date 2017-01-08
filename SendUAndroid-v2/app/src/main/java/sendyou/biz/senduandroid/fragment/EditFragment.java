package sendyou.biz.senduandroid.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sendyou.biz.senduandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private static final String TAG = "EditFragment";
    private int num;

    public EditFragment() {
        // Required empty public constructor
    }

    public EditFragment(int num) {
        this.num = num;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        // Inflate the layout for this fragment
        return view;
    }

}
