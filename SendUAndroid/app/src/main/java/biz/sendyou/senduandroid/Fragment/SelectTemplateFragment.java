package biz.sendyou.senduandroid.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.CardTemplate;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SelectTemplateFragment extends Fragment {

    private boolean isFirstAttach = true;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_TEMPLATE = "card-templates";
    private final String LOGTAG = "SelectTemplateFragment";
    private RecyclerView recyclerView;
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    //TODO: DummyDatas should not be saved as static
    private static List<CardTemplate> thumbImages;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    //Create Dummy data for testing recyclerViewAdapter
    public SelectTemplateFragment() {

    }

    @SuppressWarnings("unused")
    public static SelectTemplateFragment newInstance(List<CardTemplate>inputTemplates ,int columnCount) {
        SelectTemplateFragment fragment = new SelectTemplateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);

        //TODO Remove dummyData setting code
        thumbImages = inputTemplates;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_template_list, container, false);

        // Set the adapter
            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.templatelist);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            setImagesToRecyclerView();
            Log.i(LOGTAG, "setRecyclerViewAdapter");

        ImageView previousButton = (ImageView)view.findViewById(R.id.previousstep);
        ImageView nextButton = (ImageView)view.findViewById(R.id.nextstep);

        //Evnet handling
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrontFragment frontFragment = FrontFragment.newInstance();

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, frontFragment).commit();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOGTAG, "nextButton Clicked");
                CreateCardFragment createCardFragment = CreateCardFragment.newInstance();

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, createCardFragment).commit();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        Log.i(LOGTAG, "onAttach");
        super.onAttach(context);

        Log.i(LOGTAG, "isFirstAttach : " + isFirstAttach);
        if(!isFirstAttach)
            setImagesToRecyclerView();
        else
         isFirstAttach = false;

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.i(LOGTAG, "onDetach");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(CardTemplate item);
    }
    @Override
    public void onPause() {
        Fragment mFragment = getFragmentManager().findFragmentByTag("SelectTemplateFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onPause();
    }

    private void setImagesToRecyclerView(){
        Log.i(LOGTAG, "sertImagesToRecyclerView called");
        recyclerView.setAdapter(new TemplateRecyclerViewAdapter(getFragmentManager(), thumbImages, ImageLoader.getInstance(), mListener));
    }
}
