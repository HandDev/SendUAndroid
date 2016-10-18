package biz.sendyou.senduandroid.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.datatype.CardTemplate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCardFragment extends Fragment implements View.OnClickListener {


    private EditText edt;
    public static String letterText;

    public CreateCardFragment() {
        // Required empty public constructor
    }


    public static CreateCardFragment newInstance() {
        CreateCardFragment fragment = new CreateCardFragment();
        fragment.setRetainInstance(true);

        //set suportFragment Manager
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_card, container, false);

        ImageView previousButton = (ImageView)view.findViewById(R.id.previousstep);
        ImageView nextButton = (ImageView)view.findViewById(R.id.nextstep);
        final TextInputEditText textInputEditText = (TextInputEditText) view.findViewById(R.id.create_card_hint);

        //Event handling
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Remove Creating DummyData code
                List<CardTemplate> templates = new ArrayList<>();

                SelectTemplateFragment selectTemplateFragment = SelectTemplateFragment.newInstance(templates,2);

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, selectTemplateFragment).commit();

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderCardFragment orderCardFragment = OrderCardFragment.newInstance();
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, orderCardFragment).commit();
                //OrderCardFragment orderCardFragment = OrderCardFragment.newInstance();

                DrawFragment drawFragment = DrawFragment.newInstance();

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, drawFragment).commit();

                letterText = textInputEditText.getText().toString();
                Log.e("text",letterText);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
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
        Fragment mFragment = getFragmentManager().findFragmentByTag("CreateCardFragment");
        FragmentTransaction FragTsaction = getFragmentManager().beginTransaction();
        FragTsaction.remove(mFragment);
        super.onPause();
    }
}
