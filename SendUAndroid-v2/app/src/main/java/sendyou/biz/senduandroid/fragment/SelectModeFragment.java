package sendyou.biz.senduandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;

/**
 * Created by pyh42 on 2017-01-08.
 */

public class SelectModeFragment extends android.support.v4.app.DialogFragment {

    private static final String TAG = "SelectModeFragment";

    public SelectModeFragment() {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Transparent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_mode, container);
        ButterKnife.bind(this, view);
        return view;
    }
}
