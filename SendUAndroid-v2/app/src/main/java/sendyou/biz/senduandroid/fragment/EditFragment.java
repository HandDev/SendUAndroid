package sendyou.biz.senduandroid.fragment;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.Data;
import sendyou.biz.senduandroid.data.OrderData;
import sendyou.biz.senduandroid.widget.DrawCanvasView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private static final String TAG = "EditFragment";
    public static boolean flag = false;
    private Data mData;
    private OrderData orderData;

    @BindView(R.id.drawer_layout) FrameLayout rootlayout;
    @BindView(R.id.text_tool) ImageView text_tool;
    @BindView(R.id.pen_tool) ImageView pen_tool;
    @BindView(R.id.erase_tool) ImageView erase_tool;
    @BindView(R.id.undo_tool) ImageView undo_tool;
    @BindView(R.id.redo_tool) ImageView redo_tool;
    @BindView(R.id.color_tool) ImageView color_tool;
    @BindView(R.id.width_tool) ImageView width_tool;
    @BindView(R.id.preview_btn) Button preview_btn;
    @BindView(R.id.toolbox) RelativeLayout toolbox;
    @BindView(R.id.text_edit) EditText textEdit;

    private boolean penSelected = false;
    private boolean eraseSelected = false;
    private boolean textSelected = true;
    private PopupWindow mPopupWindow;

    private DrawCanvasView drawCanvasView;

    public static EditFragment newInstance(OrderData orderData) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable("orderdata", orderData);
        fragment.setArguments(args);
        return fragment;
    }

    public EditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);
        mData = (Data) getActivity().getApplication();
        this.orderData = (OrderData)getArguments().getSerializable("orderdata");

        final DisplayMetrics mDisplay = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplay);

        drawCanvasView = new DrawCanvasView(getContext());
        drawCanvasView.setLayoutParams(new ViewGroup.LayoutParams(mDisplay.widthPixels - 14, (int) ((mDisplay.widthPixels - 14) * 1.4187)));
        rootlayout.addView(drawCanvasView);

        textEdit.setMaxHeight((int) ((mDisplay.widthPixels - 14) * 1.4187));
        textEdit.setMovementMethod(null);
        textEdit.bringToFront();

        text_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textSelected) {
                    textSelected = true;
                    erase_tool.setBackgroundResource(R.color.white);
                    pen_tool.setBackgroundResource(R.color.white);
                    penSelected = false;
                    eraseSelected = false;
                }
                textEdit.setFocusable(true);
                textEdit.setFocusableInTouchMode(true);
                text_tool.setBackgroundResource(R.color.lightgray);
                drawCanvasView.setMode(DrawCanvasView.Mode.TEXT);
                textEdit.bringToFront();
            }
        });

        pen_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!penSelected) {
                    penSelected = true;
                    text_tool.setBackgroundResource(R.color.white);
                    erase_tool.setBackgroundResource(R.color.white);
                    eraseSelected = false;
                    textSelected = false;
                }
                textEdit.setFocusable(false);
                textEdit.setFocusableInTouchMode(false);
                pen_tool.setBackgroundResource(R.color.lightgray);
                drawCanvasView.setMode(DrawCanvasView.Mode.DRAW);
                drawCanvasView.bringToFront();
            }
        });

        erase_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eraseSelected) {
                    eraseSelected = true;
                    pen_tool.setBackgroundResource(R.color.white);
                    text_tool.setBackgroundResource(R.color.white);
                    penSelected = false;
                    textSelected = false;
                }
                textEdit.setFocusable(false);
                textEdit.setFocusableInTouchMode(false);
                erase_tool.setBackgroundResource(R.color.lightgray);
                drawCanvasView.setMode(DrawCanvasView.Mode.ERASER);
                drawCanvasView.bringToFront();
            }
        });

        undo_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawCanvasView.undo();
            }
        });

        redo_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawCanvasView.redo();
            }
        });

        width_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawCanvasView.getMode() == DrawCanvasView.Mode.DRAW || drawCanvasView.getMode() == DrawCanvasView.Mode.ERASER) {

                    View popupView = getLayoutInflater(getArguments()).inflate(R.layout.popup_window, null);
                    mPopupWindow = new PopupWindow(popupView,
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.setAnimationStyle(-1);
                    mPopupWindow.showAsDropDown(width_tool, 0, (int) -(toolbox.getHeight() * 1.5));

                    final TextView tv = (TextView) popupView.findViewById(R.id.width_text);
                    SeekBar seekBar = (SeekBar) popupView.findViewById(R.id.seekBar);
                    if (drawCanvasView.getMode() == DrawCanvasView.Mode.DRAW) {
                        seekBar.setProgress((int) drawCanvasView.getBrushSize());
                        tv.setText(drawCanvasView.getBrushSize() + "px");
                    }
                    else if (drawCanvasView.getMode() == DrawCanvasView.Mode.ERASER) {
                        seekBar.setProgress((int) drawCanvasView.getEraserSize());
                        tv.setText(drawCanvasView.getEraserSize() + "px");
                    }
                    seekBar.setMax(200);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            if (drawCanvasView.getMode() == DrawCanvasView.Mode.DRAW)
                                drawCanvasView.setBrushSize(i);
                            else if (drawCanvasView.getMode() == DrawCanvasView.Mode.ERASER)
                                drawCanvasView.setEraserSize(i);
                            tv.setText(i + "px");
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                }
            }
        });

        color_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(getContext())
                        .setTitle("Choose color")
                        .initialColor(0xffff0000)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                drawCanvasView.setColor(selectedColor);
                                color_tool.setColorFilter(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEdit.setHint("");
                rootlayout.buildDrawingCache();
                Bitmap captureView = rootlayout.getDrawingCache();
                FileOutputStream fos;

                String fileName = reverseString(mData.getUserInfo().getUid()) + "_" + System.currentTimeMillis() + ".jpeg";
                try {
                    File fileDirectory = new File(Environment.getExternalStorageDirectory().toString()+"/SendU/OrderTemp/");
                    fileDirectory.mkdirs();

                    File outputFile = new File(fileDirectory, fileName);
                    fos = new FileOutputStream(outputFile);
                    captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                orderData.setContentsName(fileName);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, AddressFragment.newInstance(orderData)).commit();
            }
        });

        return view;
    }

    public static String reverseString(String s) {
        return ( new StringBuffer(s) ).reverse().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}