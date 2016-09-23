package biz.sendyou.senduandroid.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import biz.sendyou.senduandroid.R;

/**
 * Created by pyh42 on 2016-08-16.
 */
public class InputDialogFragment extends DialogFragment {

    public static Context mContext;

    public InputDialogFragment() { }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_card_textdialog, container);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity)activity;
        super.onAttach(activity);
    }
}
