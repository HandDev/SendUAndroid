package sendyou.biz.senduandroid.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.fragment.EditFragment;
import sendyou.biz.senduandroid.fragment.TrackFragment;
import sendyou.biz.senduandroid.service.ResultCallback;

/**
 * Created by pyh42 on 2017-01-03.
 */

public class SelectTemplateDialog extends Dialog implements ResultCallback {

    private static final String TAG = "SelectTemplateDialog";
    private ResultCallback callback;
    private String image_url;
    private int num;

    @BindView(R.id.template_main_image) ImageView main_iamge;
    @BindView(R.id.confirm_btn) FrameLayout confirm_btn;
    @BindView(R.id.cancel_btn) FrameLayout cancel_btn;

    public SelectTemplateDialog(Context context, String url, ResultCallback listener) {
        super(context);
        image_url = url;
        callback = listener;
    }

    public SelectTemplateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectTemplateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void finishProcess() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_template);
        ButterKnife.bind(this);

        Glide.with(getContext()).load(image_url).into(main_iamge);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.finishProcess();
                dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }

    @Override
    public void dismiss() {
        recycleView(main_iamge);
        super.dismiss();
    }

    private void recycleView(View view) {
        ImageView imageView = null;
        Log.w(TAG, "Recycle " + getContext().getResources().getResourceEntryName(view.getId()));

        if(view instanceof ImageView) {
            imageView = (ImageView)view;
            Drawable d = imageView.getDrawable();
            if(d != null) {
                if (d instanceof BitmapDrawable) {
                    Bitmap b = ((BitmapDrawable) d).getBitmap();
                    imageView.setImageBitmap(null);
                    b.recycle();
                    b = null;
                }
                d.setCallback(null);
            }
        }
        Runtime.getRuntime().gc();
    }

}
