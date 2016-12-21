package sendyou.biz.senduandroid.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sendyou.biz.senduandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackFragment extends Fragment {

    private static final String TAG = "TrackFragment";

    public TrackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        Log.w(TAG, "Destory TrackFragment");
        Fragment mFragment = getFragmentManager().findFragmentByTag("TrackFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onDestroy();
    }
}
