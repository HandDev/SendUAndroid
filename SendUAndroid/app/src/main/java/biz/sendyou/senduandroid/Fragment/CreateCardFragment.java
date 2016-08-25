package biz.sendyou.senduandroid.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import biz.sendyou.senduandroid.R;
import biz.sendyou.senduandroid.Util.Http;
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

    private TextView card_text;
    private ImageView card_image;

    private OnFragmentInteractionListener mListener;

    private final String upLoadServerUri = "sendukor7833.cloudapp.net:8080/PostCardManageSystem_war/file/upload";//서버컴퓨터의 ip주소

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

        card_text = (TextView)view.findViewById(R.id.card_text);
        card_image = (ImageView)view.findViewById(R.id.card_image);

        card_text.setOnClickListener(this);
        card_image.setOnClickListener(this);

        ImageView previousButton = (ImageView)view.findViewById(R.id.previousstep);
        ImageView nextButton = (ImageView)view.findViewById(R.id.nextstep);

        //Event handling
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Remove Creating DummyData code
                List<CardTemplate> templates = new ArrayList<>();

                CardTemplate dummy1 = new CardTemplate();
                dummy1.setDrawable(getResources().getDrawable(R.drawable.arrow));

                templates.add(dummy1);

                SelectTemplateFragment selectTemplateFragment = SelectTemplateFragment.newInstance(templates,2);

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, selectTemplateFragment).commit();

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderCardFragment orderCardFragment = OrderCardFragment.newInstance();

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.mainFrameLayout, orderCardFragment).commit();

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

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_image :
                Toast.makeText(getContext(), "card_image Clicked", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), Environment.getExternalStorageDirectory().toString(), Toast.LENGTH_SHORT).show();
                try {
                    FileOutputStream output = new FileOutputStream("/template/file.jpg");
                    View image_view = (View)getActivity().findViewById(R.id.main_view);
                    viewToBitmap(image_view).compress(Bitmap.CompressFormat.JPEG, 100, output);
                    output.close();
                    try {
                        if (upLoadServerUri != null && upLoadServerUri.length() > 0) {
                            new Http().start();
                        } else {
                            //throw new Exception(getString(R.string.bad_url));
                        }
                    } catch (Exception e) {
                    }
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.card_text :
                Toast.makeText(getContext(), "card_text Clicked", Toast.LENGTH_SHORT).show();

                LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_fragment, null);

                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                alertBuilder.setView(layout);
                alertBuilder.show();
                break;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
