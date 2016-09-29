package biz.sendyou.senduandroid.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import biz.sendyou.senduandroid.ContextManager;
import biz.sendyou.senduandroid.R;

/**
 * Created by JunHyeok on 2016. 9. 29..
 */

public class SignUpDialog extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void showDialog(View view) {
        LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.activity_signup_dialog, null);

        final AlertDialog alertBuilder = new AlertDialog.Builder(view.getContext()).create();
        alertBuilder.setView(layout);
        alertBuilder.show();

        ImageView close = (ImageView)layout.findViewById(R.id.close_btn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBuilder.dismiss();
            }
        });


    }

}
