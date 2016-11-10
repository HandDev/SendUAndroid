package biz.sendyou.senduandroid.Fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import biz.sendyou.senduandroid.Activity.DrawCanvasView;
import biz.sendyou.senduandroid.BitmapManager;
import biz.sendyou.senduandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DrawFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DrawFragment() {
        // Required empty public constructor
    }

    public static DrawFragment newInstance() {
        DrawFragment fragment = new DrawFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_draw, container, false);
        //return new DrawCanvasView(this.getContext());

        final DrawCanvasView drawCanvasView = new DrawCanvasView(getContext());
        drawCanvasView.setId(R.id.drawer_canvas_view);
        drawCanvasView.setBackgroundResource(R.drawable.draw_background);

        final FrameLayout rootLayout = (FrameLayout)view.findViewById(R.id.drawer_layout);
        rootLayout.addView(drawCanvasView);

        ImageView imageView = (ImageView) view.findViewById(R.id.nextstep);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = drawCanvasView.getCanvasBitmap();

                //save bitmap to file
                BitmapManager.getInstance().setBitmap(bitmap);
                OrderCardFragment orderFinishFragment = OrderCardFragment.newInstance();
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, orderFinishFragment).commit();

            }
        });
        ImageView imageView2 = (ImageView) view.findViewById(R.id.previousstep);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCardFragment createCardFragment = CreateCardFragment.newInstance();
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, createCardFragment).commit();

            }
        });

        return view;
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
     * <p>
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
        Fragment mFragment = getFragmentManager().findFragmentByTag("DrawFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
