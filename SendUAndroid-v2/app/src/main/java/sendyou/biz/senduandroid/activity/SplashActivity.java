package sendyou.biz.senduandroid.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sendyou.biz.senduandroid.R;
import sendyou.biz.senduandroid.data.URLManager;
import sendyou.biz.senduandroid.service.RegisterToken;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @BindView(R.id.splash_background) ImageView splash_background;
    @BindView(R.id.splash_logo) ImageView splash_logo;

    @BindString(R.string.denied_message) String denied_message;
    @BindString(R.string.permission_granted) String permission_granted;
    @BindString(R.string.permission_denied) String permission_denied;
    @BindString(R.string.exit_q) String exit_q;
    @BindString(R.string.confirm) String confirm;
    @BindString(R.string.cancel) String cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Log.w(TAG, "Set image resources in views");
        Glide.with(this).load(R.drawable.background).into(splash_background);
        Glide.with(this).load(R.drawable.logo).into(splash_logo);


        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.w(TAG, "SplashActivity -> LoginActivity");
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        };

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.w(TAG, "Permission Granted");
                mHandler.sendEmptyMessageDelayed(0, 2000);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Log.w(TAG, "Permission Denied");
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(denied_message)
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "SplashActivity Destroy");
        recycleView(splash_background);
        recycleView(splash_logo);
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
        builder.setMessage(exit_q)
                .setCancelable(false)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
