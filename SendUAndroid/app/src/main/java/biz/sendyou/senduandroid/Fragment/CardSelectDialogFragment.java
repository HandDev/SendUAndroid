package biz.sendyou.senduandroid.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import biz.sendyou.senduandroid.Activity.NavigationDrawerActivity;
import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.URLManager;

public class CardSelectDialogFragment extends DialogFragment {

    private final String LOGTAG = "CardDialogFragment";

    private String imageURL; //cardID is used for s3 connection
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private ProgressDialog mProgressDialog;
    private OnFragmentInteractionListener mListener;

    public CardSelectDialogFragment() {
        // Required empty public constructor
    }

    public static CardSelectDialogFragment newInstance(String imageURL) {

        CardSelectDialogFragment fragment = new CardSelectDialogFragment();
        fragment.setImageURL(imageURL);
        return fragment;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_select_dialog, container, false);

        final ImageView preview = (ImageView)view.findViewById(R.id.templatepreview);
        imageLoader.displayImage(URLManager.s3URL + imageURL, preview, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        });

        ImageView choose = (ImageView)view.findViewById(R.id.choosebtn);
        ImageView no = (ImageView)view.findViewById(R.id.nobtn);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
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
}
