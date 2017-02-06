package sendyou.biz.senduandroid.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.activity.AddressActivity;
import sendyou.biz.senduandroid.widget.DrawCanvasView;
import sendyou.biz.senduandroid.widget.ZoomLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "EditFragment";
    public static boolean flag = false;
    private int num;

    @BindView(R.id.drawer_layout) LinearLayout rootlayout;
    @BindView(R.id.pen_tool) Button pen_tool;
    @BindView(R.id.move_tool) Button move_tool;
    @BindView(R.id.text_tool) Button text_tool;
    @BindView(R.id.complete_btn) Button completeButton;
    @BindView(R.id.zoomlayout) ZoomLayout zoomLayout;
    @BindView(R.id.edit_text) EditText editText;

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
        ButterKnife.bind(this, view);

        final DrawCanvasView drawCanvasView = new DrawCanvasView(getContext());
        rootlayout.addView(drawCanvasView);

        zoomLayout.setOnTouchListener(this);

        pen_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawCanvasView.tool_state = 1;
                zoomLayout.setFocusable(false);
                zoomLayout.setFocusableInTouchMode(false);
                drawCanvasView.setFocusable(true);
                drawCanvasView.setFocusableInTouchMode(true);
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
                editText.setInputType(InputType.TYPE_NULL);
                editText.setEnabled(false);
            }
        });

        text_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                editText.setEnabled(true);
            }
        });

        move_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawCanvasView.tool_state = 2;
                drawCanvasView.setFocusable(false);
                drawCanvasView.setFocusableInTouchMode(false);
                zoomLayout.setFocusable(true);
                zoomLayout.setFocusableInTouchMode(true);
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        return view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        zoomLayout.init(getContext());
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
