package sendyou.biz.senduandroid.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sendyou.biz.senduandroid.R;

/**
 * Created by pyh42 on 2016-12-20.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.login_background) ImageView login_background;
    @BindView(R.id.login_logo) ImageView login_logo;
    @BindView(R.id.google_login_button) Button google_login_btn;
    @BindView(R.id.facebook_login_button) Button facebook_login_btn;
    @BindView(R.id.email_login_button) Button email_login_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Log.w(TAG, "Set image resources in views");
        login_background.setImageResource(R.drawable.background);
        login_logo.setImageResource(R.drawable.icon);

        google_login_btn.setOnClickListener(this);
        facebook_login_btn.setOnClickListener(this);
        email_login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.google_login_button :
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.facebook_login_button :
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.email_login_button :
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "LoginActivity Destroy");
        recycleView(login_background);
        recycleView(login_logo);
        super.onDestroy();
    }

    private void recycleView(View view) {
        ImageView imageView = null;
        Log.w(TAG, "Recycle " + getResources().getResourceEntryName(view.getId()));

        if(view instanceof ImageView) {
            imageView = (ImageView)view;
            Drawable d = imageView.getDrawable();
            if(d instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable) d).getBitmap();
                imageView.setImageBitmap(null);
                b.recycle();
                b = null;
            }
            d.setCallback(null);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("앱을 종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
