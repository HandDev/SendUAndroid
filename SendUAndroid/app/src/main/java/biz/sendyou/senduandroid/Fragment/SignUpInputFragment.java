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

import biz.sendyou.senduandroid.R;

/**
 * Created by JunHyeok on 2016. 9. 29..
 */

public class SignUpInputFragment extends DialogFragment{

    public static Context mContext;

    public SignUpInputFragment() { }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signup_dialog, container);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity)activity;
        super.onAttach(activity);
    }
}